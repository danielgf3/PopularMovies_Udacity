package com.funnycat.popularmovies.ui.adapters;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.funnycat.popularmovies.R;
import com.funnycat.popularmovies.domain.models.Review;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by daniel on 18/01/2017.
 */

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ViewHolder> {

    private static final String TAG = "ReviewAdapter";

    private List<Review> mData;

    public ReviewAdapter(List<Review> data){
        mData = data;
    }

    public ReviewAdapter(){
        this(new ArrayList<Review>());
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_review, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bindMovie(mData.get(position));
    }

    public void swapData(List<Review> data){
        mData = data;
        if(data!=null) notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        private TextView mTv_content;
        private TextView mTV_author;

        ViewHolder(View itemView) {
            super(itemView);
            mTv_content = (TextView) itemView.findViewById(R.id.tv_content);
            mTV_author = (TextView) itemView.findViewById(R.id.tv_author);
        }

        void bindMovie(final Review review){
            mTv_content.setText(review.getContent());
            mTV_author.setText(review.getAuthor());
        }

    }
}
