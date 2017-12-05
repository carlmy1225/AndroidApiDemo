package com.jwj.demo.androidapidemo.downloader;

import com.jwj.demo.androidapidemo.util.IoUtil;
import com.jwj.demo.androidapidemo.util.MessageDigestUtil;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * Description: 描述
 * Author: wjxie
 * Date: 2017/11/14
 * Copyright: Ctrip
 */

public class WorkThread extends AbstractTerminatableThread {
    private final BlockingQueue<String> workQueue;
    private final String outputDir;


    public WorkThread(String outputDir) {
        this.workQueue = new ArrayBlockingQueue<>(100);
        this.outputDir = outputDir;
    }


    public void download(String file) {
        try {
            workQueue.put(file);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void doRun() throws Exception {
        InputStream inStream = null;
        ByteArrayOutputStream outStream = null;
        try {
            String downUrl = workQueue.take();
            URL url = new URL(downUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(30 * 1000);
            conn.setRequestMethod("GET");
            inStream = conn.getInputStream();
            if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                outStream = new ByteArrayOutputStream();
                byte[] buffer = new byte[1024];
                int len;
                while ((len = inStream.read(buffer)) != -1) {
                    outStream.write(buffer, 0, len);
                }
                byte[] picByte = outStream.toByteArray();
                savePicture(downUrl, picByte);
            }
        } catch (Throwable e1) {
//            CTLog.e("ibu.home.bg", e1);
//            ExceptionHelper.report("ibu.home.bg", e1);
        } finally {
            IoUtil.closeSilently(outStream);
            IoUtil.closeSilently(inStream);
        }
    }

    public void savePicture(String url, byte[] picByte) throws Exception {
        String fileMd5 = MessageDigestUtil.strToMD5(url);
        FileOutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(outputDir + File.separator + fileMd5);
            outputStream.write(picByte);
            outputStream.flush();
        } finally {
            IoUtil.closeSilently(outputStream);
        }
    }

}
