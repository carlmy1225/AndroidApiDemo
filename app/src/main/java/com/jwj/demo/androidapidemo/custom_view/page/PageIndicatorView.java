package com.jwj.demo.androidapidemo.custom_view.page;

import android.content.Context;
import android.database.DataSetObserver;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.jwj.demo.androidapidemo.R;
import com.jwj.demo.androidapidemo.util.DensityUtil;

/**
 * Description: 描述
 * Author: wjxie
 * Date: 2017/11/8
 * Copyright: Ctrip
 */

public class PageIndicatorView extends LinearLayout implements ViewPager.OnPageChangeListener {
    ViewPager mPager;
    int currentIndex;  //当前的

    public PageIndicatorView(Context context) {
        super(context);
    }

    public PageIndicatorView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public void setViewPager(ViewPager viewPager) {
        if (mPager != null) {
            mPager.removeOnPageChangeListener(this);
        }
        this.mPager = viewPager;
        if (mPager != null && mPager.getAdapter() != null) {
            mPager.getAdapter().registerDataSetObserver(observer);
            updateIndicator(mPager.getAdapter().getCount());
            mPager.addOnPageChangeListener(this);
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        getChildAt(currentIndex % mPager.getAdapter().getCount()).setSelected(false);
        currentIndex = position;
        getChildAt(currentIndex % mPager.getAdapter().getCount()).setSelected(true);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    public void updateIndicator(int count) {
        removeAllViews();
        LinearLayout.LayoutParams margin = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        margin.setMargins(DensityUtil.dp2px(12), 0, 0, 0);
        for (int i = 0; i < count; i++) {
            ImageView v = new ImageView(getContext());
            v.setBackgroundResource(R.drawable.myctrip_selector_bg_circle_focus);
            v.setSelected(i == currentIndex % count);
            addView(v, margin);
        }
    }

    public final DataSetObserver observer = new DataSetObserver() {
        @Override
        public void onChanged() {
            if (mPager != null && mPager.getAdapter() != null) {
                updateIndicator(mPager.getAdapter().getCount());
            }
        }
    };
}
