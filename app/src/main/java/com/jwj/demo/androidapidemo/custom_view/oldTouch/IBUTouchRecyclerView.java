package com.jwj.demo.androidapidemo.custom_view.oldTouch;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.jwj.demo.androidapidemo.R;


/**
 * Created by jwj on 17/10/13.
 */
public class IBUTouchRecyclerView extends RecyclerView {
    private int mTopVisibleHeight;   //头部需要挖空的高度
    private int mTotalScrolled = 0;
    int scrollFlag;         //1表示触摸滚动 ， 3表示触摸之后滑动滚动
    int mLastY;
    int mLastX;

    IBUTouchContainerView vg;
    int resId;
    Activity activity;
    float oldY;
    boolean isInterceptd = false;

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

        setLayoutFrozen(false);
        activity = (Activity) context;
        setOnScrollListener(new OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                mTotalScrolled += dy;
                if (vg != null && vg.isShouldIntercepted() && dy < 0 && scrollFlag == 3) {
                    stopScroll();
                    vg.startAnimator(1);
                }
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }
        });
    }

    public LayoutManager setLinearLayoutManager() {
        LayoutManager manager = new LinearLayoutManager(getContext()) {
            @Override
            public void onLayoutCompleted(State state) {
                super.onLayoutCompleted(state);
                int childCount = getChildCount();
                int contentHeight = 0;
                for (int i = 0; i < childCount; i++) {
                    contentHeight += getChildAt(i).getHeight();
                }
                int contentLength = contentHeight + getPaddingTop() + getPaddingBottom();
                int height = getHeight();
                if (contentLength + mTopVisibleHeight > getHeight()) {
                    isInterceptd = true;
                } else {
                    isInterceptd = false;
                }
            }
        };
        setLayoutManager(manager);
        return manager;
    }

    @Override
    protected void onMeasure(int widthSpec, int heightSpec) {
        super.onMeasure(widthSpec, heightSpec);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (resId > 0) {
            View view = activity.findViewById(resId);
            if (view != null) mTopVisibleHeight = view.getMeasuredHeight();
        }
        android.support.v4.view.ViewCompat.setY(this, mTopVisibleHeight);
    }

    public int getmTotalScrolled() {
        //return mTotalScrolled;
        LinearLayoutManager layoutManager = (LinearLayoutManager) this.getLayoutManager();
        int position = layoutManager.findFirstVisibleItemPosition();
        View firstVisiableChildView = layoutManager.findViewByPosition(position);

        if (firstVisiableChildView != null) {
            int itemHeight = firstVisiableChildView.getHeight();

            int scrollY = (position) * itemHeight - firstVisiableChildView.getTop();
            Log.d("recycler_scrollY", scrollY + "");
            return scrollY;
        } else {
            Log.d("recycler_scrollY", 0 + "");
            return 0;
        }
    }

    @Override
    protected void attachLayoutAnimationParameters(View child, ViewGroup.LayoutParams params, int index, int count) {
        super.attachLayoutAnimationParameters(child, params, index, count);
    }

    public boolean isScrollEnable() {
        return isInterceptd;
    }


//    @Override
//    public boolean dispatchTouchEvent(MotionEvent ev) {
//        if (vg == null) {
//            vg = (IBUTouchContainerView) getParent();
//        }
//
//        int x = (int) ev.getX();
//        int y = (int) ev.getY();
//
//        switch (ev.getAction()) {
//            case MotionEvent.ACTION_DOWN:
//                vg.requestDisallowInterceptTouchEvent(true);
//                scrollFlag = 0;
//                break;
//            case MotionEvent.ACTION_MOVE:
//                int deltaX = mLastX - x;
//                int deltaY = mLastY - y;
//                if (vg.isIntercepted(deltaY, deltaX)) {
//                    vg.requestDisallowInterceptTouchEvent(false);
//                    return false;
//                }
//                break;
//            case MotionEvent.ACTION_UP:
//                break;
//            default:
//                break;
//        }
//        mLastX = x;
//        mLastY = y;
//
//        return super.dispatchTouchEvent(ev);
//    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        int y = (int) e.getY();
        int x = (int) e.getX();
        if (vg == null) {
            vg = (IBUTouchContainerView) getParent();
        }

        switch (e.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mLastY = y;
                mLastX = x;
                scrollFlag = 0;
                break;

            case MotionEvent.ACTION_MOVE:
                scrollFlag = 1;
                int deltaX = mLastX - x;
                int deltaY = mLastY - y;
                mLastY = y;

                if (deltaY < 0 && getmTotalScrolled() == 0) {
                    vg.requestDisallowInterceptTouchEvent(false);
                    return false;
                }
                break;
            case MotionEvent.ACTION_CANCEL:
                e.setAction(MotionEvent.ACTION_UP);
                return false;
            case MotionEvent.ACTION_UP:
                scrollFlag |= 2;
                break;
        }
        return super.onTouchEvent(e);
    }


}
