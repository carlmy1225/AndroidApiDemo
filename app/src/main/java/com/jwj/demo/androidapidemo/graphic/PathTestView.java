package com.jwj.demo.androidapidemo.graphic;

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
 * Date: 2017/11/7
 * Copyright: Ctrip
 */

public class PathTestView extends View {

    public PathTestView(Context context) {
        super(context);
    }

    public PathTestView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        Path path = new Path();
        path.moveTo(0, 0);
        path.cubicTo(0.09f * 100, 0.97f * 100, 0.33f * 100, 0.88f * 100, 100, 100);
        path.close();

        Paint p = new Paint();
        p.setColor(0xffff0000);
        canvas.drawPath(path, p);

    }
}
