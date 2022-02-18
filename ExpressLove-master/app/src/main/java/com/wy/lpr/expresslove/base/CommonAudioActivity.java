package com.wy.lpr.expresslove.base;

import android.os.Build;
import android.os.Bundle;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.wy.lpr.expresslove.main.MyApplication;
import com.wy.lpr.expresslove.utils.PlayAudioUtil;

public class CommonAudioActivity extends AppCompatActivity {

    public static PlayAudioUtil mPlayAudioUtil;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (mPlayAudioUtil == null){
            mPlayAudioUtil = new PlayAudioUtil(MyApplication.getContext());
            mPlayAudioUtil.startAudioPlayer();
        }
        ActionBar actionBar=getSupportActionBar();//后面几行都用于隐藏标题栏
        if(actionBar !=null)
        {
            actionBar.hide();
        }
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();

        if (Build.VERSION.SDK_INT >= 21) {//设置顶部状态栏为半透明
            getWindow().setFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }
}
