package com.jwj.demo.androidapidemo.downloader;

import java.lang.ref.WeakReference;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Description: 描述
 * Author: wjxie
 * Date: 2017/11/14
 * Copyright: Ctrip
 */

public class TerminationToken {
    protected volatile boolean toShutdown = false;
    public final AtomicInteger reservations = new AtomicInteger();

    private final Queue<WeakReference<Terminatable>> coordinatedThreads;

    public TerminationToken() {
        coordinatedThreads = new ConcurrentLinkedQueue<>();
    }

    public boolean isToShutdown() {
        return toShutdown;
    }

    protected void setToShutdown(boolean toShutdown){
        this.toShutdown = toShutdown;
    }


    protected  void register(Terminatable thread){
        coordinatedThreads.add(new WeakReference<Terminatable>(thread));
    }

    protected void notifyThreadTermination(Terminatable thread){
        WeakReference<Terminatable> wrThread;
        Terminatable otherThread;
        while(null != (wrThread = coordinatedThreads.poll())){
            otherThread = wrThread.get();
            if(null != otherThread && otherThread != thread){
                otherThread.terminate();
            }
        }
    }


}
