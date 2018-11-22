package com.example.user.myapplication.model;

import com.orm.SugarRecord;

public class RSSItem extends SugarRecord {

    private String imageUrl;
    private String title;
    private String description;
    private String link;
    private String pubDate;
    private String rss_link;

    public RSSItem(String imageUrl, String title, String description, String link, String pubDate, String rss_link) {
        this.imageUrl = imageUrl;
        this.title = title;
        this.description = description;
        this.link = link;
        this.pubDate = pubDate;
        this.rss_link = rss_link;
    }

    public RSSItem() {}

    public String getImageUrl() {
        if (imageUrl.equals(""))
            return null;
        return imageUrl;
    }
    public String getTitle() {
        return title;
    }
    public String getDescription() {
        return description;
    }
    public String getLink() {
        return link;
    }
    public String getPubDate() {
        return pubDate;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public void setPubDate(String pubDate) {
        this.pubDate = pubDate;
    }
}
