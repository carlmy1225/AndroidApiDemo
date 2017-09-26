package com.jwj.demo.androidapidemo.connection.usb;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbAccessory;
import android.hardware.usb.UsbManager;
import android.os.ParcelFileDescriptor;
import android.util.Log;

import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Description: 描述
 * Author: wjxie
 * Date: 2017/9/12
 * Copyright: Ctrip
 */

public class USBUtil {
    public final String TAG = USBUtil.class.getSimpleName();

    private static final String ACTION_USB_PERMISSION =
            "com.android.example.USB_PERMISSION";

    UsbManager usbManager;
    UsbAccessory usbAccessory;
    ParcelFileDescriptor mFileDescriptor;
    FileOutputStream mOutputStream;
    FileInputStream mInputStream;

    private BroadcastReceiver mUsbReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (ACTION_USB_PERMISSION.equals(action)) {
                synchronized (this) {
                    usbAccessory = (UsbAccessory) intent.getParcelableExtra(UsbManager.EXTRA_ACCESSORY);
                    UsbAccessory[] accessoryList = usbManager.getAccessoryList();
                    if (intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false)) {
                        if (usbAccessory != null) {
                            //call method to set up accessory communication
                            openAccessory();
                        }
                    } else {
                        Log.d(TAG, "permission denied for accessory " + usbAccessory);
                    }
                }
            } else if (UsbManager.ACTION_USB_DEVICE_DETACHED.equals(action)) {
                UsbAccessory accessory = (UsbAccessory) intent.getParcelableExtra(UsbManager.EXTRA_ACCESSORY);
                if (accessory != null) {
                    // call your method that cleans up and closes communication with the accessory
                    closeAccessory();
                }
            }
        }
    };


    public USBUtil(Context context, Intent intent) {
        usbManager = (UsbManager) context.getSystemService(Context.USB_SERVICE);
        usbAccessory = intent.getParcelableExtra(UsbManager.EXTRA_ACCESSORY);
        UsbAccessory[] accessoryList = usbManager.getAccessoryList();
    }


    public void start(Context context) {
        requestPermission(context);
    }

    public void register(Context context) {
        IntentFilter filter = new IntentFilter(ACTION_USB_PERMISSION);
        context.registerReceiver(mUsbReceiver, filter);
    }

    public void unregister(Context context) {
        context.unregisterReceiver(mUsbReceiver);
    }


    private void requestPermission(Context context) {
        PendingIntent mPermissionIntent = PendingIntent.getBroadcast(context, 0, new Intent(ACTION_USB_PERMISSION), 0);
        usbManager.requestPermission(usbAccessory, mPermissionIntent);
    }


    private void openAccessory() {
        mFileDescriptor = usbManager.openAccessory(usbAccessory);
        if (mFileDescriptor != null) {
            FileDescriptor fd = mFileDescriptor.getFileDescriptor();
            mOutputStream = new FileOutputStream(fd);
            mInputStream = new FileInputStream(fd);
        }
    }

    public void closeAccessory() {
        try {
            if (mOutputStream != null) {
                mOutputStream.close();
            }
        } catch (IOException e) {
        }


        try {
            if (mInputStream != null) {
                mInputStream.close();
            }
        } catch (IOException e) {

        }

        try {
            if (mFileDescriptor != null) {
                mFileDescriptor.close();
            }
        } catch (IOException e) {

        }
    }

}
