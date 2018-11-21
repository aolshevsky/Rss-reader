package com.example.user.myapplication.model;

import java.util.List;

/**
 This class handle rss xml
 **/
public class RSSFeed {

    private String title;
    private String description;
    private String link;
    private String rss_link;
    private String language;
    List<RSSItem> items;


    public RSSFeed(String title, String description, String link,
                   String rss_link, String language) {
        this.title = title;
        this.description = description;
        this.link = link;
        this.rss_link = rss_link;
        this.language = language;
    }


    public void setItems(List<RSSItem> items) {
        this.items = items;
    }



    public List<RSSItem> getItems() {
        return this.items;
    }

    public String getTitle() {
        return this.title;
    }

    public String getDescription() {
        return this.description;
    }

    public String getLink() {
        return this.link;
    }

    public String getRSSLink() {
        return this.rss_link;
    }

    public String getLanguage() {
        return this.language;
    }
}
