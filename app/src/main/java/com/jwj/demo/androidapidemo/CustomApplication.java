package com.jwj.demo.androidapidemo;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import com.jwj.demo.androidapidemo.model.User;
import com.jwj.demo.androidapidemo.struct.aspectJ.Role;

/**
 * Description: 描述
 * Author: wjxie
 * Date: 2017/9/29
 * Copyright: Ctrip
 */

public class CustomApplication extends Application {

    private User user;

    static CustomApplication application;


    @Override
    public void onCreate() {
        super.onCreate();
        application = this;
        initUser();


        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

            }

            @Override
            public void onActivityStarted(Activity activity) {

            }

            @Override
            public void onActivityResumed(Activity activity) {

            }

            @Override
            public void onActivityPaused(Activity activity) {

            }

            @Override
            public void onActivityStopped(Activity activity) {

            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

            }

            @Override
            public void onActivityDestroyed(Activity activity) {

            }
        });
    }

    private void initUser(){
        user = new User();
        user.name = "jwj";
        user.role = Role.MANAGER;
    }


    public static CustomApplication getApplication(){
        return application;
    }

    public User getUser() {
        return user;
    }
}
