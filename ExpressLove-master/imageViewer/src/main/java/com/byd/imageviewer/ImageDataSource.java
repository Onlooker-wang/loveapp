package com.byd.imageviewer;

import android.net.Uri;

import androidx.annotation.DrawableRes;

import java.io.File;

/**
 * @author <a href="mailto:jenly1314@gmail.com">Jenly</a>
 */
public interface ImageDataSource {

    /**
     * 返回图片数据源
     * @return {@link Uri}, {@code url}, {@code path},{@link File}, {@link DrawableRes resId}
     */
    Object getDataSource();
}
