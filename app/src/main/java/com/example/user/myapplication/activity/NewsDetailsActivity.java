package com.example.user.myapplication.activity;


import android.os.Bundle;
import android.webkit.WebView;

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
        newsDetailsView.loadUrl(bundle.getString("Link"));
    }

}
