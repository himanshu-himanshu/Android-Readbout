package com.finalproject.milestone_readbout.adapters;

import android.content.Context;
import android.content.Intent;
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
import com.finalproject.milestone_readbout.models.SavedNewsFirebaseModel;
import com.finalproject.milestone_readbout.ui.activities.NewsDetailActivity;

import java.util.ArrayList;

public class SavedNewsAdapter extends RecyclerView.Adapter<SavedNewsAdapter.ViewHolder> {
    Context context;
    ArrayList<SavedNewsFirebaseModel> savedNewsFirebaseModels;

    public SavedNewsAdapter(Context context, ArrayList<SavedNewsFirebaseModel> savedNewsFirebaseModels) {
        this.context = context;
        this.savedNewsFirebaseModels = savedNewsFirebaseModels;
    }

    @NonNull
    @Override
    public SavedNewsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.saved_news_card, null, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SavedNewsAdapter.ViewHolder holder, int position) {

        String title = savedNewsFirebaseModels.get(position).getTitle();
        String desc = savedNewsFirebaseModels.get(position).getDescription();
        String imgUrl = savedNewsFirebaseModels.get(position).getImageUrl();
        String webUrl = savedNewsFirebaseModels.get(position).getWebUrl();

        holder.newsTitle.setText(title);
        holder.newsDescription.setText(desc);
        Glide.with(context).load(imgUrl).into(holder.imageView);

//        holder.cardView.setOnClickListener(v -> {
//            Intent intent = new Intent(context, NewsDetailActivity.class);
//            intent.putExtra("Title", title);
//            intent.putExtra("Description", desc);
//            intent.putExtra("Body", body);
//            intent.putExtra("Author", author);
//            intent.putExtra("Date", date);
//            intent.putExtra("ImageURL", imgUrl);
//            intent.putExtra("webUrl", webUrl);
//            context.startActivity(intent);
//        });
    }

    @Override
    public int getItemCount() {
        return savedNewsFirebaseModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView newsTitle, newsDescription;
        CardView cardView;
        ImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            newsTitle = itemView.findViewById(R.id.savedNewsTitle);
            newsDescription = itemView.findViewById(R.id.savedNewsDescription);
            imageView = itemView.findViewById(R.id.savedNewsImage);
            cardView = itemView.findViewById(R.id.savedNewsCard);
        }
    }
}
