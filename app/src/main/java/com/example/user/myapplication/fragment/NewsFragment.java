package com.example.user.myapplication.fragment;


import android.app.ProgressDialog;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.user.myapplication.Adapter.ListAdapter;
import com.example.user.myapplication.Adapter.VerticalSpace;
import com.example.user.myapplication.Presenter.ReadRssPresenter;
import com.example.user.myapplication.R;
import com.example.user.myapplication.View.IReadRssView;
import com.example.user.myapplication.model.RSSItem;
import com.example.user.myapplication.utils.Connection;
import com.example.user.myapplication.utils.Parser;
import com.example.user.myapplication.utils.RSSParser;
import com.orm.query.Select;

import java.util.ArrayList;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import es.dmoral.toasty.Toasty;


public class NewsFragment extends Fragment implements IReadRssView {

    private RecyclerView newsRecyclerView;
    private ListAdapter adapter;
    private ImageButton reload_news_btn;
    private SwipeRefreshLayout swipeRefreshLayout;

    private String rssLink;

    private View newsView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        newsView = inflater.inflate(R.layout.fragment_news, container, false);
        Bundle bundle = getActivity().getIntent().getExtras();
        rssLink = bundle.getString("RssLink");


        initializeView();
        loadNews();


        getActivity().setTitle("News");
        return newsView;
    }

    private void initializeView(){
        reload_news_btn = newsView.findViewById(R.id.reload_news_btn);
        reload_news_btn.setVisibility(View.INVISIBLE);
        newsRecyclerView = newsView.findViewById(R.id.newsRecyclerView);
        swipeRefreshLayout = newsView.findViewById(R.id.swipe_to_refresh_layout);

        swipeRefreshLayout.setColorSchemeResources(R.color.refresh, R.color.refresh1, R.color.refresh2);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(true);
                ArrayList<RSSItem> rssItems = getSortedNewsFromDB();
                refreshOnlineNews(rssItems, false);
            }
        });

    }


    private void loadNews(){
        initializeRecyclerView();

        if(Connection.isOnline(getContext())) {
            ArrayList<RSSItem> rssItems = getSortedNewsFromDB();
            adapter.addModels(rssItems);
            newsRecyclerView.setAdapter(adapter);
            //Log.d("myDB", "Last item: " + rssItems.get(0).getTitle());
            refreshOnlineNews(rssItems, true);
        } else {
            ArrayList<RSSItem> rssItems = getSortedNewsFromDB();
            adapter.addModels(rssItems);
            newsRecyclerView.setAdapter(adapter);
            Toasty.error(getContext(), "No Internet Connection. See last saving news.", Toast.LENGTH_LONG).show();
        }
    }

    private void refreshOnlineNews(ArrayList<RSSItem> rssItems, Boolean isWithUpdateButton){
        ReadRssPresenter readRss = new ReadRssPresenter(rssLink, rssItems.size() != 0 ? rssItems.get(0):  null, isWithUpdateButton);
        readRss.attachView(this);
        readRss.execute();
    }

    private ArrayList<RSSItem> getSortedNewsFromDB(){
        ArrayList<RSSItem> rssItems = RSSParser.getRssItems(rssLink);
        Parser.sortDates(rssItems);
        return rssItems;
    }

    @Override
    public void checkNeedToUpdateNews(final ArrayList<RSSItem> newRssItems, Boolean isWithUpdateButton) {
        ArrayList<RSSItem> rssItems = RSSParser.getRssItems(rssLink);
        Parser.sortDates(rssItems);
        if (newRssItems.size() != 0){
            if (rssItems.size() == 0 || !newRssItems.get(0).getTitle().equals(rssItems.get(0).getTitle())){
                if (isWithUpdateButton) {
                    reload_news_btn.setVisibility(View.VISIBLE);
                    Toasty.info(getContext(), "Press reload to get new news.", Toast.LENGTH_LONG).show();
                    reload_news_btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            initializeRecyclerView();
                            setListAdapter(newRssItems);
                            reload_news_btn.setVisibility(View.INVISIBLE);
                        }
                    });
                } else {
                    adapter.addModels(rssItems);
                    newsRecyclerView.setAdapter(adapter);
                    initializeRecyclerView();
                    setListAdapter(newRssItems);
                    reload_news_btn.setVisibility(View.INVISIBLE);
                }
            }
        }
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public ProgressDialog getProgressDialog() {
        return new ProgressDialog(getContext());
    }

    @Override
    public void initializeRecyclerView() {
        int orientation = this.getResources().getConfiguration().orientation;
        Boolean isHorizontal;
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            isHorizontal = false;
            newsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        } else {
            isHorizontal = true;
            newsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL,false));
        }

        newsRecyclerView.addItemDecoration(new VerticalSpace(20));
        adapter = new ListAdapter(getContext(), isHorizontal);
    }

    @Override
    public void setListAdapter(ArrayList<RSSItem> rssItems) {

        ArrayList<RSSItem> rssItemsDB = getSortedNewsFromDB();
        //Log.d("myDB", "Items delete: " + String.valueOf(rssItemsDelete.size()));
        //Log.d("myDB", "Items new: " + String.valueOf(rssItems.size()));
        /*
        for (RSSItem item:rssItemsDelete) {
            item.delete();
        }
        */
        for (RSSItem item:rssItems) {
            item.save();
        }
        rssItems.addAll(rssItemsDB);
        adapter.addModels(rssItems);
        newsRecyclerView.setAdapter(adapter);
    }
}
