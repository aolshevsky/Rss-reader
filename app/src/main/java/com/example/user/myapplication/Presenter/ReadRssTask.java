package com.example.user.myapplication.Presenter;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;

import com.example.user.myapplication.View.IReadRssView;
import com.example.user.myapplication.Model.RSSItem;
import com.example.user.myapplication.Utils.RSSParser;

import java.util.ArrayList;


public class ReadRssTask extends AsyncTask<Void, Void, Void> {

    private IReadRssView view;

    private String address;

    private ProgressDialog progressDialog;
    private ArrayList<RSSItem> rssItems;
    private RSSItem lastRssItem;
    private Boolean isWithUpdateButton;

    public void attachView(IReadRssView view){
        this.view = view;
        initializePD();
    }

    public ReadRssTask(String address, RSSItem item, Boolean isWithUpdateButton) {
        this.lastRssItem = item;
        this.address = address;
        this.isWithUpdateButton = isWithUpdateButton;
        rssItems = new ArrayList<>();
    }

    private void initializePD(){
        progressDialog = view.getProgressDialog();
        progressDialog.setMessage("Loading...");
    }

    @Override
    protected void onPreExecute() {
       // progressDialog.show();
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(Void v) {
        super.onPostExecute(v);
        //progressDialog.dismiss();
        view.checkNeedToUpdateNews(rssItems, isWithUpdateButton);
    }


    @Override
    protected Void doInBackground(Void... v) {
        rssItems.addAll(RSSParser.getRSSFeedItems(address, lastRssItem));
        return null;
    }

}
