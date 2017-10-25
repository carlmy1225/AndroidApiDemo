package com.jwj.demo.androidapidemo.custom_view.touch;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.support.v4.widget.ScrollerCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Interpolator;

import com.jwj.demo.androidapidemo.R;

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

    ScrollerCompat scrollerCompat;

    private void init() {
        scrollerCompat = ScrollerCompat.create(getContext() ,sQuinticInterpolator);
    }

    @Override
    public boolean dispatchNestedPreFling(float velocityX, float velocityY) {

        Log.d("preFling_velocityY = " , velocityY + "");
        velocityY *= 0.5f;

        Log.d("preFling_before_scrolly = " , getmTotalScrolled() + "");

        boolean b = super.dispatchNestedPreFling(velocityX, velocityY);

        Log.d("preFling_after_scrolly = " , getmTotalScrolled() + "");


        return  b;
    }




    static final Interpolator sQuinticInterpolator = new Interpolator() {
        @Override
        public float getInterpolation(float t) {
            t -= 1.0f;
            return t * t * t * t * t + 1.0f;
        }
    };


    @Override
    public boolean fling(int velocityX, int velocityY) {

        Log.d("fling_velocityY = " , velocityY + "");
        Log.d("fling_before_scrolly = " , getmTotalScrolled() + "");

        boolean b = super.fling(velocityX, velocityY);

        Log.d("fling_after_scrolly = " , getmTotalScrolled() + "");



        return  b;
    }






    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        controller = new IBUTouchController(getContext());
    }

    public LayoutManager setLinearLayoutManager() {
        LayoutManager manager = new ScrollSpeedLinearLayoutManger(getContext()) {
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
                    isInterceptd =  false;
                }
            }
        };
        setLayoutManager(manager);
        return manager;
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (resId > 0) {
            View view = activity.findViewById(resId);
            if (view != null) mTopVisibleHeight = view.getMeasuredHeight();
        }
        setPadding(getPaddingLeft(), getPaddingTop() + mTopVisibleHeight, getPaddingRight(), getPaddingBottom());
    }


    @Override
    public void onScrolled(int dx, int dy) {
        super.onScrolled(dx, dy);
    }

    public int getmTotalScrolled() {
        //return mTotalScrolled;
        LinearLayoutManager layoutManager = (LinearLayoutManager) this.getLayoutManager();
        int position = layoutManager.findFirstVisibleItemPosition();
        View firstVisiableChildView = layoutManager.findViewByPosition(position);

        if (firstVisiableChildView != null) {
            int itemHeight = firstVisiableChildView.getHeight();
            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) firstVisiableChildView.getLayoutParams();
            if (getAdapter() != null) {
                int type = getAdapter().getItemViewType(position);
                itemHeight += params.topMargin + params.bottomMargin;
                childTypeHeight.put(type, itemHeight);
            }
            int scrollY = getTotalHeight(position) - (int) firstVisiableChildView.getTop() + getPaddingTop();

            Log.d("recycler_scrolly", scrollY + "");
            return scrollY;
        } else {
            return 0;
        }
    }


    public int getTotalHeight(int position) {
        int total = 0;
        if (getAdapter() != null) {
            for (int i = 0; i < position; i++) {
                int type = getAdapter().getItemViewType(position);
                total += childTypeHeight.get(type);
            }
        }
        return total;
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (!isInit) {
            controller.init((ViewGroup) getParent().getParent());
            isInit = true;
        }

        controller.onTouchEvent(ev);
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {

        controller.onTouchEvent(e);
        return super.onTouchEvent(e);
    }


    @Override
    public boolean dispatchNestedPreScroll(int dx, int dy, int[] consumed, int[] offsetInWindow) {
        if(onPreScrollListener !=null){
            return onPreScrollListener.onPreScroll(dy);
        }
        return super.dispatchNestedPreScroll(dx, dy, consumed, offsetInWindow);
    }

    OnPreScrollListener onPreScrollListener;

    public void setOnPreScrollListener(OnPreScrollListener onPreScrollListener) {
        this.onPreScrollListener = onPreScrollListener;
    }

    public interface OnPreScrollListener{

        boolean onPreScroll(int detaY);
    }

}
