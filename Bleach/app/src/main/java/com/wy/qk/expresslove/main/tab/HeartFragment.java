package com.wy.qk.expresslove.main.tab;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.wy.qk.expresslove.R;
import com.wy.qk.expresslove.utils.TimeUtil;
import com.wy.qk.expresslove.utils.heartview.HeartView;

public class HeartFragment extends PagerFragment {
    private static final String TAG = "HeartFragment";
    private View mViewRoot;
    private HeartView mHeartView;
    private String mDifferTime;
    private TimeUtil mTimeUtil;
    private TextView mLoveTime;
    private RelativeLayout mLoveTimeLayout;
    private WebView mWebViewTop, mWebViewBottom;
    private TabMainActivity.MyOnTouchListener mMyOnTouchListener;
    private GestureDetector mGestureDetector;
    private SVCGestureListener mGestureListener = new SVCGestureListener();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "onCreate: ");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mViewRoot = inflater.inflate(R.layout.fragment_heart, container, false);
        Log.i(TAG, "onCreateView: ");
        initView();
        return mViewRoot;
    }


    @Override
    public void onResume() {
        super.onResume();
        Log.i(TAG, "onResume: ");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.i(TAG, "onPause: ");
    }

    private void initView() {
        mTimeUtil = new TimeUtil();
        mWebViewTop = (WebView) mViewRoot.findViewById(R.id.webView_1);
        mWebViewBottom = (WebView) mViewRoot.findViewById(R.id.webView_2);
        mLoveTime = (TextView) mViewRoot.findViewById(R.id.hours_tv);
        mLoveTimeLayout = (RelativeLayout) mViewRoot.findViewById(R.id.we_love_time);
        mHeartView = (HeartView) mViewRoot.findViewById(R.id.surfaceView);
        mDifferTime = mTimeUtil.getTimeDifference("2021-09-17 10:10", mTimeUtil.getNowTime());
        Log.i(TAG, "initTime: " + mDifferTime);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mLoveTime.setText(mDifferTime);
                mLoveTimeLayout.setVisibility(View.VISIBLE);
            }
        }, 1000);

        initWebView(mWebViewTop, "file:///android_asset/index.html");
        initWebView(mWebViewBottom, "file:///android_asset/index2.html");

        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_TIME_TICK);
        getActivity().registerReceiver(mBroadcastReceiver, filter);

        mGestureDetector = new GestureDetector(getActivity(), mGestureListener);
        mGestureDetector.setIsLongpressEnabled(true);
        mGestureDetector.setOnDoubleTapListener(mGestureListener);

        mMyOnTouchListener = new TabMainActivity.MyOnTouchListener() {

            @Override
            public boolean onTouch(MotionEvent ev) {
                return mGestureDetector.onTouchEvent(ev);
            }
        };
        ((TabMainActivity) getActivity()).registerMyOnTouchListener(mMyOnTouchListener);

    }


    @SuppressLint("SetJavaScriptEnabled")
    private void initWebView(WebView webView, String url) {
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
    }

    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (Intent.ACTION_TIME_TICK.equals(intent.getAction())) {
                //系统每分钟接收一次广播更新时间
                mDifferTime = mTimeUtil.getTimeDifference("2021-09-17 10:10", mTimeUtil.getNowTime());
                mLoveTime.setText(mDifferTime);
            }
        }
    };


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.i(TAG, "onDestroyView: ");
        ((TabMainActivity) getActivity()).unregisterMyOnTouchListener(mMyOnTouchListener);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy: ");
    }


    public class SVCGestureListener implements GestureDetector.OnGestureListener, GestureDetector.OnDoubleTapListener {

        @Override
        public boolean onDown(MotionEvent e) {
            return false;
        }

        @Override
        public void onShowPress(MotionEvent e) {

        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            return false;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            return false;
        }

        @Override
        public void onLongPress(MotionEvent e) {
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            return false;
        }

        //点击的时候执行
        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            mHeartView.reDraw();
            return true;
        }

        @Override
        public boolean onDoubleTap(MotionEvent e) {
            return false;
        }

        @Override
        public boolean onDoubleTapEvent(MotionEvent e) {
            return false;
        }
    }

}
