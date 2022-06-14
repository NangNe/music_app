package com.sub.andro.musicapp.Retrofit;

import com.sub.andro.musicapp.Model.SongModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiInterface {

    //make sure you give the same name that is after your base url

    @GET("studio")
    Call<List<SongModel>> getStudio();
}
