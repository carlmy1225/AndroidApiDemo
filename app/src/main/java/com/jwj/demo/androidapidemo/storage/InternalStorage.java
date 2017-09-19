package com.jwj.demo.androidapidemo.storage;

import android.content.Context;
import android.util.Log;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * 设备的内部存储，为应用程序所私有，跟随应用卸载而删除
 * Author: wjxie
 * Date: 2017/9/19
 * Copyright: Ctrip
 */

public class InternalStorage {

    //

    public InternalStorage(Context context) {
        //一些内部存储空间相关的方法：
        context.getCacheDir();//应用保存临时存储文件的内部目录
        context.getFilesDir(); //取得内部文件在文件系统中保存位置的绝对路径
        //context.getDir(name,mode)//创建（或者打开已存在的）内部存储空间所在的目录
        context.deleteFile("filename"); //删除文件
        context.fileList(); //文件类表
    }


    public void saveFile(Context context, String content) {
        FileOutputStream fos = null;
        try {
            fos = context.openFileOutput("internal_storage", Context.MODE_PRIVATE); //context.
            fos.write(content.getBytes());
            fos.flush();
            fos.close();

        } catch (Exception e) {
            Log.d("Exception_InStorage", e.getMessage());
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void readFile(Context context) {
        try {
            FileInputStream fis = context.openFileInput("internal_storage");
            byte[] bytes = new byte[1024];
            int read = 0;

            while ((read = fis.read(bytes)) != 0) {
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {

        }
    }


}
