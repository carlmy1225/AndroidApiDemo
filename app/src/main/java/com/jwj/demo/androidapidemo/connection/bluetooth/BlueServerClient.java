package com.jwj.demo.androidapidemo.connection.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;

import java.io.IOException;
import java.util.UUID;

/**
 * Description: 描述
 * Author: wjxie
 * Date: 2017/9/27
 * Copyright: Ctrip
 */

public class BlueServerClient extends Thread {
    BluetoothAdapter mAdapter;
    BluetoothServerSocket mServerSocket;
    BlueToothManager manager;

    public BlueServerClient(BluetoothAdapter adapter, BlueToothManager blueToothManager) {
        this.mAdapter = adapter;
        this.manager = blueToothManager;
        try {
            mServerSocket = mAdapter.listenUsingRfcommWithServiceRecord("blue_test", UUID.randomUUID());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public synchronized void start() {
        super.start();
        startServer();
    }

    private void startServer() {
        while (true) {
            try {
                BluetoothSocket mSocket = mServerSocket.accept();
                BlueConnection blueConnection = new BlueConnection(mSocket);
                manager.managerConnection(blueConnection);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                canceServerSocket();
                break;
            }
        }
    }

    private void canceServerSocket() {
        try {
            mServerSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
