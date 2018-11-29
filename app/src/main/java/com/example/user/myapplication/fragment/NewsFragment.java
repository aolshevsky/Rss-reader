package com.example.user.myapplication.fragment;


import android.app.ProgressDialog;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.user.myapplication.Adapter.ListAdapter;
import com.example.user.myapplication.Adapter.VerticalSpace;
import com.example.user.myapplication.Presenter.NewsPresenter;
import com.example.user.myapplication.Presenter.ReadRssTask;
import com.example.user.myapplication.R;
import com.example.user.myapplication.View.IReadRssView;
import com.example.user.myapplication.model.RSSItem;
import com.example.user.myapplication.utils.Connection;

import java.util.ArrayList;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import es.dmoral.toasty.Toasty;


public class NewsFragment extends Fragment implements IReadRssView {

    private RecyclerView newsRecyclerView;
    private ImageButton reload_news_btn;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ListAdapter adapter;
    private String rssLink;

    private NewsPresenter newsPresenter;

    private View newsView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        newsView = inflater.inflate(R.layout.fragment_news, container, false);
        Bundle bundle = getActivity().getIntent().getExtras();
        rssLink = bundle.getString("RssLink");

        newsPresenter = new NewsPresenter();
        newsPresenter.attachView(this);
        initializeView();
        newsPresenter.loadNews();


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
                setSwipeRefreshingVisibility(true);
                ArrayList<RSSItem> rssItems = newsPresenter.getSortedNewsFromDB();
                refreshOnlineNews(rssItems, false);
            }
        });

    }

    @Override
    public void refreshOnlineNews(ArrayList<RSSItem> rssItems, Boolean isWithUpdateButton){
        ReadRssTask readRss = new ReadRssTask(rssLink, rssItems.size() != 0 ? rssItems.get(0):  null, isWithUpdateButton);
        readRss.attachView(this);
        readRss.execute();
    }


    @Override
    public ProgressDialog getProgressDialog() {
        return new ProgressDialog(getContext());
    }

    @Override
    public Boolean isOnline() {
        return Connection.isOnline(getContext());
    }

    @Override
    public void onLoadNewsError(String message) {
        Toasty.error(getContext(), message, Toast.LENGTH_LONG).show();
    }

    @Override
    public String getRssLink() {
        return rssLink;
    }

    @Override
    public void setReloadBtnVisibility(Boolean isVisible) {
        if (isVisible)
            reload_news_btn.setVisibility(View.VISIBLE);
        else
            reload_news_btn.setVisibility(View.INVISIBLE);
    }

    @Override
    public void setSwipeRefreshingVisibility(Boolean isVisible) {
        swipeRefreshLayout.setRefreshing(isVisible);
    }

    @Override
    public void onReloadBtnClick(final ArrayList<RSSItem> newRssItems) {
        Toasty.info(getContext(), "Press reload to get new news.", Toast.LENGTH_LONG).show();
        reload_news_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setSwipeRefreshingVisibility(true);
                setReloadBtnVisibility(false);
                initializeRecyclerView();
                newsPresenter.addNewNews(newRssItems);
                setSwipeRefreshingVisibility(false);
            }
        });
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
    public void setListAdapter(ArrayList<RSSItem> rssItems){
        adapter.addModels(rssItems);
        newsRecyclerView.setAdapter(adapter);
    }

    @Override
    public void checkNeedToUpdateNews(ArrayList<RSSItem> newRssItems, Boolean isWithUpdateButton) {
        newsPresenter.checkNeedToUpdateNews(newRssItems, isWithUpdateButton);
    }
}
