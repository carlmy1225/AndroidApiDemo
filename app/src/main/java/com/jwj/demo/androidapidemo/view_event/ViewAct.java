package com.jwj.demo.androidapidemo.view_event;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.MotionEvent;
import android.view.View;

import com.jwj.demo.androidapidemo.BaseAct;
import com.jwj.demo.androidapidemo.R;

/**
 * Description: 描述
 * Author: wjxie
 * Date: 2017/9/8
 * Copyright: Ctrip
 */

public class ViewAct extends BaseAct {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_view_layout);
        findViewById(R.id.btn).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    return true;
                }
                return false;
            }
        });
    }
}
