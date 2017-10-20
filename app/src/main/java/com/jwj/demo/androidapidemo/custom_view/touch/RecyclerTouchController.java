package com.jwj.demo.androidapidemo.custom_view.touch;

import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Description: 描述
 * Author: wjxie
 * Date: 2017/10/20
 * Copyright: Ctrip
 */

public class RecyclerTouchController {
    //recyclerview相关
    /**
     * 滑动底部y坐标
     */
    float recyclerBottomLimitY;

    /**
     * 滑动顶部的y坐标
     */
    float recyclerTopLimitY;

    /**
     * 起始y坐标
     */
    float recyclerOldY;

    IBUTouchRecyclerView recyclerView;


    public RecyclerTouchController(IBUTouchRecyclerView recyclerView) {
        this.recyclerView = recyclerView;
    }


    float animatorY;

    public void onAnimatorStart() {
        animatorY = recyclerView.getY();
    }


    public void onAnimator(float percent, int up) {
        int recyclerY = 0;
        switch (up) {
            case IBUTouchUtil.AUTO_REFRESH_UP:
                recyclerY = (int) (percent * (animatorY - recyclerTopLimitY));
                break;
            case IBUTouchUtil.AUTO_SCROLL_DOWN:
                recyclerY = (int) (percent * (animatorY - recyclerOldY) * 1.2f);
                break;
        }

        if (up == IBUTouchUtil.AUTO_SCROLL_DOWN) {
            computeViewPositionRange(recyclerView, animatorY, recyclerY, recyclerOldY, recyclerTopLimitY);
        }
    }


    public void scrollUp(int dy, RecyclerView recyclerView) {

    }


    public void scrollRecyclerViewTo(float scrollY) {
        ViewCompat.setY(recyclerView, scrollY);
    }


    public float getScrollY() {
        return recyclerView.getY();
    }

    public float getY() {
        return recyclerView.getY();
    }


    /**
     * 内容是否足够
     *
     * @return
     */
    public boolean isEnoughContent() {
        return recyclerView.isScrollEnable();
    }


    public boolean isCallPullTopToDown() {
        return recyclerView.getY() > recyclerTopLimitY;  //+ barBgView.getHeight();
    }


    /**
     * 提供给父容器，是否可以拦截下拉刷新了
     *
     * @return
     */
    public boolean isInterceptedRefresh() {
        return true;
    }


    private void computeViewPositionRange(View view, float positionY, int deltaY, float maxScroll, float minScroll) {
        if (deltaY > 0) {   //向上
            if (positionY - deltaY < minScroll) {
                ViewCompat.setY(view, minScroll);
            } else {
                ViewCompat.setY(view, positionY - deltaY);
            }
        } else {
            if (positionY - deltaY > maxScroll) {
                ViewCompat.setY(view, maxScroll);
            } else {
                ViewCompat.setY(view, positionY - deltaY);
            }
        }
    }
}
