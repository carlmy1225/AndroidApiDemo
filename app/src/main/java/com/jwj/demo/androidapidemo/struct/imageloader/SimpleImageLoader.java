package com.jwj.demo.androidapidemo.struct.imageloader;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.LruCache;
import android.widget.ImageView;

import java.io.File;

import okhttp3.internal.cache.DiskLruCache;

/**
 * Description: 描述
 * Author: wjxie
 * Date: 2017/11/16
 * Copyright: Ctrip
 */

public class SimpleImageLoader {
    public final int VERSION = 20171116;

    private LruCache<String, Bitmap> lruCache;
    private DiskLruCache diskLruCache;

    public SimpleImageLoader(Context context) {
        int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        int cacheSize = maxMemory / 8;
        lruCache = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getRowBytes() * value.getHeight() / 1024;
            }
        };

        File cacheDir = new File(context.getExternalCacheDir().getAbsolutePath() + File.separator + "bitmap");
//        diskLruCache = DiskLruCache.create(FileSystem.SYSTEM, cacheDir, VERSION, )
    }

    public void displayImageView(ImageView iv, String url) {

    }

    public void init() {
    }
}
