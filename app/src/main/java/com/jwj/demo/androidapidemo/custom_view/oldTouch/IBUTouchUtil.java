package com.jwj.demo.androidapidemo.custom_view.oldTouch;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.os.Build;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.LinearLayout;

import com.airbnb.lottie.LottieAnimationView;
import com.airbnb.lottie.LottieComposition;
import com.airbnb.lottie.OnCompositionLoadedListener;
import com.jwj.demo.androidapidemo.R;
import com.jwj.demo.androidapidemo.logger.LogUtil;
import com.jwj.demo.androidapidemo.util.ViewUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jwj on 17/10/19.
 */
public class IBUTouchUtil {
    /**
     * 自动滚动向下
     */
    final int AUTO_SCROLL_DOWN = -1;

    /**
     * 自动滚动向上
     */
    final int AUTO_SCROLL_UP = 1;

    /**
     * 下拉未触发刷新，向上动画
     */
    final int AUTO_REFRESH_UP = 2;


    /**
     * 增加的滑动间距
     */
    private final int TAP = 3;


    /**
     * recycler内容被遮盖的距离
     */
    private int recyclerScrollTo = 200;

    /**
     * topview滚动的高度
     */
    private int topHeight;


    /**
     * 下拉刷新的高度
     */
    private final int refreshHeight = 170;

    /**
     * 松开手时向下或向上动画的分割线
     */
    private int animatorHeight = 200;

    //recyclerview相关
    /**
     * 滑动底部y坐标
     */
    float recyclerBottomLimitY;

    /**
     * 滑动顶部的y坐标
     */
    float recyclerTopLimitY;

    /**
     * 起始y坐标
     */
    float recyclerOldY;

    /**
     * 判断滑动松手时刻,动画是该往下,还是往上
     */
    int isAutoScrollDirection = 0;
//    ValueAnimator animator;

    ScrollAnimator animator;


    IBUTouchBgView mainBgView;
    //    ViewGroup topView;
    IBUTouchRecyclerView recyclerView;
    View barBgView;
    List<View> icons = new ArrayList<>();
    private Context mContext;
    RefreshFunction refreshFunction;
    TopScrollFunction topScrollFunction;
    IBUTouchContainerView parent;
    boolean isInit;

    /**
     * 刷新回调
     */
    public interface RefreshCallBack {
        void onFreshStart();

        void onFreshing();

        void onComplete();
    }


    public IBUTouchUtil(Context context) {
        this.mContext = context;
    }


    public void init(IBUTouchContainerView parent) {
        this.parent = parent;
        mainBgView = (IBUTouchBgView) parent.findViewWithTag(parent.getResources().getString(R.string.ibu_touch_mainbg_tag));
        recyclerView = (IBUTouchRecyclerView) parent.findViewWithTag(parent.getResources().getString(R.string.ibu_touch_recyclerview_tag));
        barBgView = parent.findViewWithTag(parent.getResources().getString(R.string.ibu_touch_barbgview_tag));
        ViewGroup topView = (ViewGroup) parent.findViewWithTag(parent.getResources().getString(R.string.ibu_touch_topview_tag));
        LottieAnimationView refreshView = (LottieAnimationView) parent.findViewWithTag(parent.getResources().getString(R.string.ibu_touch_refresh_tag));


        if (topView != null) {
            topScrollFunction = new TopScrollFunction(topView);
        }

        if (refreshView != null) {
            refreshFunction = new RefreshFunction(refreshView);
            refreshFunction.setRecoverCallBack(new RefreshFunction.RecoverCallBack() {
                float topDistance, recyclerY;

                @Override
                public void onAnimator(float percent) {
                    int topY = (int) (percent * topDistance);
                    scrollTopView(-topY);

                    if (mainBgView != null) {
                        mainBgView.autoBackScale(percent);
                    }
                    scrollRecyclerViewTo(recyclerOldY + (recyclerY - recyclerOldY) * percent);
                }

                @Override
                public void onAnimatorStart() {
                    topDistance = getTopViewPositionY();
                    recyclerY = recyclerView.getY();
                }
            });
        }
        computeInitCondition(topView);
    }


    public void scrollTopView(float y) {
        if (topScrollFunction != null) {
            android.support.v4.view.ViewCompat.setY(topScrollFunction.getTopView(), y);
        }
    }

    public float getTopViewY() {
        if (topScrollFunction != null) {
            return topScrollFunction.getTopView().getY();
        }
        return 0;
    }


    private float getTopViewPositionY() {
        if (topScrollFunction != null) {
            return topScrollFunction.getTopViewY();
        }
        return 0;
    }

    private int getStatusHeight(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            return ViewUtil.getStatusBarHeight(context);
        }
        return 0;
    }


    /**
     * 计算一些需要初始化的值
     */
    void computeInitCondition(final ViewGroup topView) {
        final LinearLayout coverIconView = (LinearLayout) topView.getChildAt(2);

        parent.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                if (!isInit) {
                    int statusHeight = getStatusHeight(getmContext());
                    topHeight = (int) coverIconView.getY() - statusHeight;
                    android.support.v4.view.ViewCompat.setPaddingRelative(barBgView, 0, statusHeight, 0, 0);
                    //


                    if (topScrollFunction != null) {
                        topScrollFunction.init(topHeight);
                    }
                    recyclerOldY = recyclerView.getPaddingTop() + recyclerView.getY();
                    recyclerBottomLimitY = recyclerOldY + refreshHeight;
                    recyclerTopLimitY = barBgView.getHeight() - recyclerScrollTo;
                    isInit = true;
                }
                parent.getViewTreeObserver().removeOnPreDrawListener(this);
                return false;
            }
        });


        coverIconView.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                if (topHeight == 0) {
                    topHeight = (int) coverIconView.getY();
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        topHeight -= ViewUtil.getStatusBarHeight(getmContext());
                    }
                    if (topScrollFunction != null) {
                        topScrollFunction.init(topHeight);
                    }

                    recyclerOldY = recyclerView.getPaddingTop() + recyclerView.getY();
                    recyclerBottomLimitY = recyclerOldY + refreshHeight;
                    recyclerTopLimitY = barBgView.getHeight() - recyclerScrollTo;
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
            int statuHeight = ViewUtil.getStatusBarHeight(getmContext());
            ViewGroup.LayoutParams var3 = barBgView.getLayoutParams();
            var3.height += statuHeight;
            barBgView.setLayoutParams(var3);
            barBgView.setPadding(barBgView.getPaddingLeft(), barBgView.getPaddingTop() + statuHeight, barBgView.getPaddingRight(), barBgView.getPaddingBottom());
        }
    }


    public boolean onTouch(int deltaY) {
        final float topScrollY = getTopViewPositionY();

        if (topScrollFunction != null) {
            topScrollFunction.scrollTopView(deltaY, isZeroCanTouchUp(), refreshHeight, recyclerView.getY() > recyclerTopLimitY + barBgView.getHeight());
        }

        if (deltaY > 0) {
            if (topScrollY < 0) {
                pullDown(Math.abs(getTopViewY()) * 1f / refreshHeight, Math.abs(getTopViewY()), deltaY);
                return true;
            } else if (topScrollY == 0) {
                if (!isZeroCanTouchUp()) {
                    return true;
                }
            } else if (topScrollY < topHeight) {
            } else {
            }
            computeViewPositionRange(recyclerView, recyclerView.getY(), deltaY, recyclerOldY, recyclerTopLimitY);
        } else if (deltaY < 0) {
            if (topScrollY <= 0) {
                deltaY = deltaY / 3;
                //下拉刷新
                pullDown(Math.abs(getTopViewY()) * 1f / refreshHeight, Math.abs(getTopViewY()), deltaY);
                return true;
            } else if (topScrollY > 0) {
                int recyclerDeltaY = (int) (deltaY * 1.3f);
                computeViewPositionRange(recyclerView, recyclerView.getY(), recyclerDeltaY, recyclerOldY, recyclerTopLimitY);
            }
        } else {
            return true;
        }
        handleEffectAnimtor(getTopViewPositionY());
        return false;

    }


    private void pullDown(float percent, float scrollY, int deltaY) {
        mainBgView.downScalePercent(percent);
        computeViewPositionRange(recyclerView, recyclerView.getY(), deltaY, recyclerBottomLimitY, recyclerBottomLimitY - refreshHeight);
        if (refreshFunction != null) {
            refreshFunction.pullRefresh(percent, scrollY, deltaY);
        }
    }


    /**
     * 下拉刷新是否拦截
     *
     * @param deltaY
     * @param deltaX
     * @return
     */
    public boolean isRefreshIntercepted(int deltaY, int deltaX) {
        boolean intercepted = false;
        if (getTopViewPositionY() == 0 && deltaY < 0)
            intercepted = true;
        if (getTopViewPositionY() < 0) {
            intercepted = true;
        }
        Log.d("intercepted2 =", getTopViewPositionY() + ",intercepted =" + intercepted);
        return intercepted;
    }


    /**
     * 处理背景渐变效果
     */

    void handleEffectAnimtor(float translateY) {
        float percent = computeRangeAlpha(translateY * 1f / topHeight, 0, 1);


        if (translateY < 0) {
            translateY = 0;
        }
        mainBgView.scrollTo(0, (int) translateY);

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

    /**
     * 处理手势松开后,自动滑动的方向
     */
    public void handleAutoScrollDirection() {
        if (getTopViewPositionY() < 0) {
            isAutoScrollDirection = AUTO_REFRESH_UP;
        } else if (getTopViewPositionY() < topHeight) {
            isAutoScrollDirection = AUTO_SCROLL_UP;
        } else {
            isAutoScrollDirection = AUTO_SCROLL_DOWN;
        }
    }


    private void computeViewPositionRange(View view, float positionY, int deltaY, float maxScroll, float minScroll) {
        Log.d("position_range", "posistionY =" + positionY + ",deltaY =" + deltaY);

        if (deltaY > 0) {   //向上
            if (positionY - deltaY < minScroll) {
                android.support.v4.view.ViewCompat.setY(view, minScroll);
            } else {
                android.support.v4.view.ViewCompat.setY(view, positionY - deltaY);
            }
        } else {
            if (positionY - deltaY > maxScroll) {
                android.support.v4.view.ViewCompat.setY(view, maxScroll);
            } else {
                android.support.v4.view.ViewCompat.setY(view, positionY - deltaY);
            }
        }
    }


    public boolean isAnimating() {
        if (animator != null && animator.isRunning()) {
            return true;
        }

        if (refreshFunction != null && refreshFunction.isRunning()) {
            return true;
        }
        return false;
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
                float topDistance, topRecycler;
                int up;

                @Override
                public void onAnimationStart(Animator animation, Object... args) {
                    topDistance = getTopViewPositionY();
                    topRecycler = recyclerView.getY();
                    up = (int) args[0];
                }

                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    float percent = (float) animation.getAnimatedValue();
                    int topY;
                    int recyclerY;

                    if (up > 0) {
                        topY = (int) (percent * 1.2f * (topHeight - Math.abs(topDistance)));
                        recyclerY = (int) (percent * (topRecycler - recyclerTopLimitY));
                    } else {
                        topY = -(int) (percent * topDistance);
                        recyclerY = (int) (percent * (topRecycler - recyclerOldY) * 1.2f);
                    }

                    computeViewPositionRange(recyclerView, topRecycler, recyclerY, recyclerOldY, recyclerTopLimitY);
                    int desY = (int) computeRangeAlpha(topDistance + topY, 0, topHeight);
                    handleEffectAnimtor(desY);
                    scrollTopView(-desY);
                }
            };
        }
        animator.start(up);
    }


    private void scrollRecyclerViewTo(float scrollY) {
        android.support.v4.view.ViewCompat.setY(recyclerView, scrollY);
    }

    /**
     * 判断是否自动滚动
     *
     * @param director
     * @return
     */
    public boolean isAutoScroll(int director, boolean isVelocityEnable, int velocityDirection) {
        if (getTopViewPositionY() > 0 && getTopViewPositionY() < topHeight) {       //滑动动画，滑到一般时候
            if (isAutoScrollDirection == AUTO_SCROLL_DOWN) {
                startAnimator(AUTO_SCROLL_DOWN);
            } else {
                if (getTopViewPositionY() > animatorHeight || isVelocityEnable) {
                    startAnimator(AUTO_SCROLL_UP);
                } else {
                    startAnimator(AUTO_SCROLL_DOWN);
                }
            }
            return true;
        } else if (getTopViewPositionY() == topHeight) {        //图标滑动顶部时候
            if (recyclerView.getY() > recyclerTopLimitY) {
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
            if (refreshFunction != null) {
                if (Math.abs(getTopViewPositionY()) >= refreshHeight) {
                    //播放刷新动画
                    refreshFunction.startRefreshAnima();
                } else {
                    refreshFunction.releasePullAnimation();
                }
            }
        }
        return false;
    }


    /**
     * 滑动滑动是否拦截
     *
     * @param deltaY
     * @param deltaX
     * @return
     */
    private boolean isScrollIntercepted(int deltaY, int deltaX) {
        final float topY = getTopViewPositionY();
        boolean intercepted = false;
        if (deltaY > 0) {
            if (topY >= 0 && topY < topHeight) {
                intercepted = true;
            } else if (getTopViewPositionY() == topHeight) {
                LogUtil.d("isScrollIntercepted_1", "<" + intercepted);
                intercepted = false;
            } else {
                intercepted = true;
            }
        } else if (deltaY < 0) {
            float scrollY = recyclerView.getmTotalScrolled();
            if (scrollY == 0) {
                intercepted = true;
            } else {
                LogUtil.d("isScrollIntercepted_2", "<" + intercepted);
                intercepted = false;
            }
        } else {
            intercepted = true;
        }

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


    public boolean isIntercepted(int deltaY, int deltaX) {
        LogUtil.d("touch_util", "isScrollIntercepted = " + isScrollIntercepted(deltaY, deltaX) +
                ",isScrollEnable = " + recyclerView.isScrollEnable() +
                ",isRefreshIntercepted =" + isRefreshIntercepted(deltaY, deltaX) +
                ",deltaY =" + deltaY + ", totalScrolled ==" + recyclerView.getmTotalScrolled() + ", topviewY =" + getTopViewPositionY());
        if (isAnimating()) {
            return true;
        }

        if (Math.abs(deltaY) * 0.5f > Math.abs(deltaX)) {
            return (isScrollIntercepted(deltaY, deltaX) && recyclerView.isScrollEnable())
                    || isRefreshIntercepted(deltaY, deltaX);
        }
        return false;
    }

    public Context getmContext() {
        return mContext;
    }


    public final static class TopScrollFunction {
        /**
         * topview滚动的高度
         */
        private int topHeight;

        final int TAP = 2;


        ViewGroup topView;

        public TopScrollFunction(ViewGroup topView) {
            this.topView = topView;
        }

        protected void init(int topHeight) {
            this.topHeight = topHeight;
        }


        /**
         * @param deltaY            滑动的y方向的距离，有方向之分
         * @param isZeroCanTouchUp  在y坐标为0时，是否需要向上滑动
         * @param refreshHeight     刷新的高度临界值
         * @param recyclerYToScroll recyclerview滑动到什么时候，可以下滑
         * @return
         */
        public boolean scrollTopView(int deltaY, boolean isZeroCanTouchUp, int refreshHeight, boolean recyclerYToScroll) {
            final float topScrollY = getTopViewY();
            if (deltaY > 0) {
                if (topScrollY < 0) {
                    if (topScrollY + deltaY > 0) {
                        android.support.v4.view.ViewCompat.setY(topView, 0);
                    } else {
                        android.support.v4.view.ViewCompat.setY(topView, -(topScrollY + deltaY));
                    }
                    return true;
                } else if (topScrollY == 0) {
                    if (!isZeroCanTouchUp) {
                        return true;
                    }
                    android.support.v4.view.ViewCompat.setY(topView, -deltaY);
                } else if (topScrollY < topHeight) {
                    if (topScrollY + deltaY > topHeight) {
                        android.support.v4.view.ViewCompat.setY(topView, -topHeight);
                    } else if (topScrollY + deltaY + TAP > topHeight) {
                        android.support.v4.view.ViewCompat.setY(topView, -topHeight);
                    } else {
                        android.support.v4.view.ViewCompat.setY(topView, -(topScrollY + deltaY + TAP));
                    }
                } else {
                    android.support.v4.view.ViewCompat.setY(topView, -topHeight);
                }
            } else if (deltaY < 0) {
                if (topScrollY <= 0) {
                    //下拉刷新
                    deltaY = deltaY / 3;
                    if (Math.abs(topScrollY + deltaY) > refreshHeight) {
                        android.support.v4.view.ViewCompat.setY(topView, refreshHeight);
                    } else if (Math.abs(topScrollY + deltaY) <= refreshHeight) {
                        computeViewPositionRange(topView, topView.getY(), deltaY, refreshHeight, 0);
                    }
                    return true;
                } else if (topScrollY > 0) {
                    int topDetalY = (int) (deltaY * 0.7f);
                    if (recyclerYToScroll) {
                        computeViewPositionRange(topView, topView.getY(), topDetalY, topHeight, 0);
                    }
                } else if (topView.getY() >= topHeight) {

                }
            } else {
                return true;
            }
            return false;
        }

        private void computeViewPositionRange(View view, float positionY, int deltaY, float maxScroll, float minScroll) {
            Log.d("position_range", "posistionY =" + positionY + ",deltaY =" + deltaY);

            if (deltaY > 0) {   //向上
                if (positionY - deltaY < minScroll) {
                    android.support.v4.view.ViewCompat.setY(view, minScroll);
                } else {
                    android.support.v4.view.ViewCompat.setY(view, positionY - deltaY);
                }
            } else {
                if (positionY - deltaY > maxScroll) {
                    android.support.v4.view.ViewCompat.setY(view, maxScroll);
                } else {
                    android.support.v4.view.ViewCompat.setY(view, positionY - deltaY);
                }
            }
        }

        private float getTopViewY() {
            return -topView.getY();
        }

        public ViewGroup getTopView() {
            return topView;
        }
    }

    /**
     * 下拉刷新控件，控制类
     */
    public static class RefreshFunction {

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
         * 刷新结束
         */
        final int STATE_END = 3;
        int freshState = STATE_READY;  //刷新的状态

        LottieAnimationView refreshView;
        ScrollAnimator mScrollAnimator;

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
        private RefreshCallBack refreshCallBack;

        /**
         * 恢复回调
         */
        RecoverCallBack recoverCallBack;


        public RefreshFunction(LottieAnimationView refreshView) {
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

        private void startRefreshAnima() {
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
                mScrollAnimator = new ScrollAnimator(300, new AccelerateDecelerateInterpolator(), 1f, 0) {
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
                            android.support.v4.view.ViewCompat.setAlpha(refreshView, percent * alpha);
                            android.support.v4.view.ViewCompat.setY(refreshView, refreshY * percent);
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

        public void setRefreshCallBack(RefreshCallBack refreshCallBack) {
            this.refreshCallBack = refreshCallBack;
        }

        public void setRecoverCallBack(RecoverCallBack recoverCallBack) {
            this.recoverCallBack = recoverCallBack;
        }
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


    public void setRefreshCallBack(RefreshCallBack refreshCallBack) {
        if (refreshFunction != null) {
            refreshFunction.setRefreshCallBack(refreshCallBack);
        }
    }


    public interface OnTouchListener {
        void scrollToTop();

        void scrollToZero();

        void scrollToBottom();
    }

}
