package com.wy.lpr.expresslove.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class AnimationView {
    private static final String TAG = "AnimationView";
    private static final boolean DEBUG = true;
    private static final int MSG_START = 0xf1;
    private static final int MSG_STOP = 0xf2;
    private static final int STATE_STOP = 0xf3;
    private static final int STATE_RUNNING = 0xf4;

    private int mState = STATE_RUNNING;
    private ImageView mImageView;
    private List<Integer> mResourceIdList = null;
    private Timer mTimer = null;
    private AnimTimerTask mTimeTask = null;
    private int mFrameIndex = 0;
    private boolean mIsLooping = false;
    private AnimCallback mCallBack;
    private Context mContext;
    private Bitmap mLastBitmap;
    private long mStartTime;

    private int mTotalResourceNumber = 0;
    private String mStringID;

    public AnimationView() {
        mTimer = new Timer();
    }

    /**
     *
     */
    public void setAnimation(Context context,ImageView imageview, List<Integer> resourceIdList, String stringId) {
        if (imageview == null) {
            if (DEBUG) Log.d(TAG, "setAnimation---stringId = " + stringId);
        }
        mContext = context;
        mImageView = imageview;
        mResourceIdList = resourceIdList;
        mTotalResourceNumber = mResourceIdList.size();
        mStringID = stringId;
        if (mContext != null) {
            mImageView.setBackgroundResource(mResourceIdList.get(0));
        }
    }

    /**
     *
     */
    public void start(boolean loop, int duration) {
        if (DEBUG)
            Log.d(TAG, "start mTotalResourceNumber = " + mTotalResourceNumber + "---mStringID = " + mStringID);
        mStartTime = System.currentTimeMillis();
        mIsLooping = loop;
        mFrameIndex = 0;
        mState = STATE_RUNNING;
        if (mTimer != null) {
            if (mTimeTask != null) {
                mTimeTask.cancel();
            }
            mTimeTask = new AnimTimerTask();
            mTimer.schedule(mTimeTask, 0, duration);
        }
    }

    /**
     *
     */
    public void stop() {
        if (DEBUG)
            Log.d(TAG, "stop mTotalResourceNumber = " + mTotalResourceNumber + "---mStringID = " + mStringID);
        if (mTimeTask != null) {
            /**
             * author:luobin
             * date:20160705
             * 128_P003741 车辆设置界面下方位置没有车辆图标（Once）
             * 每次进入设置界面，都会播放“倒车外后视镜翻转的动画”，播放动画是一个过程
             * 当在此过程中被强制stop的时候，就会出现异常。
             * 根据类AnimTimerTask发送stop逻辑，我们在它没有正常播放完动画就被强制
             * 停止的情况下，将动画的最后一张图片置为背景图片，这样就避免了动画被强制停止
             * 之后导致的显示空白问题。
             */
//            if(mFrameIndex != (mResourceIdList.size() -1)){
//                mImageView.setBackgroundResource(mResourceIdList.get(0));
//            }
            mFrameIndex = 0;
            mState = STATE_STOP;
            mTimer.purge();
            mTimeTask.cancel();
            mTimeTask = null;
            mLastBitmap = ((BitmapDrawable) mImageView.getBackground()).getBitmap();
            recycleImage();
            long intervalTotalTime = System.currentTimeMillis() - mStartTime;
            Log.i(TAG, "SurfaceView time: intervalTotalTime == " + intervalTotalTime / 1000 + "s" + intervalTotalTime % 1000 + "ms");
            mImageView = null;
        }
    }

    public void setAnimCallback(AnimCallback animCallback) {
        mCallBack = animCallback;
    }

    /**
     *
     */
    private class AnimTimerTask extends TimerTask {
        @Override
        public void run() {
            if (mFrameIndex < 0 || mState == STATE_STOP) {
                return;
            }
            if (mFrameIndex < mResourceIdList.size()) {
                Message msg = AnimHandler.obtainMessage(MSG_START, 0, 0, null);
                msg.sendToTarget();
            } else {
                mFrameIndex = 0;
                if (!mIsLooping) {
                    Message msg = AnimHandler.obtainMessage(MSG_STOP, 0, 0, null);
                    msg.sendToTarget();
                    mState = STATE_STOP;
                }
            }
        }
    }

    private Handler AnimHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_START: {
                    if (mFrameIndex >= 0 && mFrameIndex < mResourceIdList.size() && mState == STATE_RUNNING) {
                        if (mImageView != null) {//for case 128_P001889
                            recycleImage();
                            mImageView.setBackground(setBitmap(mResourceIdList.get(mFrameIndex)));
                            mLastBitmap = ((BitmapDrawable) mImageView.getBackground()).getBitmap();
                        } else {
                            if (DEBUG) Log.d(TAG, "mImageView ==null");
                        }
                        mFrameIndex++;
                    }
                }
                break;
                case MSG_STOP: {
                    stop();
                    mCallBack.AnimStop();
                }
                break;
                default:
                    break;
            }
        }
    };

    public interface AnimCallback {
        void AnimStop();
    }

    private Drawable setBitmap(int resId) {
        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inPreferredConfig = Bitmap.Config.RGB_565;
        opt.inPurgeable = true;
        opt.inInputShareable = true;
        //获取资源图片
        Bitmap bitmap = null;
        try {
            InputStream is = mContext.getResources().openRawResource(resId);
            bitmap = BitmapFactory.decodeStream(is, null, opt);
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new BitmapDrawable(mContext.getResources(), bitmap);
    }

    private void recycleImage(){
        if (mLastBitmap != null) {
            mLastBitmap.recycle();
            mLastBitmap = null;
        }
    }

    public void replace(){
        if (mTimer != null) {
            mTimer.purge();
        }
        if (mTimeTask != null) {
            mTimeTask.cancel();
        }
        mTimer = null;
        mTimeTask = null;
    }
}
