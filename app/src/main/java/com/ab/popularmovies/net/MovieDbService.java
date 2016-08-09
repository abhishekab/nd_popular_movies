package com.ab.popularmovies.net;

import com.ab.popularmovies.model.MovieDbApiResponse;
import com.ab.popularmovies.model.ReviewResponse;
import com.ab.popularmovies.model.TrailerResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by q4J1X056 on 09-08-2016.
 */
public interface MovieDbService {


    @GET("{sort_criteria}")
    Call<MovieDbApiResponse> getMovies(@Path("sort_criteria") String sortCriteria,@Query("api_key") String apiKey);

    @GET("{id}/videos")
    Call<TrailerResponse> getTrailers(@Path("id") String id, @Query("api_key") String apiKey);

    @GET("{id}/reviews")
    Call<ReviewResponse> getReviews(@Path("id") String id, @Query("api_key") String apiKey);
}
