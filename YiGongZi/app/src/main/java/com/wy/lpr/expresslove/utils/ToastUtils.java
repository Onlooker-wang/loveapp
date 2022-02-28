package com.wy.lpr.expresslove.utils;

import android.content.Context;
import android.widget.Toast;

public class ToastUtils {
    public static void showLongToast(Context context, CharSequence content) {
        Toast.makeText(context.getApplicationContext(), content, Toast.LENGTH_LONG).show();
    }

    public static void showShortToast(Context context, CharSequence content) {
        Toast.makeText(context.getApplicationContext(), content, Toast.LENGTH_SHORT).show();
    }
}
