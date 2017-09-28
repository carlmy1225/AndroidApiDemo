package com.jwj.demo.androidapidemo.custom_view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.LinearLayout;

import com.jwj.demo.androidapidemo.R;

/**
 * 顶部镂空，带弧度的的自定义view
 * <p> top_visible_height 或者 top_visible_view_id 需要设置一个，默认优先使用id.
 * <p> 设置layerDrawable做镂空效果处理，背景不需要进行设置
 * Author: wjxie
 * Date: 2017/9/26
 * Copyright: Ctrip
 */

public class LayerContentView extends LinearLayout {

    private final int MIN_QUAD_HEIGHT = 120; //默认的曲线弧度
    private final float FACTOR = 0.4f;   //滑动因子

    private Path mPath;
    private Paint mPaint;
    PorterDuffXfermode duffXfermode;
    Bitmap bgBitmap;
    ValueAnimator valueAnimator;

    private int layerColor;   //背景色
    private int resId;  //使用view，来挖空头部的高度
    private int mTopVisibleHeight;   //头部需要挖空的高度

    private int defQuadHeight = MIN_QUAD_HEIGHT;  //默认曲线高度
    private int tempQuadHeight;      //弧度拉升的高度

    public LayerContentView(@NonNull Context context) {
        this(context, null);
    }

    public LayerContentView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LayerContentView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.layer_content, defStyleAttr, 0);
        defQuadHeight = array.getDimensionPixelSize(R.styleable.layer_content_quad_arc_size, MIN_QUAD_HEIGHT);
        layerColor = array.getColor(R.styleable.layer_content_layer_background_color, 0x00000000);
        resId = array.getResourceId(R.styleable.layer_content_top_visible_view_id, 0);
        mTopVisibleHeight = array.getDimensionPixelSize(R.styleable.layer_content_top_visible_height, 0);

        init();
    }

    private void init() {
        setBackgroundColor(0x00000000); //设置为透明，有其他情况要处理，可以去掉

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
        if (resId > 0) {
            View view = findViewById(resId);
            if (view != null) mTopVisibleHeight = view.getMeasuredHeight();
        }
        updateQuad(w / 2, 0);
        bgBitmap = createBgBitmap();
    }

    /**
     * 更新弧线
     *
     * @param x      弧度的x方向位置
     * @param deltaY 弧度的变化值
     */
    public void secureUpdateQuad(float x, float deltaY) {
        if (deltaY < 0) {
            tempQuadHeight = (int) deltaY;
            updateQuad(x, Math.abs(deltaY));
        } else {
            updateQuad(x, 0);
            tempQuadHeight = 0;
        }
    }

    /**
     * 根据百分比重置弧线，为初始化时候 (0~1.0)
     *
     * @param percent
     */
    public void secureResetQuad(float percent) {
        if (percent < 0) {
            percent = 0;
        } else if (percent > 1) {
            percent = 1;
        }
        updateQuad(getWidth() / 2, -defQuadHeight * percent / FACTOR);
    }


    private void updateQuad(float x, float deltaY) {
        float quadY = mTopVisibleHeight + defQuadHeight + deltaY * FACTOR;
        float bottomPosition = mTopVisibleHeight + deltaY * FACTOR * 0.5F;

        mPath.reset();
        mPath.moveTo(0, 0);
        mPath.lineTo(getWidth(), 0);
        mPath.lineTo(getWidth(), bottomPosition);
        mPath.quadTo(x, quadY, 0, bottomPosition);
        mPath.close();
        invalidate();
    }

    /**
     * 弧线回弹至初始位置
     */
    public void canceUpEvent() {
        if (tempQuadHeight != 0) {
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
                    int temp = (int) (factor * Math.abs(tempQuadHeight));
                    updateQuad(getWidth() / 2, temp);
                    postInvalidate();
                }
            });

            valueAnimator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    tempQuadHeight = 0;
                }
            });

            valueAnimator.start();
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int sc = canvas.saveLayer(0, 0, getWidth(), getHeight(), null, Canvas.ALL_SAVE_FLAG);
        canvas.drawPath(mPath, mPaint);
        mPaint.setXfermode(duffXfermode);
        mPaint.setColor(0xffdddddd);
        if (bgBitmap != null) {
            canvas.drawBitmap(bgBitmap, 0, 0, mPaint);
        }
        canvas.restoreToCount(sc);
    }

    /**
     * 创建背景颜色
     *
     * @return
     */
    public Bitmap createBgBitmap() {
        Bitmap bgBitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_4444);
        Canvas canvas = new Canvas(bgBitmap);
        canvas.drawColor(layerColor);
        return bgBitmap;
    }

}
