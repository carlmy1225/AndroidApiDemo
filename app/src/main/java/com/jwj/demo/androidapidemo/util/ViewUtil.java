package com.jwj.demo.androidapidemo.util;

import android.content.Context;
import android.content.res.Resources;

/**
 * Description: 描述
 * Author: wjxie
 * Date: 2017/10/20
 * Copyright: Ctrip
 */

public class ViewUtil {

    public static int getStatusBarHeight(Context context) {
        int result = 0;
        Resources resource = context.getResources();
        int resourceId = resource.getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = resource.getDimensionPixelSize(resourceId);
        }
        return result;
    }

}
