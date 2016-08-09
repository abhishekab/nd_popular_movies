package com.ab.popularmovies.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by q4J1X056 on 09-08-2016.
 */
public class Trailer {
    @SerializedName("id")
    public String id;
    @SerializedName("key")
    public String key;
    @SerializedName("name")
    public String name;
    @SerializedName("site")
    public String site;
    @SerializedName("type")
    public String type;
}
