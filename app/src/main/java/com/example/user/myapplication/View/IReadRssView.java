package com.example.user.myapplication.View;

import android.app.ProgressDialog;

import com.example.user.myapplication.model.NewsItemModel;

import java.util.ArrayList;

public interface IReadRssView {
    ProgressDialog getProgressDialog();
    void initializeRecyclerView();
    void setListAdapter(ArrayList<NewsItemModel> newsItemModels);
}
