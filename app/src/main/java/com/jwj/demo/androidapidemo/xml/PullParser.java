package com.jwj.demo.androidapidemo.xml;

import android.content.Context;
import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.util.List;

/**
 * Description: 描述
 * Author: wjxie
 * Date: 2017/9/19
 * Copyright: Ctrip
 */

public class PullParser implements IParser {
    XmlPullParser mParser;

    public PullParser() {
        try {
            mParser = XmlPullParserFactory.newInstance().newPullParser();
        } catch (Exception e) {
            Log.d("PullParser", e.getMessage());
        }
    }

    @Override
    public List parser(Context context, String path) {
        try {
            InputStream in = context.getAssets().open(path);
            mParser.setInput(in, "utf-8");
            int eventType = mParser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                if (eventType == XmlPullParser.START_DOCUMENT) {

                } else if (eventType == XmlPullParser.START_TAG) {

                } else if (eventType == XmlPullParser.END_TAG) {

                } else if (eventType == XmlPullParser.TEXT) {

                }
                mParser.next();
            }
        } catch (Exception e) {
            Log.d("PullParser", e.getMessage());
        }

        return null;
    }
}
