package com.jwj.demo.androidapidemo.custom_view.recycler;

import android.content.Context;
import android.graphics.PointF;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSmoothScroller;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.View;

import com.jwj.demo.androidapidemo.custom_view.touch.IBUTouchRecyclerView;

/**
 * Created by jwj on 17/10/25.
 */
public class ScrollSpeedLinearLayoutManger extends LinearLayoutManager {

    private IBUTouchRecyclerView touchRecyclerView;

    public ScrollSpeedLinearLayoutManger(Context context, IBUTouchRecyclerView ibuTouchRecyclerView) {
        super(context, VERTICAL, false);
        this.touchRecyclerView = ibuTouchRecyclerView;
    }


    @Override
    public void smoothScrollToPosition(RecyclerView recyclerView, RecyclerView.State state, int position) {
        RecyclerView.SmoothScroller smoothScroller = new CenterSmoothScroller(recyclerView.getContext());
        smoothScroller.setTargetPosition(position);
        startSmoothScroll(smoothScroller);
    }

    public int getScrollY() {
        if (!shouldScroll()) {
            return 1000;
        }
        return touchRecyclerView.getmTotalScrolled();
    }

    public boolean shouldScroll() {
        LinearLayoutManager layoutManager = (LinearLayoutManager) touchRecyclerView.getLayoutManager();
        View view = layoutManager.findViewByPosition(0);
        if (view == null) {
            return false;
        }
        return true;
    }


    @Override
    public int scrollVerticallyBy(int dy, RecyclerView.Recycler recycler, RecyclerView.State state) {
        return super.scrollVerticallyBy(dy, recycler, state);
    }

    private class CenterSmoothScroller extends LinearSmoothScroller {

        CenterSmoothScroller(Context context) {
            super(context);
        }

        @Nullable
        @Override
        public PointF computeScrollVectorForPosition(int targetPosition) {
            return ScrollSpeedLinearLayoutManger.this.computeScrollVectorForPosition(targetPosition);
        }

        @Override
        public int calculateDtToFit(int viewStart, int viewEnd, int boxStart, int boxEnd, int snapPreference) {
            return (boxStart + (boxEnd - boxStart) / 2) - (viewStart + (viewEnd - viewStart) / 2);
        }

        protected float calculateSpeedPerPixel(DisplayMetrics displayMetrics) {
            return 0.5f;
        }

        @Override
        protected int getVerticalSnapPreference() {
            return SNAP_TO_START;
        }
    }


}
