package com.funnycat.popularmovies.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import com.funnycat.popularmovies.domain.models.Movie;
import com.funnycat.popularmovies.ui.adapters.MoviesAdapter;
import com.funnycat.popularmovies.ui.adapters.OnItemClickListener;

import java.util.ArrayList;
import java.util.List;

public class MovieListFragment extends Fragment implements OnItemClickListener{

    private static final String TAG= "MovieListFragment";


    public static final String ARG_COLUMN_COUNT = "column-count";
    public static final String ARG_ID = "id";

    private static int DEFAULT_COLUMN_COUNT = 2;

    private int mId;
    private int mColumnCount;

    private ProgressBar mProgressB;
    private LinearLayout mEmptyLayout;
    private RecyclerView mRecyclerView;


    private List<Movie> mList = new ArrayList<>();
    private MoviesAdapter mAdapter;


    private OnFragmentInteractionListener mListener;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public MovieListFragment() {
    }


    public static MovieListFragment newInstance(int id, int columnCount) {
        MovieListFragment fragment = new MovieListFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_ID, id);
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    public static MovieListFragment newInstance(int id) {
        return newInstance(id, DEFAULT_COLUMN_COUNT);
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mId = getArguments().getInt(ARG_ID);
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movie_list, container, false);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.list);
        if (mColumnCount <= 1) {
            mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        } else {
            mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), mColumnCount));
        }
        mRecyclerView.setAdapter(mAdapter = new MoviesAdapter(mList, this));
        mProgressB = (ProgressBar) view.findViewById(R.id.pb_loading);
        mEmptyLayout = (LinearLayout) view.findViewById(R.id.ll_empty_layout);

        Button btRetry = (Button) view.findViewById(R.id.bt_retry);
        btRetry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onChargeInitData();
            }
        });

        onChargeInitData();
        return view;
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

    private void onChargeInitData(){
        changeVisibilityStartLoading();
        Bundle b = new Bundle();
        b.putInt(ARG_ID, mId);
        b.putString(OnFragmentInteractionListener.ACTION, OnFragmentInteractionListener.ACTION_CHARGE_INIT_DATA);
        if(mListener!=null) mListener.onFragmentInteraction(b);
    }

    public void chargeData(List<Movie> movies){
        Log.d(TAG, "chargeInitData");
        boolean isEmpty = movies==null || movies.isEmpty();
        if(!isEmpty){
            Log.d(TAG, "movies!=null");
            mList.clear();
            mList.addAll(movies);
            mAdapter.notifyDataSetChanged();
        }
        changeVisibilityFinishLoading(isEmpty);
    }

    private void changeVisibilityStartLoading(){
        mProgressB.setVisibility(View.VISIBLE);
        mRecyclerView.setVisibility(View.GONE);
        mEmptyLayout.setVisibility(View.GONE);
    }

    private void changeVisibilityFinishLoading(boolean isEmpty){
        mProgressB.setVisibility(View.GONE);
        mRecyclerView.setVisibility((isEmpty)?View.GONE : View.VISIBLE);
        mEmptyLayout.setVisibility((isEmpty)?View.VISIBLE : View.GONE);
    }

    @Override
    public <T> void onItemClick(T item, Pair<View, String>... sharedElements) {
        Bundle args = new Bundle();
        args.putInt(ARG_ID, mId);
        args.putSerializable(OnFragmentInteractionListener.ARG_EXTRA, (Movie) item);
        if(mListener != null) mListener.onStartNewActivity(args, sharedElements);
    }

}
