package com.example.user.myapplication.fragment;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import es.dmoral.toasty.Toasty;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.user.myapplication.Adapter.ListAdapter;
import com.example.user.myapplication.Adapter.VerticalSpace;
import com.example.user.myapplication.R;
import com.example.user.myapplication.View.IReadRssView;
import com.example.user.myapplication.model.RSSItem;
import com.example.user.myapplication.Presenter.ReadRssPresenter;
import com.example.user.myapplication.utils.Connection;

import java.util.ArrayList;
import java.util.List;


public class NewsFragment extends Fragment implements IReadRssView {

    private RecyclerView newsRecyclerView;
    private ListAdapter adapter;

    private String rssLink;

    private View newsView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        newsView = inflater.inflate(R.layout.fragment_news, container, false);
        Bundle bundle = getActivity().getIntent().getExtras();
        rssLink = bundle.getString("RssLink");


        initializeView();
        if(Connection.isOnline(getActivity()))
        {
            initializeRecyclerView();
            List<RSSItem> rssItems = RSSItem.find(RSSItem.class, "rsslink = ?", rssLink);

            adapter.addModels(new ArrayList<>(rssItems));
            newsRecyclerView.setAdapter(adapter);
            ReadRssPresenter readRss = new ReadRssPresenter(rssLink);
            readRss.attachView(this);
            //readRss.execute();
        } else{
            initializeRecyclerView();
            List<RSSItem> rssItems = RSSItem.find(RSSItem.class, "rsslink = ?", rssLink);

            adapter.addModels(new ArrayList<>(rssItems));
            newsRecyclerView.setAdapter(adapter);
            Toasty.error(getContext(), "No Internet Connection. See last saving news.", Toast.LENGTH_LONG).show();
        }


        getActivity().setTitle("News");
        return newsView;
    }

    private void initializeView(){
        newsRecyclerView = newsView.findViewById(R.id.newsRecyclerView);
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

        List<RSSItem> rssItemsDelete = RSSItem.find(RSSItem.class, "rsslink = ?", rssLink);
        Log.d("DB", "Items: " + String.valueOf(rssItemsDelete.size()));
        for (RSSItem item:rssItemsDelete) {
            item.delete();
        }
        for (RSSItem item:rssItems) {
            item.save();
        }
        adapter.addModels(rssItems);
        newsRecyclerView.setAdapter(adapter);
    }
}
