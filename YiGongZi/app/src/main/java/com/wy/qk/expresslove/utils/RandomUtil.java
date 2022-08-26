package com.wy.qk.expresslove.utils;

import android.graphics.Color;
import android.util.Log;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;


public class RandomUtil {
    private static final String TAG = "RandomUtil";
    public static float circle = (float) (2 * Math.PI);

    public static int rgba(int r, int g, int b, int a) {
        return Color.argb(a, r, g, b);
    }

    public static int randomInt(int min, int max) {
        return (int) Math.floor(Math.random() * (max - min + 1)) + min;
    }

    public static float random(float min, float max) {
        return (float) (Math.random() * (max - min) + min);
    }

    //产生随机的argb颜色
    public static int randomrgba(int rmin, int rmax, int gmin, int gmax, int bmin, int bmax, int a) {
        int r = Math.round(random(rmin, rmax));
        int g = Math.round(random(gmin, gmax));
        int b = Math.round(random(bmin, bmax));
        int limit = 5;
        if (Math.abs(r - g) <= limit && Math.abs(g - b) <= limit && Math.abs(b - r) <= limit) {
            return rgba(rmin, rmax, gmin, gmax);
        } else {
            return rgba(r, g, b, a);
        }
    }

    //角度转弧度
    public static float degrad(float angle) {
        return circle / 360 * angle;
    }

    /**
     * 产生origin - bound之间的随机整数
     *
     * @param origin
     * @param bound
     * @return
     */
    public static int threadLocalRandom(int origin, int bound) {
        int i = ThreadLocalRandom.current().nextInt(origin, bound);
        Log.i(TAG, "threadLocalRandom: " + i);
        return i;
    }

    public static int random(int bound) {
        final Random random = new Random();
        int i = random.nextInt(bound);
        Log.i(TAG, "random: " + i);
        return i;
    }
}
