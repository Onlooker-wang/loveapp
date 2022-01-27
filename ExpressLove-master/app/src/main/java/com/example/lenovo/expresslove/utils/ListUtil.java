/*
 * Copyright 2019-2029 BYD Corporation. All rights reserved.
 *
 * BYD Corporation and its licensors retain all intellectual property
 * and proprietary rights in and to this software, related documentation
 * and any modifications thereto. Any use, reproduction, disclosure or
 * distribution of this software and related documentation without an express
 * license agreement from BYD Corporation is strictly prohibited.
 */

package com.example.lenovo.expresslove.utils;

import android.content.Context;
import android.content.res.TypedArray;

import java.util.ArrayList;
import java.util.List;


public class ListUtil {

    public static List<Integer> getArrayData(Context context, int resId) {
        TypedArray array = context.getResources().obtainTypedArray(resId);

        int len = array.length();
        List<Integer> intArray = new ArrayList<>();

        for (int i = 0; i < len; i++) {
            intArray.add(array.getResourceId(i, 0));
        }
        array.recycle();
        return intArray;
    }
}
