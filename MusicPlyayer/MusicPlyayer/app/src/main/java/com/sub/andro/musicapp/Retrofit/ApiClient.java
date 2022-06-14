package com.sub.andro.musicapp.Retrofit;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {

    private static Retrofit retrofit;

    private static final String BASE_URL = "http://starlord.hackerearth.com/";

    public static Retrofit getRetrofitInstance()
    {
        if(retrofit == null)
        {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())         //to convert json object to java object
                    .build();
        }
        return retrofit;
    }
//so our api client is complete
}
