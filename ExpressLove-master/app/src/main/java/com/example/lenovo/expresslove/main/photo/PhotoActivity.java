package com.example.lenovo.expresslove.main.photo;
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
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;


import com.bigkoo.alertview.AlertView;
import com.bigkoo.alertview.OnItemClickListener;
import com.example.lenovo.expresslove.R;
import com.example.lenovo.expresslove.base.CommonAudioActivity;
import com.example.lenovo.expresslove.utils.SharedPreferencesUtils;
import com.example.lenovo.expresslove.zoom.DragPhotoActivity;
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
import java.util.List;

/**
 * Created by wangya on 2022/2/16.
 */
public class PhotoActivity extends CommonAudioActivity implements TakePhoto.TakeResultListener, InvokeListener {
    private static final String TAG = "PhotoActivity";
    private TakePhoto mTakePhoto;
    private InvokeParam mInvokeParam;
    private Context mContext;


    private RecyclerView mRecyclerView;
    private PhotoAdapter mPhotoAdapter;

    private List<TImage> mSelectMedia = new ArrayList<>();
    private ArrayList<Uri> mUris = new ArrayList<>();
    private ArrayList<String> mPathList = new ArrayList<>();
    private String[] mImagePathFromSp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getTakePhoto().onCreate(savedInstanceState);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);
        mContext = getApplicationContext();
        //设置RecyclerView
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new GridLayoutManager(mContext, 3));
        mPhotoAdapter = new PhotoAdapter(mContext, onAddPicListener, onPicClickListener);
        mPhotoAdapter.setSelectMax(10000);
        mRecyclerView.setAdapter(mPhotoAdapter);

        initImage();
    }


    private void initImage() {
        //SharedPreferencesUtils.deleteDataForSp(mContext);
        mImagePathFromSp = SharedPreferencesUtils.getSharedPreferences(mContext, "ImagePath");
        Log.i(TAG, "initImage mImagePathFromSp: " + mImagePathFromSp);
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

    }

    //加号的点击事件
    private PhotoAdapter.onAddPicListener onAddPicListener = new PhotoAdapter.onAddPicListener() {
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
            startPhotoActivity(PhotoActivity.this, (ImageView) view);
        }
    };

    //图片点击所做的操作
    public void startPhotoActivity(Context context, ImageView imageView) {
        Intent intent = new Intent(context, DragPhotoActivity.class);
        int location[] = new int[2];
        imageView.getLocationOnScreen(location);
        intent.putExtra("left", location[0]);
        intent.putExtra("top", location[1]);
        intent.putExtra("height", imageView.getHeight());
        intent.putExtra("width", imageView.getWidth());

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
            Log.i(TAG, "showImg getCompressPath: " + images.get(i).getCompressPath());
            if (images.get(i).getCompressPath() != null) {
                mSelectMedia.add(images.get(i));
                mPathList.add(images.get(i).getCompressPath());
            }
        }
        String[] path = (String[]) mPathList.toArray(new String[0]);
        SharedPreferencesUtils.setSharedPreferences(mContext, "ImagePath", path);

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
                .setMaxSize(102400)
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
     * 没有继承TakePhotoActivity 所写
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
