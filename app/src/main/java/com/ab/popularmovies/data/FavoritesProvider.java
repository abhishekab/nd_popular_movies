
package com.ab.popularmovies.data;

import android.annotation.TargetApi;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.util.Log;

public class FavoritesProvider extends ContentProvider {


    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private static final String LOG_TAG = FavoritesProvider.class.getSimpleName();
    private FavoritesDbHelper mOpenHelper;

    static final int FAVORITES = 100;
    static final int FAVORITE_WITH_ID = 101;

    static UriMatcher buildUriMatcher() {

        UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        sUriMatcher.addURI(FavoritesContract.CONTENT_AUTHORITY, FavoritesContract.PATH_MOVIES, FAVORITES);
        sUriMatcher.addURI(FavoritesContract.CONTENT_AUTHORITY, FavoritesContract.PATH_MOVIES + "/#", FAVORITE_WITH_ID);
        return sUriMatcher;
    }


    @Override
    public boolean onCreate() {
        mOpenHelper = new FavoritesDbHelper(getContext());
        return true;
    }

    @Override
    public String getType(Uri uri) {

        // Use the Uri Matcher to determine what kind of URI this is.
        final int match = sUriMatcher.match(uri);

        switch (match) {
            case FAVORITES:
                return FavoritesContract.MovieEntry.CONTENT_TYPE;
            case FAVORITE_WITH_ID:
                return FavoritesContract.MovieEntry.CONTENT_ITEM_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {
        Cursor retCursor;
        switch (sUriMatcher.match(uri)) {
            case FAVORITES: {
                retCursor = mOpenHelper.getReadableDatabase().query(FavoritesContract.MovieEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            }
            case FAVORITE_WITH_ID: {
                retCursor = getFavoriteWithId(uri, projection, sortOrder);
                break;
            }

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;
    }

    private Cursor getFavoriteWithId(Uri uri, String[] projection, String sortOrder) {


        final String selectionStatement= FavoritesContract.MovieEntry._ID+"=?";
        String[] selectionArguments={FavoritesContract.MovieEntry.getIdStringFromUri(uri)};
        return mOpenHelper.getReadableDatabase().query(FavoritesContract.MovieEntry.TABLE_NAME,
                projection,
                selectionStatement,
                selectionArguments,
                null,
                null,
                null);
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        Uri returnUri;

        switch (match) {
            case FAVORITES: {

                long _id = db.insert(FavoritesContract.MovieEntry.TABLE_NAME, null, values);
                if (_id > 0)
                    returnUri = FavoritesContract.MovieEntry.buildMovieUriWithId(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        Log.d(LOG_TAG,"Inserted:"+returnUri.toString());
        return returnUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {

        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsDeleted = 0;

       if(selection==null) selection="1";
        switch (match) {
            case FAVORITES: {

                rowsDeleted = db.delete(FavoritesContract.MovieEntry.TABLE_NAME, selection, selectionArgs);

                break;

            }
            case FAVORITE_WITH_ID: {
                final String selectionStatement= FavoritesContract.MovieEntry._ID+"=?";
                String[] selectionArguments={FavoritesContract.MovieEntry.getIdStringFromUri(uri)};
                rowsDeleted = db.delete(FavoritesContract.MovieEntry.TABLE_NAME, selectionStatement, selectionArguments);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if (rowsDeleted != 0)
            getContext().getContentResolver().notifyChange(uri, null);

        return rowsDeleted;
    }


    @Override
    public int update(
            Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsUpdated = 0;
        switch (match) {
            case FAVORITES: {
                rowsUpdated = db.update(FavoritesContract.MovieEntry.TABLE_NAME,values, selection, selectionArgs);
                break;
            }

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if (rowsUpdated != 0)
            getContext().getContentResolver().notifyChange(uri, null);

        return rowsUpdated;
    }


}