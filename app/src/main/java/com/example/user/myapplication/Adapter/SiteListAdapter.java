package com.example.user.myapplication.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.example.user.myapplication.Adapter.Interface.ISiteAdapterListener;
import com.example.user.myapplication.Model.RSSFeed;
import com.example.user.myapplication.R;

import java.util.List;

public class SiteListAdapter  extends ArrayAdapter<RSSFeed> {

    private Context context;
    private ISiteAdapterListener siteAdapterListener;
    private int resource;

    public SiteListAdapter(Context context, int resource, List<RSSFeed> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        initializeSiteListener();
    }

    private void initializeSiteListener(){
        try {
            siteAdapterListener = (ISiteAdapterListener) context;
        } catch (ClassCastException ignored) {
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        String title = getItem(position).getTitle();
        String description = getItem(position).getDescription();
        final String link = getItem(position).getRSSLink();

        LayoutInflater inflater = LayoutInflater.from(context);
        convertView = inflater.inflate(resource, parent, false);

        TextView txtTitle = convertView.findViewById(R.id.rss_title);
        TextView txtDescription = convertView.findViewById(R.id.rss_description);
        TextView txtLink = convertView.findViewById(R.id.rss_link);

        View touch_view = convertView.findViewById(R.id.rss_item_click);
        touch_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (siteAdapterListener != null){
                    siteAdapterListener.putExtra("RssLink", link);
                    siteAdapterListener.onRssItemClick();
                }

            }
        });

        txtTitle.setText(title);
        txtDescription.setText(description);
        txtLink.setText(link);

        return convertView;
    }
}
