package com.jwj.demo.androidapidemo.custom_view;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.jwj.demo.androidapidemo.BaseAct;
import com.jwj.demo.androidapidemo.R;
import com.jwj.demo.androidapidemo.custom_view.adapter.IBUMainAdapter;
import com.jwj.demo.androidapidemo.custom_view.adapter.IBUMainModel;
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


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ibu_touch_act);

        bgView = (IBUTouchBgView) findViewById(R.id.bg_view);
        mRecyclerView = (IBUTouchRecyclerView) findViewById(R.id.ibu_recycler_view);
        coverIconView = findViewById(R.id.cover_icon_view);
        topContentView = findViewById(R.id.top_content_view);
        wechatIconView = findViewById(R.id.wechat_icon);
        mAdapter = new IBUMainAdapter(this);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLinearLayoutManager();
        loadData();
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

                for (int i = 0; i < 2; i++) {
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
