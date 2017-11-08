package com.jwj.demo.androidapidemo.custom_view;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.jwj.demo.androidapidemo.BaseAct;
import com.jwj.demo.androidapidemo.R;

/**
 * Created by jwj on 17/10/22.
 */
public class ScrollViewAct extends BaseAct {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.test_imgae);

//        LottieAnimationView lottieView = (LottieAnimationView) findViewById(R.id.lottie_view);
//        lottieView.loop(true);
//        lottieView.playAnimation();
    }
}
