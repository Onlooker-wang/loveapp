package com.wy.qk.expresslove.utils;

import android.content.Context;
import android.media.MediaPlayer;

import com.wy.qk.expresslove.R;


public class PlayAudioUtil {

    private MediaPlayer mMediaPlayer = null;
    private Context mContext;

    public PlayAudioUtil(Context context) {
        mContext = context;
    }

    public void startAudioPlayer() {
        stopAudio();
        playAudio(R.raw.duoxingyun);
    }

    private void playAudio(int audioResId) {
        try {
            stopAudio();
            mMediaPlayer = MediaPlayer.create(mContext, audioResId);
            mMediaPlayer.start();
            mMediaPlayer.setLooping(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void stopAudio() {
        if (mMediaPlayer != null && mMediaPlayer.isPlaying()) {
            mMediaPlayer.stop();
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }
}
