package com.wy.lpr.expresslove.utils;

import android.app.Dialog;
import android.content.Context;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wy.lpr.expresslove.R;

public class CommomDialog extends Dialog implements View.OnClickListener {
    private static final String TAG = "CommomDialog";

    private TextView mContentTxt;
    private TextView mTitleTxt;
    private TextView mSubmitTxt;
    private TextView mCancelTxt;
    private ImageView mSmile, mSad;
    private static EditText mPasswordInput;

    private Context mContext;
    private String mContent;
    private OnCloseListener mListener;
    private String mPositiveName;
    private String mNegativeName;
    private String mTitle;
    private TypeTextView2 mTypeTextView;
    private AssetManager mAssetManager;
    private RelativeLayout mDialogLayout;
    private int mImageId = 0;
    private int mResId = 0;
    private boolean mItemVisible;

    public CommomDialog(Context context, int themeResId, OnCloseListener listener) {
        super(context, themeResId);
        this.mContext = context;
        this.mListener = listener;
    }

    public CommomDialog(Context context, int themeResId, String content) {
        super(context, themeResId);
        this.mContext = context;
        this.mContent = content;
    }

    public CommomDialog(Context context, int themeResId, String content, OnCloseListener listener) {
        super(context, themeResId);
        Log.i(TAG, "CommomDialog: " + content + ",theme:" + themeResId + ",content:" + content);
        this.mContext = context;
        this.mContent = content;
        this.mListener = listener;
    }

    public CommomDialog(AssetManager assetManager, Context context, int themeResId, String content, OnCloseListener listener) {
        super(context, themeResId);
        Log.i(TAG, "CommomDialog: " + content + ",theme:" + themeResId + ",content:" + content);
        this.mContext = context;
        this.mContent = content;
        this.mListener = listener;
        this.mAssetManager = assetManager;
    }

    protected CommomDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        this.mContext = context;
    }

    public CommomDialog setTitle(String title) {
        this.mTitle = title;
        return this;
    }

    public CommomDialog setBackground(int imageId) {
        mImageId = imageId;
        return this;
    }

    public CommomDialog setPositiveButton(String name) {
        this.mPositiveName = name;
        return this;
    }

    public CommomDialog setNegativeButton(String name) {
        this.mNegativeName = name;
        return this;
    }

    public CommomDialog setItemVisibility(int resId) {
        mResId = resId;
        return this;
    }

    public static String getPassword() {
        String passWord = mPasswordInput.getText().toString().trim();
        Log.i(TAG, "getPassword: " + passWord);
        return passWord;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_commom);
        setCanceledOnTouchOutside(false);
        initView();
    }

    private void initView() {
        mContentTxt = (TextView) findViewById(R.id.content);
        mPasswordInput = (EditText) findViewById(R.id.password_input);
        mDialogLayout = (RelativeLayout) findViewById(R.id.dialog_layout);
        mTypeTextView = findViewById(R.id.dialog_sentence);
        /*Typeface typeface = Typeface.createFromAsset(mAssetManager, "fonts/myttf.ttf");
        mTypeTextView.setTypeface(typeface);*/
        mTitleTxt = (TextView) findViewById(R.id.title);
        mSubmitTxt = (TextView) findViewById(R.id.submit);
        mSubmitTxt.setOnClickListener(this);
        mCancelTxt = (TextView) findViewById(R.id.cancel);
        mCancelTxt.setOnClickListener(this);
        mSad = findViewById(R.id.sad);
        mSad.setOnClickListener(this);
        mSmile = findViewById(R.id.smile);
        mSmile.setOnClickListener(this);

        mTypeTextView.start(mContent);
        mContentTxt.setText(mContent);
        if (!TextUtils.isEmpty(mPositiveName)) {
            mSubmitTxt.setText(mPositiveName);
        }

        if (!TextUtils.isEmpty(mNegativeName)) {
            mCancelTxt.setText(mNegativeName);
        }

        if (!TextUtils.isEmpty(mTitle)) {
            mTitleTxt.setText(mTitle);
        }
        if (mImageId != 0) {
            mDialogLayout.setBackgroundResource(mImageId);
        }

        if (mResId == Constant.CONTENT_TEXT) {
            mContentTxt.setVisibility(View.VISIBLE);
            mPasswordInput.setVisibility(View.GONE);
        } else if (mResId == Constant.PASSWORD_INPUT_ET) {
            mContentTxt.setVisibility(View.GONE);
            mPasswordInput.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cancel:
                /*Log.i(TAG, "onClick: cancel:" + cancelTxt.getText().toString());
                if (cancelTxt.getText().toString().equals("滚呐！肉麻啦")) {
                    if (listener != null) {
                        listener.onClick(this, false);
                    }
                    cancelTxt.setText("开心！爱你呦");
                    submitTxt.setText("滚呐！肉麻啦");
                    Animation scaleAnimation = AnimationUtils.loadAnimation(MyApplication.getContext(), R.anim.anim);
                    cancelTxt.startAnimation(scaleAnimation);
                    submitTxt.startAnimation(scaleAnimation);
                    Log.i(TAG, "onClick: end" + cancelTxt.getText().toString());
                } else {
                    if (listener != null) {
                        listener.onClick(this, true);
                    }
                }*/

                if (mListener != null) {
                    mListener.onClick(this, false);
                }
                break;
            case R.id.submit:
                /*Log.i(TAG, "onClick: submit" + submitTxt.getText().toString());
                if (submitTxt.getText().toString().equals("开心！爱你呦")) {
                    if (listener != null) {
                        listener.onClick(this, true);
                    }
                } else {
                    if (listener != null) {
                        listener.onClick(this, false);
                    }
                    submitTxt.setText("开心！爱你呦");
                    cancelTxt.setText("滚呐！肉麻啦");
                    Animation scaleAnimation = AnimationUtils.loadAnimation(MyApplication.getContext(), R.anim.anim);
                    submitTxt.startAnimation(scaleAnimation);
                    cancelTxt.startAnimation(scaleAnimation);
                }*/
                if (mListener != null) {
                    mListener.onClick(this, true);
                }
                break;
            case R.id.smile:
                /*Log.i(TAG, "onClick: "+mSmile.getDrawable());
                if (mSmile.getDrawable().getCurrent().getConstantState() == (ContextCompat.
                        getDrawable(mContext,R.drawable.smile).getConstantState())) {
                    Log.i(TAG, "onClick: mSmile   smile");
                    if (listener != null) {
                        listener.onClick(this, true);
                    }
                } else {
                    Log.i(TAG, "onClick: mSmile    sad");
                    if (listener != null) {
                        listener.onClick(this, false);
                    }
                    mSmile.setImageResource(R.drawable.smile);
                    mSad.setImageResource(R.drawable.sad);
                }*/
            case R.id.sad:
                /*Log.i(TAG, "onClick: "+mSad.getDrawable());
                if (mSmile.getDrawable().getCurrent().getConstantState() == (ContextCompat.
                        getDrawable(mContext,R.drawable.smile).getConstantState())) {
                    Log.i(TAG, "onClick: sad    sad");
                    if (listener != null) {
                        listener.onClick(this, false);
                    }
                    mSmile.setImageResource(R.drawable.sad);
                    mSad.setImageResource(R.drawable.smile);

                } else {
                    Log.i(TAG, "onClick: sad   smile");
                    if (listener != null) {
                        listener.onClick(this, true);
                    }
                }*/
                /*if (mSad.getDrawable().getCurrent().getConstantState() ==(ContextCompat.
                        getDrawable(mContext,R.drawable.smile).getConstantState())) {
                    Log.i(TAG, "onClick: mSad   smile");
                    if (listener != null) {
                        listener.onClick(this, true);
                    }

                } else {
                    Log.i(TAG, "onClick: mSad   sad");
                    if (listener != null) {
                        listener.onClick(this, false);
                    }
                    mSad.setImageResource(R.drawable.smile);
                    mSmile.setImageResource(R.drawable.sad);
                }*/
        }
    }

    public interface OnCloseListener {
        void onClick(Dialog dialog, boolean confirm);
    }
}
