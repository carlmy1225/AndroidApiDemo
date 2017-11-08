package com.jwj.demo.androidapidemo.animation;

import android.animation.TimeInterpolator;
import android.graphics.Path;
import android.graphics.PathMeasure;

/**
 * Description:  超级简单的插值器
 * 可以去到网站 http://cubic-bezier.com/#.01,1,.95,.42 , 去设计自己想要的动画曲线，
 * 然后传入贝塞尔曲线的3个坐标点，就可以实现任意的曲线的动画了
 * Author: wjxie
 * Date: 2017/11/8
 * Copyright: Ctrip
 */

public class CustomInterceptor implements TimeInterpolator {
    PathMeasure pathMeasure;
    float[] pos = new float[2];

    public CustomInterceptor(float x1, float y1, float x2, float y2, float x3, float y3) {
        pathMeasure = new PathMeasure();
        Path path = new Path();
        path.moveTo(0, 0);
        path.cubicTo(x1, y1, x2, y2, x3, y3);
        pathMeasure.setPath(path, false);
    }

    @Override
    public float getInterpolation(float input) {
        float distance = pathMeasure.getLength() * input;
        pathMeasure.getPosTan(distance, pos, null);
        return pos[1];
    }
}
