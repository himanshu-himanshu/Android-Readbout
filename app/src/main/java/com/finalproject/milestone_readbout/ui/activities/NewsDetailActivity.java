package com.finalproject.milestone_readbout.ui.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.finalproject.milestone_readbout.R;
import com.finalproject.milestone_readbout.utils.Constants;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class NewsDetailActivity extends AppCompatActivity {
    private FirebaseFirestore db;
    SharedPreferences sharedpreferences;
    String title, desc, body, author, date, imageUrl, webUrl, loggedUserID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = FirebaseFirestore.getInstance();

        sharedpreferences = getSharedPreferences(Constants.PREFERENCE_NAME, MODE_PRIVATE);
        loggedUserID = sharedpreferences.getString(Constants.LOGGED_USER_ID, "");

        Intent intent = getIntent();

        title = intent.getStringExtra("Title");
        desc = intent.getStringExtra("Description");
        body = intent.getStringExtra("Body");
        author = intent.getStringExtra("Author");
        date = intent.getStringExtra("Date");
        imageUrl = intent.getStringExtra("ImageURL");
        webUrl = intent.getStringExtra("webUrl");

        setContentView(R.layout.activity_news_detail);

        TextView newsDetailHeading = findViewById(R.id.newsHeading);
        TextView newsAuthor = findViewById(R.id.newsAuthor);
        TextView newsDate = findViewById(R.id.newsDate);
        TextView newsBody = findViewById(R.id.newsBody);
        ImageView newsImage = findViewById(R.id.newsImage);
        ImageView backImage = findViewById(R.id.categoryBackImage);
        ImageView savedButton = findViewById(R.id.saveNewsButton);

        Glide.with(this).load(imageUrl).into(newsImage);

        newsDetailHeading.setText(title);
        newsAuthor.setText(author);
        newsDate.setText(date);
        newsBody.setText(body);

        backImage.setOnClickListener(v -> {
            this.finish();
        });

        savedButton.setOnClickListener(v -> {
            saveDataToFirebase();
            Toast.makeText(this, "News saved to database", Toast.LENGTH_LONG).show();
        });
    }

    private void saveDataToFirebase() {
        Map<String, Object> newsData = new HashMap<>();
        newsData.put("userID", loggedUserID);
        newsData.put("title", title);
        newsData.put("imageUrl", imageUrl);
        newsData.put("webUrl", webUrl);

        db.collection("savedNews")
                .add(newsData)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.e("TAG", "DocumentSnapshot written with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("TAG", "Error adding document", e);
                    }
                });

    }
}