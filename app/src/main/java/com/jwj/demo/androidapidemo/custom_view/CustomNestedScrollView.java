package com.jwj.demo.androidapidemo.custom_view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ScrollView;

import com.jwj.demo.androidapidemo.R;

/**
 * Description: 描述
 * Author: wjxie
 * Date: 2017/9/26
 * Copyright: Ctrip
 */

public class CustomNestedScrollView extends ScrollView {

    private TopBgView topBgView;
    private int mLastMotionY;

    ScrollCallBack scrollCallBack;


    public interface ScrollCallBack {
        void onScroll(int scrollY, int deltaY);
    }


    public CustomNestedScrollView(Context context) {
        super(context);
        init();
    }

    public CustomNestedScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    void init() {
        setNestedScrollingEnabled(false);
    }


    public void setScrollCallBack(ScrollCallBack scrollCallBack) {
        this.scrollCallBack = scrollCallBack;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        topBgView = (TopBgView) findViewById(R.id.top_bg_view);
    }

    @Override
    protected boolean overScrollBy(int deltaX, int deltaY, int scrollX, int scrollY, int scrollRangeX, int scrollRangeY, int maxOverScrollX, int maxOverScrollY, boolean isTouchEvent) {
        if (topBgView != null) {
            if (scrollCallBack != null) {
                scrollCallBack.onScroll(scrollY, deltaY);
            }
        }
        return super.overScrollBy(deltaX, deltaY, scrollX, scrollY, scrollRangeX, scrollRangeY, maxOverScrollX, maxOverScrollY, isTouchEvent);
    }


    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (topBgView == null) {
            return super.onTouchEvent(ev);
        }

        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mLastMotionY = (int) ev.getY();
                break;

            case MotionEvent.ACTION_MOVE:
                final int y = (int) ev.getY();
                int deltaY = mLastMotionY - y;

                if (getScrollY() == 0) {   //滑动顶部
                    if (deltaY < 0) {
                        topBgView.secureUpdateQuad(ev.getX(), deltaY);
                        return true;
                    } else {
                        topBgView.canceUpEvent();
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                topBgView.canceUpEvent();
                break;
        }
        return super.onTouchEvent(ev);
    }

}
