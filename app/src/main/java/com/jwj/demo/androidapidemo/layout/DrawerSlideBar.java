package com.jwj.demo.androidapidemo.layout;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.LinearLayout;

/**
 * Description: 描述
 * Author: wjxie
 * Date: 2018/1/30
 * Copyright: Ctrip
 */

public class DrawerSlideBar extends LinearLayout {

    AttributeSet attrs;

    public DrawerSlideBar(Context context) {
        super(context);
    }

    public DrawerSlideBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.attrs = attrs;
    }

    public AttributeSet getAttributeSet() {
        return attrs;
    }
}
