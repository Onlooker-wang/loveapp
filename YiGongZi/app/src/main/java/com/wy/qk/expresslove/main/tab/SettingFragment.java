package com.wy.qk.expresslove.main.tab;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
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

import com.wy.qk.expresslove.R;
import com.wy.qk.expresslove.app.MyApplication;
import com.wy.qk.expresslove.main.FireworksActivity;
import com.wy.qk.expresslove.main.LoginActivity;
import com.wy.qk.expresslove.music.MusicActivity;
import com.wy.qk.expresslove.utils.CircleImageDrawable;
import com.wy.qk.expresslove.utils.Constant;
import com.wy.qk.expresslove.utils.SharedPreferencesUtils;

import java.io.IOException;

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
    private Uri mHeadImageUri, mHeadImageCropUri;
    private String mUriStr, mCropUriStr;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mViewRoot = inflater.inflate(R.layout.fragment_setting, container, false);
        mContext = MyApplication.getContext();
        initView();
        setListener();
        return mViewRoot;
    }

    @Override
    public void onResume() {
        super.onResume();
        mUriStr = SharedPreferencesUtils.getString(mContext, Constant.HEAD_IMAGE_URI_SP, Constant.HEAD_IMAGE_URI, "");
        mCropUriStr = SharedPreferencesUtils.getString(mContext, Constant.HEAD_IMAGE_URI_SP, Constant.HEAD_IMAGE_CROP_URI, "");
        Log.i(TAG, "onCreate: uriStr = " + mUriStr + ",mCropUriStr = " + mCropUriStr);
        mHeadImageUri = Uri.parse(mUriStr);//原图uri
        mHeadImageCropUri = Uri.parse(mCropUriStr);//裁剪后uri
        setHeadImage(mHeadImageCropUri, mCropUriStr);
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

    }

    private void setHeadImage(Uri uri, String uriStr) {
        Bitmap bitmap = null;
        try {
            bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (uriStr.equals("")) {
            mHeadImg.setImageDrawable(mCircleImageDrawable);
        } else {
            mHeadImg.setImageDrawable(new CircleImageDrawable(bitmap));
        }
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
                startActivity(new Intent(getActivity(), LoginActivity.class));
            }
        });

        mNextLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), UserInfoActivity.class));
            }
        });
    }
}
