package com.ab.popularmovies.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by q4J1X056 on 27-07-2016.
 */
public class Movie {

    @SerializedName("id")
    public String id;

    @SerializedName("original_title")
    public String originalTitle;

    @SerializedName("poster_path")
    public String posterPath;

    @SerializedName("overview")
    public String overview;

    @SerializedName("vote_average")
    public double userRating;

    @SerializedName("release_date")
    public String releaseDate;

}
