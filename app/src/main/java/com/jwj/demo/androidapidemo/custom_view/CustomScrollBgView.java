package com.jwj.demo.androidapidemo.custom_view;

import android.content.Context;
import android.graphics.Canvas;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.NestedScrollView;
import android.util.AttributeSet;
import android.view.View;

/**
 * Description: 描述
 * Author: wjxie
 * Date: 2017/9/26
 * Copyright: Ctrip
 */

public class CustomScrollBgView extends NestedScrollView {

    private View bgroundView;
    private View contentView;

    public CustomScrollBgView(@NonNull Context context) {
        super(context);
    }

    public CustomScrollBgView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
    }


}
