package com.finalproject.milestone_readbout;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.finalproject.milestone_readbout.adapters.RecyclerAdapter;
import com.finalproject.milestone_readbout.adapters.RecyclerViewAdapter;
import com.finalproject.milestone_readbout.api.Utilities;
import com.finalproject.milestone_readbout.models.ArticlesModel;
import com.finalproject.milestone_readbout.models.GuardianNewsModel;
import com.finalproject.milestone_readbout.models.GuardianResponse;
import com.finalproject.milestone_readbout.models.NewsModel;
import com.finalproject.milestone_readbout.models.ResultsModel;
import com.finalproject.milestone_readbout.utils.Constants;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TrendingFragment extends Fragment {

    //ArrayList<ArticlesModel> articlesModelsArrayList; // News API
    //RecyclerViewAdapter adapter; // News API

    ArrayList<ResultsModel> resultsModelArrayList; // Guardian API
    RecyclerAdapter adapterSecond; // Guardian API
    private RecyclerView recyclerViewTrending;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.trending_fragment, null);

        recyclerViewTrending = view.findViewById(R.id.trendingRecyclerView);

        //articlesModelsArrayList = new ArrayList<>(); // NewsAPI

        resultsModelArrayList = new ArrayList<>(); // Guardian API

        recyclerViewTrending.setLayoutManager(new LinearLayoutManager(getContext()));

        //adapter = new RecyclerViewAdapter(getContext(), articlesModelsArrayList); // NewsAPI

        adapterSecond = new RecyclerAdapter(getContext(), resultsModelArrayList); // Guardian API

        recyclerViewTrending.setAdapter(adapterSecond);

        fetchNews();

        return view;
    }

    private void fetchNews() {
        Utilities.getInterface().getGuardianNews(Constants.GUARDIAN_API_KEY, Constants.SHOW_FIELDS).enqueue(new Callback<GuardianResponse>() {
            @Override
            public void onResponse(Call<GuardianResponse> call, Response<GuardianResponse> response) {
                if (response.isSuccessful()) {
                    try {
                        JSONObject jsonObject = new JSONObject(new Gson().toJson(response.body()));
                        JSONObject responseJsonObject = jsonObject.getJSONObject("response");
                        JSONArray resultsJsonArray = responseJsonObject.getJSONArray("results");

                        Log.e("cvbnop", String.valueOf(resultsJsonArray));

                        for (int i = 0; i < resultsJsonArray.length(); i++) {

                            // Get a single news at position i within the list of news
                            JSONObject currentNews = resultsJsonArray.getJSONObject(i);
                            // For a given news, extract the value for the key called "webTitle"
                            String webTitle = currentNews.getString("webTitle");
                            // For a given news, extract the value for the key called "sectionName"
                            String sectionName = currentNews.getString("sectionName");
                            // For a given news, extract the value for the key called "webPublicationDate"
                            String webPublicationDate = currentNews.getString("webPublicationDate");
                            // For a given news, extract the value for the key called "webUrl"
                            String webUrl = currentNews.getString("webUrl");

                            String thumbnail = null;
                            String trailText = null;

                            JSONObject fieldsObject = currentNews.getJSONObject("fields");
                            //thumbnail = fieldsObject.getString("thumbnail");
                            // For a given news, if it contains the key called "fields", extract JSONObject
                            // associated with the key "fields"

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
                                }
                            }

                            // Create a new {@link News} object with the title and url from the JSON response.
                            ResultsModel gResponse = new ResultsModel(sectionName, webPublicationDate, webTitle, webUrl, fieldsObject, thumbnail, trailText);

                            // Add the new {@link News} to list of newsList.
                            resultsModelArrayList.add(gResponse);
                        }

                        adapterSecond.notifyDataSetChanged();
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
