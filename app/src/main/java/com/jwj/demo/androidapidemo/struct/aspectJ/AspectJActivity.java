package com.jwj.demo.androidapidemo.struct.aspectJ;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.jwj.demo.androidapidemo.R;

public class AspectJActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aspect_j);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        findViewById(R.id.zan).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                testZan();
            }
        });

        findViewById(R.id.send).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendTopic();
            }
        });

        findViewById(R.id.delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteTopic();
            }
        });
    }


    @PermissionManager(role = Role.NORMAL, value="zan")
    private void testZan(){
        Toast.makeText(this,"已经赞了",Toast.LENGTH_SHORT)
                .show();

    }

    @PermissionManager(role = Role.NORMAL, value="send")
    private void sendTopic(){
        Toast.makeText(this,"发帖了",Toast.LENGTH_SHORT)
                .show();
    }

    @PermissionManager(role = Role.MANAGER, value="delete")
    private void deleteTopic(){
        Toast.makeText(this,"删除帖子",Toast.LENGTH_SHORT)
                .show();
    }

}
