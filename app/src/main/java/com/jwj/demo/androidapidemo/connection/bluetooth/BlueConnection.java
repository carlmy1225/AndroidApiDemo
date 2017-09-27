package com.jwj.demo.androidapidemo.connection.bluetooth;

import android.bluetooth.BluetoothSocket;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

/**
 * Description: 描述
 * Author: wjxie
 * Date: 2017/9/27
 * Copyright: Ctrip
 */

public class BlueConnection extends Thread {

    private BluetoothSocket mSocket;

    public BlueConnection(BluetoothSocket socket) {
        this.mSocket = socket;
    }

    public void run() {
        read();
    }


    public void read() {
        while (true) {
            try {
                InputStream is = mSocket.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                String str = reader.readLine();
                while (str != null) {
                    Log.d("blue_connection", str);
                    str = reader.readLine();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public void write(byte[] content) {
        try {
            OutputStream ous = mSocket.getOutputStream();
            ous.write(content);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
