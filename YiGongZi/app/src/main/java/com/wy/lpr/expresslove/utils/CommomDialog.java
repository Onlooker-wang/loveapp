package com.wy.lpr.expresslove.utils;

import android.app.Dialog;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.wy.lpr.expresslove.R;
import com.wy.lpr.expresslove.main.MyApplication;

public class CommomDialog extends Dialog implements View.OnClickListener {
    private static final String TAG = "CommomDialog";

    private TextView contentTxt;
    private TextView titleTxt;
    private TextView submitTxt;
    private TextView cancelTxt;
    private ImageView mSmile, mSad;

    private Context mContext;
    private String content;
    private OnCloseListener listener;
    private String positiveName;
    private String negativeName;
    private String title;
    private TypeTextView2 mTypeTextView;
    private AssetManager mAssetManager;

    public CommomDialog(Context context) {
        super(context);
        this.mContext = context;
    }

    public CommomDialog(Context context, int themeResId, String content) {
        super(context, themeResId);
        this.mContext = context;
        this.content = content;
    }

    public CommomDialog(Context context, int themeResId, String content, OnCloseListener listener) {
        super(context, themeResId);
        Log.i(TAG, "CommomDialog: " + content + ",theme:" + themeResId + ",content:" + content);
        this.mContext = context;
        this.content = content;
        this.listener = listener;
    }
    public CommomDialog(AssetManager assetManager,Context context, int themeResId, String content, OnCloseListener listener) {
        super(context, themeResId);
        Log.i(TAG, "CommomDialog: " + content + ",theme:" + themeResId + ",content:" + content);
        this.mContext = context;
        this.content = content;
        this.listener = listener;
        this.mAssetManager = assetManager;
    }

    protected CommomDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        this.mContext = context;
    }

    public CommomDialog setTitle(String title) {
        this.title = title;
        return this;
    }

    public CommomDialog setPositiveButton(String name) {
        this.positiveName = name;
        return this;
    }

    public CommomDialog setNegativeButton(String name) {
        this.negativeName = name;
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_commom);
        setCanceledOnTouchOutside(false);
        initView();
    }

    private void initView() {
        contentTxt = (TextView) findViewById(R.id.content);
        mTypeTextView=findViewById(R.id.dialog_sentence);
        Typeface typeface = Typeface.createFromAsset(mAssetManager, "fonts/myttf.ttf");
        mTypeTextView.setTypeface(typeface);
        titleTxt = (TextView) findViewById(R.id.title);
        submitTxt = (TextView) findViewById(R.id.submit);
        submitTxt.setOnClickListener(this);
        cancelTxt = (TextView) findViewById(R.id.cancel);
        cancelTxt.setOnClickListener(this);
        mSad = findViewById(R.id.sad);
        mSad.setOnClickListener(this);
        mSmile = findViewById(R.id.smile);
        mSmile.setOnClickListener(this);

        mTypeTextView.start(content);
        contentTxt.setText(content);
        if (!TextUtils.isEmpty(positiveName)) {
            submitTxt.setText(positiveName);
        }

        if (!TextUtils.isEmpty(negativeName)) {
            cancelTxt.setText(negativeName);
        }

        if (!TextUtils.isEmpty(title)) {
            titleTxt.setText(title);
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cancel:
                Log.i(TAG, "onClick: cancel:"+cancelTxt.getText().toString());
                if (cancelTxt.getText().toString().equals("滚呐！肉麻啦")){
                    if (listener != null) {
                        listener.onClick(this, false);
                    }
                    cancelTxt.setText("开心！爱你呦");
                    submitTxt.setText("滚呐！肉麻啦");
                    Animation scaleAnimation = AnimationUtils.loadAnimation(MyApplication.getContext(), R.anim.anim);
                    cancelTxt.startAnimation(scaleAnimation);
                    submitTxt.startAnimation(scaleAnimation);
                    Log.i(TAG, "onClick: end"+cancelTxt.getText().toString());
                }else {
                    if (listener != null) {
                        listener.onClick(this, true);
                    }
                }
                break;
            case R.id.submit:
                Log.i(TAG, "onClick: submit"+submitTxt.getText().toString());
                if (submitTxt.getText().toString().equals("开心！爱你呦")){
                    if (listener != null) {
                        listener.onClick(this, true);
                    }
                }else {
                    if (listener != null) {
                        listener.onClick(this, false);
                    }
                    submitTxt.setText("开心！爱你呦");
                    cancelTxt.setText("滚呐！肉麻啦");
                    Animation scaleAnimation = AnimationUtils.loadAnimation(MyApplication.getContext(), R.anim.anim);
                    submitTxt.startAnimation(scaleAnimation);
                    cancelTxt.startAnimation(scaleAnimation);
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
