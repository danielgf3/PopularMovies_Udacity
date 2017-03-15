package com.funnycat.popularmovies.ui.fragments;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v4.util.Pair;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.funnycat.popularmovies.R;
import com.funnycat.popularmovies.connectivity.Trailers_Request;
import com.funnycat.popularmovies.domain.models.Trailer;
import com.funnycat.popularmovies.ui.adapters.OnItemClickListener;
import com.funnycat.popularmovies.ui.adapters.TrailerAdapter;
import com.google.android.youtube.player.YouTubeIntents;

import java.util.List;


public class DetailMovieFragment extends Fragment implements OnItemClickListener, LoaderManager.LoaderCallbacks<List<Trailer>>{

    private static final String TAG = "DetailMovieFragment";

    private static final String ARG_ID = "id";
    private static final String ARG_SYPNOSIS = "sypnosis";
    private static final String ARG_HAS_TRAILERS = "hasTrailers";

    private int mId;
    private String mSypnosis;
    private boolean mHasTrailer;

    private ProgressBar mProgressB;
    private LinearLayout mEmptyLayout;
    private RecyclerView mRecyclerView;
    private TextView mTitleTrailer;

    private TrailerAdapter mAdapter;

    private OnFragmentInteractionListener mListener;

    public DetailMovieFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param id Parameter 1.
     * @param sypnosis Parameter 2.
     * @param hasTrailers Parameter 2.
     * @return A new instance of fragment DetailMovieFragment.
     */
    public static DetailMovieFragment newInstance(int id, String sypnosis, boolean hasTrailers) {
        DetailMovieFragment fragment = new DetailMovieFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_ID, id);
        args.putString(ARG_SYPNOSIS, sypnosis);
        args.putBoolean(ARG_HAS_TRAILERS, hasTrailers);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mId = getArguments().getInt(ARG_ID);
            mSypnosis = getArguments().getString(ARG_SYPNOSIS);
            mHasTrailer = getArguments().getBoolean(ARG_HAS_TRAILERS);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_detail_movie, container, false);
        TextView tvSypnosis = (TextView) view.findViewById(R.id.tv_text_synopsis);
        tvSypnosis.setText(mSypnosis);

        mTitleTrailer = (TextView) view.findViewById(R.id.tv_title_trailer);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.list);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, true));
        mRecyclerView.setAdapter(mAdapter = new TrailerAdapter(this));

        mProgressB = (ProgressBar) view.findViewById(R.id.pb_loading);
        mEmptyLayout = (LinearLayout) view.findViewById(R.id.ll_empty_layout);
        changeVisibilityStartLoading();
        LoaderManager loader = getActivity().getSupportLoaderManager();

        Loader<Cursor> movieLoader = loader.getLoader(mId);
        if(movieLoader==null){
            loader.initLoader(mId, null, this);
        }else{
            loader.restartLoader(mId, null, this);
        }
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
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
        mTitleTrailer.setVisibility(View.GONE);
    }

    private void changeVisibilityFinishLoading(boolean success, boolean isEmpty){
        mProgressB.setVisibility(View.GONE);
        mRecyclerView.setVisibility((isEmpty)?View.GONE : View.VISIBLE);
        mEmptyLayout.setVisibility((!success && isEmpty)?View.VISIBLE : View.GONE);
        mTitleTrailer.setVisibility((isEmpty)?View.GONE : View.VISIBLE);
    }

    @Override
    public Loader<List<Trailer>> onCreateLoader(int id, Bundle args) {
        if(mId == id){
            Log.d(TAG, "onCreateLoader, id: " + id);
            return new AsyncTaskLoader<List<Trailer>>(getContext()) {

                List<Trailer> mData = null;

                @Override
                protected void onStartLoading() {
                    if (mData != null) {
                        deliverResult(mData);
                    } else {
                        forceLoad();
                    }
                }

                @Override
                public List<Trailer> loadInBackground() {
                    return new Trailers_Request(mId).execute();
                }

                @Override
                public void deliverResult(List<Trailer> data) {
                    mData = data;
                    super.deliverResult(data);
                }
            };

        }else throw new RuntimeException("Loader Not Implemented: " + id);
    }

    @Override
    public void onLoadFinished(Loader<List<Trailer>> loader, List<Trailer> data) {
        mAdapter.swapData(data);
        mRecyclerView.smoothScrollToPosition(0);
        changeVisibilityFinishLoading(data!=null, mAdapter.getItemCount() == 0);
    }

    @Override
    public void onLoaderReset(Loader<List<Trailer>> loader) {

    }

    //Todo enviar a la acitivity
    @Override
    public <T> void onItemClick(T item, Pair<View, String>... sharedElements) {
        Trailer trailer = (Trailer) item;
        try {
            startActivity(YouTubeIntents.createPlayVideoIntentWithOptions(getContext(), trailer.getKey(), true, true));
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
        }
    }
}
