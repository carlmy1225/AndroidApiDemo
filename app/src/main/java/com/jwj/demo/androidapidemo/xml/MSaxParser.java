package com.jwj.demo.androidapidemo.xml;

import android.content.Context;
import android.util.Log;

import java.io.InputStream;
import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

/**
 * Description: 描述
 * Author: wjxie
 * Date: 2017/9/19
 * Copyright: Ctrip
 */

public class MSaxParser implements IParser {

    SAXParser saxParser;
    List mList;

    public MSaxParser() {
        try {
            saxParser = SAXParserFactory.newInstance().newSAXParser();
        } catch (Exception e) {
            Log.d("MSaxParser", e.getMessage());
        }
    }

    @Override
    public List parser(Context context, String path) {
        try {
            InputStream in = context.getAssets().open(path);
            saxParser.parse(in, new SaxTypeHandle(City.class));
        } catch (Exception e) {
            Log.d("MSaxParser", e.getMessage());
        }
        return null;
    }


}
