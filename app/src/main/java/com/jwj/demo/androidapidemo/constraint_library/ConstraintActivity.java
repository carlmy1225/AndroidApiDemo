package com.jwj.demo.androidapidemo.constraint_library;

import android.os.Bundle;
import android.support.constraint.Barrier;
import android.support.constraint.Group;
import android.support.constraint.Guideline;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.jwj.demo.androidapidemo.R;

public class ConstraintActivity extends AppCompatActivity {

    Group group;
    Barrier barrier;
    Guideline guideline;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_constraint);
        checkMarginGoneAttr();
        checkGroupView();

        group = (Group) findViewById(R.id.button_group);
        barrier = (Barrier) findViewById(R.id.barrier);
        guideline = (Guideline) findViewById(R.id.guide_line);
    }

    private void checkMarginGoneAttr() {
        Button button = (Button) findViewById(R.id.buttonB);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View view = findViewById(R.id.buttonA);
                view.setVisibility(view.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
            }
        });
    }


    private void checkGroupView() {
        final View buttonC = findViewById(R.id.button_c);
        buttonC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                group.setVisibility(group.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
            }
        });
    }
}
