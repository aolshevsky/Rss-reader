package com.example.user.myapplication.Presenter;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;

import com.example.user.myapplication.View.IReadRssView;
import com.example.user.myapplication.model.RSSItem;
import com.example.user.myapplication.utils.RSSParser;

import java.util.ArrayList;


public class ReadRssPresenter extends AsyncTask<Void, Void, Void> {

    private IReadRssView view;

// gazeta, lenta, scincemag, vesti
    private String[] address = {"https://www.nasa.gov/rss/dyn/ames_news.rss",
            "http://www.sciencemag.org/rss/news_current.xml",
            "http://www.zrpress.ru/rss/sport.xml",
            "https://lenta.ru/rss",
            "https://news.yandex.ru/society.rss",
            "http://news.yandex.ru/Pskov/index.rss",
            "https://www.gazeta.ru/export/rss/social_more.xml",
            "http://www.vesti.ru/vesti.rss",
            "http://static.feed.rbc.ru/rbc/internal/rss.rbc.ru/rbc.ru/news.rss",
            "https://news.tut.by/rss/all.rss"};
    private ProgressDialog progressDialog;
    private ArrayList<RSSItem> rssItems;
    private RSSParser rssParser;

    public void attachView(IReadRssView view){
        this.view = view;
        progressDialog = view.getProgressDialog();
        progressDialog.setMessage("Loading...");
        rssParser = RSSParser.getInstance();
        rssItems = new ArrayList<>();
    }

    public ReadRssPresenter() {}

    @Override
    protected void onPreExecute() {
        Log.d("myLog", "PreExecute");
        progressDialog.show();
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        Log.d("myLog", "onPostExecute");
        progressDialog.dismiss();
        view.initializeRecyclerView();
        view.setListAdapter(rssItems);
    }


    @Override
    protected Void doInBackground(Void... params) {
        Log.d("myLog", "doInBackground");
        rssItems.addAll(rssParser.getRSSFeedItems(address[6]));
        return null;
    }

}
