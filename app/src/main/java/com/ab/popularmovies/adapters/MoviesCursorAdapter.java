package com.ab.popularmovies.adapters;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.widget.CursorAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ab.popularmovies.R;
import com.ab.popularmovies.Utils;
import com.ab.popularmovies.data.FavoritesContract;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

/**
 * Created by q4J1X056 on 10-08-2016.
 */
public class MoviesCursorAdapter extends CursorAdapter {

    boolean showTodayAsExpanded = true;
    public static final String LOG_TAG = MoviesCursorAdapter.class.getSimpleName();

    public MoviesCursorAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.grid_item_movies, parent, false);
        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ImageView imageViewPoster = (ImageView) view;
        Uri localImage = Utils.getLocalImageUriFromId(cursor.getLong(FavoritesContract.MovieEntry.COL_ID), context);
        if (localImage != null) {
            Log.d(LOG_TAG,"Local Copy");
            Picasso.with(context).load(localImage).into(imageViewPoster);
        } else {
            Picasso.with(context).load(Utils.
                    getCompletePosterPath(context, cursor.getString(FavoritesContract.MovieEntry.COL_POSTER_PATH))).placeholder(R.drawable.error_image)
                    .into(imageViewPoster)

            ;
        }
    }


}