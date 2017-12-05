package com.jwj.demo.androidapidemo.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * Description: 描述
 * Author: wjxie
 * Date: 2017/11/16
 * Copyright: Ctrip
 */

public class BitmapUtil {

    /**
     * 从文件加载图片，并传入指定的图片大小进行等比压缩
     *
     * @param file
     * @param reqWith
     * @param reqHeight
     * @return
     */
    public static Bitmap decodeBitmapFromFile(String file, int reqWith, int reqHeight) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(file, options);
        options.inSampleSize = getSampleSize(options.outWidth, options.outHeight, reqWith, reqHeight);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(file, options);
    }


    public static Bitmap decodeBitmapFromFile(Context context, int resId, int reqWith, int reqHeight) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(context.getResources(), resId, options);
        options.inSampleSize = getSampleSize(options.outWidth, options.outHeight, reqWith, reqHeight);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(context.getResources(), resId, options);
    }


    /**
     * 获取压缩的比例
     *
     * @param srcWidth
     * @param srcHeight
     * @param reqWidth
     * @param reqHeight
     * @return
     */
    private static int getSampleSize(int srcWidth, int srcHeight, int reqWidth, int reqHeight) {
        int sampleSize = 1;
        int halfWidth = srcWidth / 2;
        int halfHeight = srcHeight / 2;
        while (halfWidth / sampleSize > reqWidth &&
                halfHeight / sampleSize > reqHeight) {
            sampleSize *= 2;
        }
        return sampleSize;
    }
}
