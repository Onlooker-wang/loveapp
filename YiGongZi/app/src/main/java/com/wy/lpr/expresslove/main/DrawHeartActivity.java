package com.wy.lpr.expresslove.main;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebViewClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.byd.update_app.UpdateAppManager;
import com.byd.update_app.listener.ExceptionHandler;
import com.wy.lpr.expresslove.R;
import com.wy.lpr.expresslove.base.CommonAudioActivity;
import com.wy.lpr.expresslove.http.UpdateAppHttpUtil;
import com.wy.lpr.expresslove.main.photo.PhotoActivity;
import com.wy.lpr.expresslove.utils.CommonFlashAnimationHelper;
import com.wy.lpr.expresslove.utils.TimeUtil;
import com.wy.lpr.expresslove.utils.TypeTextView2;
import com.wy.lpr.expresslove.utils.heartview.HeartView;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;

public class DrawHeartActivity extends CommonAudioActivity {
    private static final String TAG = "DrawHeartActivity";
    private static final String LOVE1 = "Honey，我们在一起已经";
    private static final String LOVE2 = "小时啦！";
    private static final String LOVE3 = "这是我们相爱在一起的时光。Love you forever";
    private String mUpdateUrl = "https://raw.githubusercontent.com/Onlooker-wang/loveapp/main/extras/update.json";

    private WebView mWebViewTop, mWebViewBottom;
    private HeartView mHeartView;
    private TypeTextView2 mTypeTextView;//打字机
    // 倒计时
    private TextView daysTv, hoursTv, minutesTv, secondsTv;
    private long mDay = 652;
    private long mHour = 15;
    private long mMin = 37;
    private long mSecond = 00;// 天 ,小时,分钟,秒
    private boolean isRun = true;
    private String mDifferTime;
    private float mDifferTimeFloat;
    private TimeUtil mTimeUtil;
    private TextView tv_text;
    private TextView tv_text_1;
    private TextView tv_text_2;
    private int clo = 0;
    private String mLoveTotal;
    private RelativeLayout mWeLoveTime;
    private ImageView mPic;
    private LinearLayout mClick;
    private static long mLastClickTime;
    private final static int MIN_CLICK_DELAY_TIME = 2800;
    private String mCurrentUserName;

    private Handler timeHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                //computeTime();
                hoursTv.setText(mDifferTime);
                mWeLoveTime.setVisibility(View.VISIBLE);
                CommonFlashAnimationHelper.showSplash(mPic, R.drawable.cat);
                /*daysTv.setText(mDay + "");*/
                /*minutesTv.setText(mMin + "");
                secondsTv.setText(mSecond + "");*/
                /*if (mDay == 0 && mHour == 0 && mMin == 0 && mSecond == 0) {
                }*/
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_draw_heart);

        //检查更新
        updateApp();
        initView();
        initHeartView();
        initTime();
        startRun();//倒计时
        setListener();

    }

    @Override
    protected void onResume() {
        super.onResume();
        initData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        CommonFlashAnimationHelper.destroySplashAnimator();
    }

    private void initView() {
        mWebViewTop = (WebView) findViewById(R.id.webView_1);
        mWebViewBottom = (WebView) findViewById(R.id.webView_2);

        mTypeTextView = (TypeTextView2) findViewById(R.id.typeTextView);
        mTimeUtil = new TimeUtil();
        hoursTv = (TextView) findViewById(R.id.hours_tv);
        tv_text = (TextView) findViewById(R.id.myword);
        tv_text_1 = (TextView) findViewById(R.id.myword_1);
        mWeLoveTime = (RelativeLayout) findViewById(R.id.we_love_time);
        mClick = (LinearLayout) findViewById(R.id.click_layout);
        mPic = (ImageView) findViewById(R.id.pic);
        shark();

        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_TIME_TICK);
        registerReceiver(mBroadcastReceiver, filter);


    }

    private void initData() {
        initWebView(mWebViewTop, "file:///android_asset/index.html");
        initWebView(mWebViewBottom, "file:///android_asset/index2.html");
    }


    private void initWebView(WebView webView, String url) {
//        final TextView textView = (TextView) findViewById(R.id.textview);

        //需要加载的网页的url
        webView.loadUrl(url);//这里写的是assets文件夹下html文件的名称，需要带上后面的后缀名，前面的路径是安卓系统自己规定的android_asset就是表示的在assets文件夹下的意思。
        webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);//自适应屏幕
        webView.getSettings().setLoadWithOverviewMode(true);//自适应屏幕
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setUseWideViewPort(true);//扩大比例的缩放
//        mWebView.getSettings().setBuiltInZoomControls(true);//设置是否出现缩放工具,这里我想就不出现了，影响效果
        WebSettings settings = webView.getSettings();
        // 如果访问的页面中要与Javascript交互，则webview必须设置支持Javascript
        settings.setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        /*Animation scaleAnimation = AnimationUtils.loadAnimation(this, R.animator.anim);
        textView.startAnimation(scaleAnimation);*/

    }


    private void initHeartView() {
        mHeartView = (HeartView) findViewById(R.id.surfaceView);
    }

    private void initTime() {
        mDifferTime = mTimeUtil.getTimeDifference("2021-09-17 10:10", mTimeUtil.getNowTime());
        //mDifferTimeFloat = mTimeUtil.strToFloat(mDifferTime);
        Log.i(TAG, "initTime: " + mDifferTime);
        /*mHour = (long) mDifferTimeFloat;
        mLoveTotal = LOVE1 + mHour + LOVE2 + LOVE3;*/
        //Log.i(TAG, "initTime: " + mHour);
    }

    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (Intent.ACTION_TIME_TICK.equals(intent.getAction())) {
                //系统每分钟接收一次广播更新时间
                mDifferTime = mTimeUtil.getTimeDifference("2021-09-17 10:10", mTimeUtil.getNowTime());
                hoursTv.setText(mDifferTime);
            }
        }
    };

    private void setListener() {
        mClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getApplicationContext(), "下次更新后再点我噢！", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(DrawHeartActivity.this, PhotoActivity.class));
            }
        });
    }

    private void delayShow(long time) {
        Observable.timer(time, TimeUnit.MILLISECONDS, AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Long>() {
                    public void onCompleted() {
                        mTypeTextView.start(mLoveTotal);
                    }

                    public void onError(Throwable e) {
                    }

                    public void onNext(Long aLong) {
                    }
                });
    }

    /**
     * 开启计时
     */
    private void startRun() {
        new Thread(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                while (isRun) {
                    try {
                        Thread.sleep(1000); // sleep 1000ms
                        Message message = Message.obtain();
                        message.what = 1;
                        timeHandler.sendMessage(message);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mHeartView.reDraw();
        return super.onTouchEvent(event);
    }

    private boolean isFastClick() {
        boolean flag = false;
        long curClickTime = System.currentTimeMillis();
        if ((curClickTime - mLastClickTime) >= MIN_CLICK_DELAY_TIME) {
            flag = true;
        }
        mLastClickTime = curClickTime;
        return flag;
    }

    /**
     * 倒计时计算
     */
    private void computeTime() {
        mSecond++;
        if (mSecond > 60) {
            mSecond = 0;
            mMin++;
            if (mMin > 60) {
                mMin = 0;
                mHour++;
                /*if (mHour > 24) {
                    mHour = 0;
                    // 倒计时结束
                    mDay++;
                }*/
            }
        }
    }

    private void shark() {
        Timer timer = new Timer();
        TimerTask taskcc = new TimerTask() {
            public void run() {
                runOnUiThread(new Runnable() {
                    public void run() {
                        if (clo == 0) {
                            clo = 1;
                            tv_text.setTextColor(getResources().getColor(R.color.text1));
                            tv_text_1.setTextColor(getResources().getColor(R.color.text2));
                        } else {
                            if (clo == 1) {

                                clo = 2;
                                tv_text.setTextColor(getResources().getColor(R.color.text3));
                                tv_text_1.setTextColor(getResources().getColor(R.color.text4));
                            } else if (clo == 2) {

                                clo = 3;
                                tv_text.setTextColor(getResources().getColor(R.color.text5));
                                tv_text_1.setTextColor(getResources().getColor(R.color.text6));

                            } else if (clo == 3) {

                                clo = 4;
                                tv_text.setTextColor(getResources().getColor(R.color.text7));
                                tv_text_1.setTextColor(getResources().getColor(R.color.text8));
                            } else if (clo == 4) {

                                clo = 5;
                                tv_text.setTextColor(getResources().getColor(R.color.text9));
                                tv_text_1.setTextColor(getResources().getColor(R.color.text10));
                            } else if (clo == 5) {

                                clo = 6;
                                tv_text.setTextColor(getResources().getColor(R.color.text2));
                                tv_text_1.setTextColor(getResources().getColor(R.color.text1));
                            } else if (clo == 6) {

                                clo = 7;
                                tv_text.setTextColor(getResources().getColor(R.color.text4));
                                tv_text_1.setTextColor(getResources().getColor(R.color.text3));
                            } else if (clo == 7) {

                                clo = 8;
                                tv_text.setTextColor(getResources().getColor(R.color.text6));
                                tv_text_1.setTextColor(getResources().getColor(R.color.text5));
                            } else if (clo == 8) {

                                clo = 9;
                                tv_text.setTextColor(getResources().getColor(R.color.text8));
                                tv_text_1.setTextColor(getResources().getColor(R.color.text7));
                            } else if (clo == 9) {

                                clo = 10;
                                tv_text.setTextColor(getResources().getColor(R.color.text10));
                                tv_text_1.setTextColor(getResources().getColor(R.color.text9));
                            } else if (clo == 10) {

                                clo = 11;
                                tv_text.setTextColor(getResources().getColor(R.color.text1));
                                tv_text_1.setTextColor(getResources().getColor(R.color.text10));
                            } else {
                                clo = 0;
                                tv_text.setTextColor(getResources().getColor(R.color.text2));
                                tv_text_1.setTextColor(getResources().getColor(R.color.text9));
                            }
                        }
                    }
                });
            }
        };
        timer.schedule(taskcc, 1, 1500);  //<span style="color: rgb(85, 85, 85); font-family: 'microsoft yahei'; font-size: 15px; line-height: 35px;">第二个参数分别是delay（多长时间后执行），第三个参数是：duration（执行间隔）单位为：ms</span>
    }


    private void updateApp() {
        new UpdateAppManager
                .Builder()
                //当前Activity
                .setActivity(this)
                //更新地址
                .setUpdateUrl(mUpdateUrl)
                .handleException(new ExceptionHandler() {
                    @Override
                    public void onException(Exception e) {
                        e.printStackTrace();
                    }
                })
                //实现httpManager接口的对象
                .setHttpManager(new UpdateAppHttpUtil())
                .build()
                .update();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    private void exit() {
        Dialog dialog = new AlertDialog.Builder(DrawHeartActivity.this)
                .setTitle("退出").setMessage("你确定要退出吗？")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
//                        for (Activity ac : allActivity) {
//                            ac.finish();
//                            dialog.dismiss();
//                        }
                    }
                }).setNegativeButton("取消", null).create();
        dialog.show();
    }
}

