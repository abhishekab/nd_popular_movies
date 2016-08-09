package com.ab.popularmovies.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by q4J1X056 on 09-08-2016.
 */
public class Review {
    @SerializedName("id")
    public String id;
    @SerializedName("author")
    public String author;
    @SerializedName("content")
    public String content;
}
