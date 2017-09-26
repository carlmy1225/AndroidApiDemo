package com.jwj.demo.androidapidemo.custom_view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.animation.LinearInterpolator;
import android.widget.LinearLayout;

import com.jwj.demo.androidapidemo.R;

/**
 * Description: 描述
 * Author: wjxie
 * Date: 2017/9/26
 * Copyright: Ctrip
 */

public class TopBgView extends LinearLayout {

    private final int MIN_QUAD_HEIGHT = 120; //默认的曲线弧度
    private final float FACTOR = 0.4f;   //滑动因子
    private final int PADDING = 60;

    private float mTopBgHeight;
    private Path mPath;
    private Paint mPaint;
    PorterDuffXfermode duffXfermode;
    Bitmap bgBitmap;

    private int quadHeight;
    private int tempX;   //曲线控制点的，x坐标


    public TopBgView(@NonNull Context context) {
        super(context);
        init();
    }

    public TopBgView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    void init() {
        mPath = new Path();
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(0xff000000);

        duffXfermode = new PorterDuffXfermode(PorterDuff.Mode.SRC_OUT);
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mTopBgHeight = findViewById(R.id.top_view).getMeasuredHeight() + PADDING;
        updateQuad(w / 2, 0);
        bgBitmap = createBgBitmap();
    }


    public void secureUpdateQuad(float x, float deltaY) {
        if (deltaY < 0) {
            quadHeight = (int) deltaY;
            tempX = (int) x;

            Log.d("secureUpdateQuad", quadHeight + "");

            updateQuad(x, Math.abs(deltaY));
        } else {
            updateQuad(x, 0);
            quadHeight = 0;
            tempX = 0;
        }
    }


    public void secureResetQuad(float percent) {
        if (percent < 0) {
            return;
        }
        updateQuad(getWidth() / 2, -MIN_QUAD_HEIGHT * percent / FACTOR);
    }


    private void updateQuad(float x, float deltaY) {
        mPath.reset();
        mPath.moveTo(0, 0);
        mPath.lineTo(getWidth(), 0);
        mPath.lineTo(getWidth(), mTopBgHeight);

        float bottom = mTopBgHeight + MIN_QUAD_HEIGHT + deltaY * FACTOR;
        Log.d("bottom", bottom + "");
        mPath.quadTo(x, bottom, 0, mTopBgHeight);
        mPath.close();
        invalidate();
    }

    ValueAnimator valueAnimator;

    public void canceUpEvent() {
        if (quadHeight != 0) {
            if (valueAnimator != null && valueAnimator.isRunning()) {
                return;
            }

            valueAnimator = ValueAnimator.ofFloat(1.0f, 0f);
            valueAnimator.setDuration(200);
            valueAnimator.setInterpolator(new LinearInterpolator());
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    float factor = (float) animation.getAnimatedValue();
                    int temp = (int) (factor * Math.abs(quadHeight));
                    updateQuad(getWidth() / 2, temp);
                    postInvalidate();
                }
            });

            valueAnimator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    quadHeight = 0;
                }
            });

            valueAnimator.start();
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Log.d("canvas", "onDraw调用了");
        int sc = canvas.saveLayer(0, 0, getWidth(), getHeight(), null, Canvas.ALL_SAVE_FLAG);
        canvas.drawPath(mPath, mPaint);
        mPaint.setXfermode(duffXfermode);
        mPaint.setColor(0xffdddddd);
        canvas.drawBitmap(bgBitmap, 0, 0, mPaint);
        canvas.restoreToCount(sc);
    }


    public Bitmap createBgBitmap() {
        Bitmap bgBitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_4444);
        Canvas canvas = new Canvas(bgBitmap);
        canvas.drawColor(0xffdddddd);
        return bgBitmap;
    }

}
