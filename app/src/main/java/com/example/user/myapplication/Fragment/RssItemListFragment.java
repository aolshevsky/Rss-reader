package com.example.user.myapplication.Fragment;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.user.myapplication.Adapter.SiteListAdapter;
import com.example.user.myapplication.R;
import com.example.user.myapplication.Model.RSSFeed;

import java.util.List;


public class RssItemListFragment extends Fragment {

    private View view;
    private ListView listView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_rss_item_list, container, false);

        listView = view.findViewById(R.id.rss_item_list);
        List<RSSFeed> rssFeeds = RSSFeed.listAll(RSSFeed.class);
        // Toasty.success(getContext(), "" + rssFeeds.toString(), Toast.LENGTH_SHORT).show();

        getActivity().setTitle("Rss channels");

        SiteListAdapter adapter = new SiteListAdapter(getContext(), R.layout.custom_rss_row_item, rssFeeds);
        listView.setAdapter(adapter);

        return view;
    }


}
