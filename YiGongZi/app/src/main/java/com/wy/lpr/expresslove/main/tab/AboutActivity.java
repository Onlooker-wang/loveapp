package com.wy.lpr.expresslove.main.tab;

import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.wy.lpr.expresslove.R;
import com.wy.lpr.expresslove.base.CommonAudioActivity;
import com.wy.lpr.expresslove.utils.StatusBarUtil;
import com.wy.update_app.utils.AppUpdateUtils;

public class AboutActivity extends CommonAudioActivity {

    private TextView mAboutVersion;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtil.StatusBarLightMode(this);
        setContentView(R.layout.activity_about);

        initView();
    }

    private void initView() {
        mAboutVersion = (TextView) findViewById(R.id.about_version);
        String versionName = AppUpdateUtils.getVersionName(this);
        mAboutVersion.setText("意公子 v:" + versionName);
    }
}
