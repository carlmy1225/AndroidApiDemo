package com.jwj.demo.androidapidemo.graphic;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.ViewGroup;

import com.jwj.demo.androidapidemo.BaseAct;

/**
 * Description: 描述
 * Author: wjxie
 * Date: 2017/11/7
 * Copyright: Ctrip
 */

public class GraphicTestAct extends BaseAct {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PathTestView view = new PathTestView(this);
        view.setLayoutParams(new ViewGroup.LayoutParams(100, 200));
        setContentView(view);
    }

}
