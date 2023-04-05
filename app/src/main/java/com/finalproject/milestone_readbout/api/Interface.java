package com.finalproject.milestone_readbout.api;
import com.finalproject.milestone_readbout.models.NewsModel;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface Interface {

    @GET("top-headlines")
    Call<NewsModel> getNews(
            @Query("country") String country,
            @Query("apiKey") String apikey
    );

    @GET("top-headlines")
    Call<NewsModel> getNewsViaCategory(
            @Query("country") String country,
            @Query("category") String category,
            @Query("apiKey") String apikey
    );
}
