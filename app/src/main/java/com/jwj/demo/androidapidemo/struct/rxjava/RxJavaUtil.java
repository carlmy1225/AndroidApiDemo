package com.jwj.demo.androidapidemo.struct.rxjava;

import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;

import io.reactivex.Flowable;
import io.reactivex.functions.Consumer;

/**
 * Description: 描述
 * Author: wjxie
 * Date: 2017/12/27
 * Copyright: Ctrip
 */

public class RxJavaUtil implements Publisher {

    public void test() {
        Flowable.just("test").subscribe(new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                System.out.println(s);
            }
        });
    }

    @Override
    public void subscribe(Subscriber s) {

    }
}
