package com.example.user.myapplication.Presenter;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;

import com.example.user.myapplication.View.IReadRssView;
import com.example.user.myapplication.model.NewsItemModel;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;


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
    private ArrayList<NewsItemModel> newsItemModels;
    private URL url;

    public void attachView(IReadRssView view){
        this.view = view;
        progressDialog = view.getProgressDialog();
        progressDialog.setMessage("Loading...");
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
        view.setListAdapter(newsItemModels);
    }


    @Override
    protected Void doInBackground(Void... params) {
        Log.d("myLog", "doInBackground");
        ProcessXml(getdata());
        return null;
    }


    private void ProcessXml(Document data) {
        Log.d("myLog", "Process_0");
        if (data != null) {
            newsItemModels = new ArrayList<>();
            Element root = data.getDocumentElement();
            Node channel = root.getChildNodes().item(1);
            NodeList items = channel.getChildNodes();
            Log.d("myLog", "Process" + items.getLength());
            for (int i = 0; i < items.getLength(); i++) {
                Node cureentchild = items.item(i);
                if (cureentchild.getNodeName().equalsIgnoreCase("item")) {
                    NewsItemModel item = new NewsItemModel();
                    NodeList itemchilds = cureentchild.getChildNodes();
                    for (int j = 0; j < itemchilds.getLength(); j++) {
                        Node cureent = itemchilds.item(j);
                        if (cureent.getNodeName().equalsIgnoreCase("title")) {
                            item.setTitle(cureent.getTextContent());
                        } else if (cureent.getNodeName().equalsIgnoreCase("description")) {
                            item.setDescription(cureent.getTextContent());
                        } else if (cureent.getNodeName().equalsIgnoreCase("pubDate")) {
                            item.setPubDate(cureent.getTextContent());
                        } else if (cureent.getNodeName().equalsIgnoreCase("link")) {
                            item.setLink(cureent.getTextContent());
                        } else if (cureent.getNodeName().equalsIgnoreCase("media:thumbnail")) {
                            String url = cureent.getAttributes().item(0).getTextContent();
                            item.setImageUrl(correctProtocol(url));
                        }else if (cureent.getNodeName().equalsIgnoreCase("enclosure")) {
                            String url = cureent.getAttributes().item(0).getTextContent();
                            item.setImageUrl(url);
                        }
                    }
                    newsItemModels.add(item);
                }
            }
        }
    }

    private String correctProtocol(String url){
        return "https:" + url.split(":")[1];
    }

    private Document getdata() {
        try {
            url = new URL(address[9]);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            InputStream inputStream = connection.getInputStream();
            DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = builderFactory.newDocumentBuilder();
            Document xmlDoc = builder.parse(inputStream);
            return xmlDoc;
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("myLog",  e.getMessage());
            return null;
        }
    }
}
