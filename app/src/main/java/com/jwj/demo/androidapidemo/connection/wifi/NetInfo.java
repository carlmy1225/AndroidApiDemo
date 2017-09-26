package com.jwj.demo.androidapidemo.connection.wifi;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * 网络的连接状态
 * Author: wjxie
 * Date: 2017/9/20
 * Copyright: Ctrip
 */

public class NetInfo {

    ConnectivityManager manager;

    public NetInfo(Context context) {
        manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
    }

    /**
     * 1. 网络是否连接
     */
    public boolean isConnected() {
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        return networkInfo.isConnected();
    }

    /**
     * 2.判断网络的连接类型  wifi
     *
     * @return
     */
    public boolean isWifi() {
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        int type = networkInfo.getType();
        return type == ConnectivityManager.TYPE_WIFI;
    }

    /**
     * 3.监听网络的变化
     */
    public void observerNetChange(Context context) {
        BroadcastReceiver receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (isConnected()) {
                    //do something
                }
            }
        };
        String action = ConnectivityManager.CONNECTIVITY_ACTION;
        IntentFilter filter = new IntentFilter(action);
        context.registerReceiver(receiver, filter);
    }

}
