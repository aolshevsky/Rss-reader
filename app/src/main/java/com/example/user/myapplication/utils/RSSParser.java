package com.example.user.myapplication.utils;

import android.util.Log;

import com.example.user.myapplication.model.RSSFeed;
import com.example.user.myapplication.model.RSSItem;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class RSSParser {

    private static String TAG_CHANNEL = "channel";
    private static String TAG_TITLE = "title";
    private static String TAG_LINK = "link";
    private static String TAG_DESRIPTION = "description";
    private static String TAG_IMAGE = "enclosure";
    private static String TAG_LANGUAGE = "language";
    private static String TAG_ITEM = "item";
    private static String TAG_PUB_DATE = "pubDate";

    private static RSSParser instance = new RSSParser();


    public static RSSParser getInstance(){
        return instance;
    }

    private RSSParser() {}


    public RSSFeed getRSSFeed(String url) {
        RSSFeed rssFeed = null;

        Document doc = getDoc(url);
        if (doc != null) {
            try {
                NodeList nodeList = doc.getElementsByTagName(TAG_CHANNEL);
                Element e = (Element) nodeList.item(0);

                String title = getValue(e, TAG_TITLE);
                String link = getValue(e, TAG_LINK);
                String description = getValue(e, TAG_DESRIPTION);
                String language = getValue(e, TAG_LANGUAGE);

                rssFeed = new RSSFeed(title, description, link, url, language);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            //
        }
        return rssFeed;
    }


    public ArrayList<RSSItem> getRSSFeedItems(String url){
        ArrayList<RSSItem> itemsList = new ArrayList<>();

        Document doc = this.getDoc(url);
        if(doc != null){
            try{
                Element root = doc.getDocumentElement();
                Node channel = root.getChildNodes().item(1);
                NodeList items = channel.getChildNodes();
                for (int i = 0; i < items.getLength(); i++) {
                    Node element = items.item(i);
                    if (element.getNodeName().equalsIgnoreCase("item")) {
                        NodeList nodes = element.getChildNodes();

                        String title = getVal(nodes, TAG_TITLE, false);
                        String image = getVal(nodes, TAG_IMAGE, true);
                        String link = getVal(nodes, TAG_LINK, false);
                        String description = getVal(nodes, TAG_DESRIPTION, false);
                        String pubDate = getVal(nodes, TAG_PUB_DATE, false);

                        RSSItem rssItem = new RSSItem(image, title, description, link, pubDate, url);

                        itemsList.add(rssItem);
                    }
                }
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        return itemsList;
    }


    private Document getDoc(String link) {
        try {
            URL url = new URL(link);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            InputStream inputStream = connection.getInputStream();
            DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = builderFactory.newDocumentBuilder();
            return builder.parse(inputStream);
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("myLog",  e.getMessage());
            return null;
        }
    }


    private String getElementValue(Node elem) {
        Node child;
        if (elem != null) {
            if (elem.hasChildNodes()) {
                for (child = elem.getFirstChild(); child != null; child = child
                        .getNextSibling()) {
                    if (child.getNodeType() == Node.TEXT_NODE || ( child.getNodeType() == Node.CDATA_SECTION_NODE)) {
                        return child.getNodeValue();
                    }
                }
            }
        }
        return "";
    }

    private String getValue(Element item, String str) {
        NodeList n = item.getElementsByTagName(str);
        return getElementValue(n.item(0));
    }

    private String getVal(NodeList nodes, String str, Boolean isImage){
        for (int j = 0; j < nodes.getLength(); j++) {
            Node current = nodes.item(j);
            if (current.getNodeName().equalsIgnoreCase(str)) {
                if (isImage)
                    return current.getAttributes().item(0).getTextContent();
                return current.getTextContent();
            }
        }
        return "";
    }
}
