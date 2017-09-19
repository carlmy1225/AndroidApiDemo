package com.jwj.demo.androidapidemo.xml;

import android.content.Context;
import android.util.Log;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

/**
 * Created by jwj on 17/9/19.
 */
public class DomParser implements IParser {

    DocumentBuilderFactory mFactory;

    public DomParser() {
        mFactory = DocumentBuilderFactory.newInstance();
    }


    @Override
    public List parser(Context context, String path) {
        return parserAssetFile(context, path);
    }

    private ArrayList parserAssetFile(Context context, String path) {
        DocumentBuilder mBuilder;
        ArrayList<City> mList = new ArrayList();
        try {
            mBuilder = mFactory.newDocumentBuilder();
            InputStream in = context.getAssets().open(path);
            Document document = mBuilder.parse(in);
            Element element = document.getDocumentElement();
            NodeList nodeList = element.getElementsByTagName("city");

            if (nodeList != null && nodeList.getLength() > 0) {
                for (int i = 0; i < nodeList.getLength(); i++) {
                    City city = new City();
                    Element cityElement = (Element) nodeList.item(i);
                    city.setId(Integer.parseInt(cityElement.getAttribute("id")));

                    Element tag = (Element) cityElement.getElementsByTagName("name").item(0);
                    city.setName(tag.getFirstChild().getNodeValue());

                    Element code = (Element) cityElement.getElementsByTagName("code").item(0);
                    city.setName(code.getFirstChild().getNodeValue());

                    mList.add(city);
                }
            }
        } catch (ParserConfigurationException e) {
            Log.d("DomParser", e.getMessage());
        } catch (SAXException e) {
            Log.d("DomParser", e.getMessage());
        } catch (IOException e) {
            Log.d("DomParser", e.getMessage());
        }
        return mList;
    }

}
