package com.jwj.demo.androidapidemo.storage;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Description: 描述
 * Author: wjxie
 * Date: 2017/9/19
 * Copyright: Ctrip
 */

public class SqliteStorage extends SQLiteOpenHelper {

    public SqliteStorage(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
