package com.finalproject.milestone_readbout.api;

import com.finalproject.milestone_readbout.utils.Constants;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Utilities {

    private static Retrofit retrofit = null;

    public static Interface getInterface() {
        if(retrofit == null) {
            retrofit = new Retrofit.Builder().baseUrl(Constants.BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();
        }

        return retrofit.create(Interface.class);
    }
}

