package com.funnycat.popularmovies.ui.activities;

import android.content.Intent;
import android.os.AsyncTask;
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
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.funnycat.popularmovies.R;
import com.funnycat.popularmovies.connectivity.Movies_Request;
import com.funnycat.popularmovies.domain.models.Movie;
import com.funnycat.popularmovies.domain.models.MovieListType;
import com.funnycat.popularmovies.ui.fragments.MovieListFragment;
import com.funnycat.popularmovies.ui.fragments.OnFragmentInteractionListener;

import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, OnFragmentInteractionListener {

    private static String TAG = "MainActivity";

    private static int SORT_BY_POPULAR = R.id.sort_popular;
    private static int SORT_BY_TOP_RATED = R.id.sort_rated;


    private MovieListFragment mFragment;
    private int mCurrentOrder = SORT_BY_POPULAR;


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

        mFragment = MovieListFragment.newInstance(0, 2);
        getSupportFragmentManager().beginTransaction().replace(R.id.content_generic,
                mFragment, "fragment_0").commit();
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
        int id = item.getItemId();

        Log.d(TAG, "onOptionsItemSelected, id: " + id);
        if (id == R.id.sort_popular || id == R.id.sort_rated){
            Log.d(TAG, "Vamos a ordenar");
            if(id != mCurrentOrder) {
                if (id == R.id.sort_popular)
                    new LoadMoviesData(mFragment).execute(MovieListType.POPULAR);
                else new LoadMoviesData(mFragment).execute(MovieListType.TOP_RATED);
            }
            mCurrentOrder = id;
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        Log.d(TAG, "onNavigationItemSelected, id: " + id);
        if (id == R.id.nav_movies) {

        } else if(id == R.id.nav_settings) {
            Toast.makeText(this, R.string.not_yet_implemented, Toast.LENGTH_LONG).show();
        } else if (id == R.id.nav_share) {
            Toast.makeText(this, R.string.not_yet_implemented, Toast.LENGTH_LONG).show();
        } else if (id == R.id.nav_send) {
            Toast.makeText(this, R.string.not_yet_implemented, Toast.LENGTH_LONG).show();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);


    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public void onFragmentInteraction(Bundle args) {
        if(args.containsKey(OnFragmentInteractionListener.ACTION) && args.getString(OnFragmentInteractionListener.ACTION) !=null) {
            switch (args.getString(OnFragmentInteractionListener.ACTION)) {
                case OnFragmentInteractionListener.ACTION_CHARGE_INIT_DATA:
                    new LoadMoviesData(mFragment).execute(MovieListType.POPULAR);
            }
        }
    }

    @Override
    public void onStartNewActivity(Bundle args, Pair<View, String>... sharedElements) {
        Intent intent = new Intent(this, DetailMovieActivity.class);
        intent.putExtra("content", args.getSerializable(OnFragmentInteractionListener.ARG_EXTRA));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(this, sharedElements);
            startActivity(intent, options.toBundle());
        }else startActivity(intent);
    }


    class LoadMoviesData extends AsyncTask<MovieListType, Void, List<Movie>>{

        private String TAG = "LoadMoviesData";
        private MovieListFragment mMovieListF;

        LoadMoviesData(MovieListFragment movieListF){
            mMovieListF = movieListF;
        }

        @Override
        protected List<Movie> doInBackground(MovieListType... params) {
            Log.d(TAG, "doInBackground");
            if(params.length>0) return new Movies_Request(params[0]).execute();
            else return null;
        }

        @Override
        protected void onPostExecute(List<Movie> movies) {
            super.onPostExecute(movies);
            Log.d(TAG, "onPostExecute");
            mMovieListF.chargeData(movies);
        }
    }
}
