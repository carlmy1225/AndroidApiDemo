package com.jwj.demo.androidapidemo.custom_view.page;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jwj.demo.androidapidemo.R;

/**
 * Description: 描述
 * Author: wjxie
 * Date: 2017/11/7
 * Copyright: Ctrip
 */

public class PageAdapter extends PagerAdapter {
    String[] tips = new String[]{"PLANE TICKET", "HOTEL RESERVATION", "TRAIN RICKETS"};
    String[] descs = new String[]{"Covering the whole world \n Self return change",
            "Covering the whole world \n Self return change", "Covering the whole world \n Self return change"};
    int count;
    Context context;
    View lastView;


    public PageAdapter(Context context) {
        this.context = context;
        count = tips.length + 1;
    }

    @Override
    public int getCount() {
        return count;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        if (object instanceof View) {
            container.removeView((View) object);
            if (lastView == object) {
                lastView = null;
            }
        }
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        if (position == count - 1) {
            View view = LayoutInflater.from(context).inflate(R.layout.myctrip_welcome_page_last_item, null);
            container.addView(view);
            lastView = view;
            return view;
        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.myctrip_welcome_page_text_item, null);
            TextView tipTv = (TextView) view.findViewById(R.id.tip_tv);
            TextView descTv = (TextView) view.findViewById(R.id.desc_tv);
            tipTv.setText(tips[position]);
            descTv.setText(descs[position]);
            container.addView(view);
            return view;
        }
    }

    public View getLastView() {
        return lastView;
    }
}
