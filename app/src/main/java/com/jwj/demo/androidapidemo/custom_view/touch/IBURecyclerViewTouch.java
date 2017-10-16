package com.jwj.demo.androidapidemo.custom_view.touch;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ScrollView;

import com.jwj.demo.androidapidemo.R;
import com.jwj.demo.androidapidemo.custom_view.ScrollInterceptCallBack;
import com.jwj.demo.androidapidemo.logger.LogUtil;

/**
 * Created by jwj on 17/10/13.
 */
public class IBURecyclerViewTouch extends RecyclerView {

    private final int MIN_QUAD_HEIGHT = 120; //默认的曲线弧度
    private final float FACTOR = 0.4f;   //滑动因子
    private int mTopVisibleHeight;   //头部需要挖空的高度
    ScrollCallBack scrollCallBack;
    private int mTotalScrolled = 0;

    int mLastY;
    int mLastX;

    CustomTouchView vg;
    int resId;
    Activity activity;

    private ScrollInterceptCallBack interceptCallBack;

    public interface ScrollCallBack {
        void onScroll(int scrollY, int deltaY);
    }

    public void setScrollCallBack(ScrollCallBack scrollCallBack) {
        this.scrollCallBack = scrollCallBack;
    }

    public void setInterceptCallBack(ScrollInterceptCallBack interceptCallBack) {
        this.interceptCallBack = interceptCallBack;
    }

    public IBURecyclerViewTouch(Context context) {
        this(context, null);
    }

    public IBURecyclerViewTouch(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public IBURecyclerViewTouch(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.layer_content, defStyleAttr, 0);
        mTopVisibleHeight = array.getDimensionPixelSize(R.styleable.layer_content_top_visible_height, 0);
        resId = array.getResourceId(R.styleable.layer_content_top_visible_view_id, 0);

        activity = (Activity) context;
        setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                mTotalScrolled += dy;
                LogUtil.d("mTotalScrolled = %d , scrollFlag = %s" , mTotalScrolled , scrollFlag);

                if(vg !=null && vg.isShouldIntercepted() && dy<0 && scrollFlag == 3){
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

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (resId > 0) {
            View view = activity.findViewById(resId);
            if (view != null) mTopVisibleHeight = view.getMeasuredHeight();
        }
        setPadding(0, mTopVisibleHeight, 0, 0);
    }

    public int getmTotalScrolled() {
        return mTotalScrolled;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if(vg ==null){
            vg = (CustomTouchView)getParent();
        }


        int x = (int) ev.getX();
        int y = (int) ev.getY();

        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                vg.requestDisallowInterceptTouchEvent(true);
                scrollFlag = 0;
                break;

            case MotionEvent.ACTION_MOVE:
                int deltaX = mLastX - x;
                int deltaY = mLastY - y;
                if (Math.abs(deltaY) > Math.abs(deltaX) && vg.isIntercepted(deltaY , deltaX)){
                    vg.requestDisallowInterceptTouchEvent(false);
                    LogUtil.d("dispatchTouchEvent_back");
                    return false;
                }
                break;
            case MotionEvent.ACTION_UP:
                break;
            default:
                break;
        }
        mLastX = x;
        mLastY = y;
        return super.dispatchTouchEvent(ev);
    }


    int scrollFlag;


    @Override
    public boolean onTouchEvent(MotionEvent e) {
        int y = (int)e.getY();
        int x = (int)e.getX();

        switch (e.getAction()){
            case MotionEvent.ACTION_DOWN:
                mLastY = y;
                mLastX = x;
                break;

            case MotionEvent.ACTION_MOVE:
                int deltaY = mLastY - y;
                int deltaX = mLastX - x;
                scrollFlag = 1;
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
