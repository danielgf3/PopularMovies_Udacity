package com.funnycat.popularmovies.ui.fragments;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v4.util.Pair;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.ShareActionProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.funnycat.popularmovies.R;
import com.funnycat.popularmovies.connectivity.Reviews_Request;
import com.funnycat.popularmovies.connectivity.Trailers_Request;
import com.funnycat.popularmovies.domain.models.Review;
import com.funnycat.popularmovies.domain.models.Trailer;
import com.funnycat.popularmovies.ui.adapters.OnItemClickListener;
import com.funnycat.popularmovies.ui.adapters.ReviewAdapter;
import com.funnycat.popularmovies.ui.adapters.TrailerAdapter;

import java.util.List;


public class DetailMovieFragment extends Fragment implements OnItemClickListener{

    private static final String TAG = "DetailMovieFragment";

    private static final String ARG_ID = "id";
    private static final String ARG_SYPNOSIS = "sypnosis";

    private int mId;
    private String mSypnosis;

    private View mTrailerTitle, mReviewTitle;
    private View mFatherTrailers, mFatherReviews;

    private RecyclerView mRecyclerViewT, mRecyclerViewR;
    private ShareActionProvider mShareActionProvider;
    private MenuItem mShareItem;

    private TrailerAdapter mTAdapter;
    private ReviewAdapter mRAdapter;



    private OnFragmentInteractionListener mListener;

    private static final int ID_TRAILERS = 1000;
    private static final int ID_REVIEWS = 2000;

    public DetailMovieFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param id Parameter 1.
     * @param sypnosis Parameter 2.
     * @return A new instance of fragment DetailMovieFragment.
     */
    public static DetailMovieFragment newInstance(int id, String sypnosis) {
        DetailMovieFragment fragment = new DetailMovieFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_ID, id);
        args.putString(ARG_SYPNOSIS, sypnosis);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mId = getArguments().getInt(ARG_ID);
            mSypnosis = getArguments().getString(ARG_SYPNOSIS);
        }
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_detail_movie, container, false);
        TextView tvSypnosis = (TextView) view.findViewById(R.id.tv_text_synopsis);
        tvSypnosis.setText(mSypnosis);

        mTrailerTitle = view.findViewById(R.id.tv_title_trailer);
        mReviewTitle = view.findViewById(R.id.tv_title_review);

        // Trailers
        mFatherTrailers = view.findViewById(R.id.l_trailers);
        mRecyclerViewT = (RecyclerView) mFatherTrailers.findViewById(R.id.list);
        mRecyclerViewT.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, true));
        mRecyclerViewT.setAdapter(mTAdapter = new TrailerAdapter(this));

        // Reviews
        mFatherReviews = view.findViewById(R.id.l_reviews);
        mRecyclerViewR = (RecyclerView) mFatherReviews.findViewById(R.id.list);
        mRecyclerViewR.setAdapter(mRAdapter = new ReviewAdapter());

        initLoader(mId, ID_TRAILERS);

        return view;
    }

    private void initLoader(int id, int type){
        LoaderManager loader = getActivity().getSupportLoaderManager();
        Loader<Cursor> movieLoader = loader.getLoader(id + type);
        if(movieLoader==null){
            loader.initLoader(id + type, null, (type==ID_TRAILERS)? mTrailerLoader : mReviewLoader);
        }else{
            loader.restartLoader(id + type, null, (type==ID_TRAILERS)? mTrailerLoader : mReviewLoader);
        }
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_detail_movie, menu);
        mShareItem = menu.findItem(R.id.action_share);
        mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(mShareItem);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }


    private void configureShareActionProvider(String trailerKey) {
        mShareItem.setVisible(true);
        Intent myShareIntent = new Intent(Intent.ACTION_SEND);
        myShareIntent.setType("text/plain");
        myShareIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.subject_share));
        String extra_text = "https://www.youtube.com/watch?v="+ trailerKey + " " + getString(R.string.vt_hashtag);
        Log.d(TAG, "vamos a compartir la url: " + extra_text);
        myShareIntent.putExtra(Intent.EXTRA_TEXT, extra_text);
        mShareActionProvider.setShareIntent(myShareIntent);
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        mTAdapter.swapData(null);
        mRAdapter.swapData(null);
    }

    private void changeVisibilityStartLoading(int type){
        View father = (type==ID_TRAILERS)? mFatherTrailers : mFatherReviews;
        father.findViewById(R.id.list).setVisibility(View.GONE);
        father.findViewById(R.id.pb_loading).setVisibility(View.VISIBLE);
        father.findViewById(R.id.ll_empty_layout).setVisibility(View.GONE);
        father.findViewById(R.id.ll_error_layout).setVisibility(View.GONE);
    }

    private void changeVisibilityFinishLoading(final int type, boolean success, boolean isEmpty){
        View father = (type==ID_TRAILERS)? mFatherTrailers : mFatherReviews;

        father.findViewById(R.id.pb_loading).setVisibility(View.GONE);
        father.findViewById(R.id.list).setVisibility((isEmpty)?View.GONE : View.VISIBLE);
        father.findViewById(R.id.ll_empty_layout).setVisibility(View.GONE);
        father.findViewById(R.id.ll_error_layout).setVisibility((!success && isEmpty)?View.VISIBLE : View.GONE);

        if(!success){
            father.findViewById(R.id.ll_error_layout).findViewById(R.id.bt_retry).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    initLoader(mId, type);
                }
            });
        }else{
            if(type == ID_TRAILERS) mTrailerTitle.setVisibility((isEmpty)? View.GONE : View.VISIBLE);
            else mReviewTitle.setVisibility((isEmpty)? View.GONE: View.VISIBLE);
        }
    }

    @SafeVarargs
    @Override
    public final <T> void onItemClick(T item, Pair<View, String>... sharedElements) {
        Trailer trailer = (Trailer) item;
        Bundle b = new Bundle();
        b.putString("trailer_key", trailer.getKey());
        mListener.onStartNewActivity(b, sharedElements);
    }

    private LoaderManager.LoaderCallbacks<List<Trailer>> mTrailerLoader = new LoaderManager.LoaderCallbacks<List<Trailer>>() {
        @Override
        public Loader<List<Trailer>> onCreateLoader(int id, Bundle args) {
            if(id == mId + ID_TRAILERS){
                Log.d(TAG, "onCreateLoader, id: " + id);
                return new AsyncTaskLoader<List<Trailer>>(getContext()) {

                    List<Trailer> mData = null;

                    @Override
                    protected void onStartLoading() {
                        if (mData != null) {
                            deliverResult(mData);
                        } else {
                            changeVisibilityStartLoading(ID_TRAILERS);
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
            if(data!=null){
                mTAdapter.swapData(data);
                mRecyclerViewT.smoothScrollToPosition(0);
                if(!data.isEmpty()) configureShareActionProvider(data.get(0).getKey());
            }
            changeVisibilityFinishLoading(ID_TRAILERS, data!=null, (data==null) || data.size() == 0);

            initLoader(mId, ID_REVIEWS);
        }

        @Override
        public void onLoaderReset(Loader<List<Trailer>> loader) {

        }
    };



    private LoaderManager.LoaderCallbacks<List<Review>> mReviewLoader = new LoaderManager.LoaderCallbacks<List<Review>>() {
        @Override
        public Loader<List<Review>> onCreateLoader(int id, Bundle args) {
            if(id == mId + ID_REVIEWS){
                Log.d(TAG, "onCreateLoader, id: " + id);
                return new AsyncTaskLoader<List<Review>>(getContext()) {

                    List<Review> mData = null;

                    @Override
                    protected void onStartLoading() {
                        if (mData != null) {
                            deliverResult(mData);
                        } else {
                            changeVisibilityStartLoading(ID_REVIEWS);
                            forceLoad();
                        }
                    }

                    @Override
                    public List<Review> loadInBackground() {
                        return new Reviews_Request(mId).execute();
                    }

                    @Override
                    public void deliverResult(List<Review> data) {
                        mData = data;
                        super.deliverResult(data);
                    }
                };

            }else throw new RuntimeException("Loader Not Implemented: " + id);
        }

        @Override
        public void onLoadFinished(Loader<List<Review>> loader, List<Review> data) {
            if(data!=null) mRAdapter.swapData(data);
            changeVisibilityFinishLoading(ID_REVIEWS, data!=null, (data==null) || data.size() == 0);
        }

        @Override
        public void onLoaderReset(Loader<List<Review>> loader) {

        }
    };

}
