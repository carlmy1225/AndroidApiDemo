package com.jwj.demo.androidapidemo.layout;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.jwj.demo.androidapidemo.R;

public class DrawerLayoutActivity extends AppCompatActivity {
    CustomDrawerLayout drawerLayout;
    DrawerSlideBar slideBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer_layout);
        drawerLayout = (CustomDrawerLayout) findViewById(R.id.drawer_layout);
        slideBar = (DrawerSlideBar) findViewById(R.id.slide_bar);
    }
}
