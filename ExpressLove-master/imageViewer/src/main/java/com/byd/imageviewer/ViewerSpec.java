package com.byd.imageviewer;

import android.content.pm.ActivityInfo;
import android.graphics.drawable.Drawable;

import androidx.annotation.Nullable;
import androidx.annotation.StyleRes;


import com.byd.imageviewer.loader.ImageLoader;

import java.util.List;

/**
 * @author <a href="mailto:jenly1314@gmail.com">Jenly</a>
 */
enum ViewerSpec {

    INSTANCE;

    int position;

    List<?> listData;

    @Nullable
    ImageLoader imageLoader;

    boolean isShowIndicator;

    @Nullable
    Drawable placeholderDrawable;

    @Nullable
    Drawable errorDrawable;

    @StyleRes
    int theme = R.style.ImageViewerTheme;

    int orientation = ActivityInfo.SCREEN_ORIENTATION_BEHIND;

    void reset(){
        position = 0;
        listData = null;
        imageLoader = null;
        isShowIndicator = false;
        placeholderDrawable = null;
        errorDrawable = null;
        theme = R.style.ImageViewerTheme;
        orientation = ActivityInfo.SCREEN_ORIENTATION_BEHIND;
    }

}
