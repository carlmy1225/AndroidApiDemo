package com.jwj.demo.androidapidemo.layout;

import android.content.Context;
import android.support.v4.widget.DrawerLayout;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

/**
 * Description: 描述
 * Author: wjxie
 * Date: 2018/1/30
 * Copyright: Ctrip
 */

public class CustomDrawerLayout extends DrawerLayout implements DrawerLayout.DrawerListener {

    DrawerSlideBar slideBar;
    DrawerSlideBgView drawerSlideBgView;
    float motionY;

    public CustomDrawerLayout(Context context) {
        super(context);
    }

    public CustomDrawerLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        addDrawerListener(this);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        decorBgDrawable();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_MOVE) {
            motionY = ev.getY();
        }
        return super.dispatchTouchEvent(ev);
    }

    private void decorBgDrawable() {
        for (int i = 0; i < getChildCount(); i++) {
            View view = getChildAt(i);
            if (view instanceof DrawerSlideBar) {
                slideBar = (DrawerSlideBar) view;
                drawerSlideBgView = new DrawerSlideBgView(getContext());

                removeView(view);
                FrameLayout bgContainer = new FrameLayout(getContext());
                bgContainer.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                bgContainer.addView(drawerSlideBgView,
                        new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                bgContainer.addView(view);

                DrawerLayout.LayoutParams params = new DrawerLayout.LayoutParams(slideBar.getLayoutParams().width, slideBar.getLayoutParams().height
                        , Gravity.START);
                addView(bgContainer, i, params);
                break;
            }
        }
    }


    public void setTouchXY(float percent) {
        drawerSlideBgView.setTouchY(motionY,percent);
    }

    @Override
    public void onDrawerSlide(View drawerView, float slideOffset) {
        setTouchXY(slideOffset);
    }

    @Override
    public void onDrawerOpened(View drawerView) {

    }

    @Override
    public void onDrawerClosed(View drawerView) {

    }

    @Override
    public void onDrawerStateChanged(int newState) {

    }
}
