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

import com.jwj.demo.androidapidemo.R;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by jwj on 17/10/13.
 */
public class IBUTouchContainerView extends RelativeLayout {

    private int mLastY = 0;
    private int mLastMotionY;
    private int mLastXIntercept = 0;
    private int mLastYIntercept = 0;

    int mMinimumVelocity;


    IBUTouchUtil ibuTouchUtil;

    /**
     * 速度追踪器
     */
    private VelocityTracker mVelocityTracker;

    /**
     * 最后滑动时刻的方向
     */
    private int mLastDirection;


    /**
     * 下拉刷新回调
     */
    private RefreshCallBack refreshCallBack;



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
        ibuTouchUtil = new IBUTouchUtil();
    }


    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        ibuTouchUtil.init(this);
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
                ibuTouchUtil.handleAutoScrollDirection();

                initOrResetVelocityTracker();
                if (mVelocityTracker != null) {
                    mVelocityTracker.addMovement(ev);
                }

                break;
            case MotionEvent.ACTION_MOVE:
                int deltaX = mLastXIntercept - x;
                int deltaY = mLastYIntercept - y;
                intercepted = isIntercepted(deltaY, deltaX);
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
                if (ibuTouchUtil.scrollTopView(deltaY)) {
                    return true;
                }

                break;
            case MotionEvent.ACTION_UP:
                final VelocityTracker velocityTracker = mVelocityTracker;
                velocityTracker.computeCurrentVelocity(10);
                initialVelocity = (int) mVelocityTracker.getYVelocity();
                boolean volocityEnable = ((Math.abs(initialVelocity) > 8));
                ibuTouchUtil.isAutoScroll(mLastDirection, volocityEnable, initialVelocity);
            case MotionEvent.ACTION_CANCEL:
                recycleVelocityTracker();
                break;
        }

        return true;
    }


    public boolean isShouldIntercepted(){
        return false; //ibuTouchUtil.isShouldIntercepted();
    }

    /**
     * @param up 大于0向上,小于0向下
     */
    protected void startAnimator(int up){
        ibuTouchUtil.startAnimator(up);
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
     * 是否需要拦截滑动事件
     * @param deltaY
     * @param deltaX
     * @return
     */
    public boolean isIntercepted(int deltaY, int deltaX) {
        return ibuTouchUtil.isIntercepted(deltaY ,deltaX) || ibuTouchUtil.isRefreshIntercepted(deltaY, deltaX);
    }



    public void setRefreshCallBack(RefreshCallBack refreshCallBack) {
        this.refreshCallBack = refreshCallBack;
    }
}
