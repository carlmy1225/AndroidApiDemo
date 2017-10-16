package com.jwj.demo.androidapidemo.custom_view.touch;

import android.animation.ValueAnimator;
import android.content.Context;
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
import android.view.animation.AccelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Scroller;

import com.jwj.demo.androidapidemo.R;
import com.jwj.demo.androidapidemo.logger.LogUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jwj on 17/10/13.
 */
public class CustomTouchView extends RelativeLayout{
    final int AUTO_SCROLL_DOWN = -1;
    final int AUTO_SCROLL_UP = 1;


    BgImageView mainBgView;
    ViewGroup topView;
    IBURecyclerViewTouch recyclerView;
    View topBgView;
    List<View> icons = new ArrayList<>();


    private Scroller mScroller;
    private int mLastX = 0;
    private int mLastY = 0;
    private int mLastMotionY;
    private int mLastMotionX;
    private int mLastXIntercept = 0;
    private int mLastYIntercept = 0;
    private int topHeight;
    private int autoScrollHeight = 200;
    private int recyclerScrollTo = 200;  //recycler内容被遮盖的距离
    private int TAP = 3;
    private boolean isBeginingDrag;
    private int mLastDirection;
    private int animatorHeight = 200;
    private int recyclerScrollTopHeight;  //滑动到顶部停止的距离

    VelocityTracker mVelocityTracker;
    int mMinimumVelocity;
    private int mMaximumVelocity;

    int mLastTopScrollY , mLastRecyclerScrollY;

    int isAutoScrollDirection = 0;  //判断滑动松手时刻,动画是该往下,还是往上


    public CustomTouchView(Context context) {
        super(context);
    }

    public CustomTouchView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    private void init(){
        final ViewConfiguration configuration = ViewConfiguration.get(getContext());
   //     mTouchSlop = configuration.getScaledTouchSlop();
        mMinimumVelocity = configuration.getScaledMinimumFlingVelocity();
        mMaximumVelocity = configuration.getScaledMaximumFlingVelocity();
       // mOverscrollDistance = configuration.getScaledOverscrollDistance();
       // mOverflingDistance = configuration.getScaledOverflingDistance();
    }


    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mainBgView = (BgImageView)getChildAt(0);
        recyclerView = (IBURecyclerViewTouch) getChildAt(1);
        topBgView = getChildAt(2);
        topBgView.setAlpha(0);

        topView = (ViewGroup)getChildAt(3);
        final View coverIconView = topView.findViewById(R.id.cover_icon_view);

        coverIconView.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                if (topHeight == 0) {
                    topHeight = (int) coverIconView.getY();
                    Log.d("top", coverIconView.getTop() + "");
                    recyclerScrollTopHeight = recyclerView.getPaddingTop() - topBgView.getHeight() + recyclerScrollTo;
                }
                coverIconView.getViewTreeObserver().removeOnPreDrawListener(this);
                return false;
            }
        });

        LinearLayout group = (LinearLayout)topView.findViewById(R.id.cover_icon_view);
        if(group !=null){
            for(int i=0; i<group.getChildCount(); i++){
                ViewGroup viewGroup = (ViewGroup) group.getChildAt(i);
                for(int j=0; j<viewGroup.getChildCount();j++){
                    icons.add(viewGroup.getChildAt(j));
                }
            }

        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        boolean intercepted = false;
        int x = (int) ev.getX();
        int y = (int) ev.getY();


        Log.d("touch_intercept_1", ev.getAction() + "");

        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                intercepted = false;
                mLastX = x;
                mLastY = y;
                mLastXIntercept = x;
                mLastYIntercept = y;
                if(topView.getScrollY() < topHeight){
                    isAutoScrollDirection = AUTO_SCROLL_UP;
                }else{
                    isAutoScrollDirection = AUTO_SCROLL_DOWN;
                }

                initOrResetVelocityTracker();

                if(mVelocityTracker !=null){
                    mVelocityTracker.addMovement(ev);
                }

                break;
            case MotionEvent.ACTION_MOVE:
                int deltaX =mLastXIntercept - x;
                int deltaY =mLastYIntercept - y;
                LogUtil.d("deltaX = %d , deltaY = %d", deltaX , deltaY) ;
                intercepted = isIntercepted(deltaY , deltaX);
                break;

            case MotionEvent.ACTION_UP:
                intercepted = false;
                mLastXIntercept = mLastYIntercept = 0;
                break;
            default:
                break;
        }

        mLastX = x;
        mLastY = y;
        mLastXIntercept = x;
        mLastYIntercept = y;

        LogUtil.d("intercepted = %s", intercepted);
        return intercepted;
    }

    public boolean isIntercepted(int deltaY , int deltaX){
        boolean intercepted = false;

        if (Math.abs(deltaY) >= Math.abs(deltaX)) {
            if(deltaY > 0){
                if(topView.getScrollY() < topHeight){
                    intercepted = true;
                }else if(topView.getScrollY() == topHeight){
                    return false;
                }else{
                    return false;
                }
            }else{
                int scrollY = recyclerView.getmTotalScrolled();
                if(scrollY <= recyclerScrollTopHeight){
                    intercepted = true;
                }else{
                    intercepted = false;
                }
            }
        } else {
            intercepted = false;
        }
        return intercepted;

    }


    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        int initialVelocity = 0;

        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mLastMotionY = (int) ev.getY();
                mLastY = mLastMotionY;
                mLastX = (int) ev.getX();
                break;

            case MotionEvent.ACTION_MOVE:
                final int y = (int) ev.getY();
                final int x = (int)ev.getX();
                int deltaY = mLastY - y;
                mLastDirection = deltaY;
                mLastY = y;

                if(deltaY > 0){
                    recyclerView.scrollBy(0, deltaY);
                    if(topView.getScrollY() < topHeight){
                        if(topView.getScrollY() + deltaY > topHeight){
                            topView.scrollBy(0 , topHeight - topView.getScrollY());
                        }else if(topView.getScrollY() + deltaY + TAP > topHeight){
                            topView.scrollBy(0 , topHeight - topView.getScrollY() - deltaY);
                        }else{
                            topView.scrollBy(0 , deltaY + TAP);
                        }
                    }else{
                        topView.scrollTo(0, topHeight);
                    }
                }else{
                    if(topView.getScrollY() < topHeight){
                        recyclerView.scrollBy(0, deltaY);
                        if(topView.getScrollY() > 0){
                            if(topView.getScrollY() + deltaY < 0){
                                topView.scrollBy(0 , - topView.getScrollY());
                            }else{
                                topView.scrollBy(0 , deltaY);
                            }
                        }else{
                            topView.scrollTo(0,0);
                        }
                    }else if(topView.getScrollY() == topHeight){
                        LogUtil.d("deltaY = %d", deltaY);
                        recyclerView.scrollBy(0, deltaY / 3);
                    }
                }

                handleEffectAnimtor(topView.getScrollY());

                mVelocityTracker.addMovement(ev);




                break;
            case MotionEvent.ACTION_UP:
                LogUtil.d("top_scrollY = %d", topView.getScrollY());
                final VelocityTracker velocityTracker = mVelocityTracker;
                velocityTracker.computeCurrentVelocity(10);
                initialVelocity = (int) mVelocityTracker.getYVelocity();

                LogUtil.d("initialVelocity = %d" , initialVelocity);

                boolean volocityEnable =  ((Math.abs(initialVelocity) > 8));
                if(isAutoScroll(mLastDirection,volocityEnable ,initialVelocity)){
                    LogUtil.d("action_up");
                }
            case MotionEvent.ACTION_CANCEL:
                recycleVelocityTracker();
                break;
        }

        return true;
    }



    /**
     * 处理背景渐变效果
     */
    void handleEffectAnimtor(int translateY){
        ViewCompat.setTranslationY(mainBgView, -translateY);
        float percent = computeRangeAlpha(translateY * 1f / topHeight,0,1);

        if(percent < 0.5f){
            mainBgView.setCustomAlpha(1-percent * 0.3f);
            topBgView.setAlpha(0);
        }else{
            mainBgView.setCustomAlpha(1-percent);
            topBgView.setAlpha((percent-0.5f)*2);
        }

        for(View view : icons){
            int alpha = (int) computeRangeAlpha((1 - percent) * 255,0,255);
            if(view.getBackground() !=null){
                view.getBackground().setAlpha(alpha);
            }else{
                view.setAlpha(1-percent);
            }

        }
    }


    /**
     * 控制 percent的边界值
     * @return
     */
    private float computeRangeAlpha(float percent , int min , int max){
        if(percent > max){
            percent = max;
        }else if(percent < min){
            percent = min;
        }
        return percent;
    }


    private void computeViewScrollRange(View view , int scrollY ,int deltaY ,int maxScroll ,int minScroll){
        if (scrollY + deltaY > maxScroll) {
            view.scrollBy(0, maxScroll - scrollY);
        } else if (scrollY + deltaY < minScroll) {
            view.scrollBy(0, -scrollY);
        } else {
            view.scrollBy(0, deltaY);
        }
    }


    /**
     *
     * @param up  大于0向上,小于0向下
     */

    ValueAnimator animator;

    public void startAnimator(final int up){
        mLastRecyclerScrollY = 0;

        final int topDistance = topView.getScrollY();
        final int topRecycler = recyclerView.getmTotalScrolled();

        if(animator !=null && animator.isRunning()){
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
                    topY = (int) (percent * 1.2f * (topHeight - topDistance));
                    recyclerY = (int) (percent * (recyclerScrollTopHeight - topRecycler));
                } else {
                    topY = -(int) (percent * topDistance);
                    recyclerY = -(int) (percent * topRecycler * 1.3f);
                }

                final int detaY = recyclerY - mLastRecyclerScrollY;
                computeViewScrollRange(recyclerView,recyclerView.getmTotalScrolled(),detaY,recyclerScrollTopHeight,0);
                int desY = (int)computeRangeAlpha(topDistance + topY, 0, topHeight);
                handleEffectAnimtor(desY);
                topView.scrollTo(0,desY);

                mLastRecyclerScrollY = recyclerY;
            }
        });

        animator.setDuration(350);
        animator.setInterpolator(new AccelerateDecelerateInterpolator());
        animator.start();
    }


    void scrollByRecyclerView(int deltaX , int deltaY){
        recyclerView.scrollBy(deltaX, deltaY);
    }


    private void recycleVelocityTracker(){
        if(mVelocityTracker !=null){
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
     * @param director
     * @return
     */
    boolean isAutoScroll(int director , boolean isVelocityEnable , int velocityDirection){
        if(topView.getScrollY() > 0 && topView.getScrollY() < topHeight){
            if(topView.getScrollY() > animatorHeight || isVelocityEnable){
                startAnimator(AUTO_SCROLL_UP);
            }else{
                startAnimator(AUTO_SCROLL_DOWN);
            }
            return true;
        }else if(topView.getScrollY() == topHeight){
            if(isVelocityEnable){
                startAnimator(isAutoScrollDirection);
            }else{
                if(recyclerView.getmTotalScrolled() >= recyclerScrollTopHeight){
                    return false;
                }else if(recyclerView.getmTotalScrolled() >= recyclerScrollTopHeight -topBgView.getHeight()/2){
                    startAnimator(AUTO_SCROLL_UP);
                }else{
                    startAnimator(isAutoScrollDirection);
                }
            }
        }
        return false;
    }


    public boolean isShouldIntercepted(){
        if(recyclerView.getmTotalScrolled() <= recyclerScrollTopHeight){
            return true;
        }

        return false;
    }

}
