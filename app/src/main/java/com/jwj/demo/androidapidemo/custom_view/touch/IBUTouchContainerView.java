package com.jwj.demo.androidapidemo.custom_view.touch;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.os.Build;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by jwj on 17/10/13.
 */
public class IBUTouchContainerView extends RelativeLayout {
    /**
     * 自动滚动向下
     */
    final int AUTO_SCROLL_DOWN = -1;

    /**
     * 自动滚动向上
     */
    final int AUTO_SCROLL_UP = 1;

    /**
     * 刷新向上动画
     */
    final int AUTO_REFRESH_UP = 2;


    /**
     * 增加的滑动间距
     */
    private final int TAP = 3;

    IBUTouchBgView mainBgView;
    ViewGroup topView;
    IBUTouchRecyclerView recyclerView;
    View barBgView;
    List<View> icons = new ArrayList<>();
    View refreshView;

    private int mLastY = 0;
    private int mLastMotionY;
    private int mLastXIntercept = 0;
    private int mLastYIntercept = 0;
    private int topHeight;
    private int recyclerScrollTo = 200;  //recycler内容被遮盖的距离

    private int refreshHeight = 200;  //下拉刷新的高度


    /**
     * 最后滑动时刻的方向
     */
    private int mLastDirection;

    /**
     * 松开手时向下或向上动画的分割线
     */
    private int animatorHeight = 200;

    /**
     * recyclerview需要滑动的距离
     */
    private float recyclerScrollTopHeight;  //滑动到顶部停止的距离

    /**
     * 速度追踪器
     */
    private VelocityTracker mVelocityTracker;

    /**
     * 下拉刷新回调
     */
    private RefreshCallBack refreshCallBack;


    int mMinimumVelocity;
    int mLastRecyclerScrollY;
    int isAutoScrollDirection = 0;  //判断滑动松手时刻,动画是该往下,还是往上
    ValueAnimator animator;

    int freshState;  //刷新的状态


    public interface ScrollCallBack {
        void onScrll(int translateY, float percent);
    }

    public interface RefreshCallBack {
        void onStart();

        void onRefreshing();

        void onComplete();
    }


    public IBUTouchContainerView(Context context) {
        super(context);
        init();
    }

    public IBUTouchContainerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        final ViewConfiguration configuration = ViewConfiguration.get(getContext());
        mMinimumVelocity = configuration.getScaledMinimumFlingVelocity();
    }


    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mainBgView = (IBUTouchBgView) getChildAt(0);
        recyclerView = (IBUTouchRecyclerView) getChildAt(1);
        barBgView = getChildAt(2);
        topView = (ViewGroup) getChildAt(4);
        //refreshView = (LottieAnimationView) findViewWithTag("refresh_view");

        initRefreshAnimation();
        computeInitCondition();
    }


    /**
     * 初始化刷新动画
     */
    void initRefreshAnimation() {
//        if (refreshView != null) {
//            LottieComposition.Factory.fromAssetFileName(getContext(), "c-loading.json", new OnCompositionLoadedListener() {
//                @Override
//                public void onCompositionLoaded(@Nullable LottieComposition composition) {
//                    refreshView.setComposition(composition);
//                    refreshView.setBackgroundResource(com.ctrip.ibu.framework.baseview.R.drawable.common_bg_white_1_c_a);
//                }
//            });
//            refreshView.loop(true);
//        }
    }


    /**
     * 计算一些需要初始化的值
     */
    void computeInitCondition() {
        final LinearLayout coverIconView = (LinearLayout) topView.getChildAt(2);
        coverIconView.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                if (topHeight == 0) {
                    topHeight = (int) coverIconView.getY();
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        //topHeight -= ViewUtil.getStatusBarHeight(getContext());
                    }
                    recyclerScrollTopHeight = recyclerView.getPaddingTop() + recyclerView.getY() - barBgView.getHeight() + recyclerScrollTo;
                }
                coverIconView.getViewTreeObserver().removeOnPreDrawListener(this);
                return false;
            }
        });

        if (coverIconView != null) {
            for (int i = 0; i < coverIconView.getChildCount(); i++) {
                ViewGroup viewGroup = (ViewGroup) coverIconView.getChildAt(i);
                for (int j = 0; j < viewGroup.getChildCount(); j++) {
                    icons.add(viewGroup.getChildAt(j));
                }
            }
        }

        if (barBgView != null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            int statuHeight = 0; //ViewUtil.getStatusBarHeight(getContext());
            ViewGroup.LayoutParams var3 = barBgView.getLayoutParams();
            var3.height += statuHeight;
            barBgView.setLayoutParams(var3);
            barBgView.setPadding(barBgView.getPaddingLeft(), barBgView.getPaddingTop() + statuHeight, barBgView.getPaddingRight(), barBgView.getPaddingBottom());
        }
    }

    private float getTopViewY() {
        return -topView.getY();
    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        boolean intercepted = false;
        int x = (int) ev.getX();
        int y = (int) ev.getY();

        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                intercepted = false;
                mLastY = y;
                mLastXIntercept = x;
                mLastYIntercept = y;
                if (getTopViewY() < 0) {
                    isAutoScrollDirection = AUTO_REFRESH_UP;
                } else if (getTopViewY() < topHeight) {
                    isAutoScrollDirection = AUTO_SCROLL_UP;
                } else {
                    isAutoScrollDirection = AUTO_SCROLL_DOWN;
                }

                initOrResetVelocityTracker();
                if (mVelocityTracker != null) {
                    mVelocityTracker.addMovement(ev);
                }

                break;
            case MotionEvent.ACTION_MOVE:
                int deltaX = mLastXIntercept - x;
                int deltaY = mLastYIntercept - y;
                intercepted = isIntercepted(deltaY, deltaX) || isRefreshIntercepted(deltaY, deltaX);
                break;

            case MotionEvent.ACTION_UP:
                intercepted = false;
                mLastXIntercept = mLastYIntercept = 0;
                break;
            default:
                break;
        }
        mLastY = y;
        mLastXIntercept = x;
        mLastYIntercept = y;
        return intercepted;
    }


    public boolean isIntercepted(int deltaY, int deltaX) {
        return (isScrollIntercepted(deltaY, deltaX) && recyclerView.isScrollEnable())
                || isRefreshIntercepted(deltaY, deltaX);
    }


    /**
     * 滑动滑动是否拦截
     *
     * @param deltaY
     * @param deltaX
     * @return
     */
    private boolean isScrollIntercepted(int deltaY, int deltaX) {
        boolean intercepted = false;
        if (Math.abs(deltaY) >= Math.abs(deltaX)) {
            if (deltaY > 0) {
                if (getTopViewY() >= 0 && getTopViewY() < topHeight) {
                    intercepted = true;
                } else if (getTopViewY() == topHeight) {
                    intercepted = false;
                }
            } else {
                float scrollY = recyclerView.getY();
                if (scrollY <= recyclerScrollTopHeight) {
                    intercepted = true;
                } else {
                    intercepted = false;
                }
            }
        } else {
            intercepted = false;
        }
        Log.d("intercepted1 =", getTopViewY() + ",intercepted =" + intercepted);
        return intercepted;
    }

    /**
     * 下拉刷新后，往上滑动到原始位置后，是否可以继续上滑
     *
     * @return
     */
    public boolean isZeroCanTouchUp() {
        return recyclerView.isScrollEnable();
    }


    /**
     * 下拉刷新是否拦截
     *
     * @param deltaY
     * @param deltaX
     * @return
     */
    private boolean isRefreshIntercepted(int deltaY, int deltaX) {
        boolean intercepted = false;
        if (Math.abs(deltaY) >= Math.abs(deltaX)) {
            if (getTopViewY() == 0 && deltaY < 0)
                intercepted = true;
            if (getTopViewY() < 0) {
                intercepted = true;
            }
        }
        Log.d("intercepted2 =", getTopViewY() + ",intercepted =" + intercepted);
        return intercepted;
    }

    public void refresh() {

    }

    public void complete() {
        if (animator != null && animator.isRunning()) {
            animator.cancel();
        }
        startRefreshEndAnimation();
        if (refreshView != null) {
//            refreshView.cancelAnimation();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        int initialVelocity = 0;

        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mLastMotionY = (int) ev.getY();
                mLastY = mLastMotionY;
                break;

            case MotionEvent.ACTION_MOVE:
                final int y = (int) ev.getY();
                final int x = (int) ev.getX();
                int deltaY = mLastY - y;
                mLastDirection = deltaY;
                mLastY = y;
                mVelocityTracker.addMovement(ev);
                if (scrollTopView(deltaY)) {
                    return true;
                }
                computeViewPositionRange(recyclerView, recyclerView.getY(), deltaY, recyclerScrollTopHeight, 0);
//                ViewCompat.setY(recyclerView, recyclerView.getY() - deltaY);
                handleEffectAnimtor(getTopViewY());
                break;
            case MotionEvent.ACTION_UP:
                final VelocityTracker velocityTracker = mVelocityTracker;
                velocityTracker.computeCurrentVelocity(10);
                initialVelocity = (int) mVelocityTracker.getYVelocity();
                boolean volocityEnable = ((Math.abs(initialVelocity) > 8));
                if (isAutoScroll(mLastDirection, volocityEnable, initialVelocity)) {
                }
            case MotionEvent.ACTION_CANCEL:
                recycleVelocityTracker();
                break;
        }

        return true;
    }


    boolean scrollTopView(int deltaY) {
        final float topScrollY = getTopViewY();

        if (deltaY > 0) {
            if (topScrollY < 0) {
                if (topScrollY + deltaY > 0) {
                    ViewCompat.setY(topView, 0);
                } else {
                    ViewCompat.setY(topView, -(topScrollY + deltaY));
                }
                pullRefresh(Math.abs(topView.getY()) * 1f / refreshHeight, Math.abs(topView.getY()));
                return true;
            } else if (topScrollY == 0) {
                if (isZeroCanTouchUp()) {
                    ViewCompat.setY(topView, -deltaY);
                }
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
        } else {
            if (topScrollY <= 0) {
                //下拉刷新
                deltaY = deltaY / 3;
                if (Math.abs(topScrollY + deltaY) > refreshHeight) {
                    ViewCompat.setY(topView, refreshHeight);
                } else if (Math.abs(topScrollY + deltaY) <= refreshHeight) {
                    ViewCompat.setY(topView, Math.abs(topScrollY + deltaY));
                }
                pullRefresh(Math.abs(topView.getY()) * 1f / refreshHeight, Math.abs(topView.getY()));
                return true;
            } else if (topScrollY > 0) {
                int recyclerDeltaY = (int) (deltaY * 1.5f);
                computeViewPositionRange(recyclerView, recyclerView.getmTotalScrolled(), recyclerDeltaY, recyclerScrollTopHeight, 0);
                if (recyclerView.getmTotalScrolled() <= recyclerScrollTopHeight - recyclerScrollTo) {
                    int topDetalY = (int) (deltaY * 0.7f);
                    computeViewPositionRange(topView, topView.getY(), topDetalY, topHeight, 0);
                }
            } else if (topView.getY() >= topHeight) {

            }
        }
        return false;
    }


    /**
     * 处理背景渐变效果
     */

    void handleEffectAnimtor(float translateY) {
        ViewCompat.setTranslationY(mainBgView, -translateY);
        float percent = computeRangeAlpha(translateY * 1f / topHeight, 0, 1);

        if (percent < 0.5f) {
            mainBgView.setCustomAlpha(1 - percent * 0.3f);
            barBgView.setAlpha(0);
        } else {
            mainBgView.setCustomAlpha(1 - percent);
            barBgView.setAlpha((percent - 0.5f) * 2);
        }

        for (View view : icons) {
            int alpha = (int) computeRangeAlpha((1 - percent) * 255, 0, 255);
            if (view.getBackground() != null) {
                view.getBackground().setAlpha(alpha);
            } else {
                view.setAlpha(1 - percent);
            }
        }
    }


    /**
     * 控制 percent的边界值
     *
     * @return
     */
    private float computeRangeAlpha(float percent, int min, int max) {
        if (percent > max) {
            percent = max;
        } else if (percent < min) {
            percent = min;
        }
        return percent;
    }


//    private void computeViewScrollRange(View view, int scrollY, int deltaY, int maxScroll, int minScroll) {
//        if (scrollY + deltaY > maxScroll) {
//            view.scrollBy(0, maxScroll - scrollY);
//        } else if (scrollY + deltaY < minScroll) {
//            view.scrollBy(0, -scrollY);
//        } else {
//            view.scrollBy(0, deltaY);
//        }
//    }


    private void computeViewPositionRange(View view, float positionY, int deltaY, float maxScroll, float minScroll) {
        if (positionY + deltaY > maxScroll) {
            ViewCompat.setY(view, maxScroll);
        } else if (positionY + deltaY < minScroll) {
            ViewCompat.setY(view, minScroll);
        } else {
            ViewCompat.setY(view, positionY + deltaY);
        }
    }


    /**
     * @param up 大于0向上,小于0向下
     */

    public void startAnimator(final int up) {
        mLastRecyclerScrollY = 0;

        final float topDistance = getTopViewY();
        final int topRecycler = recyclerView.getmTotalScrolled();

        if (animator != null && animator.isRunning()) {
            return;
        }

        animator = ValueAnimator.ofFloat(0, 1f);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float percent = (float) animation.getAnimatedValue();
                int topY = 0;
                int recyclerY = 0;

                if (up > 0) {
                    topY = (int) (percent * 1.2f * (topHeight - Math.abs(topDistance)));
                    recyclerY = (int) (percent * (recyclerScrollTopHeight - topRecycler));
                } else {
                    topY = -(int) (percent * topDistance);
                    recyclerY = -(int) (percent * topRecycler * 1.3f);
                }

                final int detaY = recyclerY - mLastRecyclerScrollY;
                computeViewPositionRange(recyclerView, recyclerView.getmTotalScrolled(), detaY, recyclerScrollTopHeight, 0);
                int desY = (int) computeRangeAlpha(topDistance + topY, 0, topHeight);
                handleEffectAnimtor(desY);

                ViewCompat.setY(topView, -desY);
                mLastRecyclerScrollY = recyclerY;
            }
        });

        animator.setDuration(350);
        animator.setInterpolator(new AccelerateDecelerateInterpolator());
        animator.start();
    }


    /**
     * 下拉刷新后回弹的动画
     * <p>
     * //     * @param up
     */
    public void startRefreshEndAnimation() {
        mLastRecyclerScrollY = 0;

        final float topDistance = getTopViewY();
        final float y = recyclerView.getY();

        final float alpha = refreshView == null ? 0 : refreshView.getAlpha();
        if (animator != null && animator.isRunning()) {
            return;
        }

        animator = ValueAnimator.ofFloat(1, 0f);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float percent = (float) animation.getAnimatedValue();
                int topY = (int) (percent * topDistance);
                ViewCompat.setY(topView, -topY);

                if (mainBgView != null) {
                    mainBgView.autoBackScale(percent);
                }

                if (refreshView.getVisibility() == View.VISIBLE && alpha > 0) {
                    ViewCompat.setAlpha(refreshView, percent * alpha);
                }

                if (y > 0) {
                    ViewCompat.setY(recyclerView, y * percent);
                }
            }
        });

        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                if (refreshView != null) {
                }
            }
        });

        animator.setDuration(350);
        animator.setInterpolator(new AccelerateDecelerateInterpolator());
        animator.start();
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


    /**
     * 判断是否自动滚动
     *
     * @param director
     * @return
     */
    boolean isAutoScroll(int director, boolean isVelocityEnable, int velocityDirection) {
        if (getTopViewY() > 0 && getTopViewY() < topHeight) {       //滑动动画，滑到一般时候
            if (isAutoScrollDirection == AUTO_SCROLL_DOWN) {
                startAnimator(AUTO_SCROLL_DOWN);
            } else {
                if (getTopViewY() > animatorHeight || isVelocityEnable) {
                    startAnimator(AUTO_SCROLL_UP);
                } else {
                    startAnimator(AUTO_SCROLL_DOWN);
                }
            }
            return true;
        } else if (getTopViewY() == topHeight) {        //图标滑动顶部时候
            if (recyclerView.getmTotalScrolled() >= recyclerScrollTopHeight) {
                return false;
            } else {
                if (isVelocityEnable) {
                    startAnimator(isAutoScrollDirection);
                } else if (recyclerView.getmTotalScrolled() >= recyclerScrollTopHeight - topView.getHeight() / 2) {
                    startAnimator(AUTO_SCROLL_UP);
                } else {
                    startAnimator(isAutoScrollDirection);
                }
            }
        } else if (getTopViewY() <= 0) {
            if (Math.abs(getTopViewY()) >= refreshHeight) {
                //播放刷新动画
                if (refreshView != null) {
//                    refreshView.playAnimation();
                }
                postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        complete();
                    }
                }, 1000);
                Log.d("refresh_doing", getTopViewY() + "");
//                startAnimatorTwo();
            } else {
                Log.d("refresh_up", getTopViewY() + "");
                startRefreshEndAnimation();
            }
        }
        return false;
    }


    /**
     * 下拉刷新向下滑动
     *
     * @param percent
     */
    private void pullRefresh(float percent, float scrollY) {
        mainBgView.downScalePercent(percent);
        ViewCompat.setY(recyclerView, scrollY);
        if (refreshView != null) {
            if (percent >= 0.5f) {
                percent = (percent - 0.5f) * 2;
                refreshView.setVisibility(View.VISIBLE);
            } else {
                percent = (percent - 0.5f) * 2;
                refreshView.setVisibility(View.GONE);
            }
            refreshView.setAlpha(computeRangeAlpha(percent, 0, 1));
            ViewCompat.setY(refreshView, scrollY);
        }
    }

    public boolean isShouldIntercepted() {
        if (recyclerView.getmTotalScrolled() <= recyclerScrollTopHeight) {
            return true;
        }
        return false;
    }

    public void setRefreshCallBack(RefreshCallBack refreshCallBack) {
        this.refreshCallBack = refreshCallBack;
    }
}
