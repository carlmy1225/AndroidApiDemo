package com.jwj.demo.androidapidemo.downloader;

/**
 * Description: 描述
 * Author: wjxie
 * Date: 2017/11/14
 * Copyright: Ctrip
 */

public abstract class AbstractTerminatableThread extends Thread implements Terminatable {

    private final TerminationToken terminationToken;

    public AbstractTerminatableThread() {
        this(new TerminationToken());
    }

    public AbstractTerminatableThread(TerminationToken terminationToken) {
        this.terminationToken = terminationToken;
    }


    @Override
    public void run() {
        Exception ex = null;
        try {
            for (; ; ) {
                if (terminationToken.isToShutdown() && terminationToken.reservations.get() <= 0) {
                    break;
                }
                doRun();
            }
        } catch (Exception e) {
            ex = e;
        } finally {
            try {
                doCleanup(ex);
            } finally {
                terminationToken.notifyThreadTermination(this);
            }
        }
    }

    @Override
    public void interrupt() {
        terminate();
    }

    public void terminate() {
        terminationToken.setToShutdown(true);
        try {
            doTerminate();
        } finally {
            if (terminationToken.reservations.get() <= 0) {
                super.interrupt();
            }
        }
    }

    public void terminate(boolean waitUtilThreadTerminated) {
        terminate();
        if (waitUtilThreadTerminated) {
            try {
                this.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    /**
     * 留给子类实现其线程处理逻辑
     *
     * @throws Exception
     */
    public abstract void doRun() throws Exception;


    /**
     * 留给子类实现，用于实现线程停止后的清理工作
     */
    public void doCleanup(Exception ex) {
        //do nothing
    }

    /**
     * 停止线程
     */
    protected void doTerminate() {
    }
}
