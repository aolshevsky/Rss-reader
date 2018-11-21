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
import com.example.user.myapplication.model.NewsItemModel;
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

    private void feedData(){
        String[] imageUrls = {"https://www.livemint.com/rf/Image-621x414/LiveMint/Period2/2018/11/21/Photos/Processed/bitcoin-price-k64G--621x414@LiveMint.jpg"};
        String[] titles = {"Cryptocurrency 'bloodbath' as Bitcoin falls 30% in a week"};
        String[] descriptions = {"Market analysts see digital currency values falling further as the sector faces renewed regulatory scrutiny in the United States."};
        String[] link = {"Market analysts see digital currency values falling further as the sector faces renewed regulatory scrutiny in the United States."};
        String[] date = {"22.09.2018"};

        List<NewsItemModel> itemModels = new ArrayList<>();
        for (int i = 0; i < 10; i++){
            for (int j = 0; j < titles.length; j++){
                NewsItemModel itemModel = new NewsItemModel(imageUrls[j], titles[j], descriptions[j], link[j], date[j]);
                itemModels.add(itemModel);
            }
        }
        //mAdapter.addModels(itemModels);
    }

    public boolean isOnline() {
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
    public void setListAdapter(ArrayList<NewsItemModel> newsItemModels) {
        adapter.addModels(newsItemModels);
        newsRecyclerView.setAdapter(adapter);
    }
}
