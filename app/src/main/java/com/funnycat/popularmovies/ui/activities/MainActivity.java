package com.funnycat.popularmovies.ui.activities;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.funnycat.popularmovies.R;
import com.funnycat.popularmovies.domain.models.MovieListType;
import com.funnycat.popularmovies.ui.fragments.MovieListFragment;
import com.funnycat.popularmovies.ui.fragments.OnFragmentInteractionListener;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, OnFragmentInteractionListener {

    private static String TAG = "MainActivity";

    private MovieListType mCurrentOrder = MovieListType.POPULAR;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        if(savedInstanceState!=null && savedInstanceState.containsKey("currentOrder")) {
            Log.d(TAG, "Teniamos algo");
            mCurrentOrder = MovieListType.valueOf(savedInstanceState.getString("currentOrder"));
            Log.d(TAG, "El currentOrder guardado es: " + mCurrentOrder);
            changeFragment(mCurrentOrder);
        }else{
            navigationView.getMenu().getItem(0).setChecked(true);
            onNavigationItemSelected(navigationView.getMenu().getItem(0));
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("currentOrder", mCurrentOrder.name());
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    private void changeFragment(MovieListType type){

        MovieListFragment fragment = MovieListFragment.newInstance(type.ordinal(), type, getResources().getInteger(R.integer.num_columns));
        getSupportFragmentManager().beginTransaction().replace(R.id.content_generic,
                fragment, type.name()).commit();
        mCurrentOrder = type;
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        Log.d(TAG, "onNavigationItemSelected, id: " + id);
        if (id == R.id.nav_popular_movies) {
            changeFragment(MovieListType.POPULAR);
        }else if(id == R.id.nav_rated_movies) {
            changeFragment(MovieListType.TOP_RATED);
        }else if(id == R.id.nav_favourites_movies){
            changeFragment(MovieListType.FAVOURITES);
        } else if(id == R.id.nav_settings) {
            Toast.makeText(this, R.string.not_yet_implemented, Toast.LENGTH_LONG).show();
        } else if (id == R.id.nav_share) {
            Toast.makeText(this, R.string.not_yet_implemented, Toast.LENGTH_LONG).show();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onFragmentInteraction(Bundle args) {

    }

    @SafeVarargs
    @Override
    public final void onStartNewActivity(Bundle args, Pair<View, String>... sharedElements) {
        Intent intent = new Intent(this, DetailMovieActivity.class);
        intent.putExtra("content", args.getParcelable(OnFragmentInteractionListener.ARG_EXTRA));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(this, sharedElements);
            startActivity(intent, options.toBundle());
        }else startActivity(intent);
    }
}
