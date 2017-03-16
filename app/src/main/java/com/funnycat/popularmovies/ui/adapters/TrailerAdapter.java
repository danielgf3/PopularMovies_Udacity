package com.funnycat.popularmovies.ui.adapters;


import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.funnycat.popularmovies.R;
import com.funnycat.popularmovies.domain.models.Trailer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by daniel on 18/01/2017.
 */

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.ViewHolder> {

    private static final String TAG = "TrailerAdapter";

    private OnItemClickListener mListener;
    private List<Trailer> mData;

    public TrailerAdapter(List<Trailer> data, OnItemClickListener listener){
        mData = data;
        mListener = listener;
    }

    public TrailerAdapter(){
        this(new ArrayList<Trailer>(), null);
    }

    public TrailerAdapter(OnItemClickListener listener){
        this(new ArrayList<Trailer>(), listener);
    }

    public TrailerAdapter(List<Trailer> data){
        this(data, null);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_trailer, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bindMovie(mData.get(position), mListener);
    }

    public void swapData(List<Trailer> data){
        mData = data;
        if(data!=null) notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mData.size();
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

        void bindMovie(final Trailer trailer, final OnItemClickListener listener){

            mItemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener!=null) listener.onItemClick(trailer, null, null);
                }
            });

            Log.d(TAG, "------ url: " + trailer.getCover());
            Glide.with(mItemView.getContext())
                    .load(trailer.getCover())
//                    .centerCrop()
//                    .override(500, 375)
//                    .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
//                    .placeholder(placeholder)
//                    .error(R.drawable.default_article)
                    .into(mIV_cover);
            mTV_title.setText(trailer.getName());
        }

    }
}
