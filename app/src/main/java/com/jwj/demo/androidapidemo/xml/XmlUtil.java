package com.jwj.demo.androidapidemo.xml;

/**
 * Created by jwj on 17/9/19.
 */
public class XmlUtil {

    public static final int DOM_TYPE = 1;
    public static final int SAX_TYPE = 2;

    public static IParser createParser(int type) {
        IParser parser;
        switch (type) {
            case DOM_TYPE:
                parser = new DomParser();
                break;

            case SAX_TYPE:
                parser = new MSaxParser();
                break;
            default:
                parser = new MSaxParser();
                break;
        }
        return parser;
    }
}
