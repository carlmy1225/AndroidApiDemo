package com.jwj.demo.androidapidemo.performance.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

import com.jwj.demo.androidapidemo.BaseAct;

/**
 * Description: 描述
 * Author: wjxie
 * Date: 2017/9/19
 * Copyright: Ctrip
 */

public class DrawTimePerformance extends BaseAct {
    private long startTime;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        startTime = System.currentTimeMillis();
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            Log.d("drawTime = ", (System.currentTimeMillis() - startTime) + "");
        }
    }

}
