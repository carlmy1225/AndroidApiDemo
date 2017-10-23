package com.jwj.demo.androidapidemo.custom_view.touch;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.view.MotionEvent;
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
    IBUTouchController.ScrollAnimator mScrollAnimator;

    int mLastMotionY , mLastY;

    IBUTouchController touchController;

    /**
          * 刷新回调
     */
    public interface RefreshCallBack {
        void onFreshStart();

        void onFreshing();

        void onComplete();
    }


    /**
     * 下拉刷新，恢复动画接口
     */
    public interface RecoverCallBack {
        void onRefreshAnimator(float percent);

        void onRefreshAnimatorStart();
    }


    /**
     * 下拉刷新回调
     */
    private RefreshCallBack refreshCallBack;

    /**
     * 恢复回调
     */
    RecoverCallBack recoverCallBack;
    boolean isIntercepted;



    public RefreshFunctonController(LottieAnimationView refreshView , IBUTouchController touchController) {
        this.refreshView = refreshView;
        this.touchController = touchController;
        initRefreshAnimation();
    }



    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mLastMotionY = (int) ev.getY();
                mLastY = mLastMotionY;
                if(touchController.isRefresh()){
                    isIntercepted = true;
                }
                break;

            case MotionEvent.ACTION_MOVE:
                final int y = (int) ev.getY();
                int deltaY = mLastY - y;
                mLastY = y;
                return refresh(deltaY);

            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                isIntercepted = false;
                break;
        }
        return false;
    }

    private  boolean refresh(int deltaY) {
//        if(touchController.getTopViewY() == 0 && isIntercepted){
//            return true;
//        }

        if(touchController.isCanrefreshDown(deltaY) && deltaY < 0){
            touchController.onRefreshScroll(deltaY , refreshHeight);
            pullRefresh(deltaY + touchController.getTopViewY(),deltaY);
            return true;
        }
        return false;
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
            recoverCallBack.onRefreshAnimatorStart();
        }

        if (mScrollAnimator != null && mScrollAnimator.isRunning()) {
            mScrollAnimator.cancel();
        }

        if (mScrollAnimator == null) {
            mScrollAnimator = new IBUTouchController.ScrollAnimator(300, new AccelerateDecelerateInterpolator(), 1f, 0) {
                float refreshY, alpha;

                @Override
                public void onAnimationStart(Animator animation) {
                    refreshY = touchController.getTopViewY();
                    alpha = refreshView == null ? 0 : refreshView.getAlpha();
                    touchController.refreshAnimatorStart();
                }

                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    float percent = (float) animation.getAnimatedValue();
                    if (recoverCallBack != null) {
                        recoverCallBack.onRefreshAnimator(percent);
                    }
                    touchController.refreshAnimator(percent);
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


    public void cancel(){
        mScrollAnimator.cancel();
    }


    /**
     * 下拉刷新向下滑动
     */
    private void pullRefresh( float scrollY, int deltaY) {
        float percent = scrollY/refreshHeight;
        ViewCompat.setY(refreshView, -scrollY);

        if (freshState == STATE_REFRESHING || freshState == STATE_START) {
            ViewCompat.setAlpha(refreshView, percent);
        } else {
            if (deltaY < 0) {
                if (percent >= 0.9f) {
                    refreshView.setVisibility(View.VISIBLE);
                    ViewCompat.setAlpha(refreshView, percent);
                } else {
                    ViewCompat.setAlpha(refreshView, percent);
                }
            } else {
                ViewCompat.setAlpha(refreshView, percent);
            }

            if (percent == 0) {
                refreshView.setVisibility(View.GONE);
            }
        }
    }

    public void setRefreshCallBack(RefreshCallBack refreshCallBack) {
        this.refreshCallBack = refreshCallBack;
    }

    public void setRecoverCallBack(RecoverCallBack recoverCallBack) {
        this.recoverCallBack = recoverCallBack;
    }


    public int getRefreshHeight() {
        return refreshHeight;
    }
}
