package com.funnycat.popularmovies.domain.models;

import java.io.Serializable;

/**
 * Created by daniel on 18/01/2017.
 */

// TODO cambiar a Parcelable

public class Movie implements Serializable{
    private int id;
    private String title;
    private String original_title;
    private String original_language;
    private String release_date;
    private boolean isAdult;
    private String poster_path;
    private String overview;
    private String backdrop_path;
    private int vote_count;
    private float vote_average;
    private float popularity;
    private boolean hasVideo;
    private Integer[] genre_ids;

    public Movie(int id, String title, String original_title, String original_language,
                 String release_date, boolean isAdult, String poster_path, String overview,
                 String backdrop_path, int vote_count, float vote_average, float popularity,
                 boolean hasVideo, Integer[] genre_ids) {
        this.id = id;
        this.title = title;
        this.original_title = original_title;
        this.original_language = original_language;
        this.release_date = release_date;
        this.isAdult = isAdult;
        this.poster_path = poster_path;
        this.overview = overview;
        this.backdrop_path = backdrop_path;
        this.vote_count = vote_count;
        this.vote_average = vote_average;
        this.popularity = popularity;
        this.hasVideo = hasVideo;
        this.genre_ids = genre_ids;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getOriginal_title() {
        return original_title;
    }

    public String getOriginal_language() {
        return original_language;
    }

    public String getRelease_date() {
        return release_date;
    }

    public boolean isAdult() {
        return isAdult;
    }

    public String getPoster_path() {
        return poster_path;
    }

    public String getOverview() {
        return overview;
    }

    public String getBackdrop_path() {
        return backdrop_path;
    }

    public int getVote_count() {
        return vote_count;
    }

    public float getVote_average() {
        return vote_average;
    }

    public float getPopularity() {
        return popularity;
    }

    public boolean hasVideo() {
        return hasVideo;
    }

    public Integer[] getGenre_ids() {
        return genre_ids;
    }
}
