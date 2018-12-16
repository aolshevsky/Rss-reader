package com.example.user.myapplication.Presenter.Interface;

import com.example.user.myapplication.Model.RSSItem;

import java.util.ArrayList;

public interface INewsPresenter {
    void addNewNews(ArrayList<RSSItem> rssItems);
    ArrayList<RSSItem> getSortedNewsFromDB();
    void checkNeedToUpdateNews(ArrayList<RSSItem> newRssItems, Boolean isWithUpdateButton);
    void loadNews();
}
