package com.example.user.myapplication.Utils;

import android.util.Log;

import com.example.user.myapplication.Model.RSSFeed;
import com.example.user.myapplication.Model.RSSItem;
import com.orm.query.Select;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;

public class RSSParser {

    public RSSParser() {}


    public static RSSFeed getRSSFeed(String url) {
        RSSFeed rssFeed = null;

        Document doc = Connection.getDoc(url);
        if (doc != null) {
            try {
                NodeList nodeList = doc.getElementsByTagName(Constants.TAG_CHANNEL);
                Element e = (Element) nodeList.item(0);

                String title = getValue(e, Constants.TAG_TITLE);
                String link = getValue(e, Constants.TAG_LINK);
                String description = getValue(e, Constants.TAG_DESCRIPTION);
                String language = getValue(e, Constants.TAG_LANGUAGE);

                rssFeed = new RSSFeed(title, description, link, url, language);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            //
        }
        return rssFeed;
    }


    public static ArrayList<RSSItem> getRSSFeedItems(String url, RSSItem lastRssItem){
        ArrayList<RSSItem> itemsList = new ArrayList<>();

        Document doc = Connection.getDoc(url);
        if(doc != null){
            try{
                Element root = doc.getDocumentElement();
                Node channel = root.getChildNodes().item(1);
                NodeList items = channel.getChildNodes();
                Log.d("myDB", "ITEMS LENGTH: " + items.getLength() + url);
                for (int i = 0; i < items.getLength(); i++) {
                    Node element = items.item(i);
                    if (element.getNodeName().equalsIgnoreCase("item")) {
                        NodeList nodes = element.getChildNodes();

                        String title = getVal(nodes, Constants.TAG_TITLE, false);
                        String image = getVal(nodes, Constants.TAG_IMAGE, true);
                        String link = getVal(nodes, Constants.TAG_LINK, false);
                        String description = getVal(nodes, Constants.TAG_DESCRIPTION, false);
                        String pubDate = getVal(nodes, Constants.TAG_PUB_DATE, false);

                        RSSItem rssItem = new RSSItem(image, title, description, link, pubDate, url);

                        if (lastRssItem != null && equalsRssItems(rssItem)) {
                            Log.d("myDB", "BREAK");
                            break;
                        }

                        itemsList.add(rssItem);
                    }
                }
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        return itemsList;
    }


    private static Boolean equalsRssItems(RSSItem item){
        ArrayList<RSSItem> find_items = (ArrayList<RSSItem>)RSSItem.find(RSSItem.class, "link = ?", item.getLink());
        if (find_items.size() != 0){
            //Log.d("myDB", "CMP: " + item.getTitle());
            return true;
        }
        //Log.d("myDB", "CMP: " + item.getTitle());
        return false;
    }



    private static String getElementValue(Node elem) {
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

    private static String getValue(Element item, String str) {
        NodeList n = item.getElementsByTagName(str);
        return getElementValue(n.item(0));
    }

    private static String getVal(NodeList nodes, String str, Boolean isImage){
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

    public static ArrayList<RSSItem> getRssItems(String rssLink){
        return (ArrayList<RSSItem>) Select
                .from(RSSItem.class)
                .where("rsslink = ?", new String[]{rssLink})
                .orderBy("pub_date Desc")
                .list();
    }

}
