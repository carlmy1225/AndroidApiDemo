package com.jwj.demo.androidapidemo.system_util;

import android.os.Build;

/**
 * Description: 描述
 * Author: wjxie
 * Date: 2017/9/28
 * Copyright: Ctrip
 */

public class SystemUtil {

    public static int getSdkVersion() {
        return Build.VERSION.SDK_INT;
    }
}
