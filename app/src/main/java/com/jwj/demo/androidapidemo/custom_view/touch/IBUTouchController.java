package com.jwj.demo.androidapidemo.custom_view.touch;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.os.Build;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.LinearInterpolator;

import com.jwj.demo.androidapidemo.R;
import com.jwj.demo.androidapidemo.custom_view.refresh.SmartRefreshLayout;
import com.jwj.demo.androidapidemo.custom_view.refresh.api.RefreshHeader;
import com.jwj.demo.androidapidemo.custom_view.refresh.listener.SimpleMultiPurposeListener;

/**
 * Description: 描述
 * Author: wjxie
 * Date: 2017/10/20
 * Copyright: Ctrip
 */

public class IBUTouchController {
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

    /**
     * 滑动的标志 (1 = 触摸滑动,  3 = 是自动滑动)
     */
    int scrollFlag;

    /**
     * 手势弹起后自动滑动的方向
     */
    int autoScrollDirection;


    boolean isInit;

    IBUTouchRecyclerController recyclerTouchController;
    IBUTouchTopController topViewController;
    ScrollAnimator animator;
    IBUTouchBgController bgTouchController;

    View barBgView;
    int topHeight;

    AutoScrollUtil autoScrollUtil;
    IBUTouchRecyclerView recyclerView;

    public IBUTouchController(Context context) {
        this.mContext = context;
        autoScrollUtil = new AutoScrollUtil(this);
    }


    public void init(ViewGroup parent) {
        recyclerView = (IBUTouchRecyclerView) parent.findViewWithTag(parent.getResources().getString(R.string.ibu_touch_recyclerview_tag));
        ViewGroup topView = (ViewGroup) parent.findViewWithTag(parent.getResources().getString(R.string.ibu_touch_topview_tag));
        IBUTouchBgView mainBgView = (IBUTouchBgView) parent.findViewWithTag(parent.getResources().getString(R.string.ibu_touch_mainbg_tag));
        barBgView = parent.findViewWithTag(parent.getResources().getString(R.string.ibu_touch_barbgview_tag));
        SmartRefreshLayout refreshLayout = (SmartRefreshLayout) recyclerView.getParent();

        recyclerTouchController = new IBUTouchRecyclerController(recyclerView, this);
        topViewController = new IBUTouchTopController(topView, this);
        bgTouchController = new IBUTouchBgController(mainBgView);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                onScroll(dy);
            }
        });
        computeInitCondition(parent);

//        refreshLayout.setHeaderMaxDragRate(1f);
        refreshLayout.setReboundInterpolator(new LinearInterpolator());
        refreshLayout.setOnMultiPurposeListener(new SimpleMultiPurposeListener() {
            @Override
            public void onHeaderPulling(RefreshHeader header, float percent, int offset, int headerHeight, int extendHeight) {
                onRefreshScroll(percent, offset, headerHeight);
            }

            @Override
            public void onHeaderReleasing(RefreshHeader header, float percent, int offset, int headerHeight, int extendHeight) {
                onRefreshScroll(percent, offset, headerHeight);
                bgTouchController.refreshRelease(percent);
            }

            @Override
            public void onHeaderStartAnimator(RefreshHeader header, int headerHeight, int extendHeight) {
            }

            @Override
            public void onHeaderFinish(RefreshHeader header, boolean success) {
            }
        });
        refreshLayout.setReboundDuration(350);


        initRecyclerView();
    }

    int offset;
    int lastPosition;
    LinearLayoutManager layoutManager;

    void initRecyclerView() {

        layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        View firstChild = layoutManager.getChildAt(0);
        offset = firstChild.getTop();
        lastPosition = layoutManager.getPosition(firstChild);

        recyclerView.setOnFlingListener(new RecyclerView.OnFlingListener() {
            @Override
            public boolean onFling(int velocityX, int velocityY) {
                return false;
            }


        });

        recyclerView.setOnPreScrollListener(new IBUTouchRecyclerView.OnPreScrollListener() {
            @Override
            public boolean onPreScroll(int dy) {

                return true;
            }
        });
    }


    /**
     * 计算一些需要初始化的值
     */
    void computeInitCondition(final ViewGroup parent) {
        parent.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                if (!isInit) {
                    int bottomY = barBgView.getHeight() + (int) barBgView.getY();
                    recyclerTouchController.init(0, bottomY);
                    isInit = true;
                }
                parent.getViewTreeObserver().removeOnPreDrawListener(this);
                return false;
            }
        });

        if (barBgView != null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            int statuHeight = getStatusHeight(parent.getContext());
            ViewGroup.LayoutParams var3 = barBgView.getLayoutParams();
            var3.height += statuHeight;
            barBgView.setLayoutParams(var3);
            barBgView.setPadding(barBgView.getPaddingLeft(), barBgView.getPaddingTop() + statuHeight, barBgView.getPaddingRight(), barBgView.getPaddingBottom());
        }
    }


    public boolean onTouchEvent(MotionEvent event) {
        autoScrollUtil.onTouchEvent(event);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                scrollFlag = 0;
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                if (isRunning()) {
                    cancelAnimator();
                }
                break;

            case MotionEvent.ACTION_MOVE:
                scrollFlag = 1;
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                scrollFlag |= 2;
                break;
        }
        return false;
    }


    /**
     * 滚动view
     *
     * @param dy
     */
    public void onScroll(int dy) {
        Log.d("onScroll_0 = ", dy + "");

        if (animator != null && animator.isRunning()) {
            return;
        }

        if (shouldStopScroll(dy)) {
            Log.d("onScroll_1 = ", getRecyclerScrollY() + "");

            recyclerView.stopScroll();
            startAnimator(1, true);
            return;
        }

        float percent = getRecyclerScrollY() * 1f / bgTouchController.scrollHeight;
        if (dy > 0) {
            if (percent > 1) {
                percent = 1;
            }
            bgTouchController.scrollTo(percent);
            topViewController.scrollTo(percent);
        } else if (dy < 0) {
            if (percent <= 1) {
                bgTouchController.scrollTo(percent);
                topViewController.scrollTo(percent);
            }
        }
        handleEffectAnimtor();
    }

    /**
     * 下拉刷新滚动
     *
     * @param percent
     * @param deltaY
     * @param refreshHeight
     */
    public void onRefreshScroll(float percent, float deltaY, int refreshHeight) {
        topViewController.refreshScroll(deltaY);
        bgTouchController.refreshPull(percent);
    }


    /**
     * @param up 大于0向上,小于0向下
     */

    public void startAnimator(int up) {
        startAnimator(up, false);
    }


    public void startAnimator(final int up, final boolean onlyRecycler) {
        if (animator != null && animator.isRunning()) {
            return;
        }

        if (animator == null) {
            animator = new ScrollAnimator(300, new AccelerateDecelerateInterpolator(), 0, 1f) {
                @Override
                public void onAnimationStart(Animator animation, Object... args) {
                    boolean justRecycler = (boolean) args[1];

                    if (!justRecycler) {
                        topViewController.animatorStart();
                        bgTouchController.animatorStart();
                    }
                    recyclerTouchController.animatorStart();
                }

                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    float percent = (float) animation.getAnimatedValue();
                    boolean justRecycler = (boolean) args[1];

                    if (!justRecycler) {
                        topViewController.animator(percent, args);
                        bgTouchController.animator(percent, args);
                        handleEffectAnimtor();
                    }
                    recyclerTouchController.animator(percent, args);
                    handleEffectAnimtor();
                }
            };
        }
        animator.start(up, onlyRecycler);
    }


    /**
     * 处理背景渐变效果
     */

    void handleEffectAnimtor() {
        float percent = computeRangeAlpha(getTopViewPositionY() * 1f / topViewController.getTopHeight(), 0, 1);
        barBgView.setAlpha(percent);
        topViewController.alphaIcons(percent);
    }


    /**
     * 判断是否自动滚动,然后调起回滚动画
     *
     * @return
     */
    public void isAutoScroll(boolean isVelocityEnable) {
        if (!topViewController.isAutoAnimator(autoScrollDirection, isVelocityEnable)) {
            if (getTopViewPositionY() == topViewController.getTopHeight()) {        //图标滑动顶部时候
                recyclerTouchController.isAnimator(autoScrollDirection, isVelocityEnable, 0);  //barheigth
            }
        }
    }


    public void handleAutoScrollDirection() {
        if (getTopViewPositionY() < 0) {
            autoScrollDirection = AUTO_REFRESH_UP;
        } else if (getTopViewPositionY() < topViewController.getTopHeight()) {
            autoScrollDirection = AUTO_SCROLL_UP;
        } else {
            autoScrollDirection = AUTO_SCROLL_DOWN;
        }
    }

    /**
     * 控制 percent的边界值
     *
     * @return
     */
    public float computeRangeAlpha(float percent, int min, int max) {
        if (percent > max) {
            percent = max;
        } else if (percent < min) {
            percent = min;
        }
        return percent;
    }

    public int getStatusHeight(Context context) {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            return ViewUtil.getStatusBarHeight(context);
//        }
        return 0;
    }

    /**
     * recyclerview 向下滚动停止的位置
     * 120 , -10  110   -12  98 100
     */
    public boolean shouldStopScroll(int dy) {
        return recyclerTouchController.getScrollY() <= recyclerTouchController.getRecyclerTopLimitY()
                && dy < 0 && scrollFlag == 3;
    }


    public void setTopHeight(int topHeight) {
        this.topHeight = topHeight;
    }


    public void setBgScrollHeight(int scrollHeight) {
        bgTouchController.init(scrollHeight);
    }


    private float getTopViewPositionY() {
        if (topViewController != null) {
            return topViewController.getTopViewY();
        }
        return 0;
    }


    public int getRecyclerScrollY() {
        return recyclerTouchController.getScrollY();
    }

    public boolean isRunning() {
        if (animator != null && animator.isRunning()) {
            return true;
        }
        return false;
    }

    public void cancelAnimator() {
        if (animator != null && animator.isRunning()) {
            animator.cancel();
        }
    }


    /**
     * 自动滚动辅助类
     */
    public static class AutoScrollUtil {

        /**
         * 速度追踪器
         */
        private VelocityTracker mVelocityTracker;

        /**
         * 最后滑动时刻的方向
         */
        private int mLastDirection;

        IBUTouchController ibuTouchController;


        public AutoScrollUtil(IBUTouchController ibuTouchController) {
            this.ibuTouchController = ibuTouchController;
        }


        public void onTouchEvent(MotionEvent ev) {
            int initialVelocity = 0;

            switch (ev.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    onActionDown(ev);
                    ibuTouchController.handleAutoScrollDirection();
                    break;

                case MotionEvent.ACTION_MOVE:
                    if (mVelocityTracker != null) {
                        mVelocityTracker.addMovement(ev);
                    }
                    break;

                case MotionEvent.ACTION_UP:
                    if (mVelocityTracker != null) {
                        final VelocityTracker velocityTracker = mVelocityTracker;
                        velocityTracker.computeCurrentVelocity(10);
                        initialVelocity = (int) mVelocityTracker.getYVelocity();
                    }
                    boolean volocityEnable = ((Math.abs(initialVelocity) > 8));
                    ibuTouchController.isAutoScroll(volocityEnable);
                    //拿到recyclerview的滑动高度
                case MotionEvent.ACTION_CANCEL:
                    recycleVelocityTracker();
                    break;
            }
        }

        public void onActionDown(MotionEvent motionEvent) {
            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                initOrResetVelocityTracker();
                if (mVelocityTracker != null) {
                    mVelocityTracker.addMovement(motionEvent);
                }
            }
        }


        private void recycleVelocityTracker() {
            if (mVelocityTracker != null) {
                mVelocityTracker.recycle();
                mVelocityTracker = null;
            }
        }


        private void initOrResetVelocityTracker() {
            if (mVelocityTracker == null) {
                mVelocityTracker = VelocityTracker.obtain();
            } else {
                mVelocityTracker.clear();
            }
        }
    }

    /**
     * 实现滚动的动画类
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
