package com.finalproject.milestone_readbout.adapters;

import android.content.Context;
import android.content.Intent;
import android.text.format.DateUtils;
import android.util.Log;
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
import com.finalproject.milestone_readbout.ui.activities.NewsDetailActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

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
        String title = resultsModelArrayList.get(position).getWebTitle();
        String desc = resultsModelArrayList.get(position).getDesc();
        String author = resultsModelArrayList.get(position).getSectionName();
        String date = resultsModelArrayList.get(position).getWebPublicationDate();
        String imgUrl = resultsModelArrayList.get(position).getImageUrl();
        String body = resultsModelArrayList.get(position).getBody();

        holder.newsTitle.setText(title);
        holder.newsDescription.setText(desc);
        holder.newsAuthor.setText(author);
        holder.newsPublishedAt.setText(getTimeDifference(formatDate(resultsModelArrayList.get(position).getWebPublicationDate())));
        Glide.with(context).load(imgUrl).into(holder.imageView);

        holder.cardView.setOnClickListener(v -> {
            Intent intent = new Intent(context, NewsDetailActivity.class);
            intent.putExtra("Title", title);
            intent.putExtra("Description", desc);
            intent.putExtra("Body", body);
            intent.putExtra("Author", author);
            intent.putExtra("Date", date);
            intent.putExtra("ImageURL", imgUrl);
            context.startActivity(intent);
        });
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

    //--------------------------------------------------------------*/
    /**-------- CODE TO FORMAT DATE INTO TIME IN NEWS CARD ---------*/
    //--------------------------------------------------------------*/

    private CharSequence getTimeDifference(String formattedDate) {
        long currentTime = System.currentTimeMillis();
        long publicationTime = getDateInMillis(formattedDate);
        return DateUtils.getRelativeTimeSpanString(publicationTime, currentTime,
                DateUtils.SECOND_IN_MILLIS);
    }
    private static long getDateInMillis(String formattedDate) {
        SimpleDateFormat simpleDateFormat =
                new SimpleDateFormat("MMM d, yyyy  h:mm a");
        long dateInMillis;
        Date dateObject;
        try {
            dateObject = simpleDateFormat.parse(formattedDate);
            dateInMillis = dateObject.getTime();
            return dateInMillis;
        } catch (ParseException e) {
            Log.e("Problem parsing date", e.getMessage());
            e.printStackTrace();
        }
        return 0;
    }

    private String formatDate(String dateStringUTC) {
        // Parse the dateString into a Date object
        SimpleDateFormat simpleDateFormat =
                new SimpleDateFormat("yyyy-MM-dd'T'kk:mm:ss'Z'");
        Date dateObject = null;
        try {
            dateObject = simpleDateFormat.parse(dateStringUTC);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        // Initialize a SimpleDateFormat instance and configure it to provide a more readable
        // representation according to the given format, but still in UTC
        SimpleDateFormat df = new SimpleDateFormat("MMM d, yyyy  h:mm a", Locale.ENGLISH);
        String formattedDateUTC = df.format(dateObject);
        // Convert UTC into Local time
        df.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date date = null;
        try {
            date = df.parse(formattedDateUTC);
            df.setTimeZone(TimeZone.getDefault());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return df.format(date);
    }

}

