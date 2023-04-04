package com.finalproject.milestone_readbout.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.finalproject.milestone_readbout.R;
import com.finalproject.milestone_readbout.api.Utilities;
import com.finalproject.milestone_readbout.models.ArticlesModel;

import java.util.ArrayList;

public class
RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    Context context;
    ArrayList<ArticlesModel> articlesModelsArrayList;

    public RecyclerViewAdapter(Context context, ArrayList<ArticlesModel> articlesModelsArrayList) {
        this.context = context;
        this.articlesModelsArrayList = articlesModelsArrayList;
    }

    @NonNull
    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.news_card_item, null, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapter.ViewHolder holder, int position) {
        holder.newsTitle.setText(articlesModelsArrayList.get(position).getTitle());
        holder.newsDescription.setText(articlesModelsArrayList.get(position).getDescription());
        holder.newsAuthor.setText(articlesModelsArrayList.get(position).getAuthor());
        holder.newsPublishedAt.setText(articlesModelsArrayList.get(position).getPublishedAt());
        Glide.with(context).load(articlesModelsArrayList.get(position).getUrlToImage()).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return articlesModelsArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView newsTitle, newsDescription, newsAuthor, newsPublishedAt;
        CardView cardView;
        ImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            newsTitle = itemView.findViewById(R.id.title);
            newsDescription = itemView.findViewById(R.id.description);
            newsAuthor = itemView.findViewById(R.id.author);
            newsPublishedAt = itemView.findViewById(R.id.published);
            imageView = itemView.findViewById(R.id.newsImage);
            cardView = itemView.findViewById(R.id.newsCard);
        }
    }
}

