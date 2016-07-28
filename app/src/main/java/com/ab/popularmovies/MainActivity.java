package com.ab.popularmovies;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.ActionMode;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;
import android.widget.ProgressBar;

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

public class MainActivity extends AppCompatActivity {

    private ProgressBar progressBarFetching;

    private MoviesListAdapter mMoviesAdapter;
    private GridView gridViewPosters;
    // initially the sort Criteria is blank
    private String sortCriteria="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkNetworkAvailable();
        progressBarFetching=(ProgressBar)findViewById(R.id.progressBarFetching);
        gridViewPosters=(GridView)findViewById(R.id.gridViewPosters);
        mMoviesAdapter=new MoviesListAdapter(MainActivity.this,R.layout.grid_item_movies,new ArrayList<Movie>());
        gridViewPosters.setAdapter(mMoviesAdapter);

    }

    @Override
    protected void onStart() {
        super.onStart();
        SharedPreferences sharedPreferences= PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
        String newlySetSortCriteria=sharedPreferences.getString(getString(R.string.pref_sortby_key),getString(R.string.sort_criteria_popular));
        // if adapter is empty or there is a change in sort criteria
        if(!sortCriteria.equals(newlySetSortCriteria) || mMoviesAdapter.isEmpty())
        {
          new FetchPopularMoviesTask().execute(newlySetSortCriteria) ;
        }

    }


    private void updateTitle( String sortCriteria)
    {
        if(sortCriteria.equals(getString(R.string.sort_criteria_popular)))
        {
            setTitle(getString(R.string.title_most_popular_movies));
        }
        else if(sortCriteria.equals(getString(R.string.sort_criteria_top_rated)))
        {
            setTitle(getString(R.string.title_top_rated_movies));
        }
        else
        {
            setTitle(R.string.app_name);
        }
    }

    private void checkNetworkAvailable() {

        if(!isNetworkAvailable(MainActivity.this))
        {
            new AlertDialog.Builder(MainActivity.this)
                    .setTitle(R.string.network_unavailable)
                    .setMessage(R.string.check_internet_connectivity)
                    .setPositiveButton(R.string.exit, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            finish();

                        }
                    }).show();




        }
    }



    boolean isNetworkAvailable(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
        return isConnected;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main,menu);
        return true;


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.action_settings)
        {
            Intent intent=new Intent(this,SettingsActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
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
                mMoviesAdapter.addAll(movies);
                // update sortCriteria only if successfullt fetched the results
                sortCriteria=newSetSortCriteria;
                updateTitle(sortCriteria);
            }
            else
            {
                // The result was null, check network coverage
                checkNetworkAvailable();
            }

        }
    }
}
