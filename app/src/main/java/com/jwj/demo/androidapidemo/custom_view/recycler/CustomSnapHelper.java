package com.jwj.demo.androidapidemo.custom_view.recycler;

import android.graphics.PointF;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.view.View;

/**
 * Description: 描述
 * Author: wjxie
 * Date: 2017/10/25
 * Copyright: Ctrip
 */

public class CustomSnapHelper extends SnapHelper {

    OrientationHelper vertiacalHelper, horizontalHelper;
    int INVALID_DISTANCE = 0;


    @Nullable
    @Override

    public int[] calculateDistanceToFinalSnap(@NonNull RecyclerView.LayoutManager layoutManager, @NonNull View targetView) {
        int[] out = new int[2];
        //水平方向滚动,则计算水平方向需要滚动的距离,否则水平方向的滚动距离为0
        if (layoutManager.canScrollHorizontally()) {
            out[0] = distanceToCenter(layoutManager, targetView,
                    OrientationHelper.createHorizontalHelper(layoutManager));
        } else {
            out[0] = 0;
        }

        //竖直方向滚动,则计算竖直方向需要滚动的距离,否则水平方向的滚动距离为0
        if (layoutManager.canScrollVertically()) {
            out[1] = distanceToCenter(layoutManager, targetView,
                    OrientationHelper.createVerticalHelper(layoutManager));
        } else {
            out[1] = 0;
        }
        return out;
    }

    @Nullable
    @Override
    public View findSnapView(RecyclerView.LayoutManager layoutManager) {
        if (layoutManager.canScrollVertically()) {
            return findTargetView(layoutManager, getVerticalHelper(layoutManager));
        } else if (layoutManager.canScrollHorizontally()) {
            //return findCenterView(layoutManager, getHorizontalHelper(layoutManager));
        }
        return null;
    }

    @Override
    public int findTargetSnapPosition(RecyclerView.LayoutManager layoutManager, int velocityX, int velocityY) {
        //判断layoutManager是否实现了RecyclerView.SmoothScroller.ScrollVectorProvider这个接口
        if (!(layoutManager instanceof RecyclerView.SmoothScroller.ScrollVectorProvider)) {
            return RecyclerView.NO_POSITION;
        }

        final int itemCount = layoutManager.getItemCount();
        if (itemCount == 0) {
            return RecyclerView.NO_POSITION;
        }

        //找到snapView
        final View currentView = findSnapView(layoutManager);
        if (currentView == null) {
            return RecyclerView.NO_POSITION;
        }

        final int currentPosition = layoutManager.getPosition(currentView);
        if (currentPosition == RecyclerView.NO_POSITION) {
            return RecyclerView.NO_POSITION;
        }

        RecyclerView.SmoothScroller.ScrollVectorProvider vectorProvider =
                (RecyclerView.SmoothScroller.ScrollVectorProvider) layoutManager;
        // 通过ScrollVectorProvider接口中的computeScrollVectorForPosition（）方法
        // 来确定layoutManager的布局方向
        PointF vectorForEnd = vectorProvider.computeScrollVectorForPosition(itemCount - 1);
        if (vectorForEnd == null) {
            return RecyclerView.NO_POSITION;
        }

        int vDeltaJump, hDeltaJump;
        if (layoutManager.canScrollHorizontally()) {
            //layoutManager是横向布局，并且内容超出一屏，canScrollHorizontally()才返回true
            //估算fling结束时相对于当前snapView位置的横向位置偏移量
            hDeltaJump = estimateNextPositionDiffForFling(layoutManager,
                    getHorizontalHelper(layoutManager), velocityX, 0);
            //vectorForEnd.x < 0代表layoutManager是反向布局的，就把偏移量取反
            if (vectorForEnd.x < 0) {
                hDeltaJump = -hDeltaJump;
            }
        } else {
            //不能横向滚动，横向位置偏移量当然就为0
            hDeltaJump = 0;
        }

        //竖向的原理同上
        if (layoutManager.canScrollVertically()) {
            vDeltaJump = estimateNextPositionDiffForFling(layoutManager,
                    getVerticalHelper(layoutManager), 0, velocityY);
            if (vectorForEnd.y < 0) {
                vDeltaJump = -vDeltaJump;
            }
        } else {
            vDeltaJump = 0;
        }

        //根据layoutManager的横竖向布局方式，最终横向位置偏移量和竖向位置偏移量二选一，作为fling的位置偏移量
        int deltaJump = layoutManager.canScrollVertically() ? vDeltaJump : hDeltaJump;
        if (deltaJump == 0) {
            return RecyclerView.NO_POSITION;
        }
        //当前位置加上偏移位置，就得到fling结束时的位置，这个位置就是targetPosition
        int targetPos = currentPosition + deltaJump;
        if (targetPos < 0) {
            targetPos = 0;
        }
        if (targetPos >= itemCount) {
            targetPos = itemCount - 1;
        }
        return targetPos;
    }

    private View findTargetView(RecyclerView.LayoutManager layoutManager,
                                OrientationHelper helper) {
        int childCount = layoutManager.getChildCount();
        if (childCount == 0) {
            return null;
        }

        LinearLayoutManager linearLayoutManager = (LinearLayoutManager) layoutManager;
        return linearLayoutManager.findViewByPosition(0);
    }


//    private View findCenterView(RecyclerView.LayoutManager layoutManager,
//                                OrientationHelper helper) {
//        int childCount = layoutManager.getChildCount();
//        if (childCount == 0) {
//            return null;
//        }
//
//        View closestChild = null;
//        //找到RecyclerView的中心坐标
//        final int center;
//        if (layoutManager.getClipToPadding()) {
//            center = helper.getStartAfterPadding() + helper.getTotalSpace() / 2;
//        } else {
//            center = helper.getEnd() / 2;
//        }
//        int absClosest = Integer.MAX_VALUE;
//
//        //遍历当前layoutManager中所有的ItemView
//        for (int i = 0; i < childCount; i++) {
//            final View child = layoutManager.getChildAt(i);
//            //ItemView的中心坐标
//            int childCenter = helper.getDecoratedStart(child) +
//                    (helper.getDecoratedMeasurement(child) / 2);
//            //计算此ItemView与RecyclerView中心坐标的距离
//            int absDistance = Math.abs(childCenter - center);
//
//            //对比每个ItemView距离到RecyclerView中心点的距离，找到那个最靠近中心的ItemView然后返回
//            if (absDistance < absClosest) {
//                absClosest = absDistance;
//                closestChild = child;
//            }
//        }
//        return closestChild;
//    }


    private OrientationHelper getVerticalHelper(RecyclerView.LayoutManager manager) {
        if (horizontalHelper == null) {
            horizontalHelper = OrientationHelper.createHorizontalHelper(manager);
        }
        return horizontalHelper;
    }

    private OrientationHelper getHorizontalHelper(RecyclerView.LayoutManager manager) {
        if (vertiacalHelper == null) {
            vertiacalHelper = OrientationHelper.createVerticalHelper(manager);
        }
        return vertiacalHelper;
    }


    private int distanceToCenter(@NonNull RecyclerView.LayoutManager layoutManager,
                                 @NonNull View targetView, OrientationHelper helper) {
        //找到targetView的中心坐标
        final int childCenter = helper.getDecoratedStart(targetView) +
                (helper.getDecoratedMeasurement(targetView) / 2);
        final int containerCenter;
        //找到容器（RecyclerView）的中心坐标
        if (layoutManager.getClipToPadding()) {
            containerCenter = helper.getStartAfterPadding() + helper.getTotalSpace() / 2;
        } else {
            containerCenter = helper.getEnd() / 2;
        }
        //两个中心坐标的差值就是targetView需要滚动的距离
        return childCenter - containerCenter;
    }

    private int estimateNextPositionDiffForFling(RecyclerView.LayoutManager layoutManager,
                                                 OrientationHelper helper, int velocityX, int velocityY) {
        //计算滚动的总距离，这个距离受到触发fling时的速度的影响
        int[] distances = calculateScrollDistance(velocityX, velocityY);
        //计算每个ItemView的长度
        float distancePerChild = computeDistancePerChild(layoutManager, helper);
        if (distancePerChild <= 0) {
            return 0;
        }
        //这里其实就是根据是横向布局还是纵向布局，来取对应布局方向上的滚动距离
        int distance =
                Math.abs(distances[0]) > Math.abs(distances[1]) ? distances[0] : distances[1];
        //distance的正负值符号表示滚动方向，数值表示滚动距离。横向布局方式，内容从右往左滚动为正；竖向布局方式，内容从下往上滚动为正
        // 滚动距离/item的长度=滚动item的个数，这里取计算结果的整数部分
        if (distance > 0) {
            return (int) Math.floor(distance / distancePerChild);
        } else {
            return (int) Math.ceil(distance / distancePerChild);
        }
    }


    private float computeDistancePerChild(RecyclerView.LayoutManager layoutManager,
                                          OrientationHelper helper) {
        View minPosView = null;
        View maxPosView = null;
        int minPos = Integer.MAX_VALUE;
        int maxPos = Integer.MIN_VALUE;
        int childCount = layoutManager.getChildCount();
        if (childCount == 0) {
            return INVALID_DISTANCE;
        }

        //循环遍历layoutManager的itemView，得到最小position和最大position，以及对应的view
        for (int i = 0; i < childCount; i++) {
            View child = layoutManager.getChildAt(i);
            final int pos = layoutManager.getPosition(child);
            if (pos == RecyclerView.NO_POSITION) {
                continue;
            }
            if (pos < minPos) {
                minPos = pos;
                minPosView = child;
            }
            if (pos > maxPos) {
                maxPos = pos;
                maxPosView = child;
            }
        }
        if (minPosView == null || maxPosView == null) {
            return INVALID_DISTANCE;
        }
        //最小位置和最大位置肯定就是分布在layoutManager的两端，但是无法直接确定哪个在起点哪个在终点（因为有正反向布局）
        //所以取两者中起点坐标小的那个作为起点坐标
        //终点坐标的取值一样的道理
        int start = Math.min(helper.getDecoratedStart(minPosView),
                helper.getDecoratedStart(maxPosView));
        int end = Math.max(helper.getDecoratedEnd(minPosView),
                helper.getDecoratedEnd(maxPosView));
        //终点坐标减去起点坐标得到这些itemview的总长度
        int distance = end - start;
        if (distance == 0) {
            return INVALID_DISTANCE;
        }
        // 总长度 / itemview个数 = itemview平均长度
        return 1f * distance / ((maxPos - minPos) + 1);
    }
}
