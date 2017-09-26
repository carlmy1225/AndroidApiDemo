package com.jwj.demo.androidapidemo.storage.memory;

import com.jwj.demo.androidapidemo.logger.LogUtil;

import java.lang.ref.Reference;
import java.lang.ref.SoftReference;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Description: 描述
 * Author: wjxie
 * Date: 2017/9/20
 * Copyright: Ctrip
 */

public abstract class CategoryMemoryCache<K, V> extends BaseMemoryCache<K, V> {
    private int MAX_NORMAL_CACHE_SIZE_IN_MB = 16;
    private int MAX_NORMAL_CACHE_SIZE = MAX_NORMAL_CACHE_SIZE_IN_MB * 1024 * 1024;

    private final int limitSize;
    private AtomicInteger cacheSize;

    //强引用存储
    private List<V> hardCache = Collections.synchronizedList(new LinkedList<V>());

    public CategoryMemoryCache(int limitSize) {
        this.limitSize = limitSize;
        cacheSize = new AtomicInteger();
        if (limitSize > MAX_NORMAL_CACHE_SIZE) {
            LogUtil.w("You set too large memory cache size (more than %1$d Mb)", String.valueOf(MAX_NORMAL_CACHE_SIZE_IN_MB));
        }
    }


    @Override
    public void put(K key, V value) {
        int valueSize = getValueSize(value);
        int realCacheSize = cacheSize.get();

        if (valueSize < limitSize) {
            while (valueSize + realCacheSize > limitSize) {
                V removeNext = removeNext();
                if (hardCache.remove(removeNext)) {
                    cacheSize.addAndGet(-getValueSize(removeNext));
                }

            }

        }

        while (valueSize + realCacheSize > limitSize) {

        }


        super.put(key, value);
    }

    @Override
    public V get(K key) {
        return super.get(key);
    }

    @Override
    public Collection<K> keys() {
        return super.keys();
    }

    @Override
    public void remove(K key) {
        super.remove(key);
    }

    @Override
    public void clear() {
        super.clear();
    }

    @Override
    protected Reference<V> createReference(V value) {
        return new SoftReference<V>(value);
    }

    public abstract int getValueSize(V value);

    protected abstract V removeNext();  //主要用来做删除的策略的

}