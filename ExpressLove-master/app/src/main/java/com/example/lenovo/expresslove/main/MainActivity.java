package com.example.lenovo.expresslove.main;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Typeface;
import android.os.Bundle;

import android.os.Handler;
import android.os.Message;
import android.view.View;

import com.example.lenovo.expresslove.R;
import com.example.lenovo.expresslove.base.CommonAudioActivity;
import com.example.lenovo.expresslove.utils.DeviceInfo;
import com.example.lenovo.expresslove.utils.ImageUtils;
import com.example.lenovo.expresslove.utils.TypeTextView2;
import com.example.lenovo.expresslove.utils.bluesnow.FlowerView;
import com.example.lenovo.expresslove.utils.factory.ImageNameFactory;
import com.example.lenovo.expresslove.utils.whitesnow.SnowView;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Func1;
import rx.schedulers.Schedulers;


public class MainActivity extends CommonAudioActivity {

    private static final String LOVE1 = "人生只有两次幸运就好，一次遇见你，一次走到底，爱虽然会迟到但不会缺席，晚点遇见你，余生都是你";
    private static final int SNOW_BLOCK = 1;
    private static final String JPG = ".jpg";

    private Canvas mCanvas;
    private int mCounter;
    private Bitmap mManyBitmapSuperposition;
    private SnowView mWhiteSnowView;//白色的雪花
    private Timer myTimer = null;
    private TimerTask mTask = null;
    private FlowerView mBlueSnowView;//蓝色的雪花
    private Handler mHandler = new Handler() {
        public void dispatchMessage(Message msg) {
            mBlueSnowView.inva();
        }
    };
    private TypeTextView2 mTypeTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        delayShow(100);
        goToNext();
    }

    private void initView() {
        mWhiteSnowView = (SnowView) findViewById(R.id.whiteSnowView);
        //mWhiteSnowView.setVisibility(View.VISIBLE);
        mTypeTextView = (TypeTextView2) findViewById(R.id.love_sentence);
        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/myttf.ttf");
        mTypeTextView.setTypeface(typeface, Typeface.ITALIC);
        mBlueSnowView = (FlowerView) findViewById(R.id.flowerview);
        mBlueSnowView.setWH(DeviceInfo.mScreenWidthForPortrait, DeviceInfo.mScreenHeightForPortrait, DeviceInfo.mDensity);
        mBlueSnowView.loadFlower();
        mBlueSnowView.addRect();
        myTimer = new Timer();
        mTask = new TimerTask() {
            public void run() {
                Message msg = new Message();
                msg.what = SNOW_BLOCK;
                mHandler.sendMessage(msg);
            }
        };
        rxJavaSolveMiZhiSuoJinAndNestedLoopAndCallbackHell();
        myTimer.schedule(mTask, 100, 10);
    }

    private void cancelTimer() {
        if (myTimer != null) {
            myTimer.cancel();
            myTimer = null;
        }
        if (mTask != null) {
            mTask.cancel();
            mTask = null;
        }
    }

    private void goToNext() {
        new Handler().postDelayed(new Runnable() { // 为了减少代码使用匿名Handler创建一个延时的调用
            public void run() {
                Intent i = new Intent(MainActivity.this, DrawHeartActivity.class);
                MainActivity.this.startActivity(i);
                MainActivity.this.finish();
            }
        }, 10000);//10秒刚好
    }

    private void delayShow(long time) {
        Observable.timer(time, TimeUnit.MILLISECONDS, AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Long>() {
                    public void onCompleted() {
                        mTypeTextView.start(LOVE1);
                    }

                    public void onError(Throwable e) {
                    }

                    public void onNext(Long aLong) {
                    }
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        cancelTimer();
    }

    private void rxJavaSolveMiZhiSuoJinAndNestedLoopAndCallbackHell() {
        Observable.from(ImageNameFactory.getAssetImageFolderName())
                .flatMap(new Func1<String, Observable<String>>() {
                    public Observable<String> call(String folderName) {
                        return Observable.from(ImageUtils.getAssetsImageNamePathList(getApplicationContext(), folderName));
                    }
                }).filter(new Func1<String, Boolean>() {
            public Boolean call(String imagePathNameAll) {
                return Boolean.valueOf(imagePathNameAll.endsWith(JPG));
            }
        }).map(new Func1<String, Bitmap>() {
            public Bitmap call(String imagePathName) {
                return ImageUtils.getImageBitmapFromAssetsFolderThroughImagePathName(getApplicationContext(), imagePathName, DeviceInfo.mScreenWidthForPortrait, DeviceInfo.mScreenHeightForPortrait);
            }
        }).map(new Func1<Bitmap, Void>() {
            public Void call(Bitmap bitmap) {
                createSingleImageFromMultipleImages(bitmap, mCounter);
                mCounter = mCounter++;
                return null;
            }
        }).subscribeOn(Schedulers.io())
                .doOnSubscribe(new Action0() {
                    public void call() {
//                        mProgressBar.setVisibility(View.VISIBLE);
                    }
                }).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Void>() {
                    public void onCompleted() {
                        /*mImageView.setImageBitmap(mManyBitmapSuperposition);
                        mProgressBar.setVisibility(View.GONE);
                        showAllViews();*/
                    }

                    public void onError(Throwable e) {
                    }

                    public void onNext(Void aVoid) {
                    }
                });
    }

    private void createSingleImageFromMultipleImages(Bitmap bitmap, int mCounter) {
        if (mCounter == 0) {
            //TODO:Caused by: java.lang.OutOfMemoryError
            try {
                mManyBitmapSuperposition = Bitmap.createBitmap(DeviceInfo.mScreenWidthForPortrait, DeviceInfo.mScreenHeightForPortrait, bitmap.getConfig());
                mCanvas = new Canvas(mManyBitmapSuperposition);
            } catch (OutOfMemoryError error) {
                error.printStackTrace();
                System.gc();
            } finally {

            }
        }
        if (mCanvas != null) {
            int number = DeviceInfo.mScreenHeightForPortrait / 64;
            if (mCounter >= (mCounter / number) * number && mCounter < ((mCounter / number) + SNOW_BLOCK) * number) {
                mCanvas.drawBitmap(bitmap, (float) ((mCounter / number) * 64), (float) ((mCounter % number) * 64), null);
            }
        }
    }

}




