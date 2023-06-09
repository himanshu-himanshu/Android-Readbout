package com.finalproject.milestone_readbout.ui.fragments;

import static android.text.Html.FROM_HTML_MODE_LEGACY;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.finalproject.milestone_readbout.R;
import com.finalproject.milestone_readbout.adapters.RecyclerAdapter;
import com.finalproject.milestone_readbout.api.Utilities;
import com.finalproject.milestone_readbout.models.GuardianResponse;
import com.finalproject.milestone_readbout.models.ResultsModel;
import com.finalproject.milestone_readbout.utils.Constants;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TrendingFragment extends Fragment {
    private ArrayList<ResultsModel> resultsModelArrayList;
    private RecyclerAdapter adapterSecond;
    RecyclerView recyclerViewTrending;
    private ProgressBar progressBar;
    private TextView progressBarText;
    String loggedUserID, language = "en", pageSize = "20", orderBy = "newest";
    private FirebaseFirestore db;
    private Boolean isFrench;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.trending_fragment, null);

        db = FirebaseFirestore.getInstance();

        Bundle data = getArguments();
        if (data != null) {
            loggedUserID = data.getString("loggedUserID");
            fetchDataFromFirebase(loggedUserID);
        } else {
            Toast.makeText(getContext(), "Bundle data Null", Toast.LENGTH_SHORT).show();
        }

        progressBar = view.findViewById(R.id.progressBar);
        progressBarText = view.findViewById(R.id.progressBarText);
        recyclerViewTrending = view.findViewById(R.id.recyclerView);

        progressBar.setVisibility(View.VISIBLE);
        progressBarText.setVisibility(View.VISIBLE);

        resultsModelArrayList = new ArrayList<>();
        recyclerViewTrending.setLayoutManager(new LinearLayoutManager(getContext()));
        adapterSecond = new RecyclerAdapter(getContext(), resultsModelArrayList);
        recyclerViewTrending.setAdapter(adapterSecond);

        return view;
    }

    private void fetchDataFromFirebase(String uid) {
        Log.e("TAG","FETCH CALLED ON TRENDING");
        DocumentReference doc = db.collection("users").document(uid);
        doc.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                isFrench = documentSnapshot.getBoolean("french");
                pageSize = documentSnapshot.getString("pageSize");
                orderBy = documentSnapshot.getString("orderBy");
                language = isFrench ? "fr" : "en";
                fetchNews();
            } else {
                Toast.makeText(getContext(), Constants.DATA_FETCHING_FAILED_FIREBASE, Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(e -> Toast.makeText(getContext(), Constants.DATA_FETCHING_FAILED_FIREBASE, Toast.LENGTH_SHORT).show());
    }

    private void fetchNews() {
        Log.e("TAG", "Inside fetch news trending" + pageSize + orderBy);
        Utilities.getInterface().getGuardianNews(Constants.GUARDIAN_API_KEY, Constants.SHOW_FIELDS, pageSize, language, orderBy).enqueue(new Callback<GuardianResponse>() {
            @Override
            public void onResponse(Call<GuardianResponse> call, Response<GuardianResponse> response) {
                if (response.isSuccessful()) {
                    try {
                        JSONObject jsonObject = new JSONObject(new Gson().toJson(response.body()));
                        JSONObject responseJsonObject = jsonObject.getJSONObject("response");
                        JSONArray resultsJsonArray = responseJsonObject.getJSONArray("results");

                        Log.e("RESULTS DATA", "CALLED" + language + isFrench);

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
                            String trailTextHtmlToText = null;
                            String newsBody;
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

                        adapterSecond.notifyDataSetChanged();
                        progressBar.setVisibility(View.INVISIBLE);
                        progressBarText.setVisibility(View.INVISIBLE);

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
