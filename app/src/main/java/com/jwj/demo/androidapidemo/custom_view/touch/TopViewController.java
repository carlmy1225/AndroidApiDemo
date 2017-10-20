package com.jwj.demo.androidapidemo.custom_view.touch;

import android.support.v4.view.ViewCompat;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import static com.jwj.demo.androidapidemo.custom_view.touch.IBUTouchUtilNew.AUTO_SCROLL_DOWN;

/**
 * Description: 描述
 * Author: wjxie
 * Date: 2017/10/20
 * Copyright: Ctrip
 */

public class TopViewController {

    /**
     * 松开手时向下或向上动画的分割线
     */
    private int animatorHeight = 200;

    /**
     * topview滚动的高度
     */
    public int topHeight;

    final int TAP = 2;


    ViewGroup topView;
    IBUTouchUtilNew touchUtilNew;

    public TopViewController(ViewGroup topView, IBUTouchUtilNew touchUtilNew) {
        this.topView = topView;
        this.touchUtilNew = touchUtilNew;
    }

    protected void init(int topHeight) {
        this.topHeight = topHeight;
    }

    private void computeViewPositionRange(View view, float positionY, int deltaY, float maxScroll, float minScroll) {
        Log.d("position_range", "posistionY =" + positionY + ",deltaY =" + deltaY);
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

    public float getTopViewY() {
        return -topView.getY();
    }

    public ViewGroup getTopView() {
        return topView;
    }


    /**
     * @param isAutoScrollDirection
     * @param isVelocityEnable
     * @param isTouchTopView        表示可以滑动topview了
     */
    public boolean animator(int isAutoScrollDirection, boolean isVelocityEnable) {
        if (getTopViewY() > 0 && getTopViewY() < topHeight) {       //滑动动画，滑到一般时候
            if (isAutoScrollDirection == IBUTouchUtilNew.AUTO_SCROLL_DOWN) {
                touchUtilNew.startAnimator(IBUTouchUtilNew.AUTO_SCROLL_DOWN);
            } else {
                if (getTopViewY() > animatorHeight || isVelocityEnable) {
                    touchUtilNew.startAnimator(IBUTouchUtilNew.AUTO_SCROLL_UP);
                } else {
                    touchUtilNew.startAnimator(AUTO_SCROLL_DOWN);
                }
            }
            return true;
        }
        return false;
    }


    /**
     * @param deltaY            滑动的y方向的距离，有方向之分
     * @param isZeroCanTouchUp  在y坐标为0时，是否需要向上滑动
     * @param refreshHeight     刷新的高度临界值
     * @param recyclerYToScroll recyclerview滑动到什么时候，可以下滑
     * @return
     */
    public void scrollUp(int deltaY) {
        final float topScrollY = getTopViewY();
        if (topScrollY < 0) {
            if (topScrollY + deltaY > 0) {
                ViewCompat.setY(topView, 0);
            } else {
                ViewCompat.setY(topView, -(topScrollY + deltaY));
            }
        } else if (topScrollY == 0) {
//            if (isZeroCanTouchUp) {
//                ViewCompat.setY(topView, -deltaY);
//            }
        } else if (topScrollY < topHeight) {
            if (topScrollY + deltaY > topHeight) {
                ViewCompat.setY(topView, -topHeight);
            } else if (topScrollY + deltaY + TAP > topHeight) {
                ViewCompat.setY(topView, -topHeight);
            } else {
                ViewCompat.setY(topView, -(topScrollY + deltaY + TAP));
            }
        } else {
            ViewCompat.setY(topView, -topHeight);
        }
    }


    public void scrollDown(int deltaY, boolean recyclerYToScroll) {
        final float topScrollY = getTopViewY();
        if (topScrollY > 0) {
            int topDetalY = (int) (deltaY * 0.7f);
            if (recyclerYToScroll) {
                computeViewPositionRange(topView, topView.getY(), topDetalY, topHeight, 0);
            }
        }
    }

    public void refreshUp(int deltaY) {

    }

    public void refreshDown(int deltaY, int refreshHeight) {
        final float topScrollY = getTopViewY();

        if (topScrollY <= 0) {
            //下拉刷新
            deltaY = deltaY / 3;
            if (Math.abs(topScrollY + deltaY) > refreshHeight) {
                ViewCompat.setY(topView, refreshHeight);
            } else if (Math.abs(topScrollY + deltaY) <= refreshHeight) {
                computeViewPositionRange(topView, topView.getY(), deltaY, refreshHeight, 0);
            }
        }
    }

    public int getTopHeight() {
        return topHeight;
    }
}
