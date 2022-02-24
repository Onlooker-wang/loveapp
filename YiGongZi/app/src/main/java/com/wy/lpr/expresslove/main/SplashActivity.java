package com.wy.lpr.expresslove.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.wy.lpr.expresslove.R;
import com.wy.lpr.expresslove.base.CommonAudioActivity;
import com.wy.lpr.expresslove.main.photo.PhotoActivity;
import com.wy.lpr.expresslove.utils.Constant;
import com.wy.lpr.expresslove.utils.RandomUtil;
import com.wy.lpr.expresslove.utils.SharedPreferencesUtils;
import com.wy.lpr.expresslove.utils.SpUtils;
import com.wy.lpr.expresslove.utils.textstyle.ShapeRevealLoopSample;

import java.util.Arrays;

import su.levenetc.android.textsurface.TextSurface;


/**
 * @desc 启动屏
 * Created by wangya on 2022/2/18.
 */
public class SplashActivity extends CommonAudioActivity {
    private static final String TAG = "SplashActivity";

    private ImageView mSplashIv;
    private String[] mImageUrl;
    private Context mContext;
    private String[] mLocalImagePath;
    private TextSurface mTextSurface;
    private String[] mUserName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 判断是否是第一次开启应用
        boolean isFirstOpen = SpUtils.getBoolean(this, MyApplication.FIRST_OPEN);
        // 如果是第一次启动，则先进入功能引导页
        /*if (!isFirstOpen) {
            Intent intent = new Intent(this, WelcomeGuideActivity.class);
            startActivity(intent);
            finish();
            return;
        }*/

        mContext = MyApplication.getContext();
        mUserName = SharedPreferencesUtils.getSharedPreferences(mContext, Constant.USER_INFO_SP, Constant.USER_NAME);
        int count = mUserName.length;
        int a = RandomUtil.random(count);
        mLocalImagePath = SharedPreferencesUtils.getSharedPreferences(mContext, mUserName[a], "OriginPath");
        //mImageUrl = SharedPreferencesUtils.getSharedPreferences(mContext, "ImageUrl");
        //Log.i(TAG, "onCreate mImageUrl: " + Arrays.toString(mImageUrl));
        int bound = mLocalImagePath.length;
        int i = RandomUtil.random(bound);
        Log.i(TAG, "onCreate: mLocalImagePath: " + Arrays.toString(mLocalImagePath)
                + ",length: " + bound + ",random i = " + i);

        // 如果不是第一次启动app，则正常显示启动屏
        setContentView(R.layout.activity_splash);

        mSplashIv = (ImageView) findViewById(R.id.iv_splash);
        mTextSurface = (TextSurface) findViewById(R.id.welcome_tv);
        //由于网络原因，有时加载不出图片，改从本地获取
        /*if (mImageUrl != null && !mImageUrl[i].equals("")) {
            Glide.with(mContext).load(mImageUrl[i]).into(mSplashIv);
        }*/
        if (mLocalImagePath != null && !mLocalImagePath[i].equals("")) {
            Glide.with(mContext).load(mLocalImagePath[i]).into(mSplashIv);
        } else {
            mSplashIv.setImageResource(R.drawable.wangye);
        }

        showTextView();


        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                enterHomeActivity();
            }
        }, 2000);
    }

    private void enterHomeActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private void showTextView() {
        mTextSurface.reset();
//		CookieThumperSample.play(mTextSurface, getAssets());
//		AlignSample.play(mTextSurface);
//		ColorSample.play(mTextSurface);
//		Rotation3DSample.play(mTextSurface);//渐渐消失又浮现
//		ScaleTextSample.run(mTextSurface);// 字体放大复原
        ShapeRevealLoopSample.play(mTextSurface);
//		ShapeRevealSample.play(mTextSurface);
//		SlideSample.play(mTextSurface);
//		SurfaceScaleSample.play(mTextSurface);
//		SurfaceTransSample.play(mTextSurface);
    }
}
