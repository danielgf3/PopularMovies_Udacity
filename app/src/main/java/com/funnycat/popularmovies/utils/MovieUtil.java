package com.funnycat.popularmovies.utils;

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
}
