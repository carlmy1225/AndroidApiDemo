package com.jwj.demo.androidapidemo.activityInfo;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.jwj.demo.androidapidemo.BaseAct;

/**
 * Description: 描述
 * Author: wjxie
 * Date: 2017/9/7
 * Copyright: Ctrip
 */

public class Aact extends BaseAct {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent i = new Intent(this, Bact.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

    }
}
