package com.jwj.demo.androidapidemo.custom_view.touch;

/**
 * Created by jwj on 17/10/20.
 */
public class BgTouchController {

    IBUTouchBgView mainBgView;
    IBUTouchController utilNew;

    int scrollHeight;

    final int TAP = 2;


    public BgTouchController(IBUTouchBgView bgView, IBUTouchController utilNew) {
        this.mainBgView = bgView;
        this.utilNew = utilNew;
    }

    public void init(int scrollHeight) {
        this.scrollHeight = scrollHeight;
    }


    int topDistance;

    public void animatorStart() {
        topDistance = getScrollY();
    }

    public void animator(float percent, Object... args) {
        int topY;
        int up = (int) args[0];

        if (up > 0) {
            topY = (int) (percent * 1.2f * (scrollHeight - Math.abs(topDistance)));
        } else {
            topY = -(int) (percent * topDistance);
        }

        int desY = (int) utilNew.computeRangeAlpha(topDistance + topY, 0, scrollHeight);
        handleEffectAnimtor(desY);
    }

    public void scrollUp(int deltaY) {
        if (mainBgView.getScrollY() + deltaY > getScrollHeight()) {
            mainBgView.scrollTo(0, getScrollHeight());
        } else {
            mainBgView.scrollTo(0, mainBgView.getScrollY() + deltaY);
        }

        float percent = mainBgView.getScrollY() * 1f / getScrollHeight();
        mainBgView.setCustomAlpha(1 - percent);
        if (alphaCallBack != null) {
            alphaCallBack.alphaChange(percent, 1);
        }
    }

    public void scrollDown(int deltaY) {
        float percent = mainBgView.getScrollY() * 1f / getScrollHeight();
        if (mainBgView.getScrollY() + deltaY < 0) {
            mainBgView.scrollTo(0, 0);
        } else {
            mainBgView.scrollTo(0, mainBgView.getScrollY() + deltaY);
        }
        mainBgView.setCustomAlpha(1 - percent);
        if (alphaCallBack != null) {
            alphaCallBack.alphaChange(percent, -1);
        }
    }

    void handleEffectAnimtor(float translateY) {
        float percent = utilNew.computeRangeAlpha(translateY * 1f / scrollHeight, 0, 1);

        if (translateY < 0) {
            translateY = 0;
        }
        mainBgView.scrollTo(0, (int) translateY);
        mainBgView.setCustomAlpha(1 - percent);
    }


    public void autoBackScale(float percent) {
        mainBgView.autoBackScale(percent);
    }


    public void refreshPull(float percent) {
        mainBgView.downScalePercent(percent);
    }

    public void refreshRelease(float percent) {
        mainBgView.autoBackScale(percent);
    }

    public int getScrollHeight() {
        return scrollHeight;
    }

    public int getScrollY() {
        return mainBgView.getScrollY();
    }


    /**
     * 透明度变化回调
     */
    public interface AlphaCallBack {
        void alphaChange(float percent, int direction);
    }

    private AlphaCallBack alphaCallBack;

    public void setAlphaCallBack(AlphaCallBack alphaCallBack) {
        this.alphaCallBack = alphaCallBack;
    }
}
