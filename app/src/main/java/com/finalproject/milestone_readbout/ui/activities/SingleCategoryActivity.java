package com.finalproject.milestone_readbout.ui.activities;

import static android.text.Html.FROM_HTML_MODE_LEGACY;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.finalproject.milestone_readbout.R;
import com.finalproject.milestone_readbout.adapters.RecyclerAdapter;
import com.finalproject.milestone_readbout.api.Utilities;
import com.finalproject.milestone_readbout.models.GuardianResponse;
import com.finalproject.milestone_readbout.models.ResultsModel;
import com.finalproject.milestone_readbout.utils.Constants;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SingleCategoryActivity extends AppCompatActivity {
    ArrayList<ResultsModel> resultsModelArrayList;
    RecyclerAdapter adapter;
    private RecyclerView recyclerViewCategory;
    private String section;
    private TextView headingTextView, progressBarText;
    ProgressBar progressBar;
    String loggedUserID, language = "en";
    Boolean isFrench;
    private FirebaseFirestore db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        db = FirebaseFirestore.getInstance();
        String sectionName = intent.getStringExtra("sectionName");
        String heading = intent.getStringExtra("heading");
        section = sectionName;
        setContentView(R.layout.activity_single_category_news);

        SharedPreferences channel = this.getSharedPreferences(Constants.PREFERENCE_NAME, MODE_PRIVATE);
        loggedUserID = channel.getString(Constants.LOGGED_USER_ID, "Default");

        fetchDataFromFirebase(loggedUserID);

        progressBar = findViewById(R.id.progressBar2);
        progressBarText = findViewById(R.id.progressBarText2);
        progressBar.setVisibility(View.VISIBLE);
        progressBarText.setVisibility(View.VISIBLE);

        ImageView backImage = findViewById(R.id.categoryBackImage);

        recyclerViewCategory = findViewById(R.id.recyclerView);

        headingTextView = findViewById(R.id.heading);

        headingTextView.setText(heading);

        resultsModelArrayList = new ArrayList<>();

        recyclerViewCategory.setLayoutManager(new LinearLayoutManager(this));

        adapter = new RecyclerAdapter(this, resultsModelArrayList);

        recyclerViewCategory.setAdapter(adapter);

        backImage.setOnClickListener(v->{
            this.finish();
        });
    }

    private void fetchDataFromFirebase(String uid) {
        //Toast.makeText(getContext(), "Trending fetch called", Toast.LENGTH_SHORT).show();
        DocumentReference doc = db.collection("users").document(uid);
        doc.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    isFrench = documentSnapshot.getBoolean("french");
                    language = isFrench ? "fr" : "en";
                    fetchNews();
                } else {
                    Toast.makeText(getApplicationContext(), Constants.DATA_FETCHING_FAILED_FIREBASE, Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), Constants.DATA_FETCHING_FAILED_FIREBASE, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fetchNews() {
        Utilities.getInterface().getSectionGuardianNews(Constants.GUARDIAN_API_KEY, Constants.SHOW_FIELDS, Constants.PAGE_SIZE, section, language).enqueue(new Callback<GuardianResponse>() {
            @Override
            public void onResponse(Call<GuardianResponse> call, Response<GuardianResponse> response) {
                if (response.isSuccessful()) {
                    try {
                        JSONObject jsonObject = new JSONObject(new Gson().toJson(response.body()));
                        JSONObject responseJsonObject = jsonObject.getJSONObject("response");
                        JSONArray resultsJsonArray = responseJsonObject.getJSONArray("results");

                        Log.e("RESULTS DATA: ", String.valueOf(resultsJsonArray));

                        for (int i = 0; i < resultsJsonArray.length(); i++) {

                            // Get a single news at position i within the list of news
                            JSONObject currentNews = resultsJsonArray.getJSONObject(i);
                            // For a given news, extract the value for the key called "webTitle"
                            String webTitle = currentNews.getString("webTitle");
                            String webTitleHtmlToText = String.valueOf(Html.fromHtml(webTitle, FROM_HTML_MODE_LEGACY));
                            // For a given news, extract the value for the key called "sectionName"
                            String sectionName = currentNews.getString("sectionName");
                            // For a given news, extract the value for the key called "webPublicationDate"
                            String webPublicationDate = currentNews.getString("webPublicationDate");
                            // For a given news, extract the value for the key called "webUrl"
                            String webUrl = currentNews.getString("webUrl");

                            String thumbnail = null;
                            String trailText;
                            String newsBody;
                            String trailTextHtmlToText = null;
                            String newsBodyHtmlToText = null;

                            JSONObject fieldsObject = currentNews.getJSONObject("fields");

                            if (currentNews.has("fields")) {
                                // Extract the JSONObject associated with the key called "fields"
                                fieldsObject = currentNews.getJSONObject("fields");
                                // If there is the key called "thumbnail", extract the value for the key called "thumbnail"
                                if (fieldsObject.has("thumbnail")) {
                                    thumbnail = fieldsObject.getString("thumbnail");
                                    Log.e("TAG", thumbnail);
                                }
                                // If there is the key called "trailText", extract the value for the key called "trailText"
                                if (fieldsObject.has("trailText")) {
                                    trailText = fieldsObject.getString("trailText");
                                    trailTextHtmlToText = String.valueOf(Html.fromHtml(trailText, FROM_HTML_MODE_LEGACY));
                                }
                                // If there is the key called "trailText", extract the value for the key called "trailText"
                                if (fieldsObject.has("body")) {
                                    newsBody = fieldsObject.getString("body");
                                    newsBodyHtmlToText = String.valueOf(Html.fromHtml(newsBody, FROM_HTML_MODE_LEGACY));
                                }
                            }

                            ResultsModel gResponse = new ResultsModel(sectionName, webPublicationDate, webTitleHtmlToText, webUrl, fieldsObject, thumbnail, trailTextHtmlToText, newsBodyHtmlToText);
                            resultsModelArrayList.add(gResponse);
                        }

                        adapter.notifyDataSetChanged();
                        progressBar.setVisibility(View.INVISIBLE);
                        progressBarText.setVisibility(View.INVISIBLE);

                        if (resultsJsonArray.length() == 0) {
                            progressBarText.setText(Constants.NO_NEWS);
                            progressBarText.setVisibility(View.VISIBLE);
                            recyclerViewCategory.setVisibility(View.INVISIBLE);
                        }
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
            @Override
            public void onFailure(Call<GuardianResponse> call, Throwable t) {
            }
        });
    }
}