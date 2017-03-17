package com.funnycat.popularmovies.domain.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.funnycat.popularmovies.utils.CollectionUtil;

/**
 * Created by daniel on 18/01/2017.
 */


public class Movie implements Parcelable{
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

    private Movie(Parcel in){
        this(in.readInt(), in.readString(), in.readString(), in.readString(), in.readString(),
                (in.readInt()==1), in.readString(), in.readString(), in.readString(), in.readInt(),
                in.readFloat(), in.readFloat(), (in.readInt()==1), CollectionUtil.splitIntegerArray(in.readString()));
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

    public void setPoster_path(String poster_path){
        this.poster_path = poster_path;
    }

    public String getOverview() {
        return overview;
    }

    public String getBackdrop_path() {
        return backdrop_path;
    }

    public void setBackdrop_path(String backdrop_path){
        this.backdrop_path = backdrop_path;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(title);
        dest.writeString(original_title);
        dest.writeString(original_language);
        dest.writeString(release_date);
        dest.writeInt(isAdult? 1 : 0);
        dest.writeString(poster_path);
        dest.writeString(overview);
        dest.writeString(backdrop_path);
        dest.writeInt(vote_count);
        dest.writeFloat(vote_average);
        dest.writeFloat(popularity);
        dest.writeInt(hasVideo? 1 : 0);
        dest.writeString(CollectionUtil.concatIntegerArray(genre_ids));
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    public Movie copy(){
        return new Movie(id, title, original_title, original_language,
                release_date, isAdult, poster_path, overview,
                backdrop_path, vote_count, vote_average, popularity,
                hasVideo, genre_ids);
    }
}
