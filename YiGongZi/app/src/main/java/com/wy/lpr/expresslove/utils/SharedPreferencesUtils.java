package com.wy.lpr.expresslove.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by ZHengBin on 2017/9/25.
 */

public class SharedPreferencesUtils {

    public static int getInt(Context context, String SPName, String strKey) {
        SharedPreferences sp = context.getSharedPreferences(
                SPName, Context.MODE_PRIVATE);
        return sp.getInt(strKey, -1);
    }

    public static int getInt(Context context, String SPName, String strKey, int defData) {
        SharedPreferences sp = context.getSharedPreferences(
                SPName, Context.MODE_PRIVATE);
        return sp.getInt(strKey, defData);
    }

    public static void putInt(Context context, String SPName, String strKey, int data) {
        SharedPreferences sp = context.getSharedPreferences(
                SPName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt(strKey, data);
        editor.apply();
    }

    public static void putString(Context context, String SPName, String key, String content) {
        SharedPreferences sp = context.getSharedPreferences(SPName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(key, content);
        editor.apply();
    }

    public static String getString(Context context, String SPName, String key, String defValue) {
        SharedPreferences sp = context.getSharedPreferences(SPName, Context.MODE_PRIVATE);
        return sp.getString(key, defValue);
    }

    public static void putBoolean(Context context, String SPName, String key, boolean value) {
        SharedPreferences sp = context.getSharedPreferences(SPName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    public static Boolean getBoolean(Context context, String SPName, String key) {
        SharedPreferences sp = context.getSharedPreferences(SPName, Context.MODE_PRIVATE);
        return sp.getBoolean(key, false);
    }

    public static String[] getSharedPreferences(Context context, String SPName, String key) {
        String regularEx = "#";
        String[] str;
        SharedPreferences sp = context.getSharedPreferences(SPName, Context.MODE_PRIVATE);
        String values;
        values = sp.getString(key, "");
        str = values.split(regularEx);
        return str;
    }

    public static void setSharedPreferences(Context context, String SPName, String key, String[] values) {
        String regularEx = "#";
        String str = "";
        SharedPreferences sp = context.getSharedPreferences(SPName, Context.MODE_PRIVATE);
        if (values != null && values.length > 0) {
            for (String value : values) {
                str += value;
                str += regularEx;
            }
            SharedPreferences.Editor editor = sp.edit();
            editor.putString(key, str);
            editor.apply();
        } else {
            SharedPreferences.Editor editor = sp.edit();
            editor.putString(key, str);
            editor.apply();
        }
    }

    public static void deleteDataForSp(Context context, String SPName) {
        SharedPreferences sp = context.getSharedPreferences(SPName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.clear();
        editor.apply();
    }

    public static void remove(Context context, String SPName, String key) {
        SharedPreferences sp = context.getSharedPreferences(SPName,
                Context.MODE_PRIVATE);
        sp.edit().remove(key).apply();
    }
}
