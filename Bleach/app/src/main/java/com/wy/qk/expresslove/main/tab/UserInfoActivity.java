package com.wy.qk.expresslove.main.tab;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.airbnb.lottie.LottieAnimationView;
import com.wy.imageviewer.ImageViewer;
import com.wy.imageviewer.loader.GlideImageLoader;
import com.wy.qk.expresslove.R;
import com.wy.qk.expresslove.app.MyApplication;
import com.wy.qk.expresslove.base.CommonAudioActivity;
import com.wy.qk.expresslove.utils.Constant;
import com.wy.qk.expresslove.utils.SharedPreferencesUtils;
import com.wy.qk.expresslove.utils.StatusBarUtil;

import java.io.IOException;

public class UserInfoActivity extends CommonAudioActivity {
    private static final String TAG = "UserInfoActivity";
    private RelativeLayout mHeadLayout, mIdLayout;
    private AvatarImageView mAvatarImageView;
    private Context mContext;
    private String mCurrentUserName;
    private TextView mUserName, mIdText;
    private Uri mHeadImageUri;
    private String mUriStr;
    private LottieAnimationView mLottieAnimationView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtil.StatusBarLightMode(this);
        setContentView(R.layout.activity_user_info);
        mContext = MyApplication.getContext();
        mUriStr = SharedPreferencesUtils.getString(mContext, Constant.HEAD_IMAGE_URI_SP, Constant.HEAD_IMAGE_URI, "");
        mHeadImageUri = Uri.parse(mUriStr);
        Log.i(TAG, "onCreate: mUriStr = " + mUriStr + ",mHeadImageUri = " + mHeadImageUri);
        initView();
        setListener();
    }

    private void initView() {
        mCurrentUserName = SharedPreferencesUtils.getString(mContext, Constant.USER_INFO_SP, Constant.CURRENT_USER_NAME, "");
        mAvatarImageView = (AvatarImageView) findViewById(R.id.head_img);
        mHeadLayout = (RelativeLayout) findViewById(R.id.head_layout);
        mIdLayout = (RelativeLayout) findViewById(R.id.id_layout);
        mUserName = (TextView) findViewById(R.id.user_name);
        mUserName.setText(mCurrentUserName);
        mIdText = (TextView) findViewById(R.id.id_tv);
        //设置json动画
        mLottieAnimationView = (LottieAnimationView) findViewById(R.id.lottie);
        mLottieAnimationView.setImageAssetsFolder("images/");
        mLottieAnimationView.setAnimation("zk.json");
        mLottieAnimationView.playAnimation();

        if (mUriStr != null && !mUriStr.equals("")) {
            Log.i(TAG, "initView: mUriStr = " + mUriStr);
            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), mHeadImageUri);
            } catch (IOException e) {
                e.printStackTrace();
            }
            mAvatarImageView.setImageBitmap(bitmap);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        String userId = SharedPreferencesUtils.getString(mContext, Constant.USER_ID_SP, Constant.CURRENT_USER_ID, "");
        Log.i(TAG, "onResume: userId = " + userId);
        if (userId.equals("")) {
            mIdText.setText(mCurrentUserName);
        } else {
            mIdText.setText(userId);
        }
    }

    private void setListener() {
        mHeadLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAvatarImageView.setHeadLayoutListener();
            }
        });

        mIdLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UserInfoActivity.this, RenameActivity.class));
            }
        });

        mAvatarImageView.setAfterCropListener(new AvatarImageView.AfterCropListener() {
            @Override
            public void afterCrop(Bitmap photo) {
                Toast.makeText(UserInfoActivity.this, "设置新的头像成功", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onClick(Uri uri, int resId) {
                loadImageView(uri, resId);
            }
        });
    }


    private void loadImageView(Uri uri, int resId) {
        Log.i(TAG, "loadImageView: uri = " + uri + ",resId = " + resId);
        // data 可以多张图片List或单张图片，支持的类型可以是{@link Uri}, {@code url}, {@code path},{@link File}, {@link DrawableRes resId}…等
        if (uri != null) {
            ImageViewer.load(uri)//要加载的原图数据，单张或多张
                    .selection(0)//当前选中位置
                    .indicator(true)//是否显示指示器，默认不显示
                    .imageLoader(new GlideImageLoader())//加载器，*必须配置，目前内置的有GlideImageLoader或PicassoImageLoader，也可以自己实现
//                      .imageLoader(new PicassoImageLoader())
                    .theme(R.style.ImageViewerTheme)//设置主题风格，默认：R.style.ImageViewerTheme
                    .orientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)//设置屏幕方向,默认：ActivityInfo.SCREEN_ORIENTATION_BEHIND
                    .start(UserInfoActivity.this, mAvatarImageView);
        } else {
            ImageViewer.load(resId)//要加载的原图数据，单张或多张
                    .selection(0)//当前选中位置
                    .indicator(true)//是否显示指示器，默认不显示
                    .imageLoader(new GlideImageLoader())//加载器，*必须配置，目前内置的有GlideImageLoader或PicassoImageLoader，也可以自己实现
//                      .imageLoader(new PicassoImageLoader())
                    .theme(R.style.ImageViewerTheme)//设置主题风格，默认：R.style.ImageViewerTheme
                    .orientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)//设置屏幕方向,默认：ActivityInfo.SCREEN_ORIENTATION_BEHIND
                    .start(UserInfoActivity.this, mAvatarImageView);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mAvatarImageView.onActivityResult(requestCode, resultCode, data);
        mHeadImageUri = mAvatarImageView.mOriginUri;
        SharedPreferencesUtils.putString(mContext, Constant.HEAD_IMAGE_URI_SP,
                Constant.HEAD_IMAGE_URI, mHeadImageUri.toString());
    }
}
