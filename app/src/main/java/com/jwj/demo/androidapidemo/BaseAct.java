package com.jwj.demo.androidapidemo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

/**
 * Description: 描述
 * Author: wjxie
 * Date: 2017/9/6
 * Copyright: Ctrip
 */

public class BaseAct extends FragmentActivity {

    public String TAG = BaseAct.class.getName();

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        Log.d(TAG, "onAttachedToWindow");
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onAttachedToWindow");
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onAttachedToWindow");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(TAG, "onAttachedToWindow");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onAttachedToWindow");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onAttachedToWindow");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onAttachedToWindow");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onAttachedToWindow");
    }
}
