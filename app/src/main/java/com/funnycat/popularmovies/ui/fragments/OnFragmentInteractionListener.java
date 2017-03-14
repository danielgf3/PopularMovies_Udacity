package com.funnycat.popularmovies.ui.fragments;

import android.os.Bundle;
import android.support.v4.util.Pair;
import android.view.View;

/**
 * Created by daniel on 20/01/2017.
 */

public interface OnFragmentInteractionListener {
    String ACTION = "action";
    String ACTION_CHARGE_INIT_DATA = "charge_init_data";
    String ARG_EXTRA = "extra";

    void onFragmentInteraction(Bundle args);
    void onStartNewActivity(Bundle args, Pair<View, String>... sharedElements);
}
