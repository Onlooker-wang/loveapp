package com.wy.qk.expresslove.app;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.lzy.okgo.OkGo;
import com.tencent.smtt.sdk.QbSdk;
import com.wy.qk.expresslove.utils.Constant;
import com.wy.qk.expresslove.utils.SharedPreferencesUtils;
import com.zhy.http.okhttp.OkHttpUtils;

import java.util.ArrayList;

public class MyApplication extends Application {
    private static final String TAG = "MyApplication";

    private static Context mContext;
    private ArrayList<String> mImageUrl;

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
        mContext = getApplicationContext();
        initX5();
        OkHttpUtils.getInstance()
                .init(this)
                .debug(true, "okHttp")
                .timeout(20 * 1000);


        OkGo.getInstance().init(this);
        //getImagesFromUrl();
        //设置应用开启时的标志位
        SharedPreferencesUtils.putInt(mContext, Constant.MUSIC_DATA_SP, Constant.APP_OPEN, 1);
    }

    /**
     * 从网络获取图片URL，并保存到sp
     */
    private void getImagesFromUrl() {
        mImageUrl = new ArrayList<>();
        for (int i = 1; i < 9; i++) {
            mImageUrl.add(String.format("https://raw.githubusercontent.com/Onlooker-wang/loveapp/main/extras/image/%d.jpg", i));
        }
        Log.i(TAG, "init mImageUrl: " + mImageUrl);
        String[] imageUrl = (String[]) mImageUrl.toArray(new String[0]);
        SharedPreferencesUtils.setSharedPreferences(mContext, "data", "ImageUrl", imageUrl);
    }


    /**
     * 初始化X5
     */
    private void initX5() {
        //x5內核初始化回调
        QbSdk.PreInitCallback cb = new QbSdk.PreInitCallback() {
            @Override
            public void onViewInitFinished(boolean arg0) {
                //x5內核初始化完成的回调，为true表示x5内核加载成功，否则表示x5内核加载失败，会自动切换到系统内核。
                Log.d("app", " onViewInitFinished is " + arg0);
            }

            @Override
            public void onCoreInitFinished() {
            }
        };
        //x5内核初始化接口
        QbSdk.initX5Environment(getApplicationContext(), cb);

    }

    public static Context getContext() {
        return mContext;
    }

    public static final String FIRST_OPEN = "first_open";
}
