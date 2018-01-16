package com.jwj.demo.androidapidemo.activityInfo;

import android.content.Context;
import android.widget.Toast;

import com.jwj.demo.androidapidemo.BaseAct;

/**
 * Description: 描述
 * Author: wjxie
 * Date: 2017/9/7
 * Copyright: Ctrip
 */

public class Bact extends BaseAct {


    @MethodReplace(clazz = "com.jwj.demo.androidapidemo.BaseAct"
            , method = "sayHello")
    public void sayHello(Context context) {
        Toast.makeText(context, "hello", Toast.LENGTH_SHORT)
                .show();
    }
}
