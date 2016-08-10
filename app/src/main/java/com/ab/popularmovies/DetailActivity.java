package com.ab.popularmovies;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import com.ab.popularmovies.fragments.DetailFragment;
import com.ab.popularmovies.model.Movie;
import com.squareup.picasso.Picasso;

public class DetailActivity extends AppCompatActivity implements DetailFragment.DetailInterface {

    private static final String DETAILFRAGMENT_TAG ="detail_fragment" ;
    private TextView textViewTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        setupToolbar();
        setupCollapsingToolbar();
        textViewTitle=(TextView)findViewById(R.id.textViewTitle);
        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.movie_detail_container , DetailFragment.newInstance(getIntent().getParcelableExtra(getString(R.string.extra_key_intent_movie)),false),DETAILFRAGMENT_TAG)
                    .commit();
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

    @Override
    public void setTitleInParent(String title) {
        setTitle(title);
        textViewTitle.setText(title);

    }

    @Override
    public void removeFragment() {
        //do Nothing
    }
}
