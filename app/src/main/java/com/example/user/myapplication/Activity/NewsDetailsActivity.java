package com.example.user.myapplication.Activity;


import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.user.myapplication.R;

import androidx.appcompat.app.AppCompatActivity;


public class NewsDetailsActivity extends AppCompatActivity {


    private WebView newsDetailsView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_news_details);

        Bundle bundle = getIntent().getExtras();

        newsDetailsView = findViewById(R.id.newsDetailsView);
        newsDetailsView.getSettings().setJavaScriptEnabled(true);
        newsDetailsView.setWebViewClient(new WebViewClient());
        newsDetailsView.loadUrl(bundle.getString("Link"));
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();

        if (newsDetailsView != null){
            newsDetailsView.destroy();
        }
    }

}
