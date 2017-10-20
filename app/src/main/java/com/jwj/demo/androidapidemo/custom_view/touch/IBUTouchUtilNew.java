package com.jwj.demo.androidapidemo.custom_view.touch;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;

import com.airbnb.lottie.LottieAnimationView;
import com.jwj.demo.androidapidemo.R;

/**
 * Description: 描述
 * Author: wjxie
 * Date: 2017/10/20
 * Copyright: Ctrip
 */

public class IBUTouchUtilNew extends RecyclerView.OnScrollListener {
    Context mContext;

    /**
     * 下拉刷新的高度
     */
    private final int refreshHeight = 170;

    /**
     * 自动滚动向下
     */
    public static final int AUTO_SCROLL_DOWN = -1;

    /**
     * 自动滚动向上
     */
    public static final int AUTO_SCROLL_UP = 1;

    /**
     * 下拉未触发刷新，向上动画
     */
    public static final int AUTO_REFRESH_UP = 2;

    int isAutoScrollDirection;

    RecyclerTouchController recyclerTouchController;
    TopViewController topViewController;
    ScrollAnimator animator;
    RefreshFunctonController refeshController;


    public interface ScrollUpDownListener {
        void onScrollUp(int deltaY);

        void onScrollDown(int deltaY);
    }

    public interface RefreshPullListener {

        void onRefreshUp();

        void onRefreshDown();
    }

    public interface AnimatorListener {
        void onAnimatorStart();

        void onAnimator(float percent, int up);
    }


    public IBUTouchUtilNew(Context context) {
        this.mContext = context;
    }


    public void init(IBUTouchContainerView parent) {
        IBUTouchRecyclerView recyclerView = (IBUTouchRecyclerView) parent.findViewWithTag(parent.getResources().getString(R.string.ibu_touch_recyclerview_tag));
        ViewGroup topView = (ViewGroup) parent.findViewWithTag(parent.getResources().getString(R.string.ibu_touch_topview_tag));
        LottieAnimationView refreshView = (LottieAnimationView) parent.findViewWithTag(parent.getResources().getString(R.string.ibu_touch_refresh_tag));

        recyclerTouchController = new RecyclerTouchController(recyclerView);
        topViewController = new TopViewController(topView);
        refeshController = new RefreshFunctonController(refreshView);

        recyclerView.addOnScrollListener(this);
    }


    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {

    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        if (dy > 0) {
            topViewController.scrollUp(dy);
        } else if (dy < 0) {
            topViewController.scrollDown(dy, recyclerTouchController.isCallPullTopToDown());
        }
    }


    private float getTopViewPositionY() {
        if (topViewController != null) {
            return topViewController.getTopViewY();
        }
        return 0;
    }


    public void onTouch(int deltaY) {
        final float topScrollY = getTopViewPositionY();
        if (deltaY > 0) {
            if (topScrollY < 0) {
                onRefreshUp(deltaY);
            }
        } else if (deltaY < 0) {
            if (topScrollY <= 0) {
                deltaY = deltaY / 3;
                onRefreshDown(deltaY);
            }
        }
    }


    public void onRefreshDown(int deltaY) {
        topViewController.refreshDown(deltaY, refreshHeight);

    }

    public void onRefreshUp(int deltaY) {
        topViewController.refreshUp(deltaY);

    }

    public void onAnimatorStart() {

    }


    public void onAnimator(float percent, Object... args) {

    }


    /**
     * 判断是否自动滚动
     *
     * @param director
     * @return
     */
    public boolean isAutoScroll(int director, boolean isVelocityEnable, int velocityDirection) {
        if (!topViewController.animator(isAutoScrollDirection, isVelocityEnable)) {
            if (getTopViewPositionY() == topViewController.getTopHeight()) {        //图标滑动顶部时候
                if (refeshControllergetY() > recyclerTopLimitY) {
                    if (isVelocityEnable) {
                        startAnimator(isAutoScrollDirection);
                    } else if (recyclerView.getY() >= recyclerTopLimitY - barBgView.getHeight() / 2) {
                        startAnimator(AUTO_SCROLL_UP);
                    } else {
                        startAnimator(isAutoScrollDirection);
                    }
                }
            } else if (getTopViewPositionY() <= 0) {
                //下拉刷新处理地方
                refeshController.isAutoAnimator(topViewController.getTopViewY());
            }
        }
    }


    /**
     * @param up 大于0向上,小于0向下
     */

    public void startAnimator(final int up) {
        if (animator != null && animator.isRunning()) {
            animator.cancel();
            return;
        }

        if (animator == null) {
            animator = new ScrollAnimator(300, new AccelerateDecelerateInterpolator(), 0, 1f) {
                @Override
                public void onAnimationStart(Animator animation, Object... args) {
                    onAnimatorStart();
                }

                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    float percent = (float) animation.getAnimatedValue();
                    onAnimator(percent, args);
                }
            };
        }
        animator.start(up);
    }


    /**
     * 滑动动画封装类
     */
    public static class ScrollAnimator extends AnimatorListenerAdapter implements ValueAnimator.AnimatorUpdateListener {
        ValueAnimator mAnimator;
        Object[] args;

        public ScrollAnimator(long duration, TimeInterpolator mInterpolator, float... values) {
            mAnimator = ValueAnimator.ofFloat(values);
            mAnimator.setDuration(duration);
            mAnimator.setInterpolator(mInterpolator);
            mAnimator.addUpdateListener(this);
            mAnimator.addListener(this);
        }

        public void onAnimationStart(Animator animation, Object... args) {
        }

        @Override
        public void onAnimationUpdate(ValueAnimator animation) {

        }

        public void cancel() {
            mAnimator.cancel();
        }

        public boolean isRunning() {
            return mAnimator.isRunning();
        }

        public void start() {
            args = null;
            mAnimator.start();
        }

        public void start(Object... args) {
            this.args = args;
            onAnimationStart(mAnimator, args);
            mAnimator.start();
        }
    }


}
