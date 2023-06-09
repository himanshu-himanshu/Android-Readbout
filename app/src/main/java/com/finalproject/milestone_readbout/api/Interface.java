package com.finalproject.milestone_readbout.api;

import com.finalproject.milestone_readbout.models.GuardianResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface Interface {

    @GET("search")
    Call<GuardianResponse> getGuardianNews(
            @Query("api-key") String apikey,
            @Query("show-fields") String showFields,
            @Query("page-size") String pageSize,
            @Query("lang") String language,
            @Query("order-by") String orderBy
    );

    @GET("search")
    Call<GuardianResponse> getSectionGuardianNews(
            @Query("api-key") String apikey,
            @Query("show-fields") String showFields,
            @Query("page-size") Integer pageSize,
            @Query("section") String section,
            @Query("lang") String language,
             @Query("order-by") String orderBy
    );

}
