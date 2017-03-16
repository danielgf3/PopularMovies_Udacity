package com.funnycat.popularmovies.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.funnycat.popularmovies.db.FavMovieContract.FavMovieEntry;

/**
 * Created by daniel on 14/03/2017.
 */

class FavMovieDBHelper extends SQLiteOpenHelper {

    /*
     * This is the name of our database. Database names should be descriptive and end with the
     * .db extension.
     */
    private static final String DATABASE_NAME = "fav_movies.db";

    private static final int DATABASE_VERSION = 1;

    FavMovieDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_FAV_MOVIES_TABLE_v1);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + FavMovieEntry.TABLE_NAME);
        onCreate(db);
    }



    private static final String SQL_CREATE_FAV_MOVIES_TABLE_v1 =
            "CREATE TABLE " + FavMovieEntry.TABLE_NAME + " (" +


                    FavMovieEntry._ID                          + " INTEGER PRIMARY KEY AUTOINCREMENT, " +

                    FavMovieEntry.COLUMN_MOVIE_ID              + " INTEGER NOT NULL, "                 +

                    FavMovieEntry.COLUMN_TITLE                 + " TEXT NOT NULL,"                     +

                    FavMovieEntry.COLUMN_ORIGINAL_TITLE        + " TEXT NOT NULL, "                    +
                    FavMovieEntry.COLUMN_ORIGINAL_LANGUAGE     + " TEXT NOT NULL, "                    +
                    FavMovieEntry.COLUMN_RELEASE_DATE          + " TEXT NOT NULL, "                    +
                    FavMovieEntry.COLUMN_ADULT                 + " INTEGER NOT NULL, "                 +

                    FavMovieEntry.COLUMN_POSTER_PATH + " TEXT NOT NULL, "                    +
                    FavMovieEntry.COLUMN_OVERVIEW              + " TEXT NOT NULL, "                    +
                    FavMovieEntry.COLUMN_BACKDROP_PATH         + " TEXT NOT NULL, "                    +
                    FavMovieEntry.COLUMN_VOTE_COUNT            + " INTEGER NOT NULL, "                 +
                    FavMovieEntry.COLUMN_VOTE_AVERAGE          + " REAL NOT NULL, "                    +
                    FavMovieEntry.COLUMN_POPULARITY            + " REAL NOT NULL, "                    +
                    FavMovieEntry.COLUMN_HAS_VIDEO             + " INT NOT NULL, "                     +
                    FavMovieEntry.COLUMN_GENRE_IDS             + " TEXT NOT NULL, "                    +

                /*
                 * To ensure this table can only contain one movie entry per movie_id, we declare
                 * the movie_id column to be unique. We also specify "ON CONFLICT REPLACE". This tells
                 * SQLite that if we have a movie entry for a certain date and we attempt to
                 * insert another movie entry with that movie_id, we replace the old movie entry.
                 */
                    " UNIQUE (" + FavMovieEntry.COLUMN_MOVIE_ID + ") ON CONFLICT REPLACE);";
}
