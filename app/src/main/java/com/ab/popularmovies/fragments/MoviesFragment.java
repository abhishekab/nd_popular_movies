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
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by q4J1X056 on 08-08-2016.
 */
public class MoviesFragment extends Fragment {


    private static final String POSITION ="position" ;
    private static final String LOG_TAG =MoviesFragment.class.getSimpleName() ;
    private ProgressBar progressBarFetching;

    private MoviesListAdapter mMoviesAdapter;
    int mPosition;
    private GridView gridViewPosters;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movies,container,false);

        gridViewPosters=(GridView)view.findViewById(R.id.gridViewPosters);
        progressBarFetching=(ProgressBar)view.findViewById(R.id.progressBarFetching);
        mMoviesAdapter=new MoviesListAdapter(getActivity(),R.layout.grid_item_movies,new ArrayList<Movie>());
        gridViewPosters.setAdapter(mMoviesAdapter);
        gridViewPosters.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {
                 mPosition=pos;
                ((MoviesInterface)getActivity()).onItemSelected(mMoviesAdapter.getItem(pos));

            }
        });

        return view;
    }




    class FetchPopularMoviesTask extends AsyncTask<String, Void, List<Movie>> {

        String movieDbBasePath = getString(R.string.path_base_moviedb);
        final String API_KEY = "api_key";
        final String LOG_TAG = FetchPopularMoviesTask.class.getSimpleName();
        String newSetSortCriteria;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBarFetching.setVisibility(View.VISIBLE);

        }

        @Override
        protected List<Movie> doInBackground(String... strings) {

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            String responseString;
            newSetSortCriteria=strings[0];

            try {
                Uri.Builder uriBuilder = Uri.parse(movieDbBasePath).buildUpon()
                        .appendPath(newSetSortCriteria)
                        .appendQueryParameter(API_KEY, getString(R.string.moviedb_api_key));
                Log.d(LOG_TAG,"Url:"+uriBuilder.build().toString());
                URL url = new URL(uriBuilder.build().toString());

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();


                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line);
                }

                if (buffer.length() == 0) {

                    return null;
                }
                responseString = buffer.toString();
                //Log.d(LOG_TAG, responseString);

            } catch (IOException e) {
                Log.e(LOG_TAG, "Error ", e);
                return null;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(LOG_TAG, "Error closing stream", e);
                    }
                }
            }


            try {

                Gson gson = new Gson();
                MovieDbApiResponse movieDbApiResponse = gson.fromJson(responseString, MovieDbApiResponse.class);

                return movieDbApiResponse.listMovies;
            } catch (Exception e) {

                Log.e(LOG_TAG, "Error in JSON parsing", e);
                return null;
            }
        }


        @Override
        protected void onPostExecute(List<Movie> movies) {
            super.onPostExecute(movies);
            progressBarFetching.setVisibility(View.GONE);
            if(movies!=null)
            {
                mMoviesAdapter.clear();
                gridViewPosters.clearChoices();
                mMoviesAdapter.addAll(movies);
                // update sortCriteria only if successfull fetched the results
                ((MoviesInterface)getActivity()).updateSortCriteria(newSetSortCriteria);

            }
            else
            {
                // The result was null, check network coverage
                ((MoviesInterface)getActivity()).checkNetworkAvailable();
            }

        }
    }







    public interface MoviesInterface
    {
        void checkNetworkAvailable();
        void onItemSelected(Movie movie);
        void updateSortCriteria(String sortCriteria);
    }





    public void updateSettingSortCriteria(String newlySetSortCriteria)
    {
        new FetchPopularMoviesTask().execute(newlySetSortCriteria) ;
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        if(gridViewPosters!=null && mPosition!= GridView.INVALID_POSITION) {
            //Log.d(LOG_TAG,"position here:"+mPosition);
            gridViewPosters.setSelection(mPosition);
        }
        super.onConfigurationChanged(newConfig);
    }
}
