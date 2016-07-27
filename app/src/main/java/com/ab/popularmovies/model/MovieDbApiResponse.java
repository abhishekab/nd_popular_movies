package com.ab.popularmovies.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by q4J1X056 on 27-07-2016.
 */
public class MovieDbApiResponse {

    @SerializedName("results")
    public List<Movie> listMovies;


}
