package com.ab.popularmovies.fragments;

import android.app.Fragment;
import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.content.CursorLoader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.ab.popularmovies.DetailActivity;
import com.ab.popularmovies.MainActivity;
import com.ab.popularmovies.R;
import com.ab.popularmovies.Utils;
import com.ab.popularmovies.adapters.MoviesCursorAdapter;
import com.ab.popularmovies.adapters.MoviesListAdapter;
import com.ab.popularmovies.data.FavoritesContract;
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
public class MoviesFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {


    private static final String POSITION = "position";
    private static final String LOG_TAG = MoviesFragment.class.getSimpleName();
    private static final int FAVORITES_LOADER =0 ;
    private ProgressBar progressBarFetching;

    private BaseAdapter mMoviesAdapter;
    int mPosition;
    private GridView gridViewPosters;



    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        if(Utils.getCurrentSortCriteria(getActivity()).equals(getActivity().getString(R.string.sort_criteria_favorite)))
            getLoaderManager().initLoader(FAVORITES_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movies, container, false);
        Log.d(LOG_TAG,"Create View");
        gridViewPosters = (GridView) view.findViewById(R.id.gridViewPosters);
        progressBarFetching = (ProgressBar) view.findViewById(R.id.progressBarFetching);
        mMoviesAdapter=initializeAdapter(Utils.getCurrentSortCriteria(getActivity()));
        gridViewPosters.setAdapter(mMoviesAdapter);

        gridViewPosters.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {
                mPosition = pos;
                if(mMoviesAdapter instanceof MoviesListAdapter) {
                    ((MoviesInterface) getActivity()).onItemSelected(((MoviesListAdapter) mMoviesAdapter).getItem(pos));
                }
                else if  (mMoviesAdapter instanceof MoviesCursorAdapter){
                    Cursor cursor=(Cursor)((MoviesCursorAdapter)mMoviesAdapter).getItem(pos);
                    ((MoviesInterface) getActivity()).onItemSelected(Utils.getMovieFromCursor(cursor));
                }

            }
        });



        return view;
    }


    BaseAdapter initializeAdapter(String sortCriteria)
    {
        BaseAdapter moviesAdapter=null;
        if(sortCriteria.equals(getActivity().getString(R.string.sort_criteria_favorite))) {

            Cursor cursor= getActivity().getContentResolver().query(FavoritesContract.MovieEntry.CONTENT_URI,
                    FavoritesContract.MovieEntry.ALL_COLUMNS,
                    null,
                    null,
                    null);
            moviesAdapter=new MoviesCursorAdapter(getActivity(),cursor,0);
        }
        else {
            //mMoviesAdapter=null;
            moviesAdapter = new MoviesListAdapter(getActivity(), R.layout.grid_item_movies, new ArrayList<Movie>());
        }

       return moviesAdapter;
    }

    void getMovies(final String sortCriteria, final MoviesListAdapter moviesListAdapter) {
        progressBarFetching.setVisibility(View.VISIBLE);
        MovieDbService movieDbService = ApiBuilder.getClient(getActivity()).create(MovieDbService.class);
        Call<MovieDbApiResponse> call = movieDbService.getMovies(sortCriteria, getString(R.string.moviedb_api_key));
        call.enqueue(new Callback<MovieDbApiResponse>() {
            @Override
            public void onResponse(Call<MovieDbApiResponse> call, Response<MovieDbApiResponse> response) {
                int statusCode = response.code();
                List<Movie> movies = response.body().listMovies;
                if (movies != null ) {
                    // update sortCriteria only if successfully fetched the results
                    gridViewPosters.clearChoices();
                    moviesListAdapter.addAll(movies);
                    mMoviesAdapter=null;
                    mMoviesAdapter=moviesListAdapter;
                    gridViewPosters.setAdapter(mMoviesAdapter);
                    // Remove callbacks for Favorites loader
                    if(getLoaderManager().getLoader(FAVORITES_LOADER)!=null) {
                        getLoaderManager().destroyLoader(FAVORITES_LOADER);
                    }
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

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new android.content.CursorLoader(getActivity(), FavoritesContract.MovieEntry.CONTENT_URI,
                FavoritesContract.MovieEntry.ALL_COLUMNS,
                null,
                null,
                null
        );
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        if(mMoviesAdapter instanceof MoviesCursorAdapter) {
            ((MoviesCursorAdapter) mMoviesAdapter).swapCursor(cursor);
            if (mPosition != GridView.INVALID_POSITION)
                gridViewPosters.setSelection(mPosition); // since smooth scroll is buggy for gridview
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        if(mMoviesAdapter instanceof MoviesCursorAdapter)
            ((MoviesCursorAdapter)mMoviesAdapter).swapCursor(null);
    }


    public interface MoviesInterface {
        void checkNetworkAvailable();

        void onItemSelected(Movie movie);

        void updateSortCriteria(String sortCriteria);
    }


    public void updateSettingSortCriteria(String newlySetSortCriteria) {
        if(newlySetSortCriteria.equals(getActivity().getString(R.string.sort_criteria_favorite)))
        {

            MoviesCursorAdapter moviesAdapter=(MoviesCursorAdapter)initializeAdapter(newlySetSortCriteria);
            // Change view and sort criteria only if there are favorites present
            if(!moviesAdapter.isEmpty()) {
                mMoviesAdapter=null;
                mMoviesAdapter=moviesAdapter;
                gridViewPosters.setAdapter(mMoviesAdapter);
                getLoaderManager().restartLoader(FAVORITES_LOADER, null, this);
                ((MoviesInterface) getActivity()).updateSortCriteria(newlySetSortCriteria);

            }
            else {
                Toast.makeText(getActivity(), R.string.no_favorites_found,Toast.LENGTH_SHORT).show();
            }

        }else {
            MoviesListAdapter moviesAdapter=(MoviesListAdapter)initializeAdapter(newlySetSortCriteria);
            getMovies(newlySetSortCriteria, moviesAdapter);
        }

    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        if (gridViewPosters != null && mPosition != GridView.INVALID_POSITION) {
            //Log.d(LOG_TAG,"position here:"+mPosition);
            gridViewPosters.setSelection(mPosition); // Since smooth scroll is buggy for gridview
        }
        super.onConfigurationChanged(newConfig);
    }
}
