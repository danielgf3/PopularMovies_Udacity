package com.funnycat.popularmovies.db;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by daniel on 14/03/2017.
 */

public class FavMovieContract {

    public static final String CONTENT_AUTHORITY = "com.funnycat.popularmovies";

    private static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_FAV_MOVIE = "fav_movie";

    public static final class FavMovieEntry implements BaseColumns{

        /* The base CONTENT_URI used to query the Weather table from the content provider */
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_FAV_MOVIE)
                .build();

        public static final String TABLE_NAME = "fav_movies";

        public static final String COLUMN_MOVIE_ID = "movie_id";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_ORIGINAL_TITLE = "original_title";
        public static final String COLUMN_ORIGINAL_LANGUAGE = "original_language";
        public static final String COLUMN_RELEASE_DATE = "release_date";
        public static final String COLUMN_ADULT = "is_adult";
        public static final String COLUMN_POSTER_PATH = "poster_path";
        public static final String COLUMN_OVERVIEW = "overview";
        public static final String COLUMN_BACKDROP_PATH = "backdrop_path";
        public static final String COLUMN_VOTE_COUNT = "vote_count";
        public static final String COLUMN_VOTE_AVERAGE = "vote_average";
        public static final String COLUMN_POPULARITY = "popularity";
        public static final String COLUMN_HAS_VIDEO = "has_video";
        public static final String COLUMN_GENRE_IDS = "genre_ids";


        public static final String DEFAULT_SORT = COLUMN_TITLE + " ASC";


        /**
         * Builds a URI that adds the movie_id to the end of content URI path.
         * This is used to query details about a single movie entry by id. This is what we
         * use for the detail view query.
         *
         * @param id
         * @return Uri to query details about a single movie entry
         */
        public static final Uri buildMovieUriWithId(int id) {
            return CONTENT_URI.buildUpon()
                    .appendPath(Integer.toString(id))
                    .build();
        }
    }


}
