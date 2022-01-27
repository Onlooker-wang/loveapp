package com.example.lenovo.expresslove.main;

import android.app.Dialog;
import android.os.Bundle;
import android.view.KeyEvent;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.lenovo.expresslove.R;
import com.example.lenovo.expresslove.base.CommonAudioActivity;
import com.example.lenovo.expresslove.utils.CommomDialog;
import com.example.particletextview.MovingStrategy.BidiHorizontalStrategy;
import com.example.particletextview.MovingStrategy.BidiVerticalStrategy;
import com.example.particletextview.MovingStrategy.CornerStrategy;
import com.example.particletextview.MovingStrategy.VerticalStrategy;
import com.example.particletextview.Object.ParticleTextViewConfig;
import com.example.particletextview.View.ParticleTextView;

public class FireworksActivity extends CommonAudioActivity {


    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fireworks);
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

    }

    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            new CommomDialog(getAssets(), this, R.style.dialog, getString(R.string.love21), new CommomDialog.OnCloseListener() {
                @Override
                public void onClick(Dialog dialog, boolean confirm) {
                    if (confirm) {

                        dialog.dismiss();
                    }

                }
            }).show();

        }
        return true;
    }
}
