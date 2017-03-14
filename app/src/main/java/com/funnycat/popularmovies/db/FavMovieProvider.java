package com.funnycat.popularmovies.db;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;

/**
 * Created by daniel on 14/03/2017.
 */

public class FavMovieProvider extends ContentProvider {
    private static String TAG = "FavMovieProvider";

    /*
     * These constant will be used to match URIs with the data they are looking for. We will take
     * advantage of the UriMatcher class to make that matching MUCH easier than doing something
     * ourselves, such as using regular expressions.
     */
    public static final int CODE_FAV_MOVIE = 100;
    public static final int CODE_FAV_MOVIE_WITH_ID = 101;

    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private FavMovieDBHelper mOpenHelper;


    public static UriMatcher buildUriMatcher() {

        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = FavMovieContract.CONTENT_AUTHORITY;

        /* This URI is content://com.funnycat.popularmovies/fav_movie/ */
        matcher.addURI(authority, FavMovieContract.PATH_FAV_MOVIE, CODE_FAV_MOVIE);

        /*
         * This URI would look something like content://com.funnycat.popularmovies/fav_movie/23
         * The "/#" signifies to the UriMatcher that if PATH_FAV_MOVIE is followed by ANY number,
         * that it should return the CODE_FAV_MOVIE_WITH_ID code
         */
        matcher.addURI(authority, FavMovieContract.PATH_FAV_MOVIE + "/#", CODE_FAV_MOVIE_WITH_ID);

        return matcher;
    }

    @Override
    public boolean onCreate() {
        mOpenHelper = new FavMovieDBHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Cursor cursor;

        switch (sUriMatcher.match(uri)){

            case CODE_FAV_MOVIE: {
                cursor = mOpenHelper.getReadableDatabase().query(
                        FavMovieContract.FavMovieEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            }

            case CODE_FAV_MOVIE_WITH_ID: {
                String movie_id = uri.getLastPathSegment();

                String[] selectionArguments = new String[]{movie_id};


                cursor = mOpenHelper.getReadableDatabase().query(
                        FavMovieContract.FavMovieEntry.TABLE_NAME,
                        projection,
                        FavMovieContract.FavMovieEntry.COLUMN_MOVIE_ID + " = ? ",
                        selectionArguments,
                        null,
                        null,
                        sortOrder);
                break;
            }

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Override
    public String getType(@NonNull Uri uri) {
        throw new RuntimeException("We are not implementing getType in Sunshine.");
    }

    @Override
    public Uri insert(@NonNull Uri uri, ContentValues values) {
        // Get access to the database and write URI matching code to recognize a single item
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();

        Uri returnUri; // URI to be returne

        Log.d(TAG, "URI: " + uri.toString());

        switch (sUriMatcher.match(uri)){
            case CODE_FAV_MOVIE: {
                // Insert new values into the database
                long id = db.insert(FavMovieContract.FavMovieEntry.TABLE_NAME, null, values);
                if ( id > 0 ) {
                    returnUri = ContentUris.withAppendedId(FavMovieContract.FavMovieEntry.CONTENT_URI, id);
                } else {
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        // Notify the resolver if the uri has been changed, and return the newly inserted URI
        getContext().getContentResolver().notifyChange(uri, null);

        // Return constructed uri (this points to the newly inserted row of data)
        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {
        // Get access to the database and write URI matching code to recognize a single item
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();

        int numRowsDeleted;

        switch (sUriMatcher.match(uri)){
            case CODE_FAV_MOVIE_WITH_ID: {
                // Get the movie ID from the URI path
                String id = uri.getLastPathSegment();

                numRowsDeleted = db.delete(FavMovieContract.FavMovieEntry.TABLE_NAME, "_id=?", new String[]{id});
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        if(numRowsDeleted != 0){
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return numRowsDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        throw new RuntimeException("We are not implementing getType in Sunshine.");
    }
}
