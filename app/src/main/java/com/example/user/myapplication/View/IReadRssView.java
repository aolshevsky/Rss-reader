package com.example.user.myapplication.View;

import android.app.ProgressDialog;

import com.example.user.myapplication.Adapter.ListAdapter;
import com.example.user.myapplication.model.RSSItem;

import java.util.ArrayList;

public interface IReadRssView {
    ProgressDialog getProgressDialog();
    String getRssLink();
    void initializeRecyclerView();
    void setListAdapter(ArrayList<RSSItem> rssItems);
    void setReloadBtnVisibility(Boolean isVisible);
    void onReloadBtnClick(ArrayList<RSSItem> newRssItems);
    void setSwipeRefreshingVisibility(Boolean isVisible);
    Boolean isOnline();
    void onLoadNewsError(String message);
    void refreshOnlineNews(ArrayList<RSSItem> rssItems, Boolean isWithUpdateButton);
    void checkNeedToUpdateNews(final ArrayList<RSSItem> newRssItems, Boolean isWithUpdateButton);
}
