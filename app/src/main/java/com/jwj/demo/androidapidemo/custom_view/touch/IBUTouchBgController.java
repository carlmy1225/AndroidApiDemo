package com.jwj.demo.androidapidemo.custom_view.touch;

import android.support.v4.view.ViewCompat;

/**
 * Created by jwj on 17/10/20.
 */
public class IBUTouchBgController {

    IBUTouchBgView mainBgView;
    /**
     * 滑动的高度
     */
    int scrollHeight;
    int defQuadHeight = 170; //弧线的高度
    float topDistance;  //临时数据存储


    public IBUTouchBgController(IBUTouchBgView bgView) {
        this.mainBgView = bgView;
        mainBgView.setDefQuadHeight(defQuadHeight);
    }


    public void init(int scrollHeight) {
        this.scrollHeight = scrollHeight;
    }


    public void animatorStart() {
        topDistance = getScrollY();
    }

    public void animator(float percent, Object... args) {
        int topY;
        int up = (int) args[0];

        if (up > 0) {
            topY = (int) (percent * (scrollHeight - Math.abs(topDistance)));
            mainBgView.setCustomAlpha(1 - percent, false);

        } else {
            topY = -(int) (percent * Math.abs(topDistance));
            mainBgView.reBackCustomAlpha(percent, 255, false);
        }
        mainBgView.reBackQuadHeight(1 - percent, false);
        int desY = (int) computeRange(Math.abs(topDistance) + topY, 0, scrollHeight);
        ViewCompat.setY(mainBgView, -desY);
    }

    /**
     * 滚动view
     *
     * @param percent
     */
    public void scrollTo(float percent) {
        ViewCompat.setY(mainBgView, -percent * (defQuadHeight + scrollHeight));
        mainBgView.setCustomAlpha(1 - percent, false);
        mainBgView.updateQuadHeight(1 - percent);
    }

    /**
     * 下拉刷新滑动
     *
     * @param percent
     */
    public void refreshPull(float percent) {
        mainBgView.downScalePercent(percent);
    }

    /**
     * 下拉刷新,回弹
     *
     * @param percent
     */
    public void refreshRelease(float percent) {
        mainBgView.downScalePercent(percent);
    }


    public float getScrollY() {
        return mainBgView.getY();
    }


    /**
     * 设置额外的高度
     */
    public void setOffset(int offsetHeight) {
        mainBgView.setOffsetHeight(offsetHeight);
    }

    /**
     * 控制范围
     *
     * @param distance
     * @param min
     * @param max
     * @return
     */
    private float computeRange(float distance, int min, int max) {
        if (distance > max) {
            distance = max;
        } else if (distance < min) {
            distance = min;
        }
        return distance;
    }

}
