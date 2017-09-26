package com.jwj.demo.androidapidemo.storage.memory;

import java.util.Collection;

/**
 * 内存缓存契约接口
 * Author: wjxie
 * Date: 2017/9/20
 * Copyright: Ctrip
 */

public interface IMemoryCache<K, V> {
    void put(K key, V value);

    V get(K key);

    void remove(K key);

    Collection keys();

    void clear();
}
