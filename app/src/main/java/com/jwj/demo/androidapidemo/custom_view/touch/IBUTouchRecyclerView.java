package com.jwj.demo.androidapidemo.custom_view.touch;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.jwj.demo.androidapidemo.R;
import com.jwj.demo.androidapidemo.custom_view.recycler.ScrollSpeedLinearLayoutManger;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by jwj on 17/10/13.
 */
public class IBUTouchRecyclerView extends RecyclerView {
    private int mTopVisibleHeight;   //头部需要挖空的高度
    int resId;
    Activity activity;
    boolean isInterceptd = false;
    IBUTouchController controller;
    private boolean isInit;

    Map<Integer, Integer> childTypeHeight = new HashMap<>();

    public IBUTouchRecyclerView(Context context) {
        this(context, null);
    }

    public IBUTouchRecyclerView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public IBUTouchRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.IBuTouchView, defStyleAttr, 0);
        mTopVisibleHeight = array.getDimensionPixelSize(R.styleable.IBuTouchView_top_visible_height, 0);
        resId = array.getResourceId(R.styleable.IBuTouchView_top_visible_view_id, 0);
        activity = (Activity) context;
        init();
    }


    private void init() {
    }


    public LayoutManager setLinearLayoutManager() {
        LayoutManager manager = new ScrollSpeedLinearLayoutManger(getContext(), this) {
            @Override
            public void onLayoutCompleted(State state) {
                super.onLayoutCompleted(state);
                int childCount = getChildCount();
                int contentHeight = 0;
                for (int i = 0; i < childCount; i++) {
                    contentHeight += getChildAt(i).getHeight();
                }
                int contentLength = contentHeight + getPaddingTop() + getPaddingBottom();
                if (contentLength + mTopVisibleHeight > getHeight()) {
                    isInterceptd = true;
                } else {
                    isInterceptd = false;
                }
            }
        };
        setLayoutManager(manager);
//        new CustomSnapHelper().attachToRecyclerView(this);
        return manager;
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (resId > 0) {
            View view = activity.findViewById(resId);
            if (view != null) mTopVisibleHeight = view.getMeasuredHeight();
        }
    }


    public View getFirstChild() {
        LinearLayoutManager layoutManager = (LinearLayoutManager) this.getLayoutManager();
        return layoutManager.findViewByPosition(0);
    }


    public int getmTotalScrolled() {
        View view = getFirstChild();
        if (view == null) {
            return 0;
        } else {
            return 835 - view.getTop();
        }
    }


    public boolean dispatchTouchEvent(MotionEvent ev) {
//        if (!isInit) {
//            controller.init((ViewGroup) getParent().getParent());
//            isInit = true;
//        }
//
//        controller.onTouchEvent(ev);
        return super.dispatchTouchEvent(ev);
    }


//    @Override
//    public boolean fling(int velocityX, int velocityY) {
//        final LayoutManager lm = getLayoutManager();
//
//        if (lm instanceof ISnappyLayoutManager) {
//            super.smoothScrollToPosition(((ISnappyLayoutManager) getLayoutManager())
//                    .getPositionForVelocity(velocityX, velocityY));
//            return true;
//        }
//        return super.fling(velocityX, velocityY);
//    }

//    @Override
//    public boolean onTouchEvent(MotionEvent e) {
//        // We want the parent to handle all touch events--there's a lot going on there,
//        // and there is no reason to overwrite that functionality--bad things will happen.
//        final boolean ret = super.onTouchEvent(e);
//        final LayoutManager lm = getLayoutManager();
//
//        if (lm instanceof ISnappyLayoutManager
//                && (e.getAction() == MotionEvent.ACTION_UP ||
//                e.getAction() == MotionEvent.ACTION_CANCEL)
//                && getScrollState() == SCROLL_STATE_IDLE) {
//            // The layout manager is a SnappyLayoutManager, which means that the
//            // children should be snapped to a grid at the end of a drag or
//            // fling. The motion event is either a user lifting their finger or
//            // the cancellation of a motion events, so this is the time to take
//            // over the scrolling to perform our own functionality.
//            // Finally, the scroll state is idle--meaning that the resultant
//            // velocity after the user's gesture was below the threshold, and
//            // no fling was performed, so the view may be in an unaligned state
//            // and will not be flung to a proper state.
//            smoothScrollToPosition(((ISnappyLayoutManager) lm).getFixScrollPos());
//        }
//
//        return ret;
//    }


}
