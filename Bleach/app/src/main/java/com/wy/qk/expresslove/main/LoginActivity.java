package com.wy.qk.expresslove.main;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spanned;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.*;

import com.wy.qk.expresslove.R;
import com.wy.qk.expresslove.adapter.PopListAdapter;
import com.wy.qk.expresslove.app.MyApplication;
import com.wy.qk.expresslove.base.CommonAudioActivity;
import com.wy.qk.expresslove.main.password.PassWordActivity;
import com.wy.qk.expresslove.main.tab.TabMainActivity;
import com.wy.qk.expresslove.utils.CommomDialog;
import com.wy.qk.expresslove.utils.CommonFlashAnimationHelper;
import com.wy.qk.expresslove.utils.Constant;
import com.wy.qk.expresslove.utils.SharedPreferencesUtils;
import com.wy.qk.expresslove.utils.StatusBarUtils;

import java.util.ArrayList;
import java.util.List;

import static com.wy.qk.expresslove.app.MyApplication.getContext;


/**
 * Created by xvshu on 2017/8/7.
 */
public class LoginActivity extends CommonAudioActivity {
    private static final String TAG = "LoginActivity";

    private Context mContext;
    private ImageView mLoginBtn = null;
    private EditText mUserIdEt = null;
    private EditText mPassEt = null;
    private TextView mPromptText = null;
    private CheckBox mCbRememberPass = null;
    private CheckBox mCbAutoLogin = null;
    private SharedPreferences mSharedPreferences = null;
    private ImageView mDropList, mPassWordVisible;
    private TextView mForgetPass;
    private LinearLayout mUserInputLayout;

    private ListView mDropViewList;
    private ListAdapter mListAdapter;
    private PopupWindow mPopupWindow;

    private String mSpKeyUser = Constant.USER_NAME;
    private String mSpKeyPass = Constant.PASS_WORD;
    private String mSpLastKeyUser = Constant.LAST_USER_NAME;
    private String mSpLastKeyPass = Constant.LAST_PASS_WORD;
    private String mSpKeyRememberPass = Constant.REMEMBER_PASS;
    private String mSpKeyAutoLogin = Constant.AUTO_LOGIN;
    private String mSpName = Constant.USER_INFO_SP;
    private String[] mUserName;
    private String[] mPassWord;
    private String mCurrentUserId;
    private String mCurrentPassWord;
    private List<String> mUserNameList = new ArrayList<>();
    private List<String> mPassWordList = new ArrayList<>();
    private String mPassForCheck;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtils.StatusBarLightMode(this);

        setContentView(R.layout.activity_login);
        mContext = MyApplication.getContext();

        initView();
        initData();
        setListener();
    }

    private void initView() {
        mLoginBtn = (ImageView) findViewById(R.id.loginBtn);
        mUserInputLayout = (LinearLayout) findViewById(R.id.user_input_layout);
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                CommonFlashAnimationHelper.showSplash(mLoginBtn, R.drawable.login1);
            }
        });

        mUserIdEt = (EditText) findViewById(R.id.userId);
        mPassEt = (EditText) findViewById(R.id.pass);
        mPromptText = (TextView) findViewById(R.id.promptText);
        mCbRememberPass = (CheckBox) findViewById(R.id.cb_rememberpass);
        mCbAutoLogin = (CheckBox) findViewById(R.id.cb_autologin);
        mSharedPreferences = this.getSharedPreferences(mSpName, Context.MODE_PRIVATE);
        mUserIdEt.setSelection(mUserIdEt.length());
        mUserIdEt.setFilters(new InputFilter[]{new LengthFilter(8, getContext())});
        mPassWordVisible = (ImageView) findViewById(R.id.pass_word_visible);
        setPasswordVisible(false);
        mForgetPass = (TextView) findViewById(R.id.forget_pass);
        mDropList = (ImageView) findViewById(R.id.drop_list);
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
                SharedPreferencesUtils.putString(mContext, Constant.USER_INFO_SP,
                        Constant.CURRENT_USER_NAME, mSharedPreferences.getString(mSpLastKeyUser, ""));
                //跳转界面
                Intent intent = new Intent(this, TabMainActivity.class);
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

        //用户下拉列表点击事件
        mDropList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDropList.setBackgroundResource(R.drawable.input_up);
                showPopupWindow();
                //监听PopupWindow消失
                if (mPopupWindow != null) {
                    mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                        @Override
                        public void onDismiss() {
                            mDropList.setBackgroundResource(R.drawable.input_down);
                        }
                    });
                }
            }
        });

        //密码可见的点击事件
        mPassWordVisible.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mPassEt.getInputType() != InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD) {
                    mPassEt.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    setPasswordVisible(true);
                } else {
                    mPassEt.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    setPasswordVisible(false);
                }
                if (null != mPassEt) {
                    mPassEt.setSelection(mPassEt.getText().length());
                }
            }
        });

        //忘记密码点击事件
        mForgetPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPassForCheck = SharedPreferencesUtils.getString(mContext, Constant.USER_INFO_SP, Constant.PASSWORD_FOR_CHECK_PASS, "");

                if (mPassForCheck.equals("")) {
                    popSetPasswordDialog();
                } else {
                    popInputPasswordDialog();
                }

                Log.i(TAG, "onClick: mForgetPass");
            }
        });

        //登录按钮
        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userRegisterOrLogin();
            }
        });
    }

    //忘记密码弹出设置查看密码对话框
    private void popSetPasswordDialog() {
        new CommomDialog(LoginActivity.this, R.style.dialog, new CommomDialog.OnCloseListener() {
            @Override
            public void onClick(Dialog dialog, boolean confirm) {

                if (confirm) {
                    if (CommomDialog.getPassword().equals("")) {
                        Toast.makeText(mContext, "密码不能为空", Toast.LENGTH_SHORT).show();
                    } else {
                        SharedPreferencesUtils.putString(mContext, Constant.USER_INFO_SP, Constant.PASSWORD_FOR_CHECK_PASS, CommomDialog.getPassword());
                        dialog.dismiss();
                        Intent intent = new Intent(LoginActivity.this, PassWordActivity.class);
                        startActivityForResult(intent, Constant.PASSWORD_REQUEST_CODE);
                    }
                } else {
                    dialog.dismiss();
                }
            }
        })
                .setItemVisibility(Constant.PASSWORD_INPUT_ET)
                .setTitle("请设置密码：")
                .setNegativeButton("取消")
                .setPositiveButton("保存")
                .setBackground(R.color.white)
                .show();
    }

    //忘记密码弹出输入查看密码对话框
    private void popInputPasswordDialog() {
        new CommomDialog(LoginActivity.this, R.style.dialog, new CommomDialog.OnCloseListener() {
            @Override
            public void onClick(Dialog dialog, boolean confirm) {
                if (confirm) {
                    if (CommomDialog.getPassword().equals(mPassForCheck)) {
                        dialog.dismiss();
                        Intent intent = new Intent(LoginActivity.this, PassWordActivity.class);
                        startActivityForResult(intent, Constant.PASSWORD_REQUEST_CODE);
                    } else {
                        Toast.makeText(mContext, "密码输入错误，请重试！", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    dialog.dismiss();
                }
            }
        })
                .setItemVisibility(Constant.PASSWORD_INPUT_ET)
                .setBackground(R.color.white)
                .setTitle("请输入密码：")
                .setPositiveButton("确定")
                .setNegativeButton("取消")
                .show();
    }

    //用户注册对话框
    private void userRegisterDialog() {
        new CommomDialog(LoginActivity.this, R.style.dialog, "当前用户名不存在，是否注册新用户？", new CommomDialog.OnCloseListener() {
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
                    SharedPreferencesUtils.putString(mContext, Constant.USER_INFO_SP, Constant.CURRENT_USER_NAME, mCurrentUserId);
                    Intent intent = new Intent(LoginActivity.this, TabMainActivity.class);
                    //intent.putExtra(Constant.CURRENT_USER_NAME, mCurrentUserId);
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
    }

    //用户登录
    private void userLogin() {
        for (int i = 0; i < mUserNameList.size(); i++) {
            if (mUserNameList.get(i).equals(mCurrentUserId)) {
                if (mPassWordList.get(i).equals(mCurrentPassWord)) {
                    Toast.makeText(mContext, "登录成功！", Toast.LENGTH_LONG).show();
                    ifRememberPass();
                    SharedPreferencesUtils.putString(mContext, Constant.USER_INFO_SP, Constant.CURRENT_USER_NAME, mCurrentUserId);
                    Intent intent = new Intent(LoginActivity.this, TabMainActivity.class);
                    //intent.putExtra(Constant.CURRENT_USER_NAME, mCurrentUserId);
                    Log.i(TAG, "onClick mCurrentUserName: " + mCurrentUserId);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(mContext, "密码不正确", Toast.LENGTH_LONG).show();
                }
                return;
            }

        }
    }

    //是否记住密码
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

    //设置密码是否可见
    private void setPasswordVisible(boolean visible) {
        if (visible) {
            mPassWordVisible.setImageDrawable(mContext.getDrawable(R.drawable.wifi_password_visible));
        } else {
            mPassWordVisible.setImageDrawable(mContext.getDrawable(R.drawable.invisible));
        }
    }

    //用户登录及注册相关
    private void userRegisterOrLogin() {
        mCurrentUserId = mUserIdEt.getText().toString().trim();
        mCurrentPassWord = mPassEt.getText().toString().trim();
        if (mCurrentUserId.equals("")) {
            mPromptText.setText("用户名为空");
            setPromptTextNull();
            return;
        }
        if (mCurrentPassWord.equals("")) {
            mPromptText.setText("密码为空");
            setPromptTextNull();
            return;
        }

        if (!mUserNameList.contains(mCurrentUserId)) {
            userRegisterDialog();
        } else {
            userLogin();
        }
    }

    //设置提示信息为空
    private void setPromptTextNull() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!mPromptText.getText().toString().equals(""))
                    mPromptText.setText("");
            }
        }, 2000);
    }

    /**
     * 显示下拉列表的popupWindow弹框
     */
    @SuppressWarnings("deprecation")
    private void showPopupWindow() {
        View contentView = LayoutInflater.from(LoginActivity.this).inflate(
                R.layout.popup_list, null);
        mDropViewList = (ListView) contentView.findViewById(R.id.dropview_list);
        mListAdapter = new PopListAdapter(mContext, mUserNameList);
        mDropViewList.setAdapter(mListAdapter);
        mDropViewList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View view, int position,
                                    long arg3) {
                mUserIdEt.setText(mUserNameList.get(position));
                mUserIdEt.setSelection(mUserNameList.get(position).length());
                mPassEt.setText(mPassWordList.get(position));
                mPassEt.setSelection(mPassWordList.get(position).length());
                mPopupWindow.dismiss();
            }
        });
        mPopupWindow = new PopupWindow(contentView,
                mUserInputLayout.getWidth(), mUserInputLayout.getHeight() * 3, true);
        mPopupWindow.setOutsideTouchable(true);

        mPopupWindow.setTouchable(true);
        mPopupWindow.setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View arg0, MotionEvent arg1) {
                // 这里如果返回true的话，touch事件将被拦截
                // 拦截后 PopupWindow的onTouchEvent不被调用，这样点击外部区域无法dismiss
                return false;
            }
        });
        //如果不设置PopupWindow的背景，无论是点击外部区域还是Back键都无法dismiss弹框
        mPopupWindow.setBackgroundDrawable(getResources().getDrawable(R.drawable.pop_background));
        // 设置好参数之后再show
        mPopupWindow.showAsDropDown(mUserInputLayout, 0, 30);
        mPopupWindow.setAnimationStyle(R.style.PopUpAnimation);
    }

    //从密码表返回结果
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constant.PASSWORD_REQUEST_CODE && resultCode == RESULT_OK) {
            int position = data.getIntExtra(Constant.PASSWORD_POSITION, 0);
            mUserIdEt.setText(mUserNameList.get(position));
            mUserIdEt.setSelection(mUserNameList.get(position).length());
            mPassEt.setText(mPassWordList.get(position));
            mPassEt.setSelection(mPassWordList.get(position).length());
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        CommonFlashAnimationHelper.destroySplashAnimator();
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
