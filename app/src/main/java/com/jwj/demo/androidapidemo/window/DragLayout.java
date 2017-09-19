package com.jwj.demo.androidapidemo.window;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

/**
 * 基于Window实现 view的拖拽实现
 * Author: wjxie
 * Date: 2017/9/7
 * Copyright: Ctrip
 */

public class DragLayout extends LinearLayout implements View.OnLongClickListener {

    WindowManager wm;
    View currentView;
    ImageView cacheIv;

    public DragLayout(Context context) {
        super(context);
        init();
    }

    public DragLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    void init() {
        wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_MOVE) {
            return true;
        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        int count = getChildCount();

        for (int i = 0; i < count; i++) {
            getChildAt(i).setOnLongClickListener(this);
        }
    }


    @Override
    public boolean onLongClick(View v) {
        currentView = v;
        currentView.setDrawingCacheEnabled(true);
        createWindowView(wm, currentView.getDrawingCache(), v.getX(), v.getY());
        v.setVisibility(View.INVISIBLE);
        return true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {
        if (currentView != null) {
            if (motionEvent.getAction() == MotionEvent.ACTION_MOVE) {
                updateWindowViewPosition(motionEvent.getX(), motionEvent.getY());
                return true;
            } else if (motionEvent.getAction() == MotionEvent.ACTION_UP || motionEvent.getAction() == MotionEvent.ACTION_CANCEL) {
                canceWindowView(motionEvent.getX(), motionEvent.getY());
                return true;
            }

        }
        return super.onTouchEvent(motionEvent);
    }

    void createWindowView(WindowManager wm, Bitmap bitmap, float x, float y) {
        if (bitmap == null || bitmap.isRecycled()) {
            return;
        }
        if (cacheIv == null) {
            cacheIv = new ImageView(getContext());
        }
        WindowManager.LayoutParams params = new WindowManager.LayoutParams();
        params.gravity = Gravity.LEFT | Gravity.TOP;
        params.x = (int) x;
        params.y = (int) y;
        params.width = bitmap.getWidth();
        params.height = bitmap.getHeight();
        cacheIv.setImageBitmap(bitmap);
        wm.addView(cacheIv, params);
    }

    void canceWindowView(float x, float y) {
        if (cacheIv != null) {
            wm.removeViewImmediate(cacheIv);
        }
        currentView.setX(x - currentView.getWidth() / 2);
        currentView.setY(y - currentView.getHeight() / 2);
        currentView.setVisibility(View.VISIBLE);
        currentView = null;
    }

    void updateWindowViewPosition(float x, float y) {
        WindowManager.LayoutParams params = (WindowManager.LayoutParams) cacheIv.getLayoutParams();
        params.x = (int) x - currentView.getWidth() / 2;
        params.y = (int) y - currentView.getHeight() / 2;
        wm.updateViewLayout(cacheIv, params);
    }
}
