package com.jwj.demo.androidapidemo.custom_view.page;

import android.animation.TimeInterpolator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.BounceInterpolator;

import com.jwj.demo.androidapidemo.R;

/**
 * Description: 描述
 * Author: wjxie
 * Date: 2017/11/7
 * Copyright: Ctrip
 */

public class PageImageView extends AppCompatImageView {

    TimeInterpolator timeInterpolator, bounceInterceptor;
    int screenWidth;
    PathMeasure pathMeasure, hourseShadow;
    float tans[] = new float[2];
    float pageLeftPercent;   //left位置距离屏幕的百分比
    float showPercent;                  //当前页滑动百分比，然后开始滑动
    private float leftToRightPercent;  //view的left位置到屏幕右边的距离的百分比
    private boolean mEnableScroll;

    public PageImageView(Context context) {
        super(context);
        init();
    }

    public PageImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        final TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.PagerWelcome, defStyleAttr, 0);
        pageLeftPercent = a.getFloat(R.styleable.PagerWelcome_pageLeftPercent, 0f);
        showPercent = a.getFloat(R.styleable.PagerWelcome_pageShowPercent, 0f);
        mEnableScroll = a.getBoolean(R.styleable.PagerWelcome_pageEnableScroll, true);

        leftToRightPercent = 1 - pageLeftPercent;
        init();
    }

    public PageImageView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    void init() {
        timeInterpolator = new AccelerateDecelerateInterpolator();
        bounceInterceptor = new BounceInterpolator();
        screenWidth = getResources().getDisplayMetrics().widthPixels;
        createAccelerateDecelerate();
        createDecelerateAccelerate();
    }

    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        if (!mEnableScroll) return;

        float percent = position + positionOffset;
        if (percent >= 2) {
            if (getId() == R.id.hourse_shadow) {
                percent = position + positionOffset * 1.3f;
                ViewCompat.setTranslationX(this, -percent * screenWidth);
            } else if (getId() == R.id.train) {
                float x = -screenWidth * leftToRightPercent - (percent - 2) * screenWidth * 1.3f;
                ViewCompat.setTranslationX(this, x);
            } else {
                percent = position + positionOffset * 1.15f;
                ViewCompat.setTranslationX(this, -percent * screenWidth);
            }
            return;
        }

        if (getId() == R.id.hourse) {
            percent = position + getFactor(positionOffset);
            ViewCompat.setTranslationX(this, -percent * screenWidth);
        } else if (getId() == R.id.hotel) {
            if (percent <= 1) {
                percent = (percent - showPercent) / (1 - showPercent) * 1.1f;
                if (percent < 0) {
                    percent = 0;
                } else if (percent > 1) {
                    percent = 1;
                }
                ViewCompat.setTranslationX(this, -percent * screenWidth * leftToRightPercent); //0.726f
            } else {
                percent = (percent - 1) * 1.5f;
                float x = -screenWidth * leftToRightPercent - percent * screenWidth;
                ViewCompat.setTranslationX(this, x);
            }
        } else if (getId() == R.id.train) {
            if (position == 1 || position == 2) {
                percent = percent - 1;
                if (percent - position <= 1) {
                    Log.d("train_translateX = ", getTranslationX() + "");
                    percent = (percent - showPercent) / (1 - showPercent);
                    if (percent < 0) {
                        percent = 0;
                    }
                    ViewCompat.setTranslationX(this, -percent * screenWidth * leftToRightPercent);  //0.929
                } else {
                    percent = (percent - 1) * 1.1f;
                    float x = -screenWidth * leftToRightPercent - percent * screenWidth;
                    ViewCompat.setTranslationX(this, x);
                }
            }
        } else if (getId() == R.id.hourse_shadow || getId() == R.id.tree) {
            percent = position + getHourseShadowFactor(positionOffset);
            ViewCompat.setTranslationX(this, -percent * screenWidth);
        } else if (getId() == R.id.plane) {
            if (position == 0 || position == 1) {
                float scrollPercent = 0.1f;
                percent = (percent - scrollPercent);
                if (percent < 0) {
                    percent = 0;
                }
                ViewCompat.setTranslationX(this, -percent * screenWidth * 1.2f);
            }
        } else if (getId() == R.id.cloud) {
            percent = position + getHourseShadowFactor(positionOffset);
            ViewCompat.setTranslationX(this, -percent * screenWidth);
        }
    }


    public float getFactor(float percent) {
        float distance = pathMeasure.getLength() * percent;
        pathMeasure.getPosTan(distance, tans, null);
        Log.d("getFactor :", "percent =" + percent + ", factor =" + tans[0] + "y =" + tans[1]);
        return tans[1];
    }


    public float getHourseShadowFactor(float percent) {
        float distance = hourseShadow.getLength() * percent;
        hourseShadow.getPosTan(distance, tans, null);
        Log.d("getFactor :", "percent =" + percent + ", factor =" + tans[0] + "y =" + tans[1]);
        return tans[1];
    }

    /**
     * 创建先减速后加速的曲线
     */
    public void createDecelerateAccelerate() {
        Path path = new Path();
        path.moveTo(0, 0);
        path.cubicTo(1f, 0.07f, 0.43f, 0.4f, 1, 1);
        hourseShadow = new PathMeasure();
        hourseShadow.setPath(path, false);
    }

    /**
     * 创建先加速减速后的曲线
     */
    public void createAccelerateDecelerate() {
        Path path = new Path();
        path.moveTo(0, 0);
        path.cubicTo(0.09f, 0.82f, 0.3f, 0.72f, 1, 1);
        pathMeasure = new PathMeasure();
        pathMeasure.setPath(path, false);
    }
}
