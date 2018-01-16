package com.jwj.demo.androidapidemo;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.jwj.demo.androidapidemo.performance.cpu.CpuProcess;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends ListActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        List<ActivityInfo> list = getAllRunningActivities(this);
        setListAdapter(new ArrayAdapter<ActivityInfo>(this, android.R.layout.simple_expandable_list_item_1, list) {
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                convertView = LayoutInflater.from(getContext()).inflate(android.R.layout.simple_expandable_list_item_1, null);
                TextView textView = (TextView) convertView;
                textView.setGravity(Gravity.LEFT);
                textView.setPadding(30, 30, 30, 30);

                final ActivityInfo info = getItem(position);
                textView.setText(info.name.substring(info.name.lastIndexOf(".") + 1));
                textView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent i = new Intent();
                        i.setClassName(info.packageName, info.name);
                        i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        startActivity(i);
                    }
                });
                return convertView;
            }
        });

        CpuProcess.getCpuInfo();
    }


    public static ArrayList<ActivityInfo> getAllRunningActivities(Context context) {
        try {
            PackageInfo pi = context.getPackageManager().getPackageInfo(
                    context.getPackageName(), PackageManager.GET_ACTIVITIES);
            return new ArrayList<>(Arrays.asList(pi.activities));
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }
}
