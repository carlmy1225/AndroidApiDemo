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
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.LinearInterpolator;

import com.jwj.demo.androidapidemo.R;

/**
 * Description: 描述
 * Author: wjxie
 * Date: 2017/10/9
 * Copyright: Ctrip
 */

public class IBURecyclerView extends RecyclerView {
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

    private int mLastMotionY;
    ScrollCallBack scrollCallBack;
    private int mTotalScrolled = 0;

    public interface ScrollCallBack {
        void onScroll(int scrollY, int deltaY);
    }

    private int coverViewId;
    private View iconViewLayout;


    public IBURecyclerView(Context context) {
        this(context, null);
    }

    public IBURecyclerView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public IBURecyclerView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.layer_content, defStyleAttr, 0);
        defQuadHeight = array.getDimensionPixelSize(R.styleable.layer_content_quad_arc_size, MIN_QUAD_HEIGHT);
        layerColor = array.getColor(R.styleable.layer_content_layer_background_color, 0x00000000);
        resId = array.getResourceId(R.styleable.layer_content_top_visible_view_id, 0);
        mTopVisibleHeight = array.getDimensionPixelSize(R.styleable.layer_content_top_visible_height, 0);

        setPadding(0, mTopVisibleHeight, 0, 0);
        init();

        setOnScrollListener(new OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                mTotalScrolled += dy;
                if (scrollCallBack != null) {
                    scrollCallBack.onScroll(mTotalScrolled, dy);
                }
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }
        });
    }

    @Override
    public void onNestedPreScroll(View target, int dx, int dy, int[] consumed) {
        super.onNestedPreScroll(target, dx, dy, consumed);
    }

    public int getScollYDistance() {
        LinearLayoutManager layoutManager = (LinearLayoutManager) this.getLayoutManager();
        int position = layoutManager.findFirstVisibleItemPosition();
        View firstVisiableChildView = layoutManager.findViewByPosition(position);
        if (firstVisiableChildView != null) {
            int itemHeight = firstVisiableChildView.getHeight();
            return (position) * itemHeight - firstVisiableChildView.getTop();
        }

        return 0;
    }


    public int getmTotalScrolled() {
        return mTotalScrolled;
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
        updateQuad(w / 2, 0, 1);
        bgBitmap = createBgBitmap();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        iconViewLayout = findViewById(coverViewId);
    }

    public void setScrollCallBack(ScrollCallBack scrollCallBack) {
        this.scrollCallBack = scrollCallBack;
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
            updateQuad(x, Math.abs(deltaY), 1);
        } else {
            updateQuad(x, 0, 1);
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
        updateQuad(getWidth() / 2, -defQuadHeight * percent / FACTOR, 1 - percent);
    }


    private void updateQuad(float x, float deltaY, float percent) {
        float quadY = mTopVisibleHeight * percent + defQuadHeight + deltaY * FACTOR;
        float bottomPosition = mTopVisibleHeight * percent + deltaY * FACTOR * 0.5F;

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
                    updateQuad(getWidth() / 2, temp, 1);
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
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mLastMotionY = (int) ev.getY();
                break;

            case MotionEvent.ACTION_MOVE:
                final int y = (int) ev.getY();
                int deltaY = mLastMotionY - y;

                if (getScollYDistance() == 0) {   //滑动顶部
                    if (deltaY < 0) {
                        secureUpdateQuad(ev.getX(), deltaY);
                    } else {
                        canceUpEvent();
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                canceUpEvent();
                break;
        }
        return super.onTouchEvent(ev);
    }


    @Override
    public void onDraw(Canvas canvas) {
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
    private Bitmap createBgBitmap() {
        Bitmap bgBitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_4444);
        Canvas canvas = new Canvas(bgBitmap);
        canvas.drawColor(layerColor);
        return bgBitmap;
    }


}
