package com.funnycat.popularmovies.ui.activities;


import android.content.Intent;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.util.Pair;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.ShareActionProvider;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.bumptech.glide.Glide;
import com.funnycat.popularmovies.R;
import com.funnycat.popularmovies.databinding.ActivityDetailMovieBinding;
import com.funnycat.popularmovies.db.DbDataMapper;
import com.funnycat.popularmovies.db.FavMovieContract.FavMovieEntry;
import com.funnycat.popularmovies.domain.models.Movie;
import com.funnycat.popularmovies.ui.fragments.DetailMovieFragment;
import com.funnycat.popularmovies.ui.fragments.OnFragmentInteractionListener;
import com.funnycat.popularmovies.utils.MovieUtil;
import com.funnycat.popularmovies.utils.PMDateUtil;
import com.github.florent37.glidepalette.BitmapPalette;
import com.github.florent37.glidepalette.GlidePalette;

public class DetailMovieActivity extends AppCompatActivity implements OnFragmentInteractionListener{

    private static final String TAG = "DetailMovieActivity";

    private Movie mMovie;
    private ActivityDetailMovieBinding mBinding;
    private DetailMovieFragment mSypnosisF;

    private int mFav_id = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_movie);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_detail_movie);

        setSupportActionBar(mBinding.toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        mBinding.toolbarLayout.setExpandedTitleColor(getResources().getColor(android.R.color.transparent));

        mMovie = getIntent().getParcelableExtra("content");
        mFav_id = getFavId();
        mBinding.toolbarLayout.setTitle(mMovie.getTitle());

        mBinding.tvTitle.setText(mMovie.getTitle());
        mBinding.tvVote.setText(String.valueOf(mMovie.getVote_count()));
        mBinding.tvVoteAverage.setText(String.valueOf(mMovie.getVote_average()));
        mBinding.tvDate.setText(PMDateUtil.dateToString(this, PMDateUtil.getDate(mMovie.getRelease_date()), true));

        loadHeader(MovieUtil.makeImageUrl(mMovie.getBackdrop_path(), true));
        Glide.with(this).load(MovieUtil.makeImageUrl(mMovie.getPoster_path())).into(mBinding.ivPoster);

        updateFavButton(false);
        mBinding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean success;
                if(mFav_id != -1){
                    success = removeFromFavorite();
                }else{
                    success = addToFavorite();
                }

                if(!success) Snackbar.make(view, "Failed to execute action. Try later.", Snackbar.LENGTH_LONG).show();
                else updateFavButton(true);
            }
        });

        mSypnosisF = DetailMovieFragment.newInstance(mMovie.getId(), mMovie.getOverview());
        getSupportFragmentManager().beginTransaction().replace(R.id.content_generic,
                mSypnosisF, "fragment_0").commit();
    }


    private void loadHeader(String url){
        Glide.with(this).load(url)
                .listener(GlidePalette.with(url)
                        .use(GlidePalette.Profile.MUTED)
                        .intoBackground(mBinding.ivBackground)

                        .use(GlidePalette.Profile.VIBRANT)
                        .intoTextColor(mBinding.tvTitle)

                        .use(GlidePalette.Profile.MUTED)
                        .intoCallBack(new BitmapPalette.CallBack() {
                            @Override
                            public void onPaletteLoaded(@Nullable Palette palette) {
                                mBinding.toolbarLayout.setStatusBarScrimColor(palette.getDarkMutedColor(getResources().getColor(R.color.colorPrimaryDark)));
                                mBinding.toolbarLayout.setContentScrimColor(palette.getMutedColor(getResources().getColor(R.color.colorPrimary)));
                            }})
                )
                .into(mBinding.ivCover);
    }

    private void updateFavButton(boolean animate){
        mBinding.fab.setFavorite(mFav_id!=-1, (mFav_id!=-1) && animate); // first: favourite, second: animate
    }

    private int getFavId(){
        int favID = -1;
        Cursor c = getContentResolver().query(FavMovieEntry.CONTENT_URI, null,
                FavMovieEntry.COLUMN_MOVIE_ID + " = ?", new String[]{String.valueOf(mMovie.getId())}, null);
        if(c!= null && c.getCount() > 0){
            c.moveToFirst();
            favID = c.getInt(c.getColumnIndex(FavMovieEntry._ID));
        }
        if(c!=null && !c.isClosed()) c.close();
        return favID;
    }

    private boolean addToFavorite(){
        Uri uri = getContentResolver().insert(FavMovieEntry.CONTENT_URI, DbDataMapper.convertMoviefromDomain(mMovie));
        if(uri != null) {
            mFav_id = Integer.parseInt(uri.getLastPathSegment());
            return true;
        }
        return false;
    }

    private boolean removeFromFavorite(){
        if(getContentResolver().delete(FavMovieEntry.buildMovieUriWithId(mFav_id), null, null) != 0){
            mFav_id = -1;
            return true;
        }
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            supportFinishAfterTransition();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }




    @Override
    public void onFragmentInteraction(Bundle args) {

    }

    @Override
    public void onStartNewActivity(Bundle args, Pair<View, String>... sharedElements) {

    }
}
