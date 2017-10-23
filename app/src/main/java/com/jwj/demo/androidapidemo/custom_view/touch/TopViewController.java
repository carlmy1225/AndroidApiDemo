package com.jwj.demo.androidapidemo.custom_view.touch;

import android.support.v4.view.ViewCompat;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import static com.jwj.demo.androidapidemo.custom_view.touch.IBUTouchController.AUTO_SCROLL_DOWN;

/**
 * Description: 描述
 * Author: wjxie
 * Date: 2017/10/20
 * Copyright: Ctrip
 */

public class TopViewController{

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
    IBUTouchController touchUtilNew;
    List<View> icons = new ArrayList<>();

    public TopViewController(final ViewGroup topView, final IBUTouchController touchUtilNew) {
        this.topView = topView;
        this.touchUtilNew = touchUtilNew;

        final LinearLayout coverIconView = (LinearLayout) topView.getChildAt(2);
        if (coverIconView != null) {
            for (int i = 0; i < coverIconView.getChildCount(); i++) {
                ViewGroup viewGroup = (ViewGroup) coverIconView.getChildAt(i);
                for (int j = 0; j < viewGroup.getChildCount(); j++) {
                    icons.add(viewGroup.getChildAt(j));
                }
            }
        }

        coverIconView.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                topHeight = (int)coverIconView.getY();
                touchUtilNew.setTopHeight(topHeight);
                coverIconView.getViewTreeObserver().removeOnPreDrawListener(this);
                return false;
            }
        });

    }

    private void computeViewPositionRange(View view, float positionY, float deltaY, float maxScroll, float minScroll) {
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

        Log.d("topView_y", topView.getY() + "");
        Log.d("topView_percent", topView.getY() / topHeight + "");

        return -topView.getY();
    }

    public ViewGroup getTopView() {
        return topView;
    }


    public boolean isAutoAnimator(int isAutoScrollDirection, boolean isVelocityEnable){
        if (getTopViewY() > 0 && getTopViewY() < topHeight) {       //滑动动画，滑到一般时候
            if (isAutoScrollDirection == com.jwj.demo.androidapidemo.custom_view.touch.IBUTouchController.AUTO_SCROLL_DOWN) {
                touchUtilNew.startAnimator(com.jwj.demo.androidapidemo.custom_view.touch.IBUTouchController.AUTO_SCROLL_DOWN);
            } else {
                if (getTopViewY() > animatorHeight || isVelocityEnable) {
                    touchUtilNew.startAnimator(com.jwj.demo.androidapidemo.custom_view.touch.IBUTouchController.AUTO_SCROLL_UP);
                } else {
                    touchUtilNew.startAnimator(AUTO_SCROLL_DOWN);
                }
            }
            return true;
        }
        return false;
    }


    float topDistance;

    public void animatorStart() {
        topDistance = getTopViewY();
    }

    public void animator(float percent, Object... args) {
        int topY;
        int up = (int)args[0];

        if (up > 0) {
            topY = (int) (percent * 1.2f * (topHeight - Math.abs(topDistance)));
        } else {
            topY = -(int) (percent * topDistance);
        }
        int desY = (int) touchUtilNew.computeRangeAlpha(topDistance + topY, 0, (int) topHeight);
        ViewCompat.setY(topView, -desY);
    }



    /**
     * @param deltaY            滑动的y方向的距离，有方向之分
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
            ViewCompat.setY(topView, -deltaY);
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

    public void scrollDown(int deltaY) {
        final float topScrollY = getTopViewY();
        if (topScrollY > 0 && deltaY !=0) {
            int topDetalY = (int) (deltaY * 0.7f);
            computeViewPositionRange(topView, topView.getY(), topDetalY, topHeight, 0);

        }
    }


    public void refreshScroll(float deltaY , int refreshHeight){
        final float topScrollY = getTopViewY();
        if (deltaY > 0) {
            if (topScrollY < 0) {
                if (topScrollY + deltaY > 0) {
                    ViewCompat.setY(topView, 0);
                } else {
                    ViewCompat.setY(topView, -(topScrollY + deltaY));
                }

            } else if (topScrollY == 0) {
                ViewCompat.setY(topView, -deltaY);
            }
        } else if (deltaY < 0) {
            if (topScrollY <= 0) {
                //下拉刷新
                deltaY = deltaY / 2;
                if (Math.abs(topScrollY + deltaY) > refreshHeight) {
                    ViewCompat.setY(topView, refreshHeight);
                } else if (Math.abs(topScrollY + deltaY) <= refreshHeight) {
                    computeViewPositionRange(topView, topView.getY(), deltaY, refreshHeight, 0);
                }
            }
        }

        Log.d("topview_y", topView.getY() + "");
    }

    public void refreshUp(float deltaY) {
        float topScrollY = getTopViewY();

        if (topScrollY < 0) {
            if (topScrollY + deltaY > 0) {
                ViewCompat.setY(topView, 0);
            } else {
                ViewCompat.setY(topView, -(topScrollY + deltaY));
            }
        }
    }

    public void refreshDown(float deltaY, int refreshHeight) {
        float topScrollY = getTopViewY();
        if (topScrollY <= 0) {
            //下拉刷新
            deltaY = deltaY / 3;
            if (Math.abs(topScrollY + deltaY) > refreshHeight) {
                android.support.v4.view.ViewCompat.setY(topView, refreshHeight);
            } else if (Math.abs(topScrollY + deltaY) <= refreshHeight) {
                computeViewPositionRange(topView, topView.getY(), deltaY, refreshHeight, 0);
            }
        }
    }


    public void alphaIcons(float percent){
        for (View view : icons) {
            int alpha = (int) touchUtilNew.computeRangeAlpha((1 - percent) * 255, 0, 255);
            if (view.getBackground() != null) {
                view.getBackground().setAlpha(alpha);
            } else {
                view.setAlpha(1 - percent);
            }
        }
    }

    public void scrollToPosition(int y){
        ViewCompat.setY(topView, y);
    }


    public float getTopHeight() {
        return topHeight;
    }
}
