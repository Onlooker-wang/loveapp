package com.wy.qk.expresslove.bean;

import android.app.Application;
import android.media.MediaPlayer;

public final class SingleMediaPlayer extends Application {
    private static MediaPlayer INSTANCE;

    public static MediaPlayer getINSTANCE() {
        if (INSTANCE == null) {
            INSTANCE = new MediaPlayer();
        }
        return INSTANCE;
    }
}
