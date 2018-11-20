package com.example.user.myapplication.fragment;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.user.myapplication.Adapter.ListAdapter;
import com.example.user.myapplication.R;


public class NewsFragment extends Fragment {

    private RecyclerView newsRecyclerView;
    private String[] items = {"Item 0", "Item 1", "Item 2", "Item 3", "Item 4", "Item 5", "Item 6", "Item 7", "Item 8", "Item 9", "Item 10", "Item 11", "Item 12", "Item 13", "Item 14 "};

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
        newsRecyclerView.setAdapter(new ListAdapter(items));
    }

}
