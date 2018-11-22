package com.example.user.myapplication.fragment;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import es.dmoral.toasty.Toasty;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.user.myapplication.R;
import com.example.user.myapplication.model.RSSFeed;

import java.util.List;


public class RssItemListFragment extends Fragment {

    private View view;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_rss_item_list, container, false);

        List<RSSFeed> rssFeeds = RSSFeed.listAll(RSSFeed.class);
        Toasty.success(getContext(), "" + rssFeeds.toString(), Toast.LENGTH_SHORT).show();

        getActivity().setTitle("Rss channels");
        return view;
    }


}
