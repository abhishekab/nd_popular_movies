package com.ab.popularmovies.net;

import android.content.Context;

import com.ab.popularmovies.R;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by q4J1X056 on 09-08-2016.
 */
public class ApiBuilder {


    private static Retrofit retrofit = null;
    public static Retrofit getClient(Context context) {
        final String BASE_URL =context.getString(R.string.path_base_moviedb) ;
        if (retrofit==null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory( GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
