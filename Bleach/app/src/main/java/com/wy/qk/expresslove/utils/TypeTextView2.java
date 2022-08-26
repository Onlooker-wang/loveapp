package com.wy.qk.expresslove.utils;

import android.content.Context;
import android.media.MediaPlayer;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

/**
 * 类描述:打字机效果
 * 出处:https://github.com/zmywly8866/TypeTextView
 */
public class TypeTextView2 extends TextView {
    private static final int TYPE_TIME_DELAY = 100;
    private Context mContext = null;
    private MediaPlayer mMediaPlayer = null;
    private OnTypeViewListener mOnTypeViewListener = null;
    private String mShowTextString = null;
    private int mTypeTimeDelay = TYPE_TIME_DELAY;
    private Timer mTypeTimer = null;

    public interface OnTypeViewListener {
        void onTypeOver();

        void onTypeStart();
    }

    class TypeTimerTask extends TimerTask {
        TypeTimerTask() {
        }

        public void run() {
            TypeTextView2.this.post(new Runnable() {
                public void run() {
                    if (TypeTextView2.this.getText().toString().length() < TypeTextView2.this.mShowTextString.length()) {
                        TypeTextView2.this.setText(TypeTextView2.this.mShowTextString.substring(0, TypeTextView2.this.getText().toString().length() + 1));
                        TypeTextView2.this.startAudioPlayer();
                        TypeTextView2.this.startTypeTimer();
                        return;
                    }
                    TypeTextView2.this.stopTypeTimer();
                    if (TypeTextView2.this.mOnTypeViewListener != null) {
                        TypeTextView2.this.mOnTypeViewListener.onTypeOver();
                    }
                }
            });
        }
    }

    public TypeTextView2(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initTypeTextView(context);
    }

    public TypeTextView2(Context context, AttributeSet attrs) {
        super(context, attrs);
        initTypeTextView(context);
    }

    public TypeTextView2(Context context) {
        super(context);
        initTypeTextView(context);
    }

    public void setOnTypeViewListener(OnTypeViewListener onTypeViewListener) {
        this.mOnTypeViewListener = onTypeViewListener;
    }

    public void start(String textString) {
        start(textString, TYPE_TIME_DELAY);
    }

    public void start(final String textString, final int typeTimeDelay) {
        if (!TextUtils.isEmpty(textString) && typeTimeDelay >= 0) {
            post(new Runnable() {
                public void run() {
                    TypeTextView2.this.mShowTextString = textString;
                    TypeTextView2.this.mTypeTimeDelay = typeTimeDelay;
                    TypeTextView2.this.setText("");
                    TypeTextView2.this.startTypeTimer();
                    if (TypeTextView2.this.mOnTypeViewListener != null) {
                        TypeTextView2.this.mOnTypeViewListener.onTypeStart();
                    }
                }
            });
        }
    }

    public void stop() {
        stopTypeTimer();
        stopAudio();
    }

    private void initTypeTextView(Context context) {
        this.mContext = context;
    }

    private void startTypeTimer() {
        stopTypeTimer();
        this.mTypeTimer = new Timer();
        this.mTypeTimer.schedule(new TypeTimerTask(), (long) this.mTypeTimeDelay);
    }

    private void stopTypeTimer() {
        if (this.mTypeTimer != null) {
            this.mTypeTimer.cancel();
            this.mTypeTimer = null;
        }
    }

    private void startAudioPlayer() {
        stopAudio();
    }

    private void playAudio(int audioResId) {
        try {
            stopAudio();
            this.mMediaPlayer = MediaPlayer.create(this.mContext, audioResId);
            this.mMediaPlayer.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void stopAudio() {
    }
}
