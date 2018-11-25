package com.example.user.myapplication.View;

import android.app.ProgressDialog;

import com.example.user.myapplication.model.RSSItem;

import java.util.ArrayList;

public interface IReadRssView {
    ProgressDialog getProgressDialog();
    void initializeRecyclerView();
    void setListAdapter(ArrayList<RSSItem> rssItems);
    void checkNeedToUpdateNews(ArrayList<RSSItem> newRssItems, Boolean isWithUpdateButton);
}
