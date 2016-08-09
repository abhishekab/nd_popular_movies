package com.ab.popularmovies.adapters;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ab.popularmovies.R;
import com.ab.popularmovies.Utils;
import com.ab.popularmovies.model.Trailer;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by q4J1X056 on 09-08-2016.
 */
public class TrailersAdapter extends RecyclerView.Adapter<TrailersAdapter.ViewHolder> {
    List<Trailer> trailerList;
    Context mContext;


    public static class ViewHolder extends RecyclerView.ViewHolder {
        View self;
        ImageView imageViewTrailer;
        public ViewHolder(View v) {
            super(v);
            self=v;
            imageViewTrailer = (ImageView) v.findViewById(R.id.imageViewTrailer);
        }
    }


    public TrailersAdapter(Context context,List<Trailer> trailerList)
    {
        mContext=context;
        this.trailerList=trailerList;
    }


    @Override
    public TrailersAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_trailer, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final TrailersAdapter.ViewHolder holder,  int position) {
        if(trailerList.get(position).site.equalsIgnoreCase("YouTube")) {
            Picasso.with(mContext).load(Utils.
                    getYouTubeIconPath(trailerList.get(position).key)).into(holder.imageViewTrailer);
            holder.self.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent=new Intent();
                    intent.setData(Utils.getYoutubeUriFromId(trailerList.get(holder.getAdapterPosition()).key));
                    intent.setAction("android.intent.action.VIEW");
                    mContext.startActivity(intent);
                }
            });


        }else {
            trailerList.remove(position);
            notifyDataSetChanged();
        }
    }

    @Override
    public int getItemCount() {
        return trailerList.size();
    }
}
