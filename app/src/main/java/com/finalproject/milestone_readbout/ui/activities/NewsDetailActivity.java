package com.finalproject.milestone_readbout.ui.activities;

import androidx.appcompat.app.AppCompatActivity;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.finalproject.milestone_readbout.R;
import com.finalproject.milestone_readbout.notification.NotificationDecorator;
import com.finalproject.milestone_readbout.utils.Constants;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.util.HashMap;
import java.util.Map;

public class NewsDetailActivity extends AppCompatActivity {
    private FirebaseFirestore db;
    SharedPreferences sharedpreferences;
    String title, desc, body, author, date, imageUrl, webUrl, loggedUserID;
    NotificationManager notificationMgr;
    NotificationDecorator notificationDecorator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = FirebaseFirestore.getInstance();

        sharedpreferences = getSharedPreferences(Constants.PREFERENCE_NAME, MODE_PRIVATE);
        loggedUserID = sharedpreferences.getString(Constants.LOGGED_USER_ID, "");

        notificationMgr = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationDecorator = new NotificationDecorator(this, notificationMgr);

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

        backImage.setOnClickListener(v -> this.finish());

        savedButton.setOnClickListener(v -> saveDataToFirebase());
    }

    private void saveDataToFirebase() {
        Map<String, Object> newsData = new HashMap<>();
        newsData.put("userID", loggedUserID);
        newsData.put("title", title);
        newsData.put("description", desc);
        newsData.put("imageUrl", imageUrl);
        newsData.put("webUrl", webUrl);

        db.collection("savedNews").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                int i = 0;
                boolean alreadyInList = false;
                for (QueryDocumentSnapshot document : task.getResult()) {
                    i++;
                    if (document.getString("webUrl").equals(webUrl)) {
                        alreadyInList = true;
                    }
                }
                if(!alreadyInList || i == 0) {
                    db.collection("savedNews").add(newsData).addOnSuccessListener(documentReference ->
                            Log.e("TAG", "Added to lis"))
                            .addOnFailureListener(e -> Log.w("TAG", "Error adding document", e));
                    Toast.makeText(getApplicationContext(), "Added to list", Toast.LENGTH_SHORT).show();
                    fetchDataFromFirebase(loggedUserID);
                } else {
                    Toast.makeText(getApplicationContext(), "Already in list", Toast.LENGTH_SHORT).show();
                }
            } else {
                Log.d("TAG", "Error getting documents: ", task.getException());
            }
        });
    }

    private void fetchDataFromFirebase(String uid) {
        DocumentReference doc = db.collection("users").document(uid);
        doc.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                boolean allowNotifications = documentSnapshot.getBoolean("allowNotifications");
                if (allowNotifications) {
                    /* Send notification messages */
                    notificationDecorator.displayExpandableNotification(Constants.SUCCESSFULLY_SAVED_NEWS, Constants.SUCCESSFULLY_SAVED_NEWS_DES);
                }
            } else {
                Toast.makeText(getApplicationContext(), Constants.DATA_FETCHING_FAILED_FIREBASE, Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(e -> Toast.makeText(getApplicationContext(), Constants.DATA_FETCHING_FAILED_FIREBASE, Toast.LENGTH_SHORT).show());
    }
}