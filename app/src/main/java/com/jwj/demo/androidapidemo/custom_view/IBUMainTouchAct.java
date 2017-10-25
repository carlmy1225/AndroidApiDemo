package com.jwj.demo.androidapidemo.custom_view;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.jwj.demo.androidapidemo.BaseAct;
import com.jwj.demo.androidapidemo.R;
import com.jwj.demo.androidapidemo.custom_view.adapter.IBUMainAdapter;
import com.jwj.demo.androidapidemo.custom_view.adapter.IBUMainModel;
import com.jwj.demo.androidapidemo.custom_view.refresh.SmartRefreshLayout;
import com.jwj.demo.androidapidemo.custom_view.refresh.api.RefreshLayout;
import com.jwj.demo.androidapidemo.custom_view.refresh.listener.OnRefreshListener;
import com.jwj.demo.androidapidemo.custom_view.touch.IBUTouchBgView;
import com.jwj.demo.androidapidemo.custom_view.touch.IBUTouchRecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jwj on 17/10/13.
 */
public class IBUMainTouchAct extends BaseAct {

    IBUTouchRecyclerView mRecyclerView;
    IBUMainAdapter mAdapter;
    View coverIconView;
    View topContentView;
    View wechatIconView;
    IBUTouchBgView bgView;
    SmartRefreshLayout refreshLayout;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ibu_touch_act);

        bgView = (IBUTouchBgView) findViewById(R.id.myctrip_touch_bg_view);
        mRecyclerView = (IBUTouchRecyclerView) findViewById(R.id.ibu_recycler_view);
        coverIconView = findViewById(R.id.cover_icon_view);
        topContentView = findViewById(R.id.top_content_view);
        wechatIconView = findViewById(R.id.wechat_icon);
        mAdapter = new IBUMainAdapter(this);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLinearLayoutManager();
        refreshLayout = (SmartRefreshLayout) findViewById(R.id.refresh_layout);

        bgView.showImage(getResources().getDrawable(R.mipmap.myctrip_bg_home), false);

        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {

            }
        });


        loadData();
//        bgView.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                bgView.showImage(getResources().getDrawable(R.mipmap.temp1), true);
//            }
//        }, 2000);
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

                for (int i = 0; i < 8; i++) {
                    ibuMainModel = new IBUMainModel();
                    ibuMainModel.itemType = IBUMainAdapter.TYPE_INFO_ITEM;
                    models.add(ibuMainModel);
                }

                try {
                    Thread.sleep(200);
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

        loadDelay();
    }


    public void loadDelay() {

        refreshLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                IBUMainModel ibuMainModel = new IBUMainModel();
                ibuMainModel.itemType = IBUMainAdapter.TYPE_INFO_ITEM;
                mAdapter.getModels().add(ibuMainModel);
                mAdapter.notifyDataSetChanged();
            }
        }, 4000);
    }


}
