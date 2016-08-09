package com.ab.popularmovies.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ab.popularmovies.R;
import com.ab.popularmovies.Utils;
import com.ab.popularmovies.model.Review;
import com.ab.popularmovies.model.Trailer;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by q4J1X056 on 09-08-2016.
 */
public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.ViewHolder> {
    List<Review> reviewList;
    Context mContext;


    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView textViewAuthor;
        TextView textViewContent;
        public ViewHolder(View v) {
            super(v);

            textViewAuthor = (TextView) v.findViewById(R.id.textViewReviewAuthor);
            textViewContent = (TextView) v.findViewById(R.id.textViewReviewContent);
        }
    }


    public ReviewsAdapter(Context context, List<Review> reviewList)
    {
        mContext=context;
        this.reviewList=reviewList;
    }


    @Override
    public ReviewsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_review, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ReviewsAdapter.ViewHolder holder, int position) {
        holder.textViewAuthor.setText(reviewList.get(position).author);
        holder.textViewContent.setText(reviewList.get(position).content);
    }

    @Override
    public int getItemCount() {
        return reviewList.size();
    }
}
