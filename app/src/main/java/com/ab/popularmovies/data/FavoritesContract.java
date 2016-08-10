
package com.ab.popularmovies.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

public class FavoritesContract {


    public static final String CONTENT_AUTHORITY = "com.ab.popularmovies";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_MOVIES = "movies";


    public static final class MovieEntry implements BaseColumns {
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_MOVIES).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MOVIES;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MOVIES;

        public static Uri buildMovieUriWithId(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static String getIdStringFromUri(Uri uri)
        {
         return uri.getLastPathSegment();
        }


        public static final String TABLE_NAME = "movies";

        public static final String COLUMN_MOVIE_TITLE="title";

        public static final String COLUMN_MOVIE_POSTER_PATH="poster_path";

        public static final String COLUMN_MOVIE_OVERVIEW="overview";

        public static final String COLUMN_MOVIE_RATING ="rating";

        public static final String COLUMN_MOVIE_RELEASE_DATE="release_date";


        public static final int COL_ID=0;

        public static final int COL_TITLE=1;

        public static final int COL_POSTER_PATH=2;

        public static final int COL_OVERVIEW=3;

        public static final int COL_RATING=4;

        public static final int COL_DATE =5;



        public static final String[] ALL_COLUMNS=
                {
                        _ID,
                        COLUMN_MOVIE_TITLE,
                        COLUMN_MOVIE_POSTER_PATH,
                        COLUMN_MOVIE_OVERVIEW,
                        COLUMN_MOVIE_RATING,
                        COLUMN_MOVIE_RELEASE_DATE

                };

    }


}
