package com.jwj.demo.androidapidemo.custom_view.page;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.jwj.demo.androidapidemo.R;
import com.jwj.demo.androidapidemo.util.DensityUtil;

/**
 * Description: 描述
 * Author: wjxie
 * Date: 2017/11/6
 * Copyright: Ctrip
 */

public class PageContainer extends FrameLayout implements ViewPager.OnPageChangeListener {

    final int SCREEN_RATE = 3;   //占屏幕的几倍宽
    float bottomPercent = 0.6f;
    int bottomY;
    Paint colorPaint;
    int screenWidth, screenHeight;
    int hourseHeight;
    float drawableScaleFactor;  //资源文件缩放比例


    public PageContainer(Context context) {
        super(context);
        init();
    }

    public PageContainer(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }


    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            int width = child.getMeasuredWidth();
            LayoutParams params = (LayoutParams) child.getLayoutParams();

            top = bottomY - child.getMeasuredHeight();
            if (child.getId() == R.id.people) {
                top += DensityUtil.dp2px(18.5f);
                bottom = top + child.getMeasuredHeight();
                child.layout(0, top, width, bottom);
            } else if (child.getId() == R.id.hotel || child.getId() == R.id.train) {
                bottom = top + child.getMeasuredHeight();
                left = right;
                child.layout(left, top, left + width, bottom - DensityUtil.dp2px(9));
            } else if (child.getId() == R.id.plane) {
                right = screenWidth - params.rightMargin;
                top = bottomY - hourseHeight;
                bottom = top + child.getMeasuredHeight();
                child.layout(right - child.getMeasuredWidth(), top, right, bottom);
            } else if (child.getId() == R.id.cloud) {
                top = bottomY - hourseHeight - 200;
                bottom = top + child.getMeasuredHeight();
                child.layout(0, top, width, bottom);
            } else {
                bottom = top + child.getMeasuredHeight();
                child.layout(0, top, width, bottom);
            }
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            if (child instanceof ImageView) {
                ImageView iv = (ImageView) child;
                if (iv.getDrawable() == null) {
                    return;
                }
                int width = iv.getDrawable().getIntrinsicWidth();
                int height = iv.getDrawable().getIntrinsicHeight();
                if (child.getId() == R.id.hourse || child.getId() == R.id.cloud
                        || child.getId() == R.id.hourse_shadow || child.getId() == R.id.tree) {
                    width = screenWidth * 3;
                    height = (int) (width * 1f / iv.getDrawable().getIntrinsicWidth() * height);
                } else if (child.getId() == R.id.people) {
                    width = screenWidth;
                    height = (int) (width * 1f / iv.getDrawable().getIntrinsicWidth() * height);
                } else {
                    width *= drawableScaleFactor;
                    height *= drawableScaleFactor;
                }
                child.measure(MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY));
                if (child.getId() == R.id.hourse) {
                    hourseHeight = iv.getDrawable().getIntrinsicHeight();
                }
            }
        }
    }

    void init() {
        screenWidth = getResources().getDisplayMetrics().widthPixels;
        screenHeight = getResources().getDisplayMetrics().heightPixels;
        bottomY = (int) (screenHeight * bottomPercent);
        colorPaint = new Paint();
        colorPaint.setAntiAlias(true);
        colorPaint.setStyle(Paint.Style.FILL);

        Drawable drawable = getResources().getDrawable(R.mipmap.myctrip_hourse_bg, getContext().getTheme());
        drawableScaleFactor = screenWidth * SCREEN_RATE * 1F / drawable.getIntrinsicWidth();
    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        Log.v("pageScroll", "positionOffset =" + positionOffset + " offsetPixels =" + positionOffsetPixels);
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            if (child instanceof PageImageView) {
                PageImageView iv = (PageImageView) child;
                iv.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }
        }
    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        colorPaint.setColor(0xff00D3E5);
        canvas.drawRect(0, 0, getWidth(), bottomY, colorPaint);
        colorPaint.setColor(0xff3B50BC);
        canvas.drawRect(0, bottomY, getWidth(), getHeight(), colorPaint);
    }
}
