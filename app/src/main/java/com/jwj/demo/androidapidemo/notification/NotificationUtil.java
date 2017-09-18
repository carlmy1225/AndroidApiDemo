package com.jwj.demo.androidapidemo.notification;

import android.app.AppOpsManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import com.jwj.demo.androidapidemo.R;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Description: 描述
 * Author: wjxie
 * Date: 2017/9/7
 * Copyright: Ctrip
 */

public class NotificationUtil {
    private static final String CHECK_OP_NO_THROW = "checkOpNoThrow";
    private static final String OP_POST_NOTIFICATION = "OP_POST_NOTIFICATION";

    public static boolean isNotificationEnabled(Context context) {
        AppOpsManager mAppOps = (AppOpsManager)
                context.getSystemService(Context.APP_OPS_SERVICE);
        ApplicationInfo appInfo = context.getApplicationInfo();
        String pkg = context.getApplicationContext().getPackageName();
        int uid = appInfo.uid;
        Class appOpsClass = null; /* Context.APP_OPS_MANAGER */
        try {
            appOpsClass = Class.forName(AppOpsManager.class.getName());
            Method checkOpNoThrowMethod = appOpsClass.getMethod(CHECK_OP_NO_THROW, Integer.TYPE, Integer.TYPE, String.class);
            Field opPostNotificationValue = appOpsClass.getDeclaredField(OP_POST_NOTIFICATION);
            int value = (int) opPostNotificationValue.get(Integer.class);
            return ((int) checkOpNoThrowMethod.invoke(mAppOps, value, uid, pkg) == AppOpsManager.MODE_ALLOWED);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 注意：api 19+以上才有效
     * 之前的api都是返回true
     *
     * @param context
     * @return
     */
    public static boolean isEnable(Context context) {
//        return NotificationManagerCompat
//                .from(context).
//                        areNotificationsEnabled();
        return true;
    }


    /**
     * 打开无效
     *
     * @param context
     * @param hanlde
     */
    public static void handleNotify(Context context, String hanlde) {
        int currentApiVersion = android.os.Build.VERSION.SDK_INT;
        try {
            Object service = context.getSystemService("statusbar");
            Class<?> statusbarManager = Class
                    .forName("android.app.StatusBarManager");
            Method expand = null;

            if (service != null) {
                if (currentApiVersion <= 16) {
                    expand = statusbarManager.getMethod("expand");
                } else {
                    if (hanlde == "open") {
                        expand = statusbarManager
                                .getMethod("expandNotificationsPanel");
                    } else if (hanlde == "close") {
                        expand = statusbarManager
                                .getMethod("collapsePanels");
                    }
                }
                expand.setAccessible(true);
                expand.invoke(service);
            }
        } catch (Exception e) {
            Log.d("caobin", "error");
        }
    }


    public static void sendNotification(Context context, String notification) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        builder.setTicker("notification test");
        builder.setContentText(notification);
        builder.setContentTitle("通知");
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, new Intent(), PendingIntent.FLAG_ONE_SHOT);
        builder.setContentIntent(pendingIntent);
        builder.setSmallIcon(R.mipmap.ic_launcher);

        NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        nm.notify(0, builder.build());
    }

    public static void turnToNotificationSetting(Context context) {
        Intent intent = new Intent();
        if (android.os.Build.VERSION.SDK_INT > 19) {  //Build.VERSION_CODES.N_MR1
            intent.setAction("android.settings.APP_NOTIFICATION_SETTINGS");
            intent.putExtra("android.provider.extra.APP_PACKAGE", context.getPackageName());
        } else if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            intent.setAction("android.settings.APP_NOTIFICATION_SETTINGS");
            intent.putExtra("app_package", context.getPackageName());
            intent.putExtra("app_uid", context.getApplicationInfo().uid);
        } else {
            intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            intent.addCategory(Intent.CATEGORY_DEFAULT);
            intent.setData(Uri.parse("package:" + context.getPackageName()));
        }
        context.startActivity(intent);
    }

}
