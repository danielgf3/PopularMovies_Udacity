package com.funnycat.popularmovies.ui.fragments;

import android.content.Context;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.util.Pair;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.funnycat.popularmovies.R;
import com.funnycat.popularmovies.connectivity.Movies_Request;
import com.funnycat.popularmovies.db.FavMovieContract;
import com.funnycat.popularmovies.domain.models.Movie;
import com.funnycat.popularmovies.domain.models.MovieListType;
import com.funnycat.popularmovies.ui.adapters.MoviesAdapter;
import com.funnycat.popularmovies.ui.adapters.OnItemClickListener;
import com.funnycat.popularmovies.utils.CollectionUtils;
import com.funnycat.popularmovies.utils.MovieUtil;

import java.util.ArrayList;
import java.util.List;

public class MovieListFragment extends Fragment implements OnItemClickListener, LoaderManager.LoaderCallbacks<Cursor>{

    private static final String TAG= "MovieListFragment";


    public static final String ARG_COLUMN_COUNT = "column-count";
    public static final String ARG_ID = "id";
    public static final String ARG_TYPE = "type";

    private static int DEFAULT_COLUMN_COUNT = 2;

    private int mId;
    private MovieListType mType;
    private int mColumnCount;

    private ProgressBar mProgressB;
    private LinearLayout mEmptyLayout;
    private LinearLayout mErrorLayout;
    private RecyclerView mRecyclerView;


    private MoviesAdapter mAdapter;


    private OnFragmentInteractionListener mListener;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public MovieListFragment() {

    }


    public static MovieListFragment newInstance(int id, MovieListType type, int columnCount) {
        MovieListFragment fragment = new MovieListFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_ID, id);
        args.putString(ARG_TYPE, type.name());
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    public static MovieListFragment newInstance(int id, MovieListType type) {
        return newInstance(id, type, DEFAULT_COLUMN_COUNT);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mId = getArguments().getInt(ARG_ID);
            mType = MovieListType.valueOf(getArguments().getString(ARG_TYPE));
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movie_list, container, false);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.list);

        if (mColumnCount <= 1) mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        else mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), mColumnCount));

        mRecyclerView.setAdapter(mAdapter = new MoviesAdapter(this));
        mRecyclerView.setHasFixedSize(true);
        mProgressB = (ProgressBar) view.findViewById(R.id.pb_loading);
        mEmptyLayout = (LinearLayout) view.findViewById(R.id.ll_empty_layout);
        mErrorLayout = (LinearLayout) view.findViewById(R.id.ll_error_layout);

        initLoader();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
//        initLoader();
    }

    private void initLoader(){
        LoaderManager loader = getActivity().getSupportLoaderManager();
        Loader<Cursor> movieLoader = loader.getLoader(mType.ordinal());
        if(movieLoader==null){
            loader.initLoader(mType.ordinal(), null, this);
        }else{
            loader.restartLoader(mType.ordinal(), null, this);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    private void changeVisibilityStartLoading(){
        mProgressB.setVisibility(View.VISIBLE);
        mRecyclerView.setVisibility(View.GONE);
        mEmptyLayout.setVisibility(View.GONE);
        mErrorLayout.setVisibility(View.GONE);
    }

    private void changeVisibilityFinishLoading(boolean success, boolean isEmpty){
        mProgressB.setVisibility(View.GONE);
        mRecyclerView.setVisibility((isEmpty)?View.GONE : View.VISIBLE);
        mEmptyLayout.setVisibility((isEmpty && success)?View.VISIBLE : View.GONE);
        mErrorLayout.setVisibility((!success)? View.VISIBLE : View.GONE);
        if(!success){
            mErrorLayout.findViewById(R.id.bt_retry).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    initLoader();
                }
            });
        }
    }

    @Override
    public <T> void onItemClick(T item, Pair<View, String>... sharedElements) {
        Bundle args = new Bundle();
        args.putInt(ARG_ID, mId);
        args.putParcelable(OnFragmentInteractionListener.ARG_EXTRA, (Movie) item);
        if(mListener != null) mListener.onStartNewActivity(args, sharedElements);
    }

    @Override
    public Loader<Cursor> onCreateLoader(final int id, Bundle args) {
        if(id == mId) {
            Log.d(TAG, "onCreateLoader, id: " + id);

            if (id == MovieListType.FAVOURITES.ordinal()) {
                Uri movieQueryUri = FavMovieContract.FavMovieEntry.CONTENT_URI;
                return new CursorLoader(getContext(), movieQueryUri, null, null, null, FavMovieContract.FavMovieEntry.DEFAULT_SORT);
            } else {
                return new AsyncTaskLoader<Cursor>(getContext()) {
                    Cursor mData = null;

                    @Override
                    protected void onStartLoading() {
                        if (mData != null) {
                            deliverResult(mData);
                        } else {
                            changeVisibilityStartLoading();
                            forceLoad();
                        }
                    }

                    @Override
                    public Cursor loadInBackground() {
                        return MovieUtil.convertListToCursor(new Movies_Request(mType).execute());
                    }

                    @Override
                    public void deliverResult(Cursor data) {
                        mData = data;
                        super.deliverResult(data);
                    }
                };
            }

        }else throw new RuntimeException("Loader Not Implemented: " + id);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mAdapter.swapCursor(data);
        changeVisibilityFinishLoading(data != null, mAdapter.getItemCount() == 0);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
    }
}
