package com.wy.lpr.expresslove.main;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.View;
import android.widget.*;

import com.wy.lpr.expresslove.R;
import com.wy.lpr.expresslove.main.photo.PhotoActivity;
import com.wy.lpr.expresslove.utils.CommomDialog;
import com.wy.lpr.expresslove.utils.Constant;
import com.wy.lpr.expresslove.utils.SharedPreferencesUtils;

import java.util.ArrayList;
import java.util.List;

import static com.wy.lpr.expresslove.main.MyApplication.getContext;


/**
 * Created by xvshu on 2017/8/7.
 */
public class LoginActivity extends Activity implements View.OnClickListener {
    private static final String TAG = "LoginActivity";

    private Context mContext;
    private Button mLoginBtn = null;
    private EditText mUserIdEt = null;
    private EditText mPassEt = null;
    private TextView mPromptText = null;
    private CheckBox mCbRememberPass = null;
    private CheckBox mCbAutoLogin = null;
    private SharedPreferences mSharedPreferences = null;

    private String mSpKeyUser = "UserName";
    private String mSpKeyPass = "PassWord";
    private String mSpLastKeyUser = "LastUserName";
    private String mSpLastKeyPass = "LastPassWord";
    private String mSpKeyRememberPass = "REMEMBERPASS";
    private String mSpKeyAutoLogin = "AUTOLOGIN";
    private String mSpName = "userInfo";
    private String[] mUserName;
    private String[] mPassWord;
    private String mCurrentUserId;
    private String mCurrentPassWord;
    private List<String> mUserNameList = new ArrayList<>();
    private List<String> mPassWordList = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        mContext = MyApplication.getContext();

        initView();
        initData();
        setListener();
    }

    private void initView() {
        mLoginBtn = (Button) findViewById(R.id.loginBtn);
        mLoginBtn.setOnClickListener(this);
        mUserIdEt = (EditText) findViewById(R.id.userId);
        mPassEt = (EditText) findViewById(R.id.pass);
        mPromptText = (TextView) findViewById(R.id.promptText);
        mCbRememberPass = (CheckBox) findViewById(R.id.cb_rememberpass);
        mCbAutoLogin = (CheckBox) findViewById(R.id.cb_autologin);
        mSharedPreferences = this.getSharedPreferences(mSpName, Context.MODE_PRIVATE);
        mUserIdEt.setSelection(mUserIdEt.length());
        mUserIdEt.setFilters(new InputFilter[]{new LengthFilter(8, getContext())});


    }

    private void initData() {
        mUserName = SharedPreferencesUtils.getSharedPreferences(mContext, mSpName, mSpKeyUser);
        mPassWord = SharedPreferencesUtils.getSharedPreferences(mContext, mSpName, mSpKeyPass);

        for (String userName : mUserName) {
            if (!userName.equals("")) {
                mUserNameList.add(userName);
            }
        }
        for (String passWord : mPassWord) {
            if (!passWord.equals("")) {
                mPassWordList.add(passWord);
            }
        }
        //判断记住密码多选框的状态
        if (mSharedPreferences.getBoolean(mSpKeyRememberPass, false)) {
            //设置默认是记录密码状态
            mCbRememberPass.setChecked(true);
            mUserIdEt.setText(mSharedPreferences.getString(mSpLastKeyUser, ""));
            mPassEt.setText(mSharedPreferences.getString(mSpLastKeyPass, ""));
            //判断自动登陆多选框状态
            if (mSharedPreferences.getBoolean(mSpKeyAutoLogin, false)) {
                //设置默认是自动登录状态
                mCbAutoLogin.setChecked(true);
                //跳转界面
                Intent intent = new Intent(this, DrawHeartActivity.class);
                intent.putExtra(Constant.CURRENT_USER_NAME, mSharedPreferences.getString(mSpLastKeyUser, ""));
                startActivity(intent);
                finish();

            }
        }

    }

    private void setListener() {
        //监听记住密码多选框按钮事件
        mCbRememberPass.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (mCbRememberPass.isChecked()) {
                    mSharedPreferences.edit().putBoolean(mSpKeyRememberPass, true).apply();
                } else {
                    mSharedPreferences.edit().putBoolean(mSpKeyRememberPass, false).apply();
                }

            }
        });

        //监听自动登录多选框事件
        mCbAutoLogin.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (mCbAutoLogin.isChecked()) {
                    mSharedPreferences.edit().putBoolean(mSpKeyAutoLogin, true).apply();
                } else {
                    mSharedPreferences.edit().putBoolean(mSpKeyAutoLogin, false).apply();
                }
            }
        });
    }

    private void ifRememberPass() {
        if (mCbRememberPass.isChecked()) {
            //记住用户名、密码、
            SharedPreferences.Editor editor = mSharedPreferences.edit();
            editor.putString(mSpLastKeyUser, mCurrentUserId);
            editor.putString(mSpLastKeyPass, mCurrentPassWord);
            editor.apply();
        } else {
            //不记住用户名、密码
            SharedPreferences.Editor editor = mSharedPreferences.edit();
            editor.putString(mSpLastKeyUser, "");
            editor.putString(mSpLastKeyPass, "");
            editor.apply();
        }
    }

    @Override
    public void onClick(View v) {
        mCurrentUserId = mUserIdEt.getText().toString().trim();
        mCurrentPassWord = mPassEt.getText().toString().trim();
        if (mCurrentUserId.equals("")) {
            mPromptText.setText("用户名为空");
            return;
        }
        if (mCurrentPassWord.equals("")) {
            mPromptText.setText("密码为空");
            return;
        }

        if (!mUserNameList.contains(mCurrentUserId)) {
            new CommomDialog(LoginActivity.this, R.style.dialog, "用户名不存在，是否注册新用户？", new CommomDialog.OnCloseListener() {
                @Override
                public void onClick(Dialog dialog, boolean confirm) {
                    if (confirm) {
                        mUserNameList.add(mCurrentUserId);
                        mPassWordList.add(mCurrentPassWord);
                        String[] userName = (String[]) mUserNameList.toArray(new String[0]);
                        SharedPreferencesUtils.setSharedPreferences(mContext, mSpName, mSpKeyUser, userName);
                        String[] pass = (String[]) mPassWordList.toArray(new String[0]);
                        SharedPreferencesUtils.setSharedPreferences(mContext, mSpName, mSpKeyPass, pass);
                        ifRememberPass();
                        Intent intent = new Intent(LoginActivity.this, DrawHeartActivity.class);
                        intent.putExtra(Constant.CURRENT_USER_NAME, mCurrentUserId);
                        startActivity(intent);
                        finish();
                    } else {
                        dialog.dismiss();
                    }
                }
            })
                    .setBackground(R.color.white)
                    .setPositiveButton("注册")
                    .setNegativeButton("取消")
                    .show();

        } else {
            for (int i = 0; i < mUserNameList.size(); i++) {
                if (mUserNameList.get(i).equals(mCurrentUserId)) {
                    if (mPassWordList.get(i).equals(mCurrentPassWord)) {
                        Toast.makeText(this, "登录成功！", Toast.LENGTH_LONG).show();
                        ifRememberPass();
                        Intent intent = new Intent(LoginActivity.this, DrawHeartActivity.class);
                        intent.putExtra(Constant.CURRENT_USER_NAME, mCurrentUserId);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(this, "密码不正确", Toast.LENGTH_LONG).show();
                    }
                    return;
                }

            }
        }
    }
}


class LengthFilter implements InputFilter {
    private final int mMax;
    private final Context mContext;

    public LengthFilter(int max, Context context) {
        mMax = max;
        mContext = context;
    }

    public CharSequence filter(CharSequence source, int start, int end, Spanned dest,
                               int dstart, int dend) {
        int keep = mMax - (dest.length() - (dend - dstart));
        if (keep <= 0) {
            final Toast toast;
            toast = Toast.makeText(mContext, mContext.getString(R.string.setting_name_max), Toast.LENGTH_SHORT);
            toast.show();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    toast.cancel();
                }
            }, 2000);
            return "";
        } else if (keep >= end - start) {
            return null; // keep original
        } else {
            keep += start;
            if (Character.isHighSurrogate(source.charAt(keep - 1))) {
                --keep;
                if (keep == start) {
                    return "";
                }
            }
            return source.subSequence(start, keep);
        }
    }

    /**
     * @return the maximum length enforced by this input filter
     */
    public int getMax() {
        return mMax;
    }
}
