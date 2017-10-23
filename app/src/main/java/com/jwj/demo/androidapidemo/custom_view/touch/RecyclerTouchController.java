package com.jwj.demo.androidapidemo.custom_view.touch;

import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

/**
 * Description: 描述
 * Author: wjxie
 * Date: 2017/10/20
 * Copyright: Ctrip
 */

public class RecyclerTouchController{
    public String TAG = RecyclerTouchController.class.getSimpleName();

    //recyclerview相关
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
    int recyclerScrollTo = 200;

    final boolean isScrollBy = true;

    int oldPaddingTop;


    IBUTouchRecyclerView recyclerView;
    IBUTouchController touchUtilNew;


    public RecyclerTouchController(IBUTouchRecyclerView recyclerView, IBUTouchController ibuTouchController) {
        this.recyclerView = recyclerView;
        this.touchUtilNew = ibuTouchController;


    }

    /**
     *
     * @param refreshHeight   下拉刷新的高度
     */
    void init(int refreshHeight , int barBottom){
        recyclerOldY = recyclerView.getmTotalScrolled();
        recyclerBottomLimitY = recyclerOldY + refreshHeight;
        recyclerTopLimitY = recyclerView.getPaddingTop() - barBottom + recyclerScrollTo;  //滑动到顶部的距离
        oldPaddingTop = recyclerView.getPaddingTop();

        Log.d(TAG, "scrollY =" + recyclerView.getmTotalScrolled());
        Log.d(TAG, "recyclerOldY =" + recyclerOldY);
        Log.d(TAG, "recyclerBottomLimitY =" + recyclerBottomLimitY);
        Log.d(TAG, "recyclerTopLimitY =" + recyclerTopLimitY);
    }


    public void scrollUp(int dy, RecyclerView recyclerView) {

    }


    public void scrollRecyclerViewTo(float scrollY) {
        ViewCompat.setY(recyclerView , scrollY);
    }


    public int getScrollY() {
        return recyclerView.getmTotalScrolled();
    }

    public float getY() {
        return recyclerView.getY();
    }



    int topRecycler;
    int mLastY;

    public void animatorStart() {
        topRecycler = recyclerView.getmTotalScrolled();
        mLastY = topRecycler;
    }

    public void animator(float percent, Object... args) {
        int up = (int) args[0];
        int recyclerY = 0;
       //percent = 1 - percent;

        if (up > 0) {
            recyclerY = (int) (percent * Math.abs(topRecycler - recyclerTopLimitY));
        } else {
            recyclerY = - (int) (percent * Math.abs(topRecycler - recyclerOldY) * 1.2f);
        }
        recyclerView.scrollBy(0, recyclerY + topRecycler - mLastY);

        //computeViewPositionRange(recyclerView, topRecycler, recyclerY, recyclerOldY, recyclerTopLimitY);

        mLastY = recyclerY + topRecycler;

    }

    public void onRefreshScroll(float deltaY, int refreshHeight ){
        if(deltaY < 0){
            deltaY = deltaY /2;
            Log.d("refresh_scorlly =" , recyclerView.getY() + "");
        }else{

        }
        computeViewPositionRange(recyclerView, recyclerView.getY(), deltaY, recyclerBottomLimitY, recyclerOldY);
    }


    float recyclerY;

    public void refreshAnimatorStart() {
        recyclerY = getY();
    }

    public void refreshAnimator(float percent) {
        scrollRecyclerViewTo(recyclerOldY + (recyclerY - recyclerOldY) * percent);
    }

    /**
     * 内容是否足够
     *
     * @return
     */
    public boolean isEnoughContent() {
        return recyclerView.isScrollEnable();
    }


    public boolean isAnimator(int isAutoScrollDirection, boolean isVelocityEnable, int barHeight) {
        Log.d(TAG,"isAnimator scrollY = " + getScrollY());
        if (getScrollY() > recyclerTopLimitY) {
            return false;
        }else{
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


    /**
     * 提供给父容器，是否可以拦截下拉刷新了
     *
     * @return
     */
    public boolean isInterceptedRefresh() {
        return true;
    }


    /**
     * scrollby
     * @param view
     * @param positionY
     * @param deltaY
     * @param maxScroll
     * @param minScroll
     */
    private void computeViewScrollRange(View view,  int positionY, int deltaY, int maxScroll, int minScroll) {
        int y = positionY;
        y = positionY + deltaY;
        if(y < minScroll){
            y = minScroll;
        }else if(y > maxScroll){
            y = maxScroll;
        }else{
            recyclerView.scrollBy(0,deltaY);
        }
    }


    private void computeViewPositionRange(View view,  float positionY, float deltaY, float maxScroll, float minScroll) {
        //移动view坐标
        float y = positionY - deltaY;
        if(y < minScroll){
            y = minScroll;
        }else if(y > maxScroll){
            y = maxScroll;
        }
        ViewCompat.setY(recyclerView , y);
    }

    public int getRecyclerTopLimitY() {
        return recyclerTopLimitY;
    }
}
