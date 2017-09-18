package com.jwj.demo.androidapidemo.performance;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;

/**
 * 电量
 */

public class Battery {
    IntentFilter filter;

    public Battery() {
        filter = new IntentFilter();
        filter.addAction(Intent.ACTION_BATTERY_CHANGED);
        filter.addAction(Intent.ACTION_BATTERY_LOW);
        filter.addAction(Intent.ACTION_BATTERY_LOW);

    }

    public BatteryInfo getBatteryInfo(Context context) {
        Intent batteryStatus = context.registerReceiver(null, filter);
        BatteryInfo batteryInfo = new BatteryInfo();
        batteryInfo.baty_status = batteryStatus.getIntExtra(BatteryManager.EXTRA_STATUS, 0);
        batteryInfo.baty_level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, 0);
        batteryInfo.baty_scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, 0);
        return batteryInfo;
    }

}
