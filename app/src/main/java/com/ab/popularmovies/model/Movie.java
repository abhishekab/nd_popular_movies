package com.ab.popularmovies.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by q4J1X056 on 27-07-2016.
 */
public class Movie implements Parcelable {

    public Movie(long id, String originalTitle, String posterPath, String overview, double userRating, String releaseDate) {
        this.id = id;
        this.originalTitle = originalTitle;
        this.posterPath = posterPath;
        this.overview = overview;
        this.userRating = userRating;
        this.releaseDate = releaseDate;
    }

    @SerializedName("id")
    public long id;

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


    Movie(Parcel in) {
        this.id = in.readLong();
        this.originalTitle = in.readString();
        this.posterPath = in.readString();
        this.overview=in.readString();
        this.userRating=in.readDouble();
        this.releaseDate=in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(originalTitle);
        dest.writeString(posterPath);
        dest.writeString(overview);
        dest.writeDouble(userRating);
        dest.writeString(releaseDate);

    }
    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

}
