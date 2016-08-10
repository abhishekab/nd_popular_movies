package com.ab.popularmovies;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.preference.PreferenceManager;

import com.ab.popularmovies.data.FavoritesContract;
import com.ab.popularmovies.model.Movie;

import java.io.File;

/**
 * Created by q4J1X056 on 27-07-2016.
 */
public class Utils {


    public static String getCompletePosterPath(Context context,String posterPath)
    {
        Uri.Builder builder= Uri.parse(context.getString(R.string.path_base_poster_moviedb))
                                .buildUpon()
                                .appendPath(context.getString(R.string.size_poster_moviedb))
                                .appendEncodedPath(posterPath);

        return builder.toString();


    }


    public  static String getYouTubeIconPath(String video_id)
    {
        return "http://img.youtube.com/vi/"+video_id+"/default.jpg";

    }

    public static Uri getYoutubeUriFromId(String id)
    {
        return Uri.parse("http://m.youtube.com/watch?v="+id);
    }


    public static ContentValues getContentValuesFromMovie(Movie movie)
    {
        ContentValues cv=new ContentValues();
        cv.put(FavoritesContract.MovieEntry._ID,movie.id);
        cv.put(FavoritesContract.MovieEntry.COLUMN_MOVIE_TITLE,movie.originalTitle);
        cv.put(FavoritesContract.MovieEntry.COLUMN_MOVIE_POSTER_PATH,movie.posterPath);
        cv.put(FavoritesContract.MovieEntry.COLUMN_MOVIE_OVERVIEW,movie.overview);
        cv.put(FavoritesContract.MovieEntry.COLUMN_MOVIE_RELEASE_DATE,movie.releaseDate);
        cv.put(FavoritesContract.MovieEntry.COLUMN_MOVIE_RATING,movie.userRating);
        return cv;
    }

    public static Movie getMovieFromCursor(Cursor cursor)
    {
        Movie movie =new Movie(
                cursor.getLong(FavoritesContract.MovieEntry.COL_ID),
                cursor.getString(FavoritesContract.MovieEntry.COL_TITLE),
                cursor.getString(FavoritesContract.MovieEntry.COL_POSTER_PATH),
                cursor.getString(FavoritesContract.MovieEntry.COL_OVERVIEW),
                cursor.getDouble(FavoritesContract.MovieEntry.COL_RATING),
                cursor.getString(FavoritesContract.MovieEntry.COL_DATE)
        );
        return movie;
    }

    public static String getCurrentSortCriteria(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPreferences.getString(context.getString(R.string.pref_sortby_key), context.getString(R.string.sort_criteria_popular));
    }


    public static Uri getLocalImageUriFromId(long id, Context context)
    {
        File root = new File(Environment.getExternalStorageDirectory()
                + File.separator + context.getString(R.string.folder_name) + File.separator);
        File fileImage = new File(root, id + ".jpg");
        if(fileImage.exists())
            return Uri.fromFile(fileImage);

        return null;
    }
}
