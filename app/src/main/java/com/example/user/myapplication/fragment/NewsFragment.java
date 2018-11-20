package com.example.user.myapplication.fragment;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.user.myapplication.Adapter.ListAdapter;
import com.example.user.myapplication.Adapter.VerticalSpace;
import com.example.user.myapplication.R;
import com.example.user.myapplication.model.NewsItemModel;

import java.util.ArrayList;
import java.util.List;


public class NewsFragment extends Fragment {

    private RecyclerView newsRecyclerView;
    private ListAdapter mAdapter;

    private View newsView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        newsView = inflater.inflate(R.layout.fragment_news, container, false);

        initializeView();

        getActivity().setTitle("News");
        return newsView;
    }

    private void initializeView(){
        newsRecyclerView = newsView.findViewById(R.id.newsRecyclerView);
        newsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        newsRecyclerView.addItemDecoration(new VerticalSpace(20));
        mAdapter = new ListAdapter(getContext());
        newsRecyclerView.setAdapter(mAdapter);
        feedData();
    }

    private void feedData(){
        String[] imageUrls = {"https://www.livemint.com/rf/Image-621x414/LiveMint/Period2/2018/11/21/Photos/Processed/bitcoin-price-k64G--621x414@LiveMint.jpg"};
        String[] titles = {"Cryptocurrency 'bloodbath' as Bitcoin falls 30% in a week"};
        String[] descriptions = {"Market analysts see digital currency values falling further as the sector faces renewed regulatory scrutiny in the United States."};
        List<NewsItemModel> itemModels = new ArrayList<>();
        for (int i = 0; i < 10; i++){
            for (int j = 0; j < titles.length; j++){
                NewsItemModel itemModel = new NewsItemModel(imageUrls[j], titles[j], descriptions[j]);
                itemModels.add(itemModel);
            }
        }
        mAdapter.addModels(itemModels);
    }

}
