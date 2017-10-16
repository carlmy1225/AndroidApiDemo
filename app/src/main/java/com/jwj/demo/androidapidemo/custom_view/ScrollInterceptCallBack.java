package com.jwj.demo.androidapidemo.custom_view;

/**
 * Description: 描述
 * Author: wjxie
 * Date: 2017/10/12
 * Copyright: Ctrip
 */

public interface ScrollInterceptCallBack {
    void onPreScroll(int scrollY, int deltaY);

    boolean isIntercept();

    void startUp();

}
