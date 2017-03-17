package com.funnycat.popularmovies.utils;

import android.content.Context;

import com.bumptech.glide.Glide;

/**
 * Created by daniel on 17/03/2017.
 */

public class Util {

    public static void clearGlide(Context context){
        Glide.get(context).clearMemory();
    }
}
