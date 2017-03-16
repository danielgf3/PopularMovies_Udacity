package com.funnycat.popularmovies.ui.fragments;

import android.os.Bundle;
import android.support.v4.util.Pair;
import android.view.View;

/**
 * Created by daniel on 20/01/2017.
 */

public interface OnFragmentInteractionListener {
    String ACTION = "action";
    String ARG_EXTRA = "extra";

    void onFragmentInteraction(Bundle args);
    void onStartNewActivity(Bundle args, Pair<View, String>... sharedElements);
}
