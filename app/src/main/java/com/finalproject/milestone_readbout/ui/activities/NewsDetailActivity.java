package com.finalproject.milestone_readbout.ui.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.finalproject.milestone_readbout.R;

public class NewsDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();

        String title = intent.getStringExtra("Title");
        String desc = intent.getStringExtra("Description");
        String body = intent.getStringExtra("Body");
        String author = intent.getStringExtra("Author");
        String date = intent.getStringExtra("Date");
        String imageUrl = intent.getStringExtra("ImageURL");

        setContentView(R.layout.activity_news_detail);

        TextView newsDetailHeading = findViewById(R.id.newsHeading);
        TextView newsAuthor = findViewById(R.id.newsAuthor);
        TextView newsDate = findViewById(R.id.newsDate);
        TextView newsBody = findViewById(R.id.newsBody);
        ImageView newsImage = findViewById(R.id.newsImage);
        ImageView backImage = findViewById(R.id.categoryBackImage);

        Glide.with(this).load(imageUrl).into(newsImage);

        newsDetailHeading.setText(title);
        newsAuthor.setText(author);
        newsDate.setText(date);
        newsBody.setText(body);

        backImage.setOnClickListener(v -> {
            this.finish();
        });


    }
}