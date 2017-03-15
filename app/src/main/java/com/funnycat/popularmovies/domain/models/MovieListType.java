package com.funnycat.popularmovies.domain.models;

/**
 * Created by daniel on 20/01/2017.
 */

public enum MovieListType{
    POPULAR("popular"),
    TOP_RATED("top_rated"),
    FAVOURITES("favourites");
    public String path;
    MovieListType(String path){
        this.path = path;
    }
}