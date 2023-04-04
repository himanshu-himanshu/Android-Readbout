package com.finalproject.milestone_readbout;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.finalproject.milestone_readbout.adapters.RecyclerViewAdapter;
import com.finalproject.milestone_readbout.api.Utilities;
import com.finalproject.milestone_readbout.models.ArticlesModel;
import com.finalproject.milestone_readbout.models.NewsModel;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TrendingFragment extends Fragment {
    String API = "8cc0afbe6caa4d6da3f9403c458e639e";
    ArrayList<ArticlesModel> articlesModelsArrayList;
    RecyclerViewAdapter adapter;
    String country = "ca";
    private RecyclerView recyclerViewTrending;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.trending_fragment, null);

        recyclerViewTrending = view.findViewById(R.id.trendingRecyclerView);
        articlesModelsArrayList = new ArrayList<>();
        recyclerViewTrending.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new RecyclerViewAdapter(getContext(), articlesModelsArrayList);
        recyclerViewTrending.setAdapter(adapter);

        fetchNews();

        return view;
    }

    private void fetchNews() {

        Utilities.getInterface().getNews(country, API).enqueue(new Callback<NewsModel>() {
            @Override
            public void onResponse(Call<NewsModel> call, Response<NewsModel> response) {
                if (response.isSuccessful()) {
                    articlesModelsArrayList.addAll(response.body().getArticles());
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<NewsModel> call, Throwable t) {

            }
        });
    }
}
