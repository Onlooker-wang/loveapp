package com.wy.lpr.expresslove.main.tab;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.wy.lpr.expresslove.R;

public class ToolFragment extends PagerFragment{
    private static final String TAG = "ToolFragment";
    private View mViewRoot;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mViewRoot = inflater.inflate(R.layout.fragment_tool, container, false);

        return mViewRoot;
    }
}
