package com.funnycat.popularmovies.ui.adapters;

import android.view.View;

/**
 * Created by daniel on 18/01/2017.
 */

public interface OnItemClickListener {

    <T> void onItemClick(T item, android.support.v4.util.Pair<View, String>... sharedElements);
}
