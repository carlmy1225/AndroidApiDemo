package com.jwj.demo.androidapidemo.connection.bluetooth;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothClass;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

/**
 * Description: 描述
 * Author: wjxie
 * Date: 2017/9/27
 * Copyright: Ctrip
 */

public class BlueToothManager {
    private final String TAG = "blue_tooth";
    private final int REQUEST_ENABLE = 2001;

    private String KEY_CONNENCTION = "key_connection";  //维持一个连接
    BlueConnection onlyConnection;


    BluetoothAdapter bluetoothAdapter;
    DeviceFoundCallBack mDeviceFoundCallBack;


    public interface DeviceFoundCallBack {
        void deviceFound(BluetoothDevice mDevice);
    }


    BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (BluetoothDevice.ACTION_FOUND.equals(intent.getAction())) {
                BluetoothDevice mDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                BluetoothClass mClass = intent.getParcelableExtra(BluetoothDevice.EXTRA_CLASS);
            }
        }
    };

    public BlueToothManager() {
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    }


    public void startServer(Activity activity) {
        if (bluetoothAdapter == null) {
            Log.d(TAG, "该设备不支持蓝牙");
            return;
        }

        if (bluetoothAdapter.isEnabled()) {
            Log.d(TAG, "蓝牙可用，即打开了蓝牙设备");
        } else {
            Log.d(TAG, "蓝牙不可用，去打开蓝牙设备");
            Intent i = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            activity.startActivityForResult(i, REQUEST_ENABLE);
            return;
        }

        //开启设备扫描
        bluetoothAdapter.startDiscovery();
        BlueServerClient serverClient = new BlueServerClient(bluetoothAdapter, this);
        serverClient.start();
    }

    public void register(Context context) {
        IntentFilter filter = new IntentFilter();
        filter.addAction(BluetoothDevice.ACTION_FOUND);
        context.registerReceiver(receiver, filter);
    }

    public void unregister(Context context) {
        context.unregisterReceiver(receiver);
    }

    public DeviceFoundCallBack getmDeviceFoundCallBack() {
        return mDeviceFoundCallBack;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_ENABLE) {
            if (resultCode == Activity.RESULT_OK) {
                //蓝牙打开成功
                Log.d(TAG, "蓝牙打开成功");
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Log.d(TAG, "取消打开蓝牙");
            }
        }
    }

    protected void managerConnection(BlueConnection mConnection) {
        this.onlyConnection = mConnection;
    }
}