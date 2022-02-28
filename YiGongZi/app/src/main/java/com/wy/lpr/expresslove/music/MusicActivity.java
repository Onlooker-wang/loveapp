package com.wy.lpr.expresslove.music;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.tbruyelle.rxpermissions.RxPermissions;
import com.wy.lpr.expresslove.R;
import com.wy.lpr.expresslove.app.MyApplication;
import com.wy.lpr.expresslove.base.CommonAudioActivity;
import com.wy.lpr.expresslove.bean.Song;
import com.wy.lpr.expresslove.utils.Constant;
import com.wy.lpr.expresslove.utils.MusicUtils;
import com.wy.lpr.expresslove.utils.ObjectUtils;
import com.wy.lpr.expresslove.utils.SharedPreferencesUtils;
import com.wy.lpr.expresslove.utils.StatusBarUtil;
import com.wy.lpr.expresslove.utils.ToastUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.wy.lpr.expresslove.utils.DateUtil.parseTime;

public class MusicActivity extends CommonAudioActivity {
    private static final String TAG = "MusicActivity";

    //音乐列表
    private RecyclerView mRvMusic;
    //扫描按钮
    private Button mBtnScan;
    //扫描界面
    private LinearLayout mScanLay;
    //清空按钮
    private TextView mTvClearList;
    //标题
    private TextView mTvTitle;
    //标题栏
    private Toolbar mToolbar;
    //已播放时间
    private TextView mTvPlayTime;
    //播放进度条
    private SeekBar mTimeSeekBar;
    //播放总时间
    private TextView mTvTotalTime;
    //上一首
    private ImageView mBtnPrevious;
    //播放暂停
    private ImageView mBtnPlayOrPause;
    //下一首
    private ImageView mBtnNext;
    //歌曲信息
    private TextView mTvPlaySongInfo;
    //播放状态
    private ImageView mPlayStateImg;
    private LinearLayout mPlayStateLay;

    private MusicListAdapter mAdapter;//歌曲适配器
    private List<Song> mList;//歌曲列表
    private RxPermissions mRxPermissions;//权限请求
    private MediaPlayer mMediaPlayer;//音频播放器
    private String mMusicData = null;
    // 记录当前播放歌曲的位置
    public int mCurrentPosition;
    private static final int INTERNAL_TIME = 1000;// 音乐进度间隔时间
    private Context mContext;


    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            // 展示给进度条和当前时间
            int progress = mMediaPlayer.getCurrentPosition();
            mTimeSeekBar.setProgress(progress);
            mTvPlayTime.setText(parseTime(progress));
            // 继续定时发送数据
            updateProgress();
            return true;
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtil.StatusBarLightMode(this);
        setContentView(R.layout.activity_music);
        mContext = MyApplication.getContext();
        initView();
        setListener();
    }

    private void initView() {
        mRvMusic = (RecyclerView) findViewById(R.id.rv_music);
        mBtnScan = (Button) findViewById(R.id.btn_scan);
        mScanLay = (LinearLayout) findViewById(R.id.scan_lay);
        mTvClearList = (TextView) findViewById(R.id.tv_clear_list);
        mTvTitle = (TextView) findViewById(R.id.tv_title);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mTvPlayTime = (TextView) findViewById(R.id.tv_play_time);
        mTimeSeekBar = (SeekBar) findViewById(R.id.time_seekBar);
        mTvTotalTime = (TextView) findViewById(R.id.tv_total_time);
        mBtnPrevious = (ImageView) findViewById(R.id.btn_previous);
        mBtnPlayOrPause = (ImageView) findViewById(R.id.btn_play_or_pause);
        mBtnNext = (ImageView) findViewById(R.id.btn_next);
        mTvPlaySongInfo = (TextView) findViewById(R.id.tv_play_song_info);
        mPlayStateImg = (ImageView) findViewById(R.id.play_state_img);
        mPlayStateLay = (LinearLayout) findViewById(R.id.play_state_lay);
        mRxPermissions = new RxPermissions(this);
        mMusicData = SharedPreferencesUtils.getString(mContext, Constant.MUSIC_DATA_SP, Constant.MUSIC_DATA_FIRST, "yes");

        if (mMusicData.equals("yes")) {//说明是第一次打开APP，未进行扫描
            mScanLay.setVisibility(View.VISIBLE);
        } else {
            mScanLay.setVisibility(View.GONE);
            initMusic();
        }
    }

    private void setListener() {
        //清空数据
        mTvClearList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mList.clear();
                mAdapter.notifyDataSetChanged();
                SharedPreferencesUtils.putString(mContext, Constant.MUSIC_DATA_SP, Constant.MUSIC_DATA_FIRST, "yes");
                mScanLay.setVisibility(View.VISIBLE);
                mToolbar.setBackgroundColor(getResources().getColor(R.color.white));
                mTvTitle.setTextColor(getResources().getColor(R.color.black));
                mTvClearList.setTextColor(getResources().getColor(R.color.black));
                if (mMediaPlayer == null) {
                    mMediaPlayer = new MediaPlayer();
                    mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mp) {
                            changeMusic(++mCurrentPosition);
                        }
                    });//监听音乐播放完毕事件，自动下一曲
                }
                if (mMediaPlayer.isPlaying()) {
                    mMediaPlayer.pause();
                    mMediaPlayer.reset();
                }
            }
        });

        //扫描本地歌曲
        mBtnScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                permissionRequest();
            }
        });

        //上一曲
        mBtnPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeMusic(--mCurrentPosition);//当前歌曲位置减1
            }
        });

        //播放或者暂停
        mBtnPlayOrPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 首次点击播放按钮，默认播放第0首，下标从0开始
                if (mMediaPlayer == null) {
                    changeMusic(0);
                } else {
                    if (mMediaPlayer.isPlaying()) {
                        mMediaPlayer.pause();
                        mBtnPlayOrPause.setBackground(getResources().getDrawable(R.mipmap.icon_pause));
                        mPlayStateImg.setBackground(getResources().getDrawable(R.mipmap.list_play_state));
                        //如果你是用TextView的leftDrawable设置的图片，在代码里面就可以通过下面代码来动态更换
//                        Drawable leftImg = getResources().getDrawable(R.mipmap.list_play_state);
//                        leftImg.setBounds(0, 0, leftImg.getMinimumWidth(), leftImg.getMinimumHeight());
//                        tvPlaySongInfo.setCompoundDrawables(leftImg, null, null, null);
                    } else {
                        mMediaPlayer.start();
                        mBtnPlayOrPause.setBackground(getResources().getDrawable(R.mipmap.icon_play));
                        mPlayStateImg.setBackground(getResources().getDrawable(R.mipmap.list_pause_state));
                    }
                }
            }
        });

        //下一曲
        mBtnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeMusic(++mCurrentPosition);//当前歌曲位置加1
            }
        });

        //播放进度条
        mTimeSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int progress = seekBar.getProgress();
                mMediaPlayer.seekTo(progress);
            }
        });
    }

    @SuppressLint("CheckResult")
    private void permissionRequest() {//使用这个框架需要制定JDK版本，建议用1.8
        mRxPermissions.request(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .subscribe(granted -> {
                    if (granted) {//请求成功之后开始扫描
                        initMusic();
                    } else {//失败时给一个提示
                        ToastUtils.showShortToast(MusicActivity.this, "未授权");
                    }
                });
    }

    //获取音乐列表
    private void initMusic() {
        mList = new ArrayList<>();//实例化

        //数据赋值
        mList = MusicUtils.getMusicData(this);//将扫描到的音乐赋值给音乐列表
        if (!ObjectUtils.isEmpty(mList) && mList != null) {
            mScanLay.setVisibility(View.GONE);
            SharedPreferencesUtils.putString(mContext, Constant.MUSIC_DATA_SP, Constant.MUSIC_DATA_FIRST, "no");
        }
        mAdapter = new MusicListAdapter(R.layout.activity_music_list_item, mList);//指定适配器的布局和数据源
        //线性布局管理器，可以设置横向还是纵向，RecyclerView默认是纵向的，所以不用处理,如果不需要设置方向，代码还可以更加的精简如下
        mRvMusic.setLayoutManager(new LinearLayoutManager(this));
        //如果需要设置方向显示，则将下面代码注释去掉即可
//        LinearLayoutManager manager = new LinearLayoutManager(this);
//        manager.setOrientation(RecyclerView.HORIZONTAL);
//        rvMusic.setLayoutManager(manager);

        //设置适配器
        mRvMusic.setAdapter(mAdapter);

        //item的点击事件
        mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                if (view.getId() == R.id.item_music) {
                    mCurrentPosition = position;
                    changeMusic(mCurrentPosition);
//                    mAdapter.notifyItemRangeChanged(0, mList.size());

                }
            }
        });

    }

    //切歌
    private void changeMusic(int position) {
        Log.e("MainActivity", "position:" + position);
        if (position < 0) {
            mCurrentPosition = position = mList.size() - 1;
            Log.e("MainActivity", "mList.size:" + mList.size());
        } else if (position > mList.size() - 1) {
            mCurrentPosition = position = 0;
        }
        Log.e("MainActivity", "position:" + position);
        if (mMediaPlayer == null) {
            mMediaPlayer = new MediaPlayer();
            mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    changeMusic(++mCurrentPosition);
                }
            });//监听音乐播放完毕事件，自动下一曲
        }

        try {
            // 切歌之前先重置，释放掉之前的资源
            mMediaPlayer.reset();
            // 设置播放源
            Log.d("Music", mList.get(position).path);
            mMediaPlayer.setDataSource(mList.get(position).path);
            mTvPlaySongInfo.setText("歌名： " + mList.get(position).song +
                    "  歌手： " + mList.get(position).singer);

//            Glide.with(this).load(mList.get(position).album_art).into(songImage);
            mTvPlaySongInfo.setSelected(true);//跑马灯效果
            mPlayStateLay.setVisibility(View.VISIBLE);

            // 开始播放前的准备工作，加载多媒体资源，获取相关信息
            mMediaPlayer.prepare();
            // 开始播放
            mMediaPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 切歌时重置进度条并展示歌曲时长
        mTimeSeekBar.setProgress(0);
        mTimeSeekBar.setMax(mMediaPlayer.getDuration());
        mTvTotalTime.setText(parseTime(mMediaPlayer.getDuration()));

        updateProgress();
        if (mMediaPlayer.isPlaying()) {
            mBtnPlayOrPause.setBackground(getResources().getDrawable(R.mipmap.icon_play));
            mPlayStateImg.setBackground(getResources().getDrawable(R.mipmap.list_pause_state));
        } else {
            mBtnPlayOrPause.setBackground(getResources().getDrawable(R.mipmap.icon_pause));
            mPlayStateImg.setBackground(getResources().getDrawable(R.mipmap.list_play_state));
        }
    }

    private void updateProgress() {
        // 使用Handler每间隔1s发送一次空消息，通知进度条更新
        Message msg = Message.obtain();// 获取一个现成的消息
        // 使用MediaPlayer获取当前播放时间除以总时间的进度
        int progress = mMediaPlayer.getCurrentPosition();
        msg.arg1 = progress;
        mHandler.sendMessageDelayed(msg, INTERNAL_TIME);
    }

}