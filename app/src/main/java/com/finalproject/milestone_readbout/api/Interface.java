package com.finalproject.milestone_readbout.api;
import android.hardware.biometrics.BiometricManager;

import com.finalproject.milestone_readbout.models.GuardianNewsModel;
import com.finalproject.milestone_readbout.models.GuardianResponse;
import com.finalproject.milestone_readbout.models.NewsModel;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface Interface {

    @GET("top-headlines")
    Call<NewsModel> getNews(
            @Query("country") String country,
            @Query("apiKey") String apikey
    );
    @GET("search")
    Call<GuardianResponse> getGuardianNews(
            @Query("api-key") String apikey,
            @Query("show-fields") String showFields
    );
    @GET("top-headlines")
    Call<NewsModel> getNewsViaCategory(
            @Query("country") String country,
            @Query("category") String category,
            @Query("apiKey") String apikey
    );
}
