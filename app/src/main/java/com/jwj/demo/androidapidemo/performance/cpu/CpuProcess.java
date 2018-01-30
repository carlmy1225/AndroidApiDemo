package com.jwj.demo.androidapidemo.performance.cpu;

import android.text.TextUtils;
import android.util.Log;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Description: 描述
 * Author: wjxie
 * Date: 2018/1/16
 * Copyright: Ctrip
 */

public class CpuProcess {
    CpuSnapShot mLastCpuInfo;

    public CpuProcess() {
    }


    public void startUp() {
        mLastCpuInfo = getCpuInfo();
    }


    public CpuSnapShot getCpuInfo() {
        BufferedReader cpuReader = null;
        BufferedReader pidReader = null;
        try {
            cpuReader = new BufferedReader(new InputStreamReader(new FileInputStream("/proc/stat")),
                    1024);
            String lineOne = cpuReader.readLine();

            int pid = android.os.Process.myPid();
            pidReader = new BufferedReader(new InputStreamReader(new FileInputStream("/proc/" + pid + "/stat")), 1024);
            String pidCpuInfo = pidReader.readLine();

            if (TextUtils.isEmpty(lineOne)) {
                return null;
            }
            return parse(lineOne, pidCpuInfo);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (cpuReader != null) {
                    cpuReader.close();
                }
                if (pidReader != null) {
                    pidReader.close();
                }
            } catch (Exception e) {
            }
        }
        return null;
    }

    private CpuSnapShot parse(String cpu, String appCpu) {
        String[] cpuInfo = cpu.split(" ");
        CpuSnapShot shot = new CpuSnapShot();
        shot.user = Long.parseLong(cpuInfo[2]);
        shot.system = Long.parseLong(cpuInfo[4]);
        shot.idle = Long.parseLong(cpuInfo[5]);
        shot.ioWait = Long.parseLong(cpuInfo[6]);
        shot.total = shot.user + Long.parseLong(cpuInfo[3]) + shot.system + shot.idle
                + shot.ioWait + Long.parseLong(cpuInfo[7]) + Long.parseLong(cpuInfo[8])
                + Long.parseLong(cpuInfo[9]) + Long.parseLong(cpuInfo[10]);

        String[] appCpuInfo = appCpu.split(" ");
        shot.app = Long.parseLong(appCpuInfo[13]) + Long.parseLong(appCpuInfo[14]) + Long.parseLong(appCpuInfo[15])
                + Long.parseLong(appCpuInfo[16]);

        Log.d("user", cpuInfo[2]);
        Log.d("nice", cpuInfo[3]);
        Log.d("system", cpuInfo[4]);
        Log.d("idle", cpuInfo[5]);
        Log.d("iowait", cpuInfo[6]);
        Log.d("irq", cpuInfo[7]);
        Log.d("softirq", cpuInfo[8]);
        Log.d("stealstolen", cpuInfo[9]);
        Log.d("guest", cpuInfo[10]);

        return shot;
    }

}
