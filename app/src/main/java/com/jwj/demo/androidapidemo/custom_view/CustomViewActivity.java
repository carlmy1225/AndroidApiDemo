package com.jwj.demo.androidapidemo.custom_view;

import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.jwj.demo.androidapidemo.R;
import com.jwj.demo.androidapidemo.logger.LogUtil;


public class CustomViewActivity extends AppCompatActivity {

    LinearLayout linearLayout;
    LayerBackgroundScrollView scrollView;
    View iconViewLayout;
    ImageView weChatView;
    LayerContentView topBgView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_view);
        linearLayout = (LinearLayout) findViewById(R.id.top_icon_layout);
        scrollView = (LayerBackgroundScrollView) findViewById(R.id.scroll_view);
        iconViewLayout = findViewById(R.id.icon_view_layout);
        weChatView = (ImageView) findViewById(R.id.wechat_icon);
        linearLayout.setVisibility(View.VISIBLE);
        topBgView = (LayerContentView) findViewById(R.id.top_bg_view);

        ViewCompat.setAlpha(linearLayout, 0);


        scrollView.setScrollCallBack(new LayerBackgroundScrollView.ScrollCallBack() {
            @Override
            public void onScroll(int scrollY, int deltaY) {
                Log.d("icon_top", iconViewLayout.getY() - scrollY + "");

                int toPosition = 0;  //需要滚动到的目标y坐标
                if (iconViewLayout.getY() - scrollY >= 0) {
                    float percent = 1 - (iconViewLayout.getY() - scrollY - toPosition) * 1.0f / iconViewLayout.getY();
                    LogUtil.d("percent = %f", percent);

                    if (percent < 0) {
                        percent = 0;
                    } else if (percent > 1) {
                        percent = 1;
                    }
                    ViewCompat.setAlpha(linearLayout, percent);
                    topBgView.secureResetQuad(percent);
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
