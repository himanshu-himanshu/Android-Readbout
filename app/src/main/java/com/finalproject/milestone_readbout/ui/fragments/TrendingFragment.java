package com.finalproject.milestone_readbout.ui.fragments;

import static android.text.Html.FROM_HTML_MODE_LEGACY;

import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.google.gson.Gson;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TrendingFragment extends Fragment {

    ArrayList<ResultsModel> resultsModelArrayList;
    RecyclerAdapter adapterSecond;
    private RecyclerView recyclerViewTrending;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.trending_fragment, null);

        recyclerViewTrending = view.findViewById(R.id.recyclerView);

        resultsModelArrayList = new ArrayList<>();

        recyclerViewTrending.setLayoutManager(new LinearLayoutManager(getContext()));

        adapterSecond = new RecyclerAdapter(getContext(), resultsModelArrayList);

        recyclerViewTrending.setAdapter(adapterSecond);

        fetchNews();

        return view;
    }

    private void fetchNews() {
        Utilities.getInterface().getGuardianNews(Constants.GUARDIAN_API_KEY, Constants.SHOW_FIELDS, Constants.PAGE_SIZE).enqueue(new Callback<GuardianResponse>() {
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
                            String trailText = null;
                            String trailTextHtmlToText = null;

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
                            }

                            ResultsModel gResponse = new ResultsModel(sectionName, webPublicationDate, webTitleHtmlToText, webUrl, fieldsObject, thumbnail, trailTextHtmlToText);
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
