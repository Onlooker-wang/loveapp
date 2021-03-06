package com.wy.lpr.expresslove.main.tab;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bigkoo.alertview.AlertView;
import com.bigkoo.alertview.OnItemClickListener;
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
import com.wy.imageviewer.ImageViewer;
import com.wy.imageviewer.loader.GlideImageLoader;
import com.wy.lpr.expresslove.R;
import com.wy.lpr.expresslove.app.MyApplication;
import com.wy.lpr.expresslove.main.FireworksActivity;
import com.wy.lpr.expresslove.main.LoginActivity;
import com.wy.lpr.expresslove.main.photo.PhotoActivity;
import com.wy.lpr.expresslove.main.photo.PhotoAdapter;
import com.wy.lpr.expresslove.music.MusicActivity;
import com.wy.lpr.expresslove.utils.Constant;
import com.wy.lpr.expresslove.utils.SharedPreferencesUtils;
import com.wy.lpr.expresslove.utils.StatusBarUtil;
import com.wy.lpr.expresslove.utils.heart.HeartLayout;
import com.wy.lpr.expresslove.utils.popwindow.MenuPopWindow;
import com.wy.lpr.expresslove.utils.popwindow.MenuPopWindowBean;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class PhotoFragment extends PagerFragment implements TakePhoto.TakeResultListener, InvokeListener {
    private static final String TAG = "PhotoFragment";
    private View mViewRoot;

    private TakePhoto mTakePhoto;
    private InvokeParam mInvokeParam;
    private Context mContext;
    private RecyclerView mRecyclerView;
    private PhotoAdapter mPhotoAdapter;
    private ImageView mMoreIv;

    private List<TImage> mSelectMedia = new ArrayList<>();
    private List<TImage> mAllImageForDelete = new ArrayList<>();
    private ArrayList<Uri> mUris = new ArrayList<>();
    private ArrayList<String> mPathList = new ArrayList<>();
    private String[] mImagePathFromSp, mOriginPathFromSp;
    private ArrayList<String> mOriginPathList = new ArrayList<>();
    private String mCurrentUserName;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mViewRoot = inflater.inflate(R.layout.fragment_photo, container, false);
        mContext = getContext();
        initView();
        initImage();
        return mViewRoot;
    }


    private void initView() {
        //??????RecyclerView
        mRecyclerView = (RecyclerView) mViewRoot.findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new GridLayoutManager(mContext, 3));
        mPhotoAdapter = new PhotoAdapter(mContext, mOnAddPicListener, onPicClickListener);
        mPhotoAdapter.setSelectMax(10000);
        mRecyclerView.setAdapter(mPhotoAdapter);

        mCurrentUserName = SharedPreferencesUtils.getString(mContext, Constant.USER_INFO_SP, Constant.CURRENT_USER_NAME, "");
        Log.i(TAG, "initView mCurrentUserName: " + mCurrentUserName);
        mMoreIv = (ImageView) mViewRoot.findViewById(R.id.more_iv);
        mMoreIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setMoreListener();
            }
        });
    }

    private void initImage() {
        //???SP????????????????????????????????????????????????
        mImagePathFromSp = SharedPreferencesUtils.getSharedPreferences(mContext, mCurrentUserName, "ImagePath");
        //???SP?????????????????????????????????????????????
        mOriginPathFromSp = SharedPreferencesUtils.getSharedPreferences(mContext, mCurrentUserName, "OriginPath");
        Log.i(TAG, "initImage mImagePathFromSp: " + Arrays.toString(mImagePathFromSp)
                + ",mOriginPathFromSp: " + Arrays.toString(mOriginPathFromSp));
        if (mImagePathFromSp != null) {
            //?????????????????????????????????????????????????????????Uri???????????????????????????getTImagesWithUris??????????????????TImage??????
            //??????????????????????????????list,?????????????????????SharedPreferences???
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
            //?????????????????????????????????????????????????????????????????????list???????????????????????????
            for (String originPath : mOriginPathFromSp) {
                Log.i(TAG, "onResume originPath: " + originPath);
                if (!originPath.equals("")) {
                    mOriginPathList.add(originPath);
                }
            }
        }

    }

    //?????????????????????
    private PhotoAdapter.onAddPicListener mOnAddPicListener = new PhotoAdapter.onAddPicListener() {
        @Override
        public void onAddPicClick(int type, int position) {
            switch (type) {
                case 0:
                    new AlertView("????????????", null, "??????", null,
                            new String[]{"??????", "??????????????????"},
                            mContext, AlertView.Style.ActionSheet, new OnItemClickListener() {
                        @Override
                        public void onItemClick(Object o, int position) {
                            TakePhoto takePhoto = getTakePhoto();
                            //??????TakePhoto????????????
                            File file = new File(Environment.getExternalStorageDirectory(), "/photo/" + System.currentTimeMillis() + ".jpg");
                            if (!file.getParentFile().exists()) file.getParentFile().mkdirs();
                            Uri imageUri = Uri.fromFile(file);

                            configCompress(takePhoto);
                            configTakePhotoOption(takePhoto);
                            switch (position) {
                                case 0:
                                    //????????????
                                    takePhoto.onPickFromCapture(imageUri);
                                    break;
                                case 1:
                                    //???????????????????????????????????????
                                    takePhoto.onPickMultiple(20);
                                    break;
                            }
                        }
                    }).show();
                    break;
                case 1:
                    // ????????????
                    mSelectMedia.remove(position);
                    //???????????????????????????
                    mPathList.remove(position);
                    //?????????????????????
                    mOriginPathList.remove(position);
                    Log.i(TAG, "onAddPicClick: mPathList = " + mPathList + ",mOriginPathList = " + mOriginPathList);
                    mPhotoAdapter.notifyItemRemoved(position);
                    //????????????????????????????????????SP
                    String[] path = (String[]) mPathList.toArray(new String[0]);
                    SharedPreferencesUtils.setSharedPreferences(mContext, mCurrentUserName, "ImagePath", path);
                    String[] originPath = (String[]) mOriginPathList.toArray(new String[0]);
                    SharedPreferencesUtils.setSharedPreferences(mContext, mCurrentUserName, "OriginPath", originPath);
                    Log.i(TAG, "onAddPicClick: path = " + Arrays.toString(path) + ",originPath = " + Arrays.toString(originPath));
                    Log.i(TAG, "onAddPicClick SharedPreferences: mPathList = " +
                            Arrays.toString(SharedPreferencesUtils.getSharedPreferences(mContext, mCurrentUserName, "ImagePath"))
                            + ",mOriginPathList = " + Arrays.toString(SharedPreferencesUtils.getSharedPreferences(mContext, mCurrentUserName, "OriginPath")));
                    break;
            }
        }
    };

    //??????????????????
    private PhotoAdapter.onPicClickListener onPicClickListener = new PhotoAdapter.onPicClickListener() {
        @Override
        public void onPicClick(View view, int position) {
            //startPhotoActivity(PhotoActivity.this, (ImageView) view, position);
            // data ??????????????????List??????????????????????????????????????????{@link Uri}, {@code url}, {@code path},{@link File}, {@link DrawableRes resId}??????
            ImageViewer.load(mOriginPathList)//??????????????????????????????????????????
                    .selection(position)//??????????????????
                    .indicator(true)//???????????????????????????????????????
                    .imageLoader(new GlideImageLoader())//????????????*?????????????????????????????????GlideImageLoader???PicassoImageLoader????????????????????????
//                      .imageLoader(new PicassoImageLoader())
                    .theme(R.style.ImageViewerTheme)//??????????????????????????????R.style.ImageViewerTheme
                    .orientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)//??????????????????,?????????ActivityInfo.SCREEN_ORIENTATION_BEHIND
                    .start(PhotoFragment.this, view);
        }
    };

    //??????????????????
    private void deleteAllPic() {
        //??????Adapter????????????
        mSelectMedia.removeAll(mAllImageForDelete);
        mPhotoAdapter.notifyDataSetChanged();
        //??????SharedPreferences???????????????????????????????????????????????????
        SharedPreferencesUtils.deleteDataForSp(mContext, mCurrentUserName);
    }

    private void setMoreListener() {
        int[] icons = {R.drawable.ic_delete};
        String[] texts = {"????????????"};
        List<MenuPopWindowBean> list = new ArrayList<>();
        MenuPopWindowBean bean;
        for (int i = 0; i < icons.length; i++) {
            bean = new MenuPopWindowBean();
            bean.setIcon(icons[i]);
            bean.setText(texts[i]);
            list.add(bean);
        }
        final MenuPopWindow pw = new MenuPopWindow(getActivity(), list);
        pw.setOutsideTouchable(true);
        pw.setOnItemClick(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Adapter adapter = parent.getAdapter();
                MenuPopWindowBean thisBean = (MenuPopWindowBean) adapter.getItem(position);
                Log.i("more", "onItemClick: " + thisBean.getText());
                switch (position) {
                    case 0:
                        deleteAllPic();
                        pw.dismiss();
                        break;
                }
            }

        });
        pw.showPopUpWindow(mMoreIv);
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
        saveAllImages(result.getImages());
    }

    private void saveAllImages(ArrayList<TImage> images) {
        for (int i = 0; i < images.size(); i++) {
            if (images.get(i).getCompressPath() != null) {
                mAllImageForDelete.add(images.get(i));
            }
        }
    }

    //????????????????????????????????????
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
        SharedPreferencesUtils.setSharedPreferences(mContext, mCurrentUserName, "ImagePath", path);

        String[] originPath = (String[]) mOriginPathList.toArray(new String[0]);
        SharedPreferencesUtils.setSharedPreferences(mContext, mCurrentUserName, "OriginPath", originPath);

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
                mAllImageForDelete.add(images.get(i));
            }

        }
        Log.i(TAG, "showSaveImg mSelectMedia: " + mSelectMedia);
        if (mSelectMedia != null) {
            mPhotoAdapter.setList(mSelectMedia);
            mPhotoAdapter.notifyDataSetChanged();
        }
    }

    //??????Takephoto ??????TakePhoto???????????????   ????????????????????????
    private void configTakePhotoOption(TakePhoto takePhoto) {
        TakePhotoOptions.Builder builder = new TakePhotoOptions.Builder();
        builder.setWithOwnGallery(true);
        builder.setCorrectImage(true);
        takePhoto.setTakePhotoOptions(builder.create());
    }

    //??????takephoto?????????????????????
    private void configCompress(TakePhoto takePhoto) {
        CompressConfig config;
        config = new CompressConfig.Builder()
                .setMaxSize(1024)
                .setMaxPixel(800)
                .enableReserveRaw(true)
                .create();
        takePhoto.onEnableCompress(config, false);
    }

    /**
     * ??????TakePhoto??????
     */
    public TakePhoto getTakePhoto() {
        if (mTakePhoto == null) {
            mTakePhoto = (TakePhoto) TakePhotoInvocationHandler.of(this).bind(new TakePhotoImpl(this, this));
        }
        return mTakePhoto;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        getTakePhoto().onSaveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        getTakePhoto().onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionManager.TPermissionType type = PermissionManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionManager.handlePermissionsResult(getActivity(), type, mInvokeParam, this);
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
