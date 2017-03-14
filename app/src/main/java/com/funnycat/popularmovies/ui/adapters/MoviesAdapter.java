package com.funnycat.popularmovies.ui.adapters;

import android.support.v4.util.Pair;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.funnycat.popularmovies.R;
import com.funnycat.popularmovies.domain.models.Movie;
import com.funnycat.popularmovies.utils.MovieUtil;

import java.util.List;

/**
 * Created by daniel on 18/01/2017.
 */

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.ViewHolder> {

    private OnItemClickListener mListener;
    private List<Movie> mItems;

    public MoviesAdapter(List<Movie> items, OnItemClickListener listener){
        mItems = items;
        mListener = listener;
    }

    public MoviesAdapter(List<Movie> items){
        this(items, null);
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_movie, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bindMovie(mItems.get(position), mListener);
    }

    @Override
    public int getItemCount() {
        return mItems.size();
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

        void bindMovie(final Movie movie, final OnItemClickListener listener){
            mItemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Pair<View, String> shared_cover = new Pair<View, String>(mIV_cover, "shared_poster");
                    Pair<View, String> shared_title = new Pair<View, String>(mIV_cover, "shared_title");
                    if(listener!=null) listener.onItemClick(movie, shared_cover, shared_title);
                }
            });


            Glide.with(mItemView.getContext())
                    .load(MovieUtil.makeImageUrl(movie.getPoster_path()))
//                    .centerCrop()
                    /*.override(500, 375)*/
//                    .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                    /*.placeholder(placeholder)
                    .error(R.drawable.default_article)*/
                    .into(mIV_cover);
            mTV_title.setText(movie.getTitle());
        }

    }
}
