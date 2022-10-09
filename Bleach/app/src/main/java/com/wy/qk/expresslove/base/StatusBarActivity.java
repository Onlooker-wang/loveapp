package com.wy.qk.expresslove.base;


import android.app.Activity;
import android.os.Bundle;

import com.jaeger.library.StatusBarUtil;
import com.wy.qk.expresslove.R;

/**
 * 参考网站：https://blog.csdn.net/weixin_45882303/article/details/121179989
 */
public class StatusBarActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status_bar);
        //设置沉浸式状态栏（颜色）
        StatusBarUtil.setColor(this, getColor(R.color.black), 0);
        //设置沉浸式状态栏（图片）
        StatusBarUtil.setTransparentForImageView(this, findViewById(R.id.linearLayout));
    }
}