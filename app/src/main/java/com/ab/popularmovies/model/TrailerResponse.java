package com.ab.popularmovies.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by q4J1X056 on 09-08-2016.
 */
public class TrailerResponse {
    @SerializedName("results")
    public List<Trailer> listTrailer;
}
