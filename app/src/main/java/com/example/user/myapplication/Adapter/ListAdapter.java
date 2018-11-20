package com.example.user.myapplication.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.user.myapplication.R;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ListAdapter extends RecyclerView.Adapter {
    private String[] items;

    public ListAdapter(String[] items) {
        this.items = items;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View row = inflater.inflate(R.layout.custom_row_item, parent, false);
        return new ItemHolder(row);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        //((ItemHolder)holder).titleTextView.setText(items[position]);
    }

    @Override
    public int getItemCount() {
        return items.length;
    }

    private class ItemHolder extends RecyclerView.ViewHolder{

        private TextView titleTextView, descriptionTextView;
        private ImageView imageView;

        private ItemHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.newsTitleTextView);
            descriptionTextView = itemView.findViewById(R.id.newsDescriptionTextView);
            imageView = itemView.findViewById(R.id.newsImageView);
        }
    }
}
