package com.wy.lpr.expresslove.main;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.wy.lpr.expresslove.R;
import com.wy.lpr.expresslove.base.CommonAudioActivity;
import com.wy.lpr.expresslove.main.photo.PhotoActivity;
import com.wy.lpr.expresslove.utils.heart.HeartLayout;

import java.util.Random;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;

public class FireworksActivity extends CommonAudioActivity {
    private static final String TAG = "FireworksActivity";

    private WebView webView;
    private HeartLayout mHeartLayout;//垂直方向的漂浮的红心
    private Random mRandom = new Random();
    private Random mRandom2 = new Random();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fireworks);

        mHeartLayout = (HeartLayout) findViewById(R.id.heart_o_red_layout);
        /*new Handler().postDelayed(new Runnable() { // 为了减少代码使用匿名Handler创建一个延时的调用
            public void run() {
                Intent i = new Intent(Main3Activity.this, Main2Activity.class);
                Main3Activity.this.startActivity(i);
                Main3Activity.this.finish();
            }
        }, 1000);*/
        webView = (WebView) findViewById(R.id.webView2);
        //需要加载的网页的url
        webView.loadUrl("file:///android_asset/index4.html");//这里写的是assets文件夹下html文件的名称，需要带上后面的后缀名，前面的路径是安卓系统自己规定的android_asset就是表示的在assets文件夹下的意思。
        webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);//自适应屏幕
        webView.getSettings().setLoadWithOverviewMode(true);//自适应屏幕
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setUseWideViewPort(true);//扩大比例的缩放
        // webView.getSettings().setBuiltInZoomControls(true);//设置是否出现缩放工具,这里我想就不出现了，影响效果
        WebSettings settings = webView.getSettings();
        // 如果访问的页面中要与Javascript交互，则webview必须设置支持Javascript
        settings.setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });

        showRedHeartLayout();

    }

    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            /*new CommomDialog(getAssets(), this, R.style.dialog, getString(R.string.love21), new CommomDialog.OnCloseListener() {
                @Override
                public void onClick(Dialog dialog, boolean confirm) {
                    if (confirm) {

                        dialog.dismiss();
                    }

                }
            }).show();*/
            finish();
        }
        return true;
    }

    private void showRedHeartLayout() {
        Observable.timer(400, TimeUnit.MILLISECONDS, AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Long>() {
                    public void onCompleted() {
                        mHeartLayout.setVisibility(View.VISIBLE);
                        mHeartLayout.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Toast.makeText(MyApplication.getContext(), "功能开发中，敬请期待...", Toast.LENGTH_SHORT).show();
                                //startActivity(new Intent(FireworksActivity.this, FireworksActivity.class));
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
}
