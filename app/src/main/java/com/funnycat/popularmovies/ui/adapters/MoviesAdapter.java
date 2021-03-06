package com.funnycat.popularmovies.ui.adapters;


import android.database.Cursor;
import android.support.v4.util.Pair;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.funnycat.popularmovies.R;
import com.funnycat.popularmovies.db.DbDataMapper;
import com.funnycat.popularmovies.domain.models.Movie;
import com.funnycat.popularmovies.utils.MovieUtil;

/**
 * Created by daniel on 18/01/2017.
 */

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.ViewHolder> {

    private OnItemClickListener mListener;
    private Cursor mCursor;

    public MoviesAdapter(OnItemClickListener listener){
        mListener = listener;
    }

    public MoviesAdapter(){

    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_movie, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        mCursor.moveToPosition(position);
        holder.bindMovie(mCursor, mListener);
    }

    @Override
    public int getItemCount() {
        int count = 0;
        if(mCursor != null) count = mCursor.getCount();
        return count;
    }

    public Cursor swapCursor(Cursor c){
        // check if this cursor is the same as the previous cursor (mCursor)
        if (mCursor == c) {
            return null; // bc nothing has changed
        }
        Cursor temp = mCursor;
        this.mCursor = c; // new cursor value assigned

        //check if this is a valid cursor, then update the cursor
        if (c != null) {
            this.notifyDataSetChanged();
        }
        return temp;
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        private View mItemView;
        private ImageView mIV_cover;
        private TextView mTV_title;

        ViewHolder(View itemView) {
            super(itemView);
            mItemView = itemView;
            mIV_cover = (ImageView) itemView.findViewById(R.id.iv_poster);
            mTV_title = (TextView) itemView.findViewById(R.id.tv_title);
        }

        void bindMovie(final Cursor cursor, final OnItemClickListener listener){
            final Movie movie = DbDataMapper.convertMovieToDomain(cursor);
            mItemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Pair<View, String> shared_cover = new Pair<View, String>(mIV_cover, "shared_poster");
                    Pair<View, String> shared_title = new Pair<View, String>(mIV_cover, "shared_title");
                    if(listener!=null) listener.onItemClick(movie, shared_cover, shared_title);
                }
            });


            Glide.with(mItemView.getContext()).load(MovieUtil.makeImageUrl(movie.getPoster_path())).into(mIV_cover);
            mTV_title.setText(movie.getTitle());
        }

    }
}
