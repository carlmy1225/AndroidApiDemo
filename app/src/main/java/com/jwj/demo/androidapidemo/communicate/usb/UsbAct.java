package com.jwj.demo.androidapidemo.communicate.usb;

import android.os.Bundle;
import android.view.View;

import com.jwj.demo.androidapidemo.BaseAct;
import com.jwj.demo.androidapidemo.R;

public class UsbAct extends BaseAct {

    USBUtil usbUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usb);
        usbUtil = new USBUtil(this, getIntent());

        findViewById(R.id.usb_open).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                usbUtil.start(v.getContext());
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        usbUtil.register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        usbUtil.unregister(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        usbUtil.closeAccessory();
    }
}
