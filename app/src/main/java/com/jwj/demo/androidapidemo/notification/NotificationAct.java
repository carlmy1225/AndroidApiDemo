package com.jwj.demo.androidapidemo.notification;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.jwj.demo.androidapidemo.BaseAct;
import com.jwj.demo.androidapidemo.R;

/**
 * Description: 描述
 * Author: wjxie
 * Date: 2017/9/7
 * Copyright: Ctrip
 */

public class NotificationAct extends BaseAct implements View.OnClickListener {

    EditText editText;
    Button sendBtn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_notification_test_layout);
        findViewById(R.id.push_state_btn).setOnClickListener(this);
        findViewById(R.id.send_notification_btn).setOnClickListener(this);
        findViewById(R.id.open_btn).setOnClickListener(this);
        findViewById(R.id.close_btn).setOnClickListener(this);

        editText = (EditText) findViewById(R.id.notification_et);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.push_state_btn) {
            if (!NotificationUtil.isEnable(this)) {
                NotificationUtil.turnToNotificationSetting(this);
            }
            String result = String.valueOf(NotificationUtil.isEnable(v.getContext()));
            Log.d("notification", result);
            Toast.makeText(v.getContext(), result
                    , Toast.LENGTH_LONG).show();
        } else if (v.getId() == R.id.send_notification_btn) {
            String noti = editText.getText().toString();
            if (!TextUtils.isEmpty(noti)) {
                NotificationUtil.sendNotification(this, noti);
            }
        } else if (v.getId() == R.id.open_btn) {
            NotificationUtil.handleNotify(this, "open");
        } else if (v.getId() == R.id.close_btn) {
            NotificationUtil.handleNotify(this, "close");
        }
    }
}
