package com.ab.popularmovies.fragments;

import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.ab.popularmovies.DetailActivity;
import com.ab.popularmovies.MainActivity;
import com.ab.popularmovies.R;
import com.ab.popularmovies.adapters.MoviesListAdapter;
import com.ab.popularmovies.model.Movie;
import com.ab.popularmovies.model.MovieDbApiResponse;
import com.ab.popularmovies.net.ApiBuilder;
import com.ab.popularmovies.net.MovieDbService;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by q4J1X056 on 08-08-2016.
 */
public class MoviesFragment extends Fragment {


    private static final String POSITION = "position";
    private static final String LOG_TAG = MoviesFragment.class.getSimpleName();
    private ProgressBar progressBarFetching;

    private MoviesListAdapter mMoviesAdapter;
    int mPosition;
    private GridView gridViewPosters;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movies, container, false);

        gridViewPosters = (GridView) view.findViewById(R.id.gridViewPosters);
        progressBarFetching = (ProgressBar) view.findViewById(R.id.progressBarFetching);
        mMoviesAdapter = new MoviesListAdapter(getActivity(), R.layout.grid_item_movies, new ArrayList<Movie>());
        gridViewPosters.setAdapter(mMoviesAdapter);
        gridViewPosters.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {
                mPosition = pos;
                ((MoviesInterface) getActivity()).onItemSelected(mMoviesAdapter.getItem(pos));

            }
        });

        return view;
    }

    
    void getMovies(final String sortCriteria) {
        progressBarFetching.setVisibility(View.VISIBLE);
        MovieDbService movieDbService = ApiBuilder.getClient(getActivity()).create(MovieDbService.class);
        Call<MovieDbApiResponse> call = movieDbService.getMovies(sortCriteria, getString(R.string.moviedb_api_key));
        call.enqueue(new Callback<MovieDbApiResponse>() {
            @Override
            public void onResponse(Call<MovieDbApiResponse> call, Response<MovieDbApiResponse> response) {
                int statusCode = response.code();
                List<Movie> movies = response.body().listMovies;
                if (movies != null) {
                    mMoviesAdapter.clear();
                    gridViewPosters.clearChoices();
                    mMoviesAdapter.addAll(movies);
                    // update sortCriteria only if successfull fetched the results
                    ((MoviesInterface) getActivity()).updateSortCriteria(sortCriteria);

                }
                progressBarFetching.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<MovieDbApiResponse> call, Throwable t) {
                // Log error here since request failed
                Log.e(LOG_TAG, t.toString());
                progressBarFetching.setVisibility(View.GONE);

                ((MoviesInterface) getActivity()).checkNetworkAvailable();
            }
        });
    }


    public interface MoviesInterface {
        void checkNetworkAvailable();

        void onItemSelected(Movie movie);

        void updateSortCriteria(String sortCriteria);
    }


    public void updateSettingSortCriteria(String newlySetSortCriteria) {
         getMovies(newlySetSortCriteria);
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        if (gridViewPosters != null && mPosition != GridView.INVALID_POSITION) {
            //Log.d(LOG_TAG,"position here:"+mPosition);
            gridViewPosters.setSelection(mPosition);
        }
        super.onConfigurationChanged(newConfig);
    }
}
