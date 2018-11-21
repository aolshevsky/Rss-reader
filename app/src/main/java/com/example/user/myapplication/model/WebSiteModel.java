package com.example.user.myapplication.model;

public class WebSiteModel {
    private Integer id;
    private String title;
    private String link;
    private String rss_link;
    private String description;

    public WebSiteModel(){}

    public WebSiteModel(String title, String link, String rss_link, String description){
        this.title = title;
        this.link = link;
        this.rss_link = rss_link;
        this.description = description;
    }


    public void setId(Integer id){
        this.id = id;
    }

    public void setTitle(String title){
        this.title = title;
    }

    public void setLink(String link){
        this.link = link;
    }

    public void setRSSLink(String rss_link){
        this.rss_link = rss_link;
    }

    public void setDescription(String description){
        this.description = description;
    }


    public Integer getId(){
        return this.id;
    }

    public String getTitle(){
        return this.title;
    }

    public String getLink(){
        return this.link;
    }

    public String getRSSLink(){
        return this.rss_link;
    }

    public String getDescription(){
        return this.description;
    }
}
