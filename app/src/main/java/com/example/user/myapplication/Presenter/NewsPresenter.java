package com.example.user.myapplication.Presenter;

import com.example.user.myapplication.Presenter.Interface.INewsPresenter;
import com.example.user.myapplication.Utils.Constants;
import com.example.user.myapplication.View.IReadRssView;
import com.example.user.myapplication.Model.RSSItem;
import com.example.user.myapplication.Utils.Parser;
import com.example.user.myapplication.Utils.RSSParser;

import java.util.ArrayList;

public class NewsPresenter extends BasePresenter<IReadRssView> implements INewsPresenter {


    @Override
    public void addNewNews(ArrayList<RSSItem> rssItems) {
        ArrayList<RSSItem> rssItemsDB = getSortedNewsFromDB();
        for (RSSItem item:rssItems)
            item.save();

        rssItems.addAll(rssItemsDB);
        int rss_size = rssItems.size();
        if (rss_size > Constants.CACHED_RSS_ITEMS){
            for (int i = Constants.CACHED_RSS_ITEMS; i < rss_size; i++){
                rssItems.get(i).delete();
            }
        }
        view.setListAdapter(rssItems);
    }

    @Override
    public ArrayList<RSSItem> getSortedNewsFromDB(){
        ArrayList<RSSItem> rssItems = RSSParser.getRssItems(view.getRssLink());
        Parser.sortDates(rssItems);
        return rssItems;
    }

    @Override
    public void checkNeedToUpdateNews(final ArrayList<RSSItem> newRssItems, Boolean isWithUpdateButton) {
        ArrayList<RSSItem> rssItems = RSSParser.getRssItems(view.getRssLink());
        Parser.sortDates(rssItems);
        if (newRssItems.size() != 0){
            if (rssItems.size() == 0 || !newRssItems.get(0).getTitle().equals(rssItems.get(0).getTitle())){
                if (isWithUpdateButton) {
                    view.setReloadBtnVisibility(true);
                    view.onReloadBtnClick(newRssItems);
                } else {
                    view.setListAdapter(rssItems);
                    view.initializeRecyclerView();
                    addNewNews(newRssItems);
                    view.setReloadBtnVisibility(false);
                }
            }
        }
        view.setSwipeRefreshingVisibility(false);
    }

    @Override
    public void loadNews(){
        view.initializeRecyclerView();

        if(view.isOnline()) {
            ArrayList<RSSItem> rssItems = getSortedNewsFromDB();
            view.setListAdapter(rssItems);
            view.refreshOnlineNews(rssItems, true);
        } else {
            ArrayList<RSSItem> rssItems = getSortedNewsFromDB();
            view.setListAdapter(rssItems);
            view.onLoadNewsError("No Internet Connection. See last saving news.");
        }
    }
}
