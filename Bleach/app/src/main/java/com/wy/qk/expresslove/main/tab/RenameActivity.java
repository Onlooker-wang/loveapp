package com.wy.qk.expresslove.main.tab;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.wy.qk.expresslove.R;
import com.wy.qk.expresslove.app.MyApplication;
import com.wy.qk.expresslove.base.CommonAudioActivity;
import com.wy.qk.expresslove.utils.Constant;
import com.wy.qk.expresslove.utils.SharedPreferencesUtils;
import com.wy.qk.expresslove.utils.StatusBarUtils;

public class RenameActivity extends CommonAudioActivity {

    private TextView mSaveBtn;
    private EditText mRenameEdit;
    private Context mContext;
    private String mCurrentUserName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtils.StatusBarLightMode(this);
        setContentView(R.layout.activity_rename);
        mContext = MyApplication.getContext();

        initView();
        setListener();
    }

    private void initView() {
        mCurrentUserName = SharedPreferencesUtils.getString(mContext, Constant.USER_INFO_SP, Constant.CURRENT_USER_NAME, "");
        mSaveBtn = (TextView) findViewById(R.id.save_btn);
        mRenameEdit = (EditText) findViewById(R.id.rename_edit);

    }

    private void setListener() {
        mRenameEdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

            }
        });

        mSaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userId = mRenameEdit.getText().toString().trim();
                SharedPreferencesUtils.putString(mContext, Constant.USER_ID_SP, Constant.CURRENT_USER_ID, userId);
                finish();
            }
        });
    }
}