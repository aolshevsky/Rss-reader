package com.example.user.myapplication.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.user.myapplication.R;
import com.example.user.myapplication.activity.NewsDetailsActivity;
import com.example.user.myapplication.model.NewsItemModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class ListAdapter extends RecyclerView.Adapter {

    private List<NewsItemModel> newsItemModels;
    private Context context;

    public ListAdapter(Context context) {
        this.context = context;
        newsItemModels = new ArrayList<>();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View row = inflater.inflate(R.layout.custom_row_item, parent, false);
        return new ItemHolder(row);
    }

    public void addModels(List<NewsItemModel> itemModels){
        if(itemModels != null){
            int pos = this.newsItemModels.size();
            this.newsItemModels.addAll(itemModels);
            notifyItemRangeChanged(pos, itemModels.size());
        }
    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        final NewsItemModel currentItem = newsItemModels.get(position);
        ItemHolder itemHolder = (ItemHolder)holder;
        itemHolder.titleTextView.setText(currentItem.getTitle());
        itemHolder.descriptionTextView.setText(currentItem.getDescription());
        itemHolder.dateTextView.setText(currentItem.getPubDate());
        Picasso.with(context).load(currentItem.getImageUrl()).resize(450, 150).into(itemHolder.imageView);
        itemHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, NewsDetailsActivity.class);
                intent.putExtra("Link", currentItem.getLink());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() { return newsItemModels.size();}

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
