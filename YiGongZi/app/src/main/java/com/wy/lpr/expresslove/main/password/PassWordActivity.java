package com.wy.lpr.expresslove.main.password;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.wy.lpr.expresslove.R;
import com.wy.lpr.expresslove.base.CommonAudioActivity;
import com.wy.lpr.expresslove.app.MyApplication;
import com.wy.lpr.expresslove.utils.Constant;
import com.wy.lpr.expresslove.utils.SharedPreferencesUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PassWordActivity extends CommonAudioActivity {
    private static final String TAG = "PassWordActivity";

    private RecyclerView mUserPassList;
    private Context mContext;
    private PassWordAdapter mPassWordAdapter;
    private List<String> mUserNameList = new ArrayList<>();
    private List<String> mPassWordList = new ArrayList<>();
    private String[] mUserNameString;
    private String[] mPassWordString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pass_word);
        mContext = MyApplication.getContext();

        initView();
        initData();
        setListener();
    }

    private void initView() {
        mUserPassList = (RecyclerView) findViewById(R.id.pass_word_list);
        mUserPassList.setLayoutManager(new GridLayoutManager(mContext, 1));
        mPassWordAdapter = new PassWordAdapter(mContext);
        mUserPassList.setAdapter(mPassWordAdapter);
        mPassWordAdapter.setAdapter(mPassWordAdapter);
    }

    private void initData() {
        mUserNameString = SharedPreferencesUtils.getSharedPreferences(mContext, Constant.USER_INFO_SP, Constant.USER_NAME);
        mPassWordString = SharedPreferencesUtils.getSharedPreferences(mContext, Constant.USER_INFO_SP, Constant.PASS_WORD);
        Log.i(TAG, "initData mUserNameString: " + Arrays.toString(mUserNameString) + ",mPassWordString: " + Arrays.toString(mPassWordString));
        for (String userName : mUserNameString) {
            if (!userName.equals("")) {
                mUserNameList.add(userName);
            }
        }
        for (String passWord : mPassWordString) {
            if (!passWord.equals("")) {
                mPassWordList.add(passWord);
            }
        }

        if (mUserNameList != null && mPassWordList != null) {
            mPassWordAdapter.setList(mUserNameList, mPassWordList);
            mPassWordAdapter.notifyDataSetChanged();
        }


    }

    private void setListener() {
        mPassWordAdapter.setItemClickListener(new PassWordAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Intent intent = getIntent();
                intent.putExtra(Constant.PASSWORD_POSITION, position);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }


}