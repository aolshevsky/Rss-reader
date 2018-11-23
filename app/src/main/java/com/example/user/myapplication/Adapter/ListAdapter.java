package com.example.user.myapplication.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.myapplication.R;
import com.example.user.myapplication.activity.NewsDetailsActivity;
import com.example.user.myapplication.fragment.NewsFragment;
import com.example.user.myapplication.model.RSSItem;
import com.example.user.myapplication.utils.Connection;
import com.example.user.myapplication.utils.Parser;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import es.dmoral.toasty.Toasty;

public class ListAdapter extends RecyclerView.Adapter {

    private List<RSSItem> RSSItems;
    private Context context;
    private Boolean isHorizontal;

    public ListAdapter(Context context, Boolean isHorizontal) {
        this.context = context;
        this.isHorizontal = isHorizontal;
        RSSItems = new ArrayList<>();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View row;
        if (!isHorizontal)
            row = inflater.inflate(R.layout.custom_row_item, parent, false);
        else
            row = inflater.inflate(R.layout.custom_hor_row_item, parent, false);
        return new ItemHolder(row);
    }

    public void addModels(List<RSSItem> itemModels){
        if(itemModels != null){
            int pos = this.RSSItems.size();
            this.RSSItems.addAll(itemModels);
            notifyItemRangeChanged(pos, itemModels.size());
        }
    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        final RSSItem currentItem = RSSItems.get(position);
        ItemHolder itemHolder = (ItemHolder)holder;
        itemHolder.titleTextView.setText(currentItem.getTitle());
        itemHolder.descriptionTextView.setText(Parser.transformDescription(currentItem.getDescription()));
        itemHolder.dateTextView.setText(Parser.formatDate(currentItem.getPubDate()));
        Picasso.with(context).load(currentItem.getImageUrl()).resize(360, 180).into(itemHolder.imageView);
        itemHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Connection.isOnline(context)) {
                    Intent intent = new Intent(context, NewsDetailsActivity.class);
                    intent.putExtra("Link", currentItem.getLink());
                    context.startActivity(intent);
                } else {
                    Toasty.error(context, "Check internet connection", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public int getItemCount() { return RSSItems.size();}

    private class ItemHolder extends RecyclerView.ViewHolder{

        private TextView titleTextView, descriptionTextView, dateTextView;
        private ImageView imageView;
        CardView cardView;

        private ItemHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.newsTitleTextView);
            descriptionTextView = itemView.findViewById(R.id.newsDescriptionTextView);
            dateTextView = itemView.findViewById(R.id.newsDateTextView);
            imageView = itemView.findViewById(R.id.newsImageView);
            cardView = itemView.findViewById(R.id.newsCardView);
        }
    }
}
