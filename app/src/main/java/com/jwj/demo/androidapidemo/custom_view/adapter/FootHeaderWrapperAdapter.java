package com.jwj.demo.androidapidemo.custom_view.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Description: 描述
 * Author: wjxie
 * Date: 2018/1/23
 * Copyright: Ctrip
 */

public class FootHeaderWrapperAdapter extends RecyclerView.Adapter {
    private final int FOOTER_TYPE = -10;
    private final int HEADER_TYPE = -9;

    private RecyclerView.Adapter mAdapter;
    private List<View> footers;
    private List<View> headers;


    public FootHeaderWrapperAdapter(List<View> footers, List<View> headers, RecyclerView.Adapter adapter) {
        this.mAdapter = adapter;
        this.footers = footers;
        this.headers = headers;
    }


    @Override
    public int getItemViewType(int position) {
        if (position < headers.size()) {
            return HEADER_TYPE;
        } else if (position > headers.size() + mAdapter.getItemCount()) {
            return FOOTER_TYPE;
        } else {
            return mAdapter.getItemViewType(position);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mAdapter != null) {
            return mAdapter.onCreateViewHolder(parent, viewType);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (mAdapter != null) {
            mAdapter.onBindViewHolder(holder, position);
        }
    }

    @Override
    public int getItemCount() {
        int count = 0;
        if (mAdapter != null) {
            count += mAdapter.getItemCount();
        }

        if (footers != null && footers.size() > 0) {
            count += footers.size();
        }

        if (headers != null && headers.size() > 0) {
            count += headers.size();
        }

        return count;
    }

    private static class HeadViewHolder extends RecyclerView.ViewHolder {
        public HeadViewHolder(View itemView) {
            super(itemView);
        }
    }

    private static class FootViewHolder extends RecyclerView.ViewHolder {
        public FootViewHolder(View itemView) {
            super(itemView);
        }
    }

}
