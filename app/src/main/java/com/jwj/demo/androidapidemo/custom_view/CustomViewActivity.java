package com.jwj.demo.androidapidemo.custom_view;

import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.jwj.demo.androidapidemo.R;


public class CustomViewActivity extends AppCompatActivity {

    LinearLayout linearLayout;
    CustomNestedScrollView scrollView;
    View iconViewLayout;
    ImageView weChatView;
    TopBgView topBgView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_view);
        linearLayout = (LinearLayout) findViewById(R.id.top_icon_layout);
        scrollView = (CustomNestedScrollView) findViewById(R.id.scroll_view);
        iconViewLayout = findViewById(R.id.icon_view_layout);
        weChatView = (ImageView) findViewById(R.id.wechat_icon);
        linearLayout.setVisibility(View.VISIBLE);
        topBgView = (TopBgView) findViewById(R.id.top_bg_view);

        ViewCompat.setAlpha(linearLayout, 0);


        scrollView.setScrollCallBack(new CustomNestedScrollView.ScrollCallBack() {
            @Override
            public void onScroll(int scrollY, int deltaY) {
                Log.d("icon_top", iconViewLayout.getY() - scrollY + "");

                if (deltaY > 0 && iconViewLayout.getY() - scrollY >= 0) {
                    ViewCompat.setAlpha(linearLayout, 1 - (iconViewLayout.getY() - scrollY) * 1.0f / iconViewLayout.getY());
                    topBgView.secureResetQuad(1 - (iconViewLayout.getY() - scrollY) * 1.0f / iconViewLayout.getY());
                } else if (deltaY < 0 && iconViewLayout.getY() - scrollY >= 0) {
                    ViewCompat.setAlpha(linearLayout, 1 - (iconViewLayout.getY() - scrollY) * 1.0f / iconViewLayout.getY());
                    topBgView.secureResetQuad(1 - (iconViewLayout.getY() - scrollY) * 1.0f / iconViewLayout.getY());
                }

                //上滑
                if (deltaY > 0 && (iconViewLayout.getY() - scrollY <= 0)) {  //由大到小
                    Log.d("onScroll", "icon上滑到达顶点");

                } else if (deltaY < 0 && iconViewLayout.getY() - scrollY >= 0) {  //下滑  比例越来越大
                    Log.d("onScroll", "icon下滑到达位置");
                }
            }
        });
    }

}
