package com.jwj.demo.androidapidemo.window;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.jwj.demo.androidapidemo.BaseAct;
import com.jwj.demo.androidapidemo.R;

/**
 * Description: 描述
 * Author: wjxie
 * Date: 2017/9/6
 * Copyright: Ctrip
 */

public class WindowTestAct extends BaseAct {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_window_test_layout);
    }
}
