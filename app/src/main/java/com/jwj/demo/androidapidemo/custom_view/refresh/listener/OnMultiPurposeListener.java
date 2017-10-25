package com.jwj.demo.androidapidemo.custom_view.refresh.listener;

import com.jwj.demo.androidapidemo.custom_view.refresh.api.RefreshHeader;
import com.jwj.demo.androidapidemo.custom_view.refresh.api.RefreshLayout;
import com.jwj.demo.androidapidemo.custom_view.refresh.constant.RefreshState;

/**
 * 多功能监听器
 * Created by SCWANG on 2017/5/26.
 */

public interface OnMultiPurposeListener {
    void onHeaderPulling(RefreshHeader header, float percent, int offset, int headerHeight, int extendHeight);

    void onHeaderReleasing(RefreshHeader header, float percent, int offset, int headerHeight, int extendHeight);

    void onHeaderStartAnimator(RefreshHeader header, int headerHeight, int extendHeight);

    void onHeaderFinish(RefreshHeader header, boolean success);

    void onStateChanged(RefreshLayout refreshLayout, RefreshState oldState, RefreshState newState);

    void onRefresh(RefreshLayout refreshlayout);

//    void onFooterPulling(RefreshFooter footer, float percent, int offset, int footerHeight, int extendHeight);
//
//    void onFooterReleasing(RefreshFooter footer, float percent, int offset, int footerHeight, int extendHeight);
//
//    void onFooterStartAnimator(RefreshFooter footer, int footerHeight, int extendHeight);
//
//    void onFooterFinish(RefreshFooter footer, boolean success);
}
