package com.wy.lpr.expresslove.main.photo;
////////////////////////////////////////////////////////////////////
//                          _ooOoo_                               //
//                         o8888888o                              //
//                         88" . "88                              //
//                         (| ^_^ |)                              //
//                         O\  =  /O                              //
//                      ____/`---'\____                           //
//                    .'  \\|     |//  `.                         //
//                   /  \\|||  :  |||//  \                        //
//                  /  _||||| -:- |||||-  \                       //
//                  |   | \\\  -  /// |   |                       //
//                  | \_|  ''\---/''  |   |                       //
//                  \  .-\__  `-`  ___/-. /                       //
//                ___`. .'  /--.--\  `. . ___                     //
//              ."" '<  `.___\_<|>_/___.'  >'"".                  //
//            | | :  `- \`.;`\ _ /`;.`/ - ` : | |                 //
//            \  \ `-.   \_ __\ /__ _/   .-` /  /                 //
//      ========`-.____`-.___\_____/___.-`____.-'========         //
//                           `=---='                              //
//      ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^        //
//         佛祖保佑       永无BUG     永不修改                  //
////////////////////////////////////////////////////////////////////

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;


import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bigkoo.alertview.AlertView;
import com.bigkoo.alertview.OnItemClickListener;
import com.byd.imageviewer.ImageViewer;
import com.byd.imageviewer.loader.GlideImageLoader;
import com.wy.lpr.expresslove.R;
import com.wy.lpr.expresslove.base.CommonAudioActivity;
import com.wy.lpr.expresslove.main.FireworksActivity;
import com.wy.lpr.expresslove.utils.SharedPreferencesUtils;
import com.wy.lpr.expresslove.utils.heart.HeartLayout;
import com.wy.lpr.expresslove.zoom.DragPhotoActivity;
import com.jph.takephoto.app.TakePhoto;
import com.jph.takephoto.app.TakePhotoImpl;
import com.jph.takephoto.compress.CompressConfig;
import com.jph.takephoto.model.InvokeParam;
import com.jph.takephoto.model.TContextWrap;
import com.jph.takephoto.model.TImage;
import com.jph.takephoto.model.TResult;
import com.jph.takephoto.model.TakePhotoOptions;
import com.jph.takephoto.permission.InvokeListener;
import com.jph.takephoto.permission.PermissionManager;
import com.jph.takephoto.permission.TakePhotoInvocationHandler;
import com.jph.takephoto.uitl.TUtils;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Created by wangya on 2022/2/16.
 */
public class PhotoActivity extends CommonAudioActivity implements TakePhoto.TakeResultListener, InvokeListener {
    private static final String TAG = "PhotoActivity";
    private TakePhoto mTakePhoto;
    private InvokeParam mInvokeParam;
    private Context mContext;
    private Random mRandom = new Random();
    private Random mRandom2 = new Random();
    private RecyclerView mRecyclerView;
    private PhotoAdapter mPhotoAdapter;
    private HeartLayout mHeartLayout;//垂直方向的漂浮的红心

    private List<TImage> mSelectMedia = new ArrayList<>();
    private ArrayList<Uri> mUris = new ArrayList<>();
    private ArrayList<String> mPathList = new ArrayList<>();
    private String[] mImagePathFromSp, mOriginPathFromSp;
    private ArrayList<String> mOriginPathList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getTakePhoto().onCreate(savedInstanceState);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);
        mContext = getApplicationContext();
        //设置RecyclerView
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new GridLayoutManager(mContext, 3));
        mPhotoAdapter = new PhotoAdapter(mContext, mOnAddPicListener, onPicClickListener);
        mPhotoAdapter.setSelectMax(10000);
        mRecyclerView.setAdapter(mPhotoAdapter);

        initView();
        initImage();
    }


    private void initView() {
        mHeartLayout = (HeartLayout) findViewById(R.id.heart_o_red_layout);
        showRedHeartLayout();
    }

    private void initImage() {
        //SharedPreferencesUtils.deleteDataForSp(mContext);
        mImagePathFromSp = SharedPreferencesUtils.getSharedPreferences(mContext, "ImagePath");
        mOriginPathFromSp = SharedPreferencesUtils.getSharedPreferences(mContext, "OriginPath");
        Log.i(TAG, "initImage mImagePathFromSp: " + Arrays.toString(mImagePathFromSp)
                + ",mOriginPathFromSp: " + Arrays.toString(mOriginPathFromSp));
        if (mImagePathFromSp != null) {
            for (String path : mImagePathFromSp) {
                Log.i(TAG, "onResume imagePath: " + path);
                if (!path.equals("")) {
                    mUris.add(Uri.parse(path));
                    mPathList.add(path);
                }
                //mImageView.setImageURI(Uri.parse(path));
            }
            Log.i(TAG, "initImage mUris: " + mUris + ",mPathList: " + mPathList);
            showSaveImg(TUtils.getTImagesWithUris(mUris, TImage.FromType.OTHER));
        }

        if (mOriginPathFromSp != null) {
            for (String originPath : mOriginPathFromSp) {
                Log.i(TAG, "onResume originPath: " + originPath);
                if (!originPath.equals("")) {
                    mOriginPathList.add(originPath);
                }
            }
        }

    }

    private void showRedHeartLayout() {
        Observable.timer(400, TimeUnit.MILLISECONDS, AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Long>() {
                    public void onCompleted() {
                        mHeartLayout.setVisibility(View.VISIBLE);
                        mHeartLayout.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Log.i(TAG, "onClick: mHeartLayout");
                                startActivity(new Intent(PhotoActivity.this, FireworksActivity.class));
                            }
                        });
                        delayDo2();
                    }

                    public void onError(Throwable e) {
                    }

                    public void onNext(Long aLong) {
                    }
                });
    }

    private int randomColor() {
        return Color.rgb(mRandom.nextInt(255), mRandom.nextInt(255), mRandom.nextInt(255));
    }

    private void delayDo2() {
        Observable.timer((long) mRandom2.nextInt(200), TimeUnit.MILLISECONDS, AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Long>() {
                    public void onCompleted() {
                        mHeartLayout.addHeart(randomColor());
                        delayDo2();
                    }

                    public void onError(Throwable e) {
                    }

                    public void onNext(Long aLong) {

                    }
                });
    }

    //加号的点击事件
    private PhotoAdapter.onAddPicListener mOnAddPicListener = new PhotoAdapter.onAddPicListener() {
        @Override
        public void onAddPicClick(int type, int position) {
            switch (type) {
                case 0:
                    new AlertView("上传图片", null, "取消", null,
                            new String[]{"拍照", "从相册中选择"},
                            PhotoActivity.this, AlertView.Style.ActionSheet, new OnItemClickListener() {
                        @Override
                        public void onItemClick(Object o, int position) {
                            TakePhoto takePhoto = getTakePhoto();
                            //获取TakePhoto图片路径
                            File file = new File(Environment.getExternalStorageDirectory(), "/photo/" + System.currentTimeMillis() + ".jpg");
                            if (!file.getParentFile().exists()) file.getParentFile().mkdirs();
                            Uri imageUri = Uri.fromFile(file);

                            configCompress(takePhoto);
                            configTakePhotoOption(takePhoto);
                            switch (position) {
                                case 0:
                                    takePhoto.onPickFromCapture(imageUri);
                                    break;
                                case 1:
                                    //设置最多几张
                                    takePhoto.onPickMultiple(20);
                                    break;
                            }
                        }
                    }).show();
                    break;
                case 1:
                    // 删除图片
                    mSelectMedia.remove(position);
                    mPathList.remove(position);
                    mPhotoAdapter.notifyItemRemoved(position);
                    String[] path = (String[]) mPathList.toArray(new String[0]);
                    SharedPreferencesUtils.setSharedPreferences(mContext, "ImagePath", path);
                    break;
            }
        }
    };

    //图片点击事件
    private PhotoAdapter.onPicClickListener onPicClickListener = new PhotoAdapter.onPicClickListener() {
        @Override
        public void onPicClick(View view, int position) {
            //startPhotoActivity(PhotoActivity.this, (ImageView) view, position);
            // data 可以多张图片List或单张图片，支持的类型可以是{@link Uri}, {@code url}, {@code path},{@link File}, {@link DrawableRes resId}…等
            ImageViewer.load(mOriginPathList)//要加载的图片数据，单张或多张
                    .selection(position)//当前选中位置
                    .indicator(true)//是否显示指示器，默认不显示
                    .imageLoader(new GlideImageLoader())//加载器，*必须配置，目前内置的有GlideImageLoader或PicassoImageLoader，也可以自己实现
//                      .imageLoader(new PicassoImageLoader())
                    .theme(R.style.ImageViewerTheme)//设置主题风格，默认：R.style.ImageViewerTheme
                    .orientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)//设置屏幕方向,默认：ActivityInfo.SCREEN_ORIENTATION_BEHIND
                    .start(PhotoActivity.this, view);
        }
    };

    //图片点击所做的操作
    public void startPhotoActivity(Context context, ImageView imageView, int position) {
        Intent intent = new Intent(context, DragPhotoActivity.class);
        int location[] = new int[2];
        imageView.getLocationOnScreen(location);
        intent.putExtra("left", location[0]);
        intent.putExtra("top", location[1]);
        intent.putExtra("height", imageView.getHeight());
        intent.putExtra("width", imageView.getWidth());
        intent.putExtra("position", position);
        intent.putExtra("path", (Serializable) mSelectMedia);
        context.startActivity(intent);
        overridePendingTransition(0, 0);
    }

    @Override
    public void takeCancel() {
    }

    @Override
    public void takeFail(TResult result, String msg) {
    }

    @Override
    public void takeSuccess(TResult result) {
        showImg(result.getImages());
    }

    //图片成功后返回执行的方法
    private void showImg(ArrayList<TImage> images) {
        for (int i = 0; i < images.size(); i++) {
            Log.i(TAG, "showImg getCompressPath: " + images.get(i).getCompressPath()
                    + ",getOriginPath: " + images.get(i).getOriginalPath());
            if (images.get(i).getCompressPath() != null) {
                mSelectMedia.add(images.get(i));
                mPathList.add(images.get(i).getCompressPath());
            }
            if (images.get(i).getOriginalPath() != null) {
                mOriginPathList.add(images.get(i).getOriginalPath());
            }
        }
        String[] path = (String[]) mPathList.toArray(new String[0]);
        SharedPreferencesUtils.setSharedPreferences(mContext, "ImagePath", path);

        String[] originPath = (String[]) mOriginPathList.toArray(new String[0]);
        SharedPreferencesUtils.setSharedPreferences(mContext, "OriginPath", originPath);

        if (mSelectMedia != null) {
            mPhotoAdapter.setList(mSelectMedia);
            mPhotoAdapter.notifyDataSetChanged();
        }
    }

    private void showSaveImg(ArrayList<TImage> images) {
        for (int i = 0; i < images.size(); i++) {
            images.get(i).setCompressPath(mImagePathFromSp[i]);
            if (images.get(i).getCompressPath() != null) {
                Log.i(TAG, "showSaveImg getCompressPath: " + images.get(i).getCompressPath());
                mSelectMedia.add(images.get(i));
            }
        }
        Log.i(TAG, "showSaveImg mSelectMedia: " + mSelectMedia);
        if (mSelectMedia != null) {
            mPhotoAdapter.setList(mSelectMedia);
            mPhotoAdapter.notifyDataSetChanged();
        }
    }

    //设置Takephoto 使用TakePhoto自带的相册   照片旋转角度纠正
    private void configTakePhotoOption(TakePhoto takePhoto) {
        TakePhotoOptions.Builder builder = new TakePhotoOptions.Builder();
        builder.setWithOwnGallery(true);
        builder.setCorrectImage(true);
        takePhoto.setTakePhotoOptions(builder.create());
    }

    //设置takephoto的照片使用压缩
    private void configCompress(TakePhoto takePhoto) {
        CompressConfig config;
        config = new CompressConfig.Builder()
                .setMaxSize(1024)
                .setMaxPixel(800)
                .enableReserveRaw(true)
                .create();
        takePhoto.onEnableCompress(config, false);
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        getTakePhoto().onSaveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        getTakePhoto().onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionManager.TPermissionType type = PermissionManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionManager.handlePermissionsResult(this, type, mInvokeParam, this);
    }

    /**
     * 获取TakePhoto实例
     */
    public TakePhoto getTakePhoto() {
        if (mTakePhoto == null) {
            mTakePhoto = (TakePhoto) TakePhotoInvocationHandler.of(this).bind(new TakePhotoImpl(this, this));
        }
        return mTakePhoto;
    }

    @Override
    public PermissionManager.TPermissionType invoke(InvokeParam invokeParam) {
        PermissionManager.TPermissionType type = PermissionManager.checkPermission(TContextWrap.of(this), invokeParam.getMethod());
        if (PermissionManager.TPermissionType.WAIT.equals(type)) {
            this.mInvokeParam = invokeParam;
        }
        return type;
    }
}
