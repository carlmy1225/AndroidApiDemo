package com.jwj.demo.androidapidemo.custom_view;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;

import com.jwj.demo.androidapidemo.BaseAct;
import com.jwj.demo.androidapidemo.R;
import com.jwj.demo.androidapidemo.custom_view.adapter.IBUMainAdapter;
import com.jwj.demo.androidapidemo.custom_view.adapter.IBUMainModel;
import com.jwj.demo.androidapidemo.custom_view.animtor.BgImageView;
import com.jwj.demo.androidapidemo.custom_view.animtor.IBUAnimatorUtil;
import com.jwj.demo.androidapidemo.custom_view.animtor.IBURecyclerView;
import com.jwj.demo.androidapidemo.logger.LogUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Description: 描述
 * Author: wjxie
 * Date: 2017/10/9
 * Copyright: Ctrip
 */

public class IBUMainAct extends BaseAct implements ScrollInterceptCallBack {

    IBURecyclerView mRecyclerView;
    Toolbar mToolBar;
    IBUMainAdapter mAdapter;
    //    View iconViewLayout;
    View coverIconView;
    View topContentView;

    int coverTopHeight;
    View wechatIconView;

    IBUAnimatorUtil animatorUtil;
    BgImageView bgView;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ibu_main_act);

        animatorUtil = new IBUAnimatorUtil();

        bgView = (BgImageView) findViewById(R.id.bg_view);
        mRecyclerView = (IBURecyclerView) findViewById(R.id.ibu_recycler_view);
        coverIconView = findViewById(R.id.cover_icon_view);
        topContentView = findViewById(R.id.top_content_view);
        wechatIconView = findViewById(R.id.wechat_icon);

        mAdapter = new IBUMainAdapter(this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mAdapter);

        coverIconView.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                if (coverTopHeight == 0) {
                    coverTopHeight = (int) coverIconView.getY();
                    Log.d("top", coverIconView.getTop() + "");
                }
                coverIconView.getViewTreeObserver().removeOnPreDrawListener(this);
                return false;
            }
        });
        loadData();

        coverIconView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animatorUtil.startAnimal(bgView ,mRecyclerView, coverTopHeight , topContentView);
            }
        });

    }


    @Override
    public void onPreScroll(int scrollY, int deltaY) {
        topContentView.scrollBy(0, (int) (deltaY * 0.8f));
    }






    int total;

    @Override
    public boolean isIntercept() {
        total ++;
        if(total <= 60){
            return true;
        }else{
            return false;
        }


//        if (topContentView.getScrollY() > startHeight) {
//            return false;
//        }
//        return true;
    }

    int startHeight = 100;


    private void scrollFloatView(int scrollY, int deltaY) {
        if (coverTopHeight == 0) {
            return;
        }

        Log.d("recyclerview_scrollY", scrollY + "");

        Log.d("top_content_height", coverTopHeight + "");
        float stopPosition = topContentView.getScrollY();
        Log.d("top_content_y", stopPosition + "");

        float percent = scrollY * 1.0f / coverTopHeight;

        Log.d("percent", percent + "");


        float rate = rateUp(percent);

        Log.d("rate", rate + "");
        Log.d("reateUp * deltaY = ", rate * deltaY + ", deltaY =" + deltaY + ",percent = " + percent);
        //设置icon背景透明度
        //alphaIconView(1 - percent, wechatIconView);
        scrollToView(topContentView, coverTopHeight, 1f, 2f, deltaY, scrollY);
    }

    int deltaY;
    int scrollY;


    public void log(int v, String msg) {
        Log.d("scroll_y_" + v, msg);
    }

    private int navigition = 1;

    public void scrollToView(View topView, int desHeight, float rateUp, float rateDown, int distanceY, int scrollY) {
        float rate = (coverTopHeight - startHeight) * 1f / coverTopHeight;

        Log.d("rate:", rate + "");

        float y = distanceY * rateUp * rate + (1 - rate) * navigition;
        navigition *= -1;

        final int deltaY = Math.round(y);
        final int stopPosition = topView.getScrollY();


        log(3, "topScrollY:" + topView.getScrollY() + ",stopPosition:" + stopPosition);


        if (distanceY > 0) {   //向上滑,滑动指定位置就停止
            if (stopPosition > desHeight) {
                topView.scrollTo(0, desHeight);
            } else if (stopPosition < desHeight) {
                if (stopPosition + deltaY > desHeight) {
                    topView.scrollBy(0, desHeight - stopPosition);
                } else {
                    log(2, "deltaY:" + deltaY + ", distanceY:" + distanceY);

                    topView.scrollBy(0, deltaY);

                    log(1, "topScrollY:" + topView.getScrollY() + ",stopPosition:" + scrollY);
                }
            }
        } else if (distanceY < 0) {
            if (scrollY > desHeight) {  // 445  -5, 442  440
                if (scrollY + deltaY < desHeight) {
                    topView.scrollBy(0, scrollY + deltaY - desHeight);
                } else {
                }
            } else {
                if (stopPosition > startHeight) {
                    if (stopPosition + deltaY > startHeight) {
                        topView.scrollBy(0, deltaY);
                    } else {
                        topView.scrollBy(0, startHeight - stopPosition);
                    }
                } else {
                    topView.scrollTo(0, startHeight);
                }
            }
        }

        scrollY += distanceY;
    }


    public void scrollByView(View topView, int desHeight, float rateUp, float rateDown, int distanceY) {
        float rate = (coverTopHeight - startHeight) * 1f / coverTopHeight;

        Log.d("rate:", rate + "");

        final int deltaY = Math.round(distanceY * 0.5f + 0.5f);
        final int stopPosition = scrollY;


        log(3, "topScrollY:" + topView.getScrollY() + ",stopPosition:" + stopPosition);


        if (distanceY > 0) {   //向上滑,滑动指定位置就停止
            if (stopPosition > desHeight) {
                topView.scrollTo(0, desHeight);
            } else if (stopPosition < desHeight) {
                if (stopPosition + deltaY > desHeight) {
                    topView.scrollBy(0, desHeight - stopPosition);
                } else {
                    log(2, "deltaY:" + deltaY + ", distanceY:" + distanceY);

                    topView.scrollBy(0, deltaY);

                    log(1, "topScrollY:" + topView.getScrollY() + ",stopPosition:" + stopPosition);
                }
            }
        } else {
            if (stopPosition > desHeight) {  // 445  -5, 442  440
                if (stopPosition + deltaY < desHeight) {
                    topView.scrollBy(0, stopPosition + deltaY - desHeight);
                } else {
                }
            } else {
                if (stopPosition > 0) {
                    if (stopPosition + deltaY > 0) {
                        topView.scrollBy(0, deltaY);
                    } else {
                        topView.scrollBy(0, -stopPosition);
                    }
                } else {
                    topView.scrollTo(0, 0);
                }
            }
        }

        scrollY += distanceY;
    }


/*
    1.向上滑动,两次的距离分别为 t1 = 4 ,t2 = 6
    这个是时候f是要大于1  假设f = 1.2f

    recyclerview滑动距离 t1 + t2 = 10
    顶部view滑动距离为  (t1 + t2) * f = 12

    此时向下滑动

 */

//    100
//    前面40不滑动，
//
//    滑动到60之后
//            t1 + t2 + t3 =100
//
//            t1 * f1+ t2 * f2 + t3 * f3=60


    public void alphaIconView(float percent, View view) {
        if (view != null && view.getBackground() != null) {
            int alpha = (int) (percent * 255);
            if (alpha > 255) {
                alpha = 255;
            }
            view.getBackground().setAlpha(alpha);
        }
    }


    /**
     * @param x (0 ~1.0)
     * @return
     */
    public float rateUp(float x) {
        return -2.25f * x * x + 2;
    }


    private float rateDown(float x) {
        if (x < 0.65f) {  //(0~0.65f)
            return 1f; //-1.25f * x * x + 1;
        } else if (x <= 1.0f) {
            return 0;  //-(x - 1) * (x - 1) + 1;
        }
        return 0;
    }


    /**
     * 滚动浮动的view的算法
     *
     * @param scrollY   y方向滚动的距离0坐标的距离
     * @param deltaY    一次滚动的间距
     * @param floatView 浮动的view
     */
    void scrollView(int scrollY, int deltaY, View floatView) {
        if (floatView != null) {
            float iconY = floatView.getY();

            Log.d("icon_y", iconY + "");
            Log.d("icon_top", scrollY + "");
            int toPosition = 0;  //需要滚动到的目标y坐标
            if (iconY - scrollY >= toPosition) {
                float percent = 1 - (iconY - scrollY - toPosition) * 1.0f / iconY;
                LogUtil.d("percent = %f", percent);

                if (percent < 0) {
                    percent = 0;
                } else if (percent > 1) {
                    percent = 1;
                }

                if (percent == 1) {
                } else {
                }
            } else {
                ViewCompat.setAlpha(coverIconView, 1);
//                coverIconView.setVisibility(View.VISIBLE);
            }
            //上滑
            if (deltaY > 0 && (iconY - scrollY <= 0)) {  //由大到小
                Log.d("onScroll", "icon上滑到达顶点");

            } else if (deltaY < 0 && iconY - scrollY >= 0) {  //下滑  比例越来越大
                Log.d("onScroll", "icon下滑到达位置");
            }
        }

    }


    void loadData() {
        new AsyncTask<String, Void, List<IBUMainModel>>() {
            @Override
            protected List<IBUMainModel> doInBackground(String... params) {
                List<IBUMainModel> models = new ArrayList<>();

                IBUMainModel ibuMainModel = new IBUMainModel();
//                ibuMainModel.itemType = IBUMainAdapter.TYPE_TITLE_ITEM;
//                models.add(ibuMainModel);
//
//                ibuMainModel = new IBUMainModel();
//                ibuMainModel.itemType = IBUMainAdapter.TYPE_ICON_ITEM;
//                models.add(ibuMainModel);

                for (int i = 0; i < 9; i++) {
                    ibuMainModel = new IBUMainModel();
                    ibuMainModel.itemType = IBUMainAdapter.TYPE_INFO_ITEM;
                    models.add(ibuMainModel);
                }

                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                return models;
            }

            @Override
            protected void onPostExecute(List<IBUMainModel> ibuMainModels) {
                mAdapter.getModels().addAll(ibuMainModels);
                mAdapter.notifyDataSetChanged();
            }
        }.executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);
    }

}
