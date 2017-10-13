package com.jwj.demo.androidapidemo.custom_view.animtor;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ViewGroup;

import com.jwj.demo.androidapidemo.R;
import com.jwj.demo.androidapidemo.custom_view.ScrollInterceptCallBack;

/**
 * Description: 描述
 * Author: wjxie
 * Date: 2017/10/9
 * Copyright: Ctrip
 */

public class IBURecyclerView extends RecyclerView {
    private int mTopVisibleHeight;   //头部需要挖空的高度

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
    }

}
