package com.jwj.demo.androidapidemo.custom_view.touch;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.jwj.demo.androidapidemo.R;


/**
 * Created by jwj on 17/10/13.
 */
public class IBUTouchBgView extends FrameLayout {
    private final int MIN_QUAD_HEIGHT = 120; //默认的曲线弧度
    private final float FACTOR = 0.4f;   //滑动因子

    private Path mPath;
    private Paint mPaint, bitmapPaint;
    private PorterDuffXfermode duffXfermode;
    private Bitmap bgBitmap;
    private ValueAnimator valueAnimator, showFadeAnimator;

    private int layerColor;   //背景色
    private int resId;  //使用view，来挖空头部的高度
    private int mTopVisibleHeight;   //头部需要挖空的高度
    private int defQuadHeight = MIN_QUAD_HEIGHT;  //默认曲线高度
    private int tempQuadHeight;      //弧度拉升的高度
    private int alpha = 255;
    private Activity activity;
    private ImageView preView;
    private float scale, totalDetalY;


    public IBUTouchBgView(Context context) {
        this(context, null);
    }

    public IBUTouchBgView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public IBUTouchBgView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.IBuTouchView, defStyleAttr, 0);
        defQuadHeight = array.getDimensionPixelSize(R.styleable.IBuTouchView_quad_arc_size, MIN_QUAD_HEIGHT);
        layerColor = array.getColor(R.styleable.IBuTouchView_layer_background_color, 0x00000000);
        resId = array.getResourceId(R.styleable.IBuTouchView_top_visible_view_id, 0);
        mTopVisibleHeight = array.getDimensionPixelSize(R.styleable.IBuTouchView_top_visible_height, 0);
        activity = (Activity) context;
        init();
    }


    private void init() {
        setBackgroundColor(0x00000000); //设置为透明，有其他情况要处理，可以去掉
        mPath = new Path();
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(0xff);
        duffXfermode = new PorterDuffXfermode(PorterDuff.Mode.SRC_OUT);

        bitmapPaint = new Paint();
        bitmapPaint.setAntiAlias(true);
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (resId > 0) {
            View view = activity.findViewById(resId);
            if (view != null) mTopVisibleHeight = view.getMeasuredHeight();
        }

        mTopVisibleHeight += 100;
        updateQuad(w / 2, 0, 1, 0);
        bgBitmap = createBgBitmap();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        int count = getChildCount();
        if (count > 0) {
            preView = (ImageView) getChildAt(0);
        }
    }

    public void showImage(Drawable drawable, boolean needAnimator) {
        if (needAnimator) {
            if (preView.getDrawable() != null) {
                TransitionDrawable td = new TransitionDrawable(new Drawable[]{preView.getDrawable(), drawable});
                preView.setImageDrawable(td);
                td.startTransition(1000);
            } else {
                preView.setImageDrawable(drawable);
            }
        } else {
            preView.setImageDrawable(drawable);
        }
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
            updateQuad(x, Math.abs(deltaY), 1, 0);
        } else {
            updateQuad(x, 0, 1, 0);
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
        updateQuad(getWidth() / 2, -defQuadHeight * percent / FACTOR, 1 - percent, 0);
    }


    private void updateQuad(float x, float deltaY, float percent, float positionDetalY) {
        float quadY = mTopVisibleHeight * percent + defQuadHeight + deltaY * FACTOR + positionDetalY;
        float bottomPosition = mTopVisibleHeight * percent + deltaY * FACTOR * 0.5F + positionDetalY;

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
                    updateQuad(getWidth() / 2, temp, 1, 0);
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
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);

        int sc = canvas.saveLayer(0, 0, getWidth(), getHeight(), null, Canvas.ALL_SAVE_FLAG);
        mPaint.setColor(0xffff0000);
        mPaint.setAlpha(alpha);
        canvas.drawPath(mPath, mPaint);

        bitmapPaint.setXfermode(duffXfermode);
        if (bgBitmap != null) {
            canvas.drawBitmap(bgBitmap, 0, 0, bitmapPaint);
        }
        canvas.restoreToCount(sc);
    }


    public void setCustomAlpha(float alphaPercent) {
        if (alphaPercent < 0) {
            alphaPercent = 0;
        }
        if (alphaPercent > 1) {
            alphaPercent = 1;
        }

        alpha = Math.round(alphaPercent * 255);
        postInvalidate();
    }

    /**
     * 向下拉动背景图变大
     * 1.2f
     *
     * @param percent
     */
    public void downScalePercent(float percent) {
        if (preView != null) {
            scale = percent * 0.5f;
            preView.setScaleX(1 + scale);
            preView.setScaleY(1 + scale);
        }
        totalDetalY = percent * 100;
        updateQuad(getWidth() / 2, 0, 1, totalDetalY);
    }

    /**
     * 恢复到原始状态
     *
     * @param percent
     */
    public void autoBackScale(float percent) {
        if (preView != null) {
            preView.setScaleX(1 + scale * percent);
            preView.setScaleY(1 + scale * percent);
        }
        updateQuad(getWidth() / 2, 0, 1, percent * totalDetalY);

        if (percent == 0) {
            scale = 0;
            totalDetalY = 0;
        }
    }


    /**
     * 创建背景颜色
     *
     * @return
     */
    private Bitmap createBgBitmap() {
        Bitmap bgBitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_4444);
        Canvas canvas = new Canvas(bgBitmap);
        canvas.drawColor(layerColor);
        return bgBitmap;
    }

    public int getmTopVisibleHeight() {
        return mTopVisibleHeight;
    }
}
