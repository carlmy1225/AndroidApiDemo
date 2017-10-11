package com.jwj.demo.androidapidemo.custom_view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;

import com.jwj.demo.androidapidemo.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Description: 描述
 * Author: wjxie
 * Date: 2017/10/9
 * Copyright: Ctrip
 */

public class IBUMainAdapter extends RecyclerView.Adapter {
    public static final int TYPE_TITLE_ITEM = 1;
    public static final int TYPE_ICON_ITEM = 2;
    public static final int TYPE_TRIP_TIME = 3;
    public static final int TYPE_AD_ITEM = 4;
    public static final int TYPE_INFO_ITEM = 5;

    List<IBUMainModel> models = new ArrayList<>();
    Context mContext;

    View iconView;
    public float iconViewY;


    public IBUMainAdapter(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder mViewHolder = null;
        View mView = null;

        switch (viewType) {
            case TYPE_TITLE_ITEM:
                mView = LayoutInflater.from(mContext).inflate(R.layout.ibu_main_item_type_title_view, parent, false);
                mViewHolder = new RecyclerView.ViewHolder(mView) {
                };
                break;
            case TYPE_ICON_ITEM:
                mView = LayoutInflater.from(mContext).inflate(R.layout.ibu_main_item_type_icon_view, parent, false);
                iconView = mView;

                if (iconViewY == 0) {
                    iconView.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                        @Override
                        public boolean onPreDraw() {
                            if (iconViewY == 0) {
                                iconViewY = iconView.getY();
                            }
                            iconView.getViewTreeObserver().removeOnPreDrawListener(this);
                            return false;
                        }
                    });
                }
                mViewHolder = new IconViewHolder(mView);
                break;

            case TYPE_TRIP_TIME:
                break;

            case TYPE_AD_ITEM:
                break;

            case TYPE_INFO_ITEM:
                ImageView iv = new ImageView(mContext);
                RecyclerView.LayoutParams params = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                params.bottomMargin = 50;
                params.leftMargin = 25;
                params.rightMargin = 25;
                iv.setLayoutParams(params);
                iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
                iv.setImageResource(R.mipmap.temp2);
                mViewHolder = new RecyclerView.ViewHolder(iv) {
                };
                break;
        }
        return mViewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemViewType(int position) {
        IBUMainModel mainModel = models.get(position);
        return mainModel.itemType;
    }

    public List<IBUMainModel> getModels() {
        return models;
    }

    @Override
    public int getItemCount() {
        return models.size();
    }

    public View getIconView() {
        return iconView;
    }

    public static class IconViewHolder extends RecyclerView.ViewHolder {
        ImageView trainIcon, flightIcon, hotelIcon;

        public IconViewHolder(View itemView) {
            super(itemView);
            trainIcon = (ImageView) itemView.findViewById(R.id.wechat_icon);
        }
    }


}
