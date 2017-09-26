package com.jwj.demo.androidapidemo.storage.memory;

import java.lang.ref.Reference;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

/**
 * Description: 描述
 * Author: wjxie
 * Date: 2017/9/20
 * Copyright: Ctrip
 */

public abstract class BaseMemoryCache<K, V> implements IMemoryCache<K, V> {

    private Map<K, Reference<V>> softMap = Collections.synchronizedMap(new HashMap<K, Reference<V>>());

    @Override
    public void put(K key, V value) {
        softMap.put(key, createReference(value));
    }

    @Override
    public V get(K key) {
        V result = null;
        Reference<V> reference = softMap.get(key);
        if (reference != null) {
            result = reference.get();
        }
        return result;
    }

    @Override
    public Collection<K> keys() {
        synchronized (softMap) {
            return new HashSet<K>(softMap.keySet());
        }
    }

    @Override
    public void remove(K key) {
        softMap.remove(key);
    }

    @Override
    public void clear() {
        softMap.clear();
    }

    protected abstract Reference<V> createReference(V value);
}
