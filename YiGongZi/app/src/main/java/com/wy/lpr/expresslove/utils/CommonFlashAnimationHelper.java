package com.wy.lpr.expresslove.utils;


import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

public class CommonFlashAnimationHelper {
    private static final String TAG = "CommonFlashAnimationHelper";

    private Context mContext;
    private static ImageView mItemImg;
    private static Animator mSplashAnimator;
    private static CommonFlashAnimationHelper INSTANCE;
    private static int mResId;

    private CommonFlashAnimationHelper(Context context) {
        mContext = context;

    }

    public static CommonFlashAnimationHelper getInstance(Context context) {
        if (null == INSTANCE) {
            INSTANCE = new CommonFlashAnimationHelper(context);
        }
        return INSTANCE;
    }

    public static void showSplash(ImageView imageView, int resId) {
        mItemImg = imageView;
        mResId = resId;
        imageView.setVisibility(View.VISIBLE);
        imageView.setImageResource(resId);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setAlpha(1.0f);
        if (mSplashAnimator != null) {
            mSplashAnimator.cancel();
            mSplashAnimator.removeAllListeners();
        }
        mSplashAnimator = ObjectAnimator.ofFloat(imageView, "alpha", 1f, 0.1f, 1f, 0.1f, 1.0f, 0.1f, 1.0f);
        mSplashAnimator.setDuration(5000);
        mSplashAnimator.addListener(new Animator.AnimatorListener() {
            @SuppressLint("LongLogTag")
            @Override
            public void onAnimationStart(Animator animator) {
                Log.d(TAG, "show splash start");

            }

            @SuppressLint("LongLogTag")
            @Override
            public void onAnimationEnd(Animator animator) {
                Log.d(TAG, "show splash end");
                showSplash(mItemImg, mResId);

            }

            @SuppressLint("LongLogTag")
            @Override
            public void onAnimationCancel(Animator animator) {
                Log.d(TAG, "show splash cancel");
            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
        mSplashAnimator.start();
    }

    public static void destroySplashAnimator() {
        if (mSplashAnimator != null) {
            mSplashAnimator.cancel();
            mSplashAnimator.removeAllListeners();
        }
    }

}
