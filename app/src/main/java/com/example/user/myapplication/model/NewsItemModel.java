package com.example.user.myapplication.model;

public class NewsItemModel {

    private String imageUrl;
    private String title;
    private String description;

    public NewsItemModel(String imageUrl, String title, String description) {
        this.imageUrl = imageUrl;
        this.title = title;
        this.description = description;
    }

    public NewsItemModel() {}

    public String getImageUrl() {
        return imageUrl;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }
}
