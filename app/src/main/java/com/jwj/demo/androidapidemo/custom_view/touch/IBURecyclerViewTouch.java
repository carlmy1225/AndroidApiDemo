package com.jwj.demo.androidapidemo.custom_view.touch;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.jwj.demo.androidapidemo.R;
import com.jwj.demo.androidapidemo.custom_view.ScrollInterceptCallBack;

/**
 * Created by jwj on 17/10/13.
 */
public class IBURecyclerViewTouch extends RecyclerView{

    private final int MIN_QUAD_HEIGHT = 120; //默认的曲线弧度
    private final float FACTOR = 0.4f;   //滑动因子
    private int mTopVisibleHeight;   //头部需要挖空的高度
    ScrollCallBack scrollCallBack;
    private int mTotalScrolled = 0;

    public interface ScrollCallBack {
        void onScroll(int scrollY, int deltaY);
    }

    public void setScrollCallBack(ScrollCallBack scrollCallBack) {
        this.scrollCallBack = scrollCallBack;
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
        mTopVisibleHeight = array.getDimensionPixelSize(R.styleable.layer_content_top_visible_height, 0) + 60;

        setPadding(0, mTopVisibleHeight, 0, 0);
        setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (scrollCallBack != null) {
                    scrollCallBack.onScroll(mTotalScrolled, dy);
                }
                mTotalScrolled += dy;
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }
        });
    }


}
