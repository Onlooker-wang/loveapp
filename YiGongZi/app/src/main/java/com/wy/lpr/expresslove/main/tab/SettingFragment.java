package com.wy.lpr.expresslove.main.tab;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.wy.lpr.expresslove.R;
import com.wy.lpr.expresslove.app.MyApplication;
import com.wy.lpr.expresslove.main.FireworksActivity;
import com.wy.lpr.expresslove.music.MusicActivity;
import com.wy.lpr.expresslove.utils.CircleImageDrawable;
import com.wy.lpr.expresslove.utils.Constant;
import com.wy.lpr.expresslove.utils.SharedPreferencesUtils;

import su.levenetc.android.textsurface.Text;

public class SettingFragment extends PagerFragment {
    private static final String TAG = "SettingFragment";
    private View mViewRoot;
    private LinearLayout mLocalMusic, mFireWork, mSetting;
    private TextView mAbout, mLoginOut, mUserId;
    private CircleImageDrawable mCircleImageDrawable;
    private ImageView mHeadImg;
    private RelativeLayout mNextLayout;
    private String mCurrentUserName;
    private Context mContext;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mViewRoot = inflater.inflate(R.layout.fragment_setting, container, false);
        mContext = MyApplication.getContext();
        initView();
        setListener();
        return mViewRoot;
    }

    private void initView() {
        mCurrentUserName = SharedPreferencesUtils.getString(mContext, Constant.USER_INFO_SP, Constant.CURRENT_USER_NAME, "");
        mCircleImageDrawable = new CircleImageDrawable(BitmapFactory.decodeResource(getResources(), R.drawable.wangye));
        mLocalMusic = (LinearLayout) mViewRoot.findViewById(R.id.local_music_layout);
        mFireWork = (LinearLayout) mViewRoot.findViewById(R.id.firework_layout);
        mSetting = (LinearLayout) mViewRoot.findViewById(R.id.setting_layout);
        mAbout = (TextView) mViewRoot.findViewById(R.id.about_entry);
        mLoginOut = (TextView) mViewRoot.findViewById(R.id.login_out);
        mNextLayout = (RelativeLayout) mViewRoot.findViewById(R.id.next_layout);
        mUserId = (TextView) mViewRoot.findViewById(R.id.user_id);
        mUserId.setText(mCurrentUserName);
        mHeadImg = (ImageView) mViewRoot.findViewById(R.id.head_img);
        mHeadImg.setImageDrawable(mCircleImageDrawable);
    }

    private void setListener() {
        mLocalMusic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), MusicActivity.class));
            }
        });

        mFireWork.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), FireworksActivity.class));
            }
        });

        mAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), AboutActivity.class));
            }
        });

        mLoginOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "onClick: ");
            }
        });

        mNextLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}
