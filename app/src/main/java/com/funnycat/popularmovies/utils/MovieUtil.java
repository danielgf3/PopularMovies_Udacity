package com.funnycat.popularmovies.utils;

import android.database.Cursor;
import android.database.MatrixCursor;
import com.funnycat.popularmovies.db.FavMovieContract;
import com.funnycat.popularmovies.domain.models.Movie;

import java.util.List;

/**
 * Created by daniel on 20/01/2017.
 */

public class MovieUtil {

    private static String TAG = "MovieUtil";

    private static String BASE_IMAGE_URL = "http://image.tmdb.org/t/p/";

    public static String makeImageUrl(String imageUrl){
        return makeImageUrl(imageUrl, false);
    }

    public static String makeImageUrl(String imageUrl, boolean fullSize){
        String defaultQuality = (fullSize)?"w500":"w185";
        return BASE_IMAGE_URL+defaultQuality+imageUrl;
    }

    public static Cursor convertListToCursor(List<Movie> movies){
        if(movies == null) return null;
        MatrixCursor matrixC = new MatrixCursor(new String[]{
                FavMovieContract.FavMovieEntry._ID,
                FavMovieContract.FavMovieEntry.COLUMN_MOVIE_ID,
                FavMovieContract.FavMovieEntry.COLUMN_TITLE,
                FavMovieContract.FavMovieEntry.COLUMN_ORIGINAL_TITLE,
                FavMovieContract.FavMovieEntry.COLUMN_ORIGINAL_LANGUAGE,
                FavMovieContract.FavMovieEntry.COLUMN_RELEASE_DATE,
                FavMovieContract.FavMovieEntry.COLUMN_ADULT,
                FavMovieContract.FavMovieEntry.COLUMN_POSTER_PATH,
                FavMovieContract.FavMovieEntry.COLUMN_OVERVIEW,
                FavMovieContract.FavMovieEntry.COLUMN_BACKDROP_PATH,
                FavMovieContract.FavMovieEntry.COLUMN_VOTE_COUNT,
                FavMovieContract.FavMovieEntry.COLUMN_VOTE_AVERAGE,
                FavMovieContract.FavMovieEntry.COLUMN_POPULARITY,
                FavMovieContract.FavMovieEntry.COLUMN_HAS_VIDEO,
                FavMovieContract.FavMovieEntry.COLUMN_GENRE_IDS});

        for (Movie m : movies) {
            matrixC.newRow().add(FavMovieContract.FavMovieEntry.COLUMN_MOVIE_ID, m.getId())
                    .add(FavMovieContract.FavMovieEntry.COLUMN_TITLE, m.getTitle())
                    .add(FavMovieContract.FavMovieEntry.COLUMN_ORIGINAL_TITLE, m.getOriginal_title())
                    .add(FavMovieContract.FavMovieEntry.COLUMN_ORIGINAL_LANGUAGE, m.getOriginal_language())
                    .add(FavMovieContract.FavMovieEntry.COLUMN_RELEASE_DATE, m.getRelease_date())
                    .add(FavMovieContract.FavMovieEntry.COLUMN_ADULT, m.isAdult()? 1: 0)
                    .add(FavMovieContract.FavMovieEntry.COLUMN_POSTER_PATH, m.getPoster_path())
                    .add(FavMovieContract.FavMovieEntry.COLUMN_OVERVIEW, m.getOverview())
                    .add(FavMovieContract.FavMovieEntry.COLUMN_BACKDROP_PATH, m.getBackdrop_path())
                    .add(FavMovieContract.FavMovieEntry.COLUMN_VOTE_COUNT, m.getVote_count())
                    .add(FavMovieContract.FavMovieEntry.COLUMN_VOTE_AVERAGE, m.getVote_average())
                    .add(FavMovieContract.FavMovieEntry.COLUMN_POPULARITY, m.getPopularity())
                    .add(FavMovieContract.FavMovieEntry.COLUMN_HAS_VIDEO, m.hasVideo()? 1:0)
                    .add(FavMovieContract.FavMovieEntry.COLUMN_GENRE_IDS, CollectionUtils.concatIntegerArray(m.getGenre_ids()));
        }
        return matrixC;
    }
}
