package com.example.user.myapplication.fragment;


import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import es.dmoral.toasty.Toasty;

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

import java.util.ArrayList;
import java.util.List;


public class NewsFragment extends Fragment implements IReadRssView {

    private RecyclerView newsRecyclerView;
    ListAdapter adapter;

    private View newsView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        newsView = inflater.inflate(R.layout.fragment_news, container, false);

        initializeView();
        if(isOnline())
        {
            ReadRssPresenter readRss = new ReadRssPresenter();
            readRss.attachView(this);
            readRss.execute();
        }else{
            Toasty.error(getContext(), "No Internet Connection", Toast.LENGTH_LONG).show();
        }


        getActivity().setTitle("News");
        return newsView;
    }

    private void initializeView(){
        newsRecyclerView = newsView.findViewById(R.id.newsRecyclerView);
        adapter = new ListAdapter(getContext());
    }

    private boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    @Override
    public ProgressDialog getProgressDialog() {
        return new ProgressDialog(getContext());
    }

    @Override
    public void initializeRecyclerView() {
        newsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        newsRecyclerView.addItemDecoration(new VerticalSpace(20));
        adapter = new ListAdapter(getContext());
    }

    @Override
    public void setListAdapter(ArrayList<RSSItem> RSSItems) {
        adapter.addModels(RSSItems);
        newsRecyclerView.setAdapter(adapter);
    }
}
