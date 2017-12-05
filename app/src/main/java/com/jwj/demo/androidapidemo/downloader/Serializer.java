package com.jwj.demo.androidapidemo.downloader;

/**
 * Description: 描述
 * Author: wjxie
 * Date: 2017/11/14
 * Copyright: Ctrip
 */

public interface Serializer {
    void init();

    void service();

    void shutdown();
}
