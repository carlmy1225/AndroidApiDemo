package com.jwj.demo.androidapidemo.storage;

import android.content.Context;
import android.os.Environment;

import java.io.File;
import java.io.IOException;

/**
 * 外部存储  app应用之间共享，所以有些应用对这些数据进行了加密
 * 1.sdk或者内部存储器
 * Author: wjxie
 * Date: 2017/9/19
 * Copyright: Ctrip
 */

public class ExternalStorage {

    boolean mExternalStorageEnable = false;     //是否可访问
    boolean mExternalStorageWritable = false;   //是否可写


    public boolean checkExternalStateEnable(Context context) {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {          //外部设备可以进行读写
            mExternalStorageEnable = mExternalStorageWritable = true;
        } else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {     //只能进行读
            mExternalStorageEnable = true;
            mExternalStorageWritable = false;
        } else {        //外部存储设别不可访问
            mExternalStorageEnable = mExternalStorageWritable = false;
        }
        return mExternalStorageEnable;
    }

    public static void save(Context context, String content) {
        String path = Environment.getExternalStorageDirectory().getAbsolutePath();  //外部存储目录
        File file = new File(path + "/androidApiDemo/test.txt");
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * @param context
     * @param str
     */

    //保存在系统归纳的一些共享的目录中
    public static void savePublicDirs(Context context, String str) {
        File dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);  //图片的目录中
        if (dir != null && dir.isDirectory()) {
            //保存文件到该目录中
        }
    }


    /**
     * 创建于应用相关的缓存目录，应用卸载后会被删除
     */
    public static void saveCacheFile(Context context) {
        context.getExternalCacheDir();  //缓存的目录，与应用相关，所以与context关联的
    }

}
