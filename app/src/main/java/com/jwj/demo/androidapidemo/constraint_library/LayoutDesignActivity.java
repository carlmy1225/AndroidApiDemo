package com.jwj.demo.androidapidemo.constraint_library;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;

import com.jwj.demo.androidapidemo.R;
import com.jwj.demo.androidapidemo.logger.LogUtil;

public class LayoutDesignActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_layout_design);
        textInputLayout();
    }


    public void textInputLayout() {
        final TextInputLayout layout = (TextInputLayout) findViewById(R.id.text_input_layout);
        layout.setErrorEnabled(true);

        TextInputEditText editText = (TextInputEditText) findViewById(R.id.edit_text);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(s) && s.length() < 6) {
                    layout.setError("用户名称长度至少为6");
                } else if (TextUtils.isEmpty(s)) {
                    layout.setError("用户名称为空");
                } else {
                    layout.setError("");
                    layout.setPasswordVisibilityToggleContentDescription("闪乱神乐搜索");
                }
            }
        });

    }


    public void showSnack(View view) {
        Snackbar snackbar = Snackbar.make(findViewById(R.id.click_btn), "你确定吗?", Snackbar.LENGTH_SHORT)
                .setCallback(new Snackbar.Callback() {
                    @Override
                    public void onDismissed(Snackbar snackbar, int event) {
                        super.onDismissed(snackbar, event);
                        LogUtil.d("snack invoke method %s", "onDismissed");
                    }

                    @Override
                    public void onShown(Snackbar snackbar) {
                        super.onShown(snackbar);
                        LogUtil.d("snack invoke method %s", "onShown");
                    }
                });
        snackbar.setAction("确定", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogUtil.d("snack click method %s", "onClick");
            }
        });
        snackbar.show();
    }
}
