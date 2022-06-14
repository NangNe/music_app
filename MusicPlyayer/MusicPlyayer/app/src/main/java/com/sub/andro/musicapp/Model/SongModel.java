package com.sub.andro.musicapp.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SongModel {

    @Expose
    @SerializedName("song")
    String song;

    //make sure you give same name that are given in api

    @Expose
    @SerializedName("url")
    String url;

    @Expose
    @SerializedName("cover_image")
    String cover_image;

    @Expose
    @SerializedName("artists")
    String artists;

    //now create contructor and getter method


    public SongModel(String song, String url, String cover_image, String artists) {
        this.song = song;
        this.url = url;
        this.cover_image = cover_image;
        this.artists = artists;
    }

    public String getSong() {
        return song;
    }

    public String getUrl() {
        return url;
    }

    public String getCover_image() {
        return cover_image;
    }

    public String getArtists() {
        return artists;
    }
}
