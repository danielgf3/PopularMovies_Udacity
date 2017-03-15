package com.funnycat.popularmovies.domain.models;

/**
 * Created by daniel on 15/03/2017.
 */

public class Trailer {
    private String key;
    private String name;
    private String cover;

    public Trailer(String key, String name) {
        this(key, name, null);
    }

    public Trailer(String key, String name, String cover) {
        this.key = key;
        this.name = name;
        this.cover = cover;
    }

    public String getKey() {
        return key;
    }

    public String getName() {
        return name;
    }

    public void setCover(String cover){
        this.cover = cover;
    }

    public String getCover(){
        return cover;
    }
}
