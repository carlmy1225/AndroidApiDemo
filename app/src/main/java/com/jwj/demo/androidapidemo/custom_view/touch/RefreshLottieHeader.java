package com.jwj.demo.androidapidemo.custom_view.touch;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.airbnb.lottie.LottieAnimationView;
import com.airbnb.lottie.LottieComposition;
import com.airbnb.lottie.OnCompositionLoadedListener;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshKernel;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.RefreshState;
import com.scwang.smartrefresh.layout.constant.SpinnerStyle;

/**
 * Description: 描述
 * Author: wjxie
 * Date: 2017/10/23
 * Copyright: Ctrip
 */

public class RefreshLottieHeader extends FrameLayout implements RefreshHeader {

    boolean isRefreshing;
    LottieComposition data1, data2;
    LottieAnimationView preView, afterView;

    boolean isAdd;

    public RefreshLottieHeader(Context context) {
        super(context);
        init();
    }

    public RefreshLottieHeader(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
    }

    void init() {
        createLottieView();
        LottieComposition.Factory.fromAssetFileName(getContext(), "data1.json", new OnCompositionLoadedListener() {
            @Override
            public void onCompositionLoaded(@Nullable LottieComposition composition) {
                preView.setComposition(composition);
            }
        });
        LottieComposition.Factory.fromAssetFileName(getContext(), "data2.json", new OnCompositionLoadedListener() {
            @Override
            public void onCompositionLoaded(@Nullable LottieComposition composition) {
                afterView.setComposition(composition);
            }
        });
        preView.loop(false);
        preView.addAnimatorListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                afterView.playAnimation();
            }
        });
        afterView.loop(true);
    }

    void createLottieView() {
        preView = new LottieAnimationView(getContext());
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        params.gravity = Gravity.CENTER_HORIZONTAL;
        params.topMargin = 50;
        addView(preView, params);

        afterView = new LottieAnimationView(getContext());
        LayoutParams params1 = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        params1.gravity = Gravity.CENTER_HORIZONTAL;
        params1.topMargin = 50;
        addView(afterView, params1);
    }


    @Override
    public void onPullingDown(float percent, int offset, int headerHeight, int extendHeight) {
        ViewCompat.setAlpha(this, percent);
        Log.d("percent", percent + "");
    }

    @Override
    public void onReleasing(float percent, int offset, int headerHeight, int extendHeight) {
        ViewCompat.setAlpha(this, percent);
        Log.d("percent", percent + "");
    }

    @NonNull
    @Override
    public View getView() {
        return this;
    }


    @Override
    public SpinnerStyle getSpinnerStyle() {
        return SpinnerStyle.Translate;
    }

    @Override
    public void setPrimaryColors(@ColorInt int... colors) {

    }

    @Override
    public void onInitialized(RefreshKernel kernel, int height, int extendHeight) {

    }

    @Override
    public void onHorizontalDrag(float percentX, int offsetX, int offsetMax) {

    }

    @Override
    public void onStartAnimator(RefreshLayout layout, int height, int extendHeight) {

    }

    @Override
    public int onFinish(RefreshLayout layout, boolean success) {
        isRefreshing = false;
        preView.cancelAnimation();
        afterView.cancelAnimation();
        return 0;
    }

    @Override
    public boolean isSupportHorizontalDrag() {
        return false;
    }

    @Override
    public void onStateChanged(RefreshLayout refreshLayout, RefreshState oldState, RefreshState newState) {

    }

    @Override
    public void onRefreshReleased(RefreshLayout layout, int headerHeight, int extendHeight) {
        isRefreshing = true;
        preView.playAnimation();
    }
}
