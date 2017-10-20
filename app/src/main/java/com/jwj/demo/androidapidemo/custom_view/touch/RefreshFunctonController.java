package com.jwj.demo.androidapidemo.custom_view.touch;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

import com.airbnb.lottie.LottieAnimationView;
import com.airbnb.lottie.LottieComposition;
import com.airbnb.lottie.OnCompositionLoadedListener;

/**
 * Description: 描述
 * Author: wjxie
 * Date: 2017/10/20
 * Copyright: Ctrip
 */

public class RefreshFunctonController {

    /**
     * 刷新准备初始状态
     */
    final int STATE_READY = 0;

    /**
     * 刷新开始
     */
    final int STATE_START = 1;

    /**
     * 正在刷新
     */
    final int STATE_REFRESHING = 2;

    /**
     * 下拉刷新的高度
     */
    private final int refreshHeight = 170;

    /**
     * 刷新结束
     */
    final int STATE_END = 3;
    int freshState = STATE_READY;  //刷新的状态

    LottieAnimationView refreshView;
    IBUTouchUtil.ScrollAnimator mScrollAnimator;

    /**
     * 下拉刷新，恢复动画接口
     */
    public interface RecoverCallBack {
        void onAnimator(float percent);

        void onAnimatorStart();
    }


    /**
     * 下拉刷新回调
     */
    private IBUTouchUtil.RefreshCallBack refreshCallBack;

    /**
     * 恢复回调
     */
    IBUTouchUtil.RefreshFunction.RecoverCallBack recoverCallBack;


    public RefreshFunctonController(LottieAnimationView refreshView) {
        this.refreshView = refreshView;
        initRefreshAnimation();
    }


    /**
     * 初始化刷新动画
     */
    private void initRefreshAnimation() {
        if (refreshView != null) {
            LottieComposition.Factory.fromAssetFileName(refreshView.getContext(), "c-loading.json", new OnCompositionLoadedListener() {
                @Override
                public void onCompositionLoaded(@Nullable LottieComposition composition) {
                    refreshView.setComposition(composition);
                }
            });
            refreshView.loop(true);
        }
    }


    public boolean isRunning() {
        return (mScrollAnimator != null && mScrollAnimator.isRunning());
    }


    public void refresh() {

    }

    public void isAutoAnimator(float scrollY) {
        //下拉刷新处理地方
        if (Math.abs(scrollY) >= refreshHeight) {
            //播放刷新动画
            startRefreshAnima();
        } else {
            releasePullAnimation();
        }
    }


    private float getTopViewPositionY() {
//        if (topScrollFunction != null) {
//            return topScrollFunction.getTopViewY();
//        }
        return 0;
    }


    public void startRefreshAnima() {
        if (freshState == STATE_REFRESHING) {
            return;
        }
        freshState = STATE_START;
        if (refreshCallBack != null) {
            refreshCallBack.onFreshStart();
        }

        if (refreshView != null) {
            refreshView.playAnimation();
        }

        freshState = STATE_REFRESHING;
        if (refreshCallBack != null) {
            refreshCallBack.onFreshing();
        }
    }

    /**
     * 下拉刷新，松开手之后的动画
     */
    protected void releasePullAnimation() {
        if (freshState == STATE_REFRESHING) {
            return;
        }

        if (recoverCallBack != null) {
            recoverCallBack.onAnimatorStart();
        }

        if (mScrollAnimator != null && mScrollAnimator.isRunning()) {
            mScrollAnimator.cancel();
        }

        if (mScrollAnimator == null) {
            mScrollAnimator = new IBUTouchUtil.ScrollAnimator(300, new AccelerateDecelerateInterpolator(), 1f, 0) {
                float refreshY, alpha;

                @Override
                public void onAnimationStart(Animator animation) {
                    refreshY = refreshView.getY();
                    alpha = refreshView == null ? 0 : refreshView.getAlpha();
                }

                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    float percent = (float) animation.getAnimatedValue();
                    if (recoverCallBack != null) {
                        recoverCallBack.onAnimator(percent);
                    }
                    if (refreshView != null && refreshView.getVisibility() == View.VISIBLE && alpha > 0) {
                        ViewCompat.setAlpha(refreshView, percent * alpha);
                        ViewCompat.setY(refreshView, refreshY * percent);
                    }
                }
            };
        }
        mScrollAnimator.start();
    }


    public void complete() {
        if (mScrollAnimator != null && mScrollAnimator.isRunning()) {
            mScrollAnimator.cancel();
        }

        freshState = STATE_END;
        if (refreshCallBack != null) {
            refreshCallBack.onComplete();
        }

        releasePullAnimation();
        if (refreshView != null) {
            refreshView.cancelAnimation();
        }
    }

    /**
     * 下拉刷新向下滑动
     *
     * @param percent
     */
    private void pullRefresh(float percent, float scrollY, int deltaY) {
        if (refreshView != null) {
            if (freshState == STATE_REFRESHING || freshState == STATE_START) {
                android.support.v4.view.ViewCompat.setAlpha(refreshView, percent);
            } else {
                if (deltaY < 0) {
                    if (percent >= 0.9f) {
                        refreshView.setVisibility(View.VISIBLE);
                        android.support.v4.view.ViewCompat.setAlpha(refreshView, percent);
                    } else {
                        android.support.v4.view.ViewCompat.setAlpha(refreshView, percent);
                    }
                } else {
                    android.support.v4.view.ViewCompat.setAlpha(refreshView, percent);
                }

                if (percent == 0) {
                    refreshView.setVisibility(View.GONE);
                }
            }
            android.support.v4.view.ViewCompat.setY(refreshView, scrollY);
        }
    }

    public void setRefreshCallBack(IBUTouchUtil.RefreshCallBack refreshCallBack) {
        this.refreshCallBack = refreshCallBack;
    }

    public void setRecoverCallBack(IBUTouchUtil.RefreshFunction.RecoverCallBack recoverCallBack) {
        this.recoverCallBack = recoverCallBack;
    }

}
