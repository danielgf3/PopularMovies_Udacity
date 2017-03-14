package com.funnycat.popularmovies.ui.activities;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.funnycat.popularmovies.R;
import com.funnycat.popularmovies.domain.models.Movie;
import com.funnycat.popularmovies.ui.fragments.OnFragmentInteractionListener;
import com.funnycat.popularmovies.ui.fragments.SypnosisFragment;
import com.funnycat.popularmovies.utils.MovieUtil;
import com.funnycat.popularmovies.utils.PMDateUtil;
import com.github.florent37.glidepalette.BitmapPalette;
import com.github.florent37.glidepalette.GlidePalette;

public class DetailMovieActivity extends AppCompatActivity implements OnFragmentInteractionListener{

    private Movie mMovie;

    private ImageView mIvBackground;
    private ImageView mIvCover;
    private ImageView mIvPoster;

    private TextView mTvTitle;
    private TextView mTvVote;
    private TextView mTvVoteAverage;
    private TextView mTvDate;

    private CollapsingToolbarLayout mToolbarLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_movie);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        mToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        mToolbarLayout.setExpandedTitleColor(getResources().getColor(android.R.color.transparent));

        mIvBackground = (ImageView) findViewById(R.id.iv_background);
        mIvCover = (ImageView) findViewById(R.id.iv_cover);
        mIvPoster = (ImageView) findViewById(R.id.iv_poster);
        mTvTitle = (TextView) findViewById(R.id.tv_title);
        mTvVote = (TextView) findViewById(R.id.tv_vote);
        mTvVoteAverage = (TextView) findViewById(R.id.tv_vote_average);
        mTvDate = (TextView) findViewById(R.id.tv_date);


        mMovie = (Movie) getIntent().getSerializableExtra("content");
        mToolbarLayout.setTitle(mMovie.getTitle());

        mTvTitle.setText(mMovie.getTitle());
        mTvVote.setText(String.valueOf(mMovie.getVote_count()));
        mTvVoteAverage.setText(String.valueOf(mMovie.getVote_average()));
        mTvDate.setText(PMDateUtil.dateToString(this, PMDateUtil.getDate(mMovie.getRelease_date()), true));

        loadHeader(MovieUtil.makeImageUrl(mMovie.getBackdrop_path(), true));
        Glide.with(this).load(MovieUtil.makeImageUrl(mMovie.getPoster_path())).into(mIvPoster);

        getSupportFragmentManager().beginTransaction().replace(R.id.content_generic,
                SypnosisFragment.newInstance(0, mMovie.getOverview()), "fragment_0").commit();
    }


    private void loadHeader(String url){
        Glide.with(this).load(url)
                .listener(GlidePalette.with(url)
                        .use(GlidePalette.Profile.MUTED)
                        .intoBackground(mIvBackground)
                        .crossfade(true)

                        .use(GlidePalette.Profile.VIBRANT)
                        .intoTextColor(mTvTitle)

                        .use(GlidePalette.Profile.MUTED)
                        .intoCallBack(new BitmapPalette.CallBack() {
                            @Override
                            public void onPaletteLoaded(@Nullable Palette palette) {
                                mToolbarLayout.setStatusBarScrimColor(palette.getDarkMutedColor(getResources().getColor(R.color.colorPrimaryDark)));
                                mToolbarLayout.setContentScrimColor(palette.getMutedColor(getResources().getColor(R.color.colorPrimary)));
                            }})
                )
                .into(mIvCover);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            supportFinishAfterTransition();
        }
        return true;
    }


    @Override
    public void onFragmentInteraction(Bundle args) {

    }

    @Override
    public void onStartNewActivity(Bundle args, Pair<View, String>... sharedElements) {

    }
}
