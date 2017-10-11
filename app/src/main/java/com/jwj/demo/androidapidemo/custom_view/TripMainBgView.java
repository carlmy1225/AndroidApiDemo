package com.jwj.demo.androidapidemo.custom_view;

import android.content.Context;
import android.graphics.Canvas;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.FrameLayout;

/**
 * Description: 描述
 * Author: wjxie
 * Date: 2017/9/29
 * Copyright: Ctrip
 */

public class TripMainBgView extends FrameLayout {
    private int topQuadY;           //曲线的y坐标
    private int topQuadHeight;      //弧度的高度
    private int coverColor;    //图片覆盖的颜色


    public TripMainBgView(Context context) {
        super(context);
    }

    public TripMainBgView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);


    }
}
