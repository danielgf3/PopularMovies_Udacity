package com.funnycat.popularmovies.domain.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by daniel on 15/03/2017.
 */

public class Trailer implements Parcelable {
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

    private Trailer(Parcel in){
        this(in.readString(), in.readString(), in.readString());
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(key);
        dest.writeString(name);
        dest.writeString(cover);
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Trailer createFromParcel(Parcel in) {
            return new Trailer(in);
        }

        public Trailer[] newArray(int size) {
            return new Trailer[size];
        }
    };
}
