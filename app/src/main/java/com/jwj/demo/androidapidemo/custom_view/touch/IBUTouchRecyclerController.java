package com.jwj.demo.androidapidemo.custom_view.touch;

import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

/**
 * Description: 描述
 * Author: wjxie
 * Date: 2017/10/20
 * Copyright: Ctrip
 */

public class IBUTouchRecyclerController {
    public String TAG = IBUTouchRecyclerController.class.getSimpleName();

    /**
     * 滑动底部y坐标
     */
    float recyclerBottomLimitY;

    /**
     * 滑动顶部的y坐标
     */
    int recyclerTopLimitY;

    /**
     * 起始y坐标
     */
    int recyclerOldY;


    /**
     * 滚动超过的部分
     */
    int recyclerScrollTo = 0;
    int oldPaddingTop;
    IBUTouchRecyclerView recyclerView;
    IBUTouchController touchUtilNew;


    public IBUTouchRecyclerController(IBUTouchRecyclerView recyclerView, IBUTouchController ibuTouchController) {
        this.recyclerView = recyclerView;
        this.touchUtilNew = ibuTouchController;
    }

    /**
     */
    void init(int refreshHeight, int barBottom) {
        recyclerOldY = recyclerView.getmTotalScrolled();
        recyclerBottomLimitY = recyclerOldY + refreshHeight;
        recyclerTopLimitY = recyclerView.getPaddingTop() - barBottom + recyclerScrollTo;  //滑动到顶部的距离
        oldPaddingTop = recyclerView.getPaddingTop();
        touchUtilNew.setBgScrollHeight(recyclerTopLimitY);
    }


    public int getScrollY() {
        if (!shouldScroll()) {
            return 1000;
        }
        return recyclerView.getmTotalScrolled();
    }

    int topRecycler;
    int mLastY;

    public void animatorStart() {
        topRecycler = recyclerView.getmTotalScrolled();
        mLastY = topRecycler;
    }

    public void animator(float percent, Object... args) {
        int up = (int) args[0];
        int recyclerY;
        if (up > 0) {
            recyclerY = (int) (percent * Math.abs(topRecycler - getRecyclerTopLimitY()));
            recyclerView.scrollBy(0, topRecycler + recyclerY - mLastY);
        } else {
            recyclerY = -(int) (percent * Math.abs(topRecycler - recyclerOldY) + 1);
            recyclerView.scrollBy(0, topRecycler + recyclerY - mLastY);
        }
        mLastY = recyclerY + topRecycler;
    }

    public boolean isAnimator(int isAutoScrollDirection, boolean isVelocityEnable, int barHeight) {
        if (getScrollY() > recyclerTopLimitY + barHeight / 2) {
            return false;
        } else {
            if (isVelocityEnable) {
                touchUtilNew.startAnimator(isAutoScrollDirection);
            } else if (recyclerView.getY() >= recyclerTopLimitY - barHeight / 2) {
                touchUtilNew.startAnimator(com.jwj.demo.androidapidemo.custom_view.touch.IBUTouchController.AUTO_SCROLL_UP);
            } else {
                touchUtilNew.startAnimator(isAutoScrollDirection);
            }
            return true;
        }
    }


//    public boolean shouldStopScrollDown() {
//        LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
//        View view = layoutManager.findViewByPosition(0);
//        if (view == null) {
//            return false;
//        } else if (Math.abs(view.getTop()) <= recyclerTopLimitY) {
//            return true;
//        } else {
//            return false;
//        }
//    }

    public boolean shouldScroll() {
        LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        View view = layoutManager.findViewByPosition(0);
        if (view == null) {
            return false;
        }
        return true;
    }


    public int getRecyclerTopLimitY() {
        return recyclerTopLimitY;
    }
}
