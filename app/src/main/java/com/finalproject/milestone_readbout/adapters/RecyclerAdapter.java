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
import com.finalproject.milestone_readbout.models.ResultsModel;

import java.util.ArrayList;
public class
RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

    Context context;
    ArrayList<ResultsModel> resultsModelArrayList;

    public RecyclerAdapter(Context context, ArrayList<ResultsModel> resultsModelArrayList) {
        this.context = context;
        this.resultsModelArrayList = resultsModelArrayList;
    }

    @NonNull
    @Override
    public RecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.news_card_item, null, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerAdapter.ViewHolder holder, int position) {
        holder.newsTitle.setText(resultsModelArrayList.get(position).getWebTitle());
        holder.newsDescription.setText(resultsModelArrayList.get(position).getDesc());
        holder.newsAuthor.setText(resultsModelArrayList.get(position).getSectionName());
        holder.newsPublishedAt.setText(resultsModelArrayList.get(position).getWebPublicationDate());
        Glide.with(context).load(resultsModelArrayList.get(position).getImageUrl()).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return resultsModelArrayList.size();
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

