package com.jwj.demo.androidapidemo.custom_view.refresh.util;

import android.support.annotation.Nullable;

import com.jwj.demo.androidapidemo.custom_view.refresh.api.RefreshFooter;
import com.jwj.demo.androidapidemo.custom_view.refresh.api.RefreshLayout;

/**
 * Description: 描述
 * Author: wjxie
 * Date: 2017/10/24
 * Copyright: Ctrip
 */

public interface LoadMoreListener {

    public RefreshLayout setFooterHeight(float dp);

    public RefreshLayout setFooterHeightPx(int px);


    public RefreshLayout setFooterMaxDragRate(float rate);


    public RefreshLayout setLoadmoreFinished(boolean finished);


    public RefreshLayout setRefreshFooter(RefreshFooter footer);


    public RefreshLayout setRefreshFooter(RefreshFooter footer, int width, int height);


    public RefreshLayout finishLoadmore();


    public RefreshLayout finishLoadmore(int delayed);


    public RefreshLayout finishLoadmore(boolean success);


    public RefreshLayout finishLoadmore(int delayed, boolean success);

    @Nullable

    public RefreshFooter getRefreshFooter();

}
