package com.ab.popularmovies;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
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
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.ab.popularmovies.adapters.MoviesListAdapter;
import com.ab.popularmovies.fragments.DetailFragment;
import com.ab.popularmovies.fragments.MoviesFragment;
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

import okhttp3.internal.Util;

public class MainActivity extends AppCompatActivity  implements MoviesFragment.MoviesInterface{
    private String sortCriteria="";
    private static final String DETAILFRAGMENT_TAG ="detail_fragment" ;
    boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //checkNetworkAvailable();
        if (findViewById(R.id.movie_detail_container) != null) {
            mTwoPane = true;
            if (savedInstanceState == null) {
                getFragmentManager().beginTransaction()
                        .replace(R.id.movie_detail_container,new DetailFragment(), DETAILFRAGMENT_TAG)
                        .commit();
            }
        }
        else {
            mTwoPane=false;
        }
    }


    @Override
    public void onStart() {
        super.onStart();
        // if adapter is empty or there is a change in sort criteria
        String newlySetSortCriteria= Utils.getCurrentSortCriteria(this);
        if(!sortCriteria.equals(newlySetSortCriteria))
        {
            MoviesFragment moviesFragment=(MoviesFragment)getFragmentManager().findFragmentById(R.id.fragment_movies);
            moviesFragment.updateSettingSortCriteria(newlySetSortCriteria);

            if(mTwoPane)
            {
                getFragmentManager().beginTransaction()
                        .replace(R.id.movie_detail_container,new DetailFragment(), DETAILFRAGMENT_TAG).commit();

            }
        }
        super.onStart();
    }





    @Override
    public void checkNetworkAvailable() {

        if(!isNetworkAvailable(MainActivity.this))
        {
            new AlertDialog.Builder(MainActivity.this)
                    .setTitle(R.string.network_unavailable)
                    .setMessage(R.string.check_internet_connectivity)
                    .setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    }).show();

        }
    }

    @Override
    public void onItemSelected(Movie movie) {
        if(mTwoPane){
            getFragmentManager().beginTransaction()
                    .replace(R.id.movie_detail_container,
                            DetailFragment.newInstance(movie,true), DETAILFRAGMENT_TAG)
                    .commit();

        }
        else {
            Intent intent =new Intent(this,DetailActivity.class);
            intent.putExtra(getString(R.string.extra_key_intent_movie),movie);
            startActivity(intent);

        }
    }

    @Override
    public void updateSortCriteria(String sortCriteria) {
        this.sortCriteria=sortCriteria;
        updateTitle(sortCriteria);
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
        else if(sortCriteria.equals(getString(R.string.sort_criteria_favorite)))
        {
           setTitle(getString(R.string.favorites));
        }
        else
        {
            setTitle(getString(R.string.app_name));
        }
    }


}
