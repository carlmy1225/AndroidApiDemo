package com.jwj.demo.androidapidemo.downloader;

/**
 * Description: 描述
 * Author: wjxie
 * Date: 2017/11/14
 * Copyright: Ctrip
 */

public class MessageFileDownloader implements Serializer {
    private WorkThread workThread;

    public MessageFileDownloader(String outputDir) {
        workThread = new WorkThread(outputDir);
    }


    @Override
    public void init() {
        workThread.start();
    }

    @Override
    public void service() {

    }

    @Override
    public void shutdown() {
        workThread.terminate();
    }
}
