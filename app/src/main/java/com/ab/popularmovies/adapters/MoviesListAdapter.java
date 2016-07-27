package com.ab.popularmovies.adapters;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.ab.popularmovies.R;
import com.ab.popularmovies.Utils;
import com.ab.popularmovies.model.Movie;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by q4J1X056 on 27-07-2016.
 */
public class MoviesListAdapter extends ArrayAdapter<Movie> {

    final String LOG_TAG=MoviesListAdapter.class.getSimpleName();

    Context context;

    public MoviesListAdapter(Context context, int resource, List<Movie> objects) {
        super(context, resource, objects);
        this.context=context;
    }



    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater mInflater = (LayoutInflater) context
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.grid_item_movies, null);
            ImageView imageViewPoster = (ImageView) convertView;
            Log.d(LOG_TAG,"Poster path:"+Utils.
                    getCompletePosterPath(context,getItem(position).posterPath));
            Picasso.with(context).load(Utils.
                    getCompletePosterPath(context,getItem(position).posterPath)).into(imageViewPoster);

        }

        return convertView;
    }
}




