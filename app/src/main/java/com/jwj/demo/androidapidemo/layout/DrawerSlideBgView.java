package com.jwj.demo.androidapidemo.layout;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Description: 描述
 * Author: wjxie
 * Date: 2018/1/30
 * Copyright: Ctrip
 */

public class DrawerSlideBgView extends View {
    Paint paint;
    Path path;

    public DrawerSlideBgView(Context context) {
        super(context);
        init();
    }

    public DrawerSlideBgView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setAntiAlias(true);
        paint.setColor(0xff374747);
        path = new Path();
    }

    public void setTouchY(float y) {
        path.reset();
        path.lineTo(0, -getHeight());
        path.rQuadTo(getWidth(), y, 0, getHeight() * 3);
        path.close();
        invalidate();
    }

    public void setBgColor(int color) {
        paint.setColor(color);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawPath(path, paint);
    }
}