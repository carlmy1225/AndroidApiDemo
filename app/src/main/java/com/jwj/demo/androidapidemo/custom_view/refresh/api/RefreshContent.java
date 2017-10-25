package com.jwj.demo.androidapidemo.custom_view.refresh.api;

import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

/**
 * 刷新内容组件
 * Created by SCWANG on 2017/5/26.
 */

public interface RefreshContent {
    void moveSpinner(int spinner);

    boolean canRefresh();

    boolean canLoadmore();

    int getMeasuredWidth();

    int getMeasuredHeight();

    void measure(int widthSpec, int heightSpec);

    void layout(int left, int top, int right, int bottom, boolean skip);

    View getView();

    View getScrollableView();

    ViewGroup.LayoutParams getLayoutParams();

    void onActionDown(MotionEvent e);

    void onActionUpOrCancel();

    boolean fling(int velocity);

    void setupComponent(com.jwj.demo.androidapidemo.custom_view.refresh.api.RefreshKernel kernel, View fixedHeader, View fixedFooter);

    void onInitialHeaderAndFooter(int headerHeight, int footerHeight);

    void setScrollBoundaryDecider(ScrollBoundaryDecider boundary);

    void setEnableLoadmoreWhenContentNotFull(boolean enable);

    AnimatorUpdateListener onLoadingFinish(RefreshKernel kernel, int footerHeight, int startDelay, int reboundDuration);
}
