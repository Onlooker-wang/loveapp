package com.wy.qk.expresslove.main;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.TextView;


import com.wy.qk.expresslove.R;
import com.wy.qk.expresslove.app.MyApplication;
import com.wy.qk.expresslove.base.CommonAudioActivity;
import com.wy.qk.expresslove.utils.AnimationView;
import com.wy.qk.expresslove.utils.ClickUtils;
import com.wy.qk.expresslove.utils.CommomDialog;
import com.wy.qk.expresslove.utils.DeviceInfo;
import com.wy.qk.expresslove.utils.ImageUtils;
import com.wy.qk.expresslove.utils.ListUtil;
import com.wy.qk.expresslove.utils.NewRadioGroup;
import com.wy.qk.expresslove.utils.TypeTextView2;
import com.wy.qk.expresslove.utils.bluesnow.FlowerView;
import com.wy.qk.expresslove.utils.factory.ImageNameFactory;
import com.wy.qk.expresslove.utils.heart.HeartLayout;
import com.wy.qk.expresslove.utils.whitesnow.SnowView;

import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

import static com.wy.qk.expresslove.app.MyApplication.getContext;


public class PictureAnimActivity extends CommonAudioActivity implements AnimationView.AnimCallback {
    private static final String TAG = "Main4Activity";

    private static final String JPG = ".jpg";
    private static final String LOVE = "心动是等你的留言，渴望是常和你见面，甜蜜是和你小路流连，温馨是看着你清澈的双眼，爱你的感觉真的妙不可言！";
    private static final int SNOW_BLOCK = 1;
    public static final String URL = "file:///android_asset/index5.html";
    private Canvas mCanvas;
    private int mCounter;
    private FlowerView mBlueSnowView;//蓝色的雪花
    private Handler mHandler = new Handler() {
        public void dispatchMessage(Message msg) {
            mBlueSnowView.inva();
        }
    };
    private HeartLayout mHeartLayout;//垂直方向的漂浮的红心
    private ImageView mImageView;//图片
    private Bitmap mManyBitmapSuperposition;
    private ProgressBar mProgressBar;
    private Random mRandom = new Random();
    private Random mRandom2 = new Random();
    private TimerTask mTask = null;
    private TypeTextView2 mTypeTextView;//打字机
    private WebSettings mWebSettings;
    private WebView mWebView;
    private SnowView mWhiteSnowView;//白色的雪花
    private Timer myTimer = null;
    private NewRadioGroup mNewRadioGroup;
    private AnimationDrawable mAnimationDrawable;
    private AnimationView mAnimationView;
    private List<Integer> mAnimViewResourcesID;
    private ImageView mAnimView, mAnimVertical;
    private Context mContext;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DeviceInfo.getInstance().initializeScreenInfo(this);
        setContentView(R.layout.activity_pic_anim);
        mContext = MyApplication.getContext();
        initView();
        initWebView();


    }

    @Override
    protected void onResume() {
        super.onResume();
        //initData();
        //setNewRadioGroupListener();
        delayShowAll(0);
    }

    private FrameLayout mWebViwFrameLayout = null;
    private FrameLayout root_fragment_layout = null;
    private TextView textview = null;

    private void initWebView() {

        mWebSettings = mWebView.getSettings();
        mWebSettings.setJavaScriptEnabled(true);
        mWebSettings.setBuiltInZoomControls(false);
        mWebSettings.setLightTouchEnabled(false);
        mWebSettings.setSupportZoom(false);
        mWebView.setHapticFeedbackEnabled(false);
    }

    private void initView() {
        mWebViwFrameLayout = (FrameLayout) findViewById(R.id.fl_webView_layout);
        root_fragment_layout = (FrameLayout) findViewById(R.id.root_fragment_layout);
        textview = (TextView) findViewById(R.id.textview);
        mWebView = new WebView(getApplicationContext());
        mWebView.setScrollBarStyle(View.SCROLLBARS_OUTSIDE_OVERLAY);
        mWebView.setVisibility(View.GONE);

        mNewRadioGroup = (NewRadioGroup) findViewById(R.id.new_radio_group);

        mAnimView = (ImageView) findViewById(R.id.anim_view);
        mAnimVertical = (ImageView) findViewById(R.id.anim_vertical);
        //scrollbars

        FrameLayout.LayoutParams fp = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        fp.gravity = Gravity.CENTER;

        mWebViwFrameLayout.addView(mWebView);

        mHeartLayout = (HeartLayout) findViewById(R.id.heart_o_red_layout);
        mTypeTextView = (TypeTextView2) findViewById(R.id.typeTextView);
        mWhiteSnowView = (SnowView) findViewById(R.id.whiteSnowView);
        mImageView = (ImageView) findViewById(R.id.image);
        mProgressBar = (ProgressBar) findViewById(R.id.progressbar);
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
        clickEvent();
        delayShowTheSnow();
        delayShow(200);
        mTypeTextView.setOnTypeViewListener(new TypeTextView2.OnTypeViewListener() {
            public void onTypeStart() {
            }

            public void onTypeOver() {
                delayShowTheSnow();
            }
        });
        mTypeTextView.setText("");
    }

    private void initData() {
        startAnimation(R.array.anim_now, mAnimVertical);
    }

    private void setNewRadioGroupListener() {
        mNewRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_dishini_new:
                        startAnimation(R.array.anim_dishini, mAnimView);
                        mAnimationView.start(true, 1000);
                        break;
                    case R.id.rb_waitan_new:
                        startAnimation(R.array.anim_waitan, mAnimView);
                        mAnimationView.start(true, 1000);
                        break;
                    case R.id.rb_dongfang_new:
                        startAnimation(R.array.anim_dongfang, mAnimView);
                        mAnimationView.start(true, 1000);
                        break;
                    case R.id.rb_shengtaiyuan_new:
                        startAnimation(R.array.anim_shengtaiyuan, mAnimView);
                        mAnimationView.start(true, 1000);
                        break;
                    case R.id.rb_bowuyuan_new:
                        startAnimation(R.array.anim_bowuyuan, mAnimView);
                        mAnimationView.start(true, 1000);
                        break;
                    case R.id.rb_now_new:
                        startAnimation(R.array.anim_now, mAnimVertical);
                        mAnimationView.start(true, 1000);
                        break;
                }
            }
        });
    }


    private void playAnimation(ImageView imageView) {
        mAnimationDrawable = (AnimationDrawable) imageView.getBackground();
        mAnimationDrawable.start();
    }

    private void startAnimation(int res, ImageView imageView) {
        Log.i(TAG, "startAnimation");
        if (imageView == mAnimVertical) {
            mAnimVertical.setVisibility(View.VISIBLE);
            mAnimView.setVisibility(View.GONE);
        } else {
            mAnimVertical.setVisibility(View.GONE);
            mAnimView.setVisibility(View.VISIBLE);
        }
        forceStopAnim();
        mAnimViewResourcesID = ListUtil.getArrayData(mContext, res);
        mAnimationView = new AnimationView();
        mAnimationView.setAnimation(getContext(), imageView, mAnimViewResourcesID, "window");
        mAnimationView.setAnimCallback(this);
    }

    private void forceStopAnim() {
        if (mAnimationView != null) {
            mAnimationView.stop();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        cancelTimer();
        mAnimationView = null;
        mAnimView = null;
        if (mWebView != null) {
            //            this.mWebView.clearCache(true);
            //            this.mWebView.clearHistory();
            //            this.mWebView.clearView();
            //            this.mWebView.clearFormData();
            //            this.mWebView.clearMatches();
            //            this.mWebView.clearSslPreferences();
            //            this.mWebView.clearAnimation();
            //            this.mWebView.clearFocus();
            //            this.mWebView.removeAllViewsInLayout();
            if (mWebViwFrameLayout != null) {
                mWebViwFrameLayout.removeAllViewsInLayout();
                mWebViwFrameLayout.removeAllViews();
            }
            mWebView.removeAllViews();
            mWebView.destroy();
            mWebView = null;
        }
        unBindDrawables(findViewById(R.id.root_fragment_layout));
        System.gc();
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

    private void rxJavaSolveMiZhiSuoJinAndNestedLoopAndCallbackHell() {
        Observable.from(ImageNameFactory.getAssetImageFolderName())
                .flatMap(new Func1<String, Observable<String>>() {
                    public Observable<String> call(String folderName) {
                        return Observable.from(ImageUtils.getAssetsImageNamePathList(getApplicationContext(), folderName));
                    }
                }).filter(new Func1<String, Boolean>() {
            public Boolean call(String imagePathNameAll) {
                return Boolean.valueOf(imagePathNameAll.endsWith(PictureAnimActivity.JPG));
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
                        mProgressBar.setVisibility(View.VISIBLE);
                    }
                }).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Void>() {
                    public void onCompleted() {
                        mImageView.setImageBitmap(mManyBitmapSuperposition);
                        mProgressBar.setVisibility(View.GONE);
                        showAllViews();
                    }

                    public void onError(Throwable e) {
                    }

                    public void onNext(Void aVoid) {
                    }
                });
    }

    private void showAllViews() {
        mImageView.setVisibility(View.GONE);
        mWhiteSnowView.setVisibility(View.GONE);
    }

    private void clickEvent() {

        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!ClickUtils.isFastDoubleClick()) {
                    delayShowAll(0L);
                }
            }
        });
    }

    private void gotoNext() {
//        mWebView.loadUrl(URL);

        delayDo();
    }

    private void delayShow(long time) {
        Observable.timer(time, TimeUnit.MILLISECONDS, AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Long>() {
                    public void onCompleted() {
                        mTypeTextView.setVisibility(View.VISIBLE);
                        mTypeTextView.start(getString(R.string.love29));
                    }

                    public void onError(Throwable e) {
                    }

                    public void onNext(Long aLong) {
                    }
                });
    }


    private void delayShowTheSnow() {
        Observable.timer(200, TimeUnit.MILLISECONDS, AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Long>() {
                    public void onCompleted() {
                        mBlueSnowView.setVisibility(View.VISIBLE);
                        showRedHeartLayout();
                    }

                    public void onError(Throwable e) {
                    }

                    public void onNext(Long aLong) {
                    }
                });

    }

    private void delayDo() {
        Observable.timer(0, TimeUnit.SECONDS, AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Long>() {
                    public void onCompleted() {
                        mWhiteSnowView.setVisibility(View.GONE);
                        mWebView.setVisibility(View.GONE);
                        textview.setVisibility(View.GONE);
//                        delayShow(5100);//延时显示显示打印机
//                        mWebView.loadUrl(Main4Activity.URL);
                        textview.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                //                                mWebView.loadUrl("file:///android_asset/love.html");
//                                startActivity(new Intent(Main4Activity.this, Main3Activity.class));
                                new CommomDialog(PictureAnimActivity.this, R.style.dialog, "求求你别离开我好吗？", new CommomDialog.OnCloseListener() {
                                    @Override
                                    public void onClick(Dialog dialog, boolean confirm) {
                                        if (confirm) {

                                            dialog.dismiss();
                                        }

                                    }
                                }).show();
                            }
                        });

                    }

                    public void onError(Throwable e) {
                    }

                    public void onNext(Long aLong) {
                    }
                });
    }

    private void delayShowAll(long time) {
        Observable.timer(time, TimeUnit.MILLISECONDS, AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Long>() {
                    public void onCompleted() {
                        gotoNext();
                    }

                    public void onError(Throwable e) {
                    }

                    public void onNext(Long aLong) {
                    }
                });
    }

    //    @Override
    //    public boolean onKeyDown(int keyCode, KeyEvent event) {
    //        if (keyCode == KeyEvent.KEYCODE_BACK) {
    //            if (this.mWebView != null) {
    //                this.mWebView.clearCache(true);
    //                this.mWebView.clearHistory();
    //                this.mWebView.clearView();
    //                this.mWebView.clearFormData();
    //                this.mWebView.clearMatches();
    //                this.mWebView.clearSslPreferences();
    //                this.mWebView.clearAnimation();
    //                this.mWebView.clearFocus();
    //                this.mWebView.removeAllViewsInLayout();
    //                this.mWebView = null;
    //            }
    //            System.gc();
    //            unBindDrawables(findViewById(R.id.root_fragment_layout));
    //            this.finish();
    //        }
    //        return false;
    //    }


    private int randomColor() {
        return Color.rgb(mRandom.nextInt(255), mRandom.nextInt(255), mRandom.nextInt(255));
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
                                /*new CommomDialog(getAssets(), PictureAnimActivity.this, R.style.dialog, getString(R.string.love4), new CommomDialog.OnCloseListener() {
                                    @Override
                                    public void onClick(Dialog dialog, boolean confirm) {
                                        if (confirm) {
                                            new CommomDialog(getAssets(), PictureAnimActivity.this, R.style.dialog, getString(R.string.love17), new CommomDialog.OnCloseListener() {
                                                @Override
                                                public void onClick(Dialog dialog, boolean confirm) {
                                                    if (confirm) {
                                                        new CommomDialog(getAssets(), PictureAnimActivity.this, R.style.dialog, getString(R.string.love1), new CommomDialog.OnCloseListener() {
                                                            @Override
                                                            public void onClick(Dialog dialog, boolean confirm) {
                                                                if (confirm) {
                                                                    new CommomDialog(getAssets(), PictureAnimActivity.this, R.style.dialog, getString(R.string.love5), new CommomDialog.OnCloseListener() {
                                                                        @Override
                                                                        public void onClick(Dialog dialog, boolean confirm) {
                                                                            if (confirm) {
                                                                                new CommomDialog(getAssets(), PictureAnimActivity.this, R.style.dialog, getString(R.string.love9), new CommomDialog.OnCloseListener() {
                                                                                    @Override
                                                                                    public void onClick(Dialog dialog, boolean confirm) {
                                                                                        if (confirm) {
                                                                                            new CommomDialog(getAssets(), PictureAnimActivity.this, R.style.dialog, getString(R.string.love19), new CommomDialog.OnCloseListener() {
                                                                                                @Override
                                                                                                public void onClick(Dialog dialog, boolean confirm) {
                                                                                                    if (confirm) {
                                                                                                        new CommomDialog(getAssets(), PictureAnimActivity.this, R.style.dialog, getString(R.string.love20), new CommomDialog.OnCloseListener() {
                                                                                                            @Override
                                                                                                            public void onClick(Dialog dialog, boolean confirm) {
                                                                                                                if (confirm) {
                                                                                                                    startActivity(new Intent(PictureAnimActivity.this, FireworksActivity.class));
                                                                                                                }
                                                                                                            }
                                                                                                        }).show();
                                                                                                    }
                                                                                                }
                                                                                            }).show();
                                                                                        }
                                                                                    }
                                                                                }).show();
                                                                            }
                                                                        }
                                                                    }).show();
                                                                }
                                                            }
                                                        }).show();
                                                    }
                                                }
                                            }).show();
                                        }

                                    }
                                }).show();
*/
                                startActivity(new Intent(PictureAnimActivity.this, FireworksActivity.class));
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


    /**
     * remove View Drawables
     *
     * @param view
     */
    private void unBindDrawables(View view) {
        if (view != null) {
            try {
                Drawable drawable = view.getBackground();
                if (drawable != null) {
                    drawable.setCallback(null);
                } else {
                }
                if (view instanceof ViewGroup && !(view instanceof AdapterView)) {
                    ViewGroup viewGroup = (ViewGroup) view;
                    int viewGroupChildCount = viewGroup.getChildCount();
                    for (int j = 0; j < viewGroupChildCount; j++) {
                        unBindDrawables(viewGroup.getChildAt(j));
                    }
                    viewGroup.removeAllViews();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    /*public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            new CommomDialog(getAssets(), this, R.style.dialog, "求求你别离开我好吗？", new CommomDialog.OnCloseListener() {
                @Override
                public void onClick(Dialog dialog, boolean confirm) {
                    if (confirm) {

                        dialog.dismiss();
                    }

                }
            }).show();

        }
        return true;
    }*/

    @Override
    public void AnimStop() {
        startAnimation(R.array.anim_now, mAnimVertical);
    }


}
