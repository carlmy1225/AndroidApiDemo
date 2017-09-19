package com.jwj.demo.androidapidemo.storage;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * 键值对的形式，存储一些简单的数据
 * Author: wjxie
 * Date: 2017/9/19
 * Copyright: Ctrip
 */

public class SharedPreferenceStorage {
    private String pref_name = "shared_pref_storage";
    SharedPreferences sharedPreferences;

    public SharedPreferenceStorage(Context context) {
        sharedPreferences = context.getSharedPreferences(pref_name, Context.MODE_PRIVATE);
    }

    public void putBoolean(Context context, String key, boolean value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }
}
