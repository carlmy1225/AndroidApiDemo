package com.jwj.demo.androidapidemo.custom_view;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.jwj.demo.androidapidemo.R;

/**
 * Description: 描述
 * Author: wjxie
 * Date: 2017/10/9
 * Copyright: Ctrip
 */

public class IBURecyclerView extends RecyclerView {
    private final int MIN_QUAD_HEIGHT = 120; //默认的曲线弧度
    private final float FACTOR = 0.4f;   //滑动因子
    private int mTopVisibleHeight;   //头部需要挖空的高度
    ScrollCallBack scrollCallBack;
    ScrollInterceptCallBack mInterceptCallBack;
    private int mTotalScrolled = 0;

    public interface ScrollCallBack {
        void onScroll(int scrollY, int deltaY);
    }

    public void setmInterceptCallBack(ScrollInterceptCallBack mInterceptCallBack) {
        this.mInterceptCallBack = mInterceptCallBack;
    }

    public IBURecyclerView(Context context) {
        this(context, null);
    }

    public IBURecyclerView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public IBURecyclerView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.layer_content, defStyleAttr, 0);
        mTopVisibleHeight = array.getDimensionPixelSize(R.styleable.layer_content_top_visible_height, 0);

        setPadding(0, mTopVisibleHeight, 0, 0);
        setOnScrollListener(new OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
//
//                if (scrollCallBack != null) {
//                    scrollCallBack.onScroll(mTotalScrolled, dy);
//                }
//                mTotalScrolled += dy;
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }
        });
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                getParent().requestDisallowInterceptTouchEvent(true);
                break;

            case MotionEvent.ACTION_MOVE:
                if (mInterceptCallBack != null && mInterceptCallBack.isIntercept()) {
                    getParent().requestDisallowInterceptTouchEvent(false);
                } else {
                    getParent().requestDisallowInterceptTouchEvent(true);
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                break;
        }
        return super.dispatchTouchEvent(ev);
    }
}
