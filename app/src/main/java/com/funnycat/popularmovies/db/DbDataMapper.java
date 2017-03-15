package com.funnycat.popularmovies.db;

import android.content.ContentValues;
import android.database.Cursor;

import com.funnycat.popularmovies.domain.models.Movie;
import com.funnycat.popularmovies.db.FavMovieContract.FavMovieEntry;
import com.funnycat.popularmovies.utils.CollectionUtils;


/**
 * Created by daniel on 14/03/2017.
 */

public class DbDataMapper {

    public static ContentValues convertMoviefromDomain(Movie movie){
        ContentValues cv = new ContentValues();
        cv.put(FavMovieEntry.COLUMN_MOVIE_ID, movie.getId());
        cv.put(FavMovieEntry.COLUMN_TITLE, movie.getTitle());
        cv.put(FavMovieEntry.COLUMN_ORIGINAL_TITLE, movie.getOriginal_title());
        cv.put(FavMovieEntry.COLUMN_ORIGINAL_LANGUAGE, movie.getOriginal_language());
        cv.put(FavMovieEntry.COLUMN_RELEASE_DATE, movie.getRelease_date());
        cv.put(FavMovieEntry.COLUMN_ADULT, movie.isAdult()? 1 : 0);
        cv.put(FavMovieEntry.COLUMN_POSTER_PATH, movie.getPoster_path());
        cv.put(FavMovieEntry.COLUMN_OVERVIEW, movie.getOverview());
        cv.put(FavMovieEntry.COLUMN_BACKDROP_PATH, movie.getBackdrop_path());
        cv.put(FavMovieEntry.COLUMN_VOTE_COUNT, movie.getVote_count());
        cv.put(FavMovieEntry.COLUMN_VOTE_AVERAGE, movie.getVote_average());
        cv.put(FavMovieEntry.COLUMN_POPULARITY, movie.getPopularity());
        cv.put(FavMovieEntry.COLUMN_HAS_VIDEO, movie.hasVideo()? 1 : 0);
        cv.put(FavMovieEntry.COLUMN_GENRE_IDS, CollectionUtils.concatIntegerArray(movie.getGenre_ids()));
        return cv;
    }




    public static Movie convertMovieToDomain(Cursor c){
        if(c==null) return null;
        int id = c.getInt(c.getColumnIndex(FavMovieEntry.COLUMN_MOVIE_ID));
        String title = c.getString(c.getColumnIndex(FavMovieEntry.COLUMN_TITLE));
        String original_title = c.getString(c.getColumnIndex(FavMovieEntry.COLUMN_ORIGINAL_TITLE));
        String original_language = c.getString(c.getColumnIndex(FavMovieEntry.COLUMN_ORIGINAL_LANGUAGE));
        String release_date = c.getString(c.getColumnIndex(FavMovieEntry.COLUMN_RELEASE_DATE));
        boolean isAdult = c.getInt(c.getColumnIndex(FavMovieEntry.COLUMN_ADULT)) == 1;
        String posterPath = c.getString(c.getColumnIndex(FavMovieEntry.COLUMN_POSTER_PATH));
        String overview = c.getString(c.getColumnIndex(FavMovieEntry.COLUMN_OVERVIEW));
        String backdropPath = c.getString(c.getColumnIndex(FavMovieEntry.COLUMN_BACKDROP_PATH));
        int voteCount = c.getInt(c.getColumnIndex(FavMovieEntry.COLUMN_VOTE_COUNT));
        float voteAverage = c.getFloat(c.getColumnIndex(FavMovieEntry.COLUMN_VOTE_AVERAGE));
        float popularity = c.getFloat(c.getColumnIndex(FavMovieEntry.COLUMN_POPULARITY));
        boolean hasVideo = c.getInt(c.getColumnIndex(FavMovieEntry.COLUMN_HAS_VIDEO)) == 1;
        Integer[] genreIdsI = CollectionUtils.splitIntegerArray(c.getString(c.getColumnIndex(FavMovieEntry.COLUMN_GENRE_IDS)));
        return new Movie(id, title, original_title, original_language, release_date, isAdult,
                posterPath, overview, backdropPath, voteCount, voteAverage, popularity, hasVideo, genreIdsI);
    }
}