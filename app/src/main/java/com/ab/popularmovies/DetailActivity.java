package com.ab.popularmovies;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import com.ab.popularmovies.model.Movie;
import com.squareup.picasso.Picasso;

public class DetailActivity extends AppCompatActivity {
    Movie movieDetailed=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        setupToolbar();
        setupCollapsingToolbar();
        if (getIntent().hasExtra(getString(R.string.extra_key_intent_movie)))
        {
            movieDetailed=getIntent().getParcelableExtra(getString(R.string.extra_key_intent_movie));
        }
        if(movieDetailed!=null) {
            TextView textViewTitle=(TextView)findViewById(R.id.textViewTitle);
            ImageView imageViewPoster = (ImageView) findViewById(R.id.imageViewPosterDetail);
            TextView textViewRating=(TextView) findViewById(R.id.textViewRating);
            TextView textViewReleaseDate=(TextView) findViewById(R.id.textViewReleaseDate);
            TextView textViewOverview=(TextView) findViewById(R.id.textViewOverview);


            setTitle(movieDetailed.originalTitle);
            textViewTitle.setText(movieDetailed.originalTitle);
            Picasso.with(this).load(Utils.
                    getCompletePosterPath(this,movieDetailed.posterPath)).into(imageViewPoster);
            textViewRating.setText(String.format(getString(R.string.rating_formatted),movieDetailed.userRating));
            textViewReleaseDate.setText(movieDetailed.releaseDate);
            textViewOverview.setText(movieDetailed.overview);


        }
    }


    private void setupToolbar() {
        // TODO Auto-generated method stub
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarDetail);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    private void setupCollapsingToolbar() {
        final CollapsingToolbarLayout collapsingToolbar = (CollapsingToolbarLayout) findViewById(
                R.id.collapse_toolbar);

       // collapsingToolbar.setTitleEnabled(false);
        collapsingToolbar.setExpandedTitleColor(getResources().getColor(R.color.transparent));
    }
}
