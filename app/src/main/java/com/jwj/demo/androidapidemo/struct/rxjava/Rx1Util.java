package com.jwj.demo.androidapidemo.struct.rxjava;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;

/**
 * Description: 描述
 * Author: wjxie
 * Date: 2017/12/27
 * Copyright: Ctrip
 */

public class Rx1Util {

    public void test() {
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> e) throws Exception {
            }
        });
    }

}
