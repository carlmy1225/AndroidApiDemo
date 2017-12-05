package com.jwj.demo.androidapidemo.struct.imageloader;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.FileDescriptor;

/**
 * Description: 描述
 * Author: wjxie
 * Date: 2017/11/16
 * Copyright: Ctrip
 */

public class ImageResize {

    /**
     * 从文件加载图片，并传入指定的图片大小进行等比压缩
     * 为什么用文件描述符的方式，因为如果使用文件方式，前后两次的流操作，会导致位置属性的变化
     *
     * @param file
     * @param reqWith
     * @param reqHeight
     * @return
     */
    public static Bitmap decodeBitmapFromFileDescriptor(FileDescriptor fd, int reqWith, int reqHeight) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFileDescriptor(fd, null, options);
        options.inSampleSize = getSampleSize(options.outWidth, options.outHeight, reqWith, reqHeight);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFileDescriptor(fd, null, options);
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
    private static int getSampleSize(final int srcWidth, final int srcHeight, int reqWidth, int reqHeight) {
        if (reqHeight == 0 || reqWidth == 0) {
            return 1;
        }

        int sampleSize = 1;

        if (srcWidth > reqWidth || srcHeight > reqHeight) {
            final int halfWidth = srcWidth / 2;
            final int halfHeight = srcHeight / 2;

            while (halfWidth / sampleSize > reqWidth &&
                    halfHeight / sampleSize > reqHeight) {
                sampleSize *= 2;
            }
        }

        return sampleSize;
    }
}
