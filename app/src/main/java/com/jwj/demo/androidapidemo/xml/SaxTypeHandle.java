package com.jwj.demo.androidapidemo.xml;

import android.util.Log;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * Description: 描述
 * Author: wjxie
 * Date: 2017/9/19
 * Copyright: Ctrip
 */

public class SaxTypeHandle extends DefaultHandler {

    List mList;
    City city;
    String tagName;
    Class typeClas;

    public SaxTypeHandle(Class typeClas) {
        this.typeClas = typeClas;
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        try {
            String className = typeClas.getSimpleName();
            if (localName.equals(className.toLowerCase())) {
                city = new City();
                city.setId(Integer.parseInt(attributes.getValue("id")));
            }
            tagName = localName;
        } catch (Exception e) {
            Log.d("SaxTypeHandle", e.getMessage());
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        if (city != null) {
            if ("name".equals(tagName)) {
                city.setName(String.valueOf(ch, start, length));
            } else if ("code".equals(tagName)) {
                city.setCode(String.valueOf(ch, start, length));
            }
        }
        super.characters(ch, start, length);
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if (city != null) {
            if ("city".equals(localName)) {
                mList.add(city);
            }
        }
    }

    @Override
    public void startDocument() throws SAXException {
        mList = new ArrayList();
    }

    @Override
    public void endDocument() throws SAXException {
        super.endDocument();
    }

}
