package com.hadi.movies.model.video;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;


public class VideoResponse {

    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("results")
    @Expose
    private List<Result> results = null;

    public int getId() {
        return id;
    }

    public List<Result> getResults() {
        return results;
    }
}