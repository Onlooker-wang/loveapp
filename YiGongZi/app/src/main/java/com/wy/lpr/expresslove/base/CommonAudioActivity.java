package com.wy.lpr.expresslove.base;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.tbruyelle.rxpermissions.RxPermissions;
import com.wy.lpr.expresslove.R;
import com.wy.lpr.expresslove.app.MyApplication;
import com.wy.lpr.expresslove.bean.SingleMediaPlayer;
import com.wy.lpr.expresslove.bean.Song;
import com.wy.lpr.expresslove.music.MusicActivity;
import com.wy.lpr.expresslove.utils.Constant;
import com.wy.lpr.expresslove.utils.MusicUtils;
import com.wy.lpr.expresslove.utils.ObjectUtils;
import com.wy.lpr.expresslove.utils.PlayAudioUtil;
import com.wy.lpr.expresslove.utils.RandomUtil;
import com.wy.lpr.expresslove.utils.SharedPreferencesUtils;
import com.wy.lpr.expresslove.utils.StatusBarUtil;
import com.wy.lpr.expresslove.utils.ToastUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.wy.lpr.expresslove.utils.DateUtil.parseTime;

public class CommonAudioActivity extends AppCompatActivity {
    private static final String TAG = "CommonAudioActivity";
    public static PlayAudioUtil mPlayAudioUtil;
    private List<Song> mList;//歌曲列表
    public int mCurrentPosition;
    private MediaPlayer mMediaPlayer;//音频播放器
    private RxPermissions mRxPermissions;//权限请求
    private String mIsFirstOpen = null;
    private Context mContext;
    private int mAppOpen;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = MyApplication.getContext();
        //播放背景音乐
        /*if (mPlayAudioUtil == null){
            mPlayAudioUtil = new PlayAudioUtil(MyApplication.getContext());
            mPlayAudioUtil.startAudioPlayer();
        }*/
        /*ActionBar actionBar = getSupportActionBar();//后面几行都用于隐藏标题栏
        if (actionBar != null) {
            Log.i(TAG, "onCreate: actionBar height = " + actionBar.getHeight());
            actionBar.hide();
        }*/
        /*getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();*/

        /*if (Build.VERSION.SDK_INT >= 21) {//设置顶部状态栏为半透明
            getWindow().setFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }*/
        //透明状态栏
        StatusBarUtil.transparencyBar(this);
        mRxPermissions = new RxPermissions(this);
        mMediaPlayer = SingleMediaPlayer.getINSTANCE();
        mAppOpen = SharedPreferencesUtils.getInt(mContext, Constant.MUSIC_DATA_SP, Constant.APP_OPEN, 1);
        mIsFirstOpen = SharedPreferencesUtils.getString(mContext, Constant.MUSIC_DATA_SP, Constant.MUSIC_DATA_FIRST, "yes");
        if (mIsFirstOpen.equals("yes")) {
            permissionRequest();
        } else {
            if (mAppOpen == 1) {
                initMusic();
                SharedPreferencesUtils.putInt(mContext, Constant.MUSIC_DATA_SP, Constant.APP_OPEN, 0);
            }
        }
    }

    private void initMusic() {

        mList = new ArrayList<>();//实例化
        //数据赋值
        mList = MusicUtils.getMusicData(this);//将扫描到的音乐赋值给音乐列表
        if (!ObjectUtils.isEmpty(mList)) {
            mCurrentPosition = RandomUtil.random(mList.size());
            SharedPreferencesUtils.putString(mContext, Constant.MUSIC_DATA_SP, Constant.MUSIC_DATA_FIRST, "no");
            Log.i(TAG, "initMusic: mCurrentPosition = " + mCurrentPosition);
        } else {
            mCurrentPosition = -1;
        }
        if (mCurrentPosition != -1 && !mMediaPlayer.isPlaying()) {

            changeMusic(mCurrentPosition);
        }

    }

    //切歌
    private void changeMusic(int position) {
        SharedPreferencesUtils.putInt(mContext, Constant.MUSIC_DATA_SP, Constant.MUSIC_CURRENT_ITEM_POSITION, mCurrentPosition);
        Log.i(TAG, "position:" + position);
        setMediaCompletion();//监听音乐播放完毕事件，自动下一曲
        try {
            // 切歌之前先重置，释放掉之前的资源
            mMediaPlayer.reset();
            // 设置播放源
            Log.i(TAG, "setDataSource:" + mList.get(position).path);
            mMediaPlayer.setDataSource(mList.get(position).path);

            // 开始播放前的准备工作，加载多媒体资源，获取相关信息
            mMediaPlayer.prepare();
            // 开始播放
            mMediaPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    //监听音乐播放完毕事件，自动下一曲
    private void setMediaCompletion() {
        mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                Log.i(TAG, "onCompletion: ");
                if (mCurrentPosition > mList.size() - 1) {
                    mCurrentPosition = -1;
                }
                changeMusic(++mCurrentPosition);
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
                        ToastUtils.showShortToast(CommonAudioActivity.this, "未授权");
                    }
                });
    }

}
