package com.ab.popularmovies.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ab.popularmovies.R;
import com.ab.popularmovies.Utils;
import com.ab.popularmovies.model.Movie;
import com.squareup.picasso.Picasso;

/**
 * Created by q4J1X056 on 08-08-2016.
 */
public class DetailFragment extends Fragment {
    private TextView textViewTitle;
    private ImageView imageViewPoster;
    private TextView textViewRating;
    private TextView textViewReleaseDate;
    private TextView textViewOverview;
    Movie movieDetailed=null;
    static final String ARG_MOVIE="movie";
    static final String ARG_IS_TWO_PANE="istwopane";

    boolean isTwoPane;


    public static Fragment newInstance(Parcelable movie, boolean isTwoPane) {
        DetailFragment fragment=new DetailFragment();
        Bundle bundle=new Bundle();
        bundle.putParcelable(ARG_MOVIE,movie);
        bundle.putBoolean(ARG_IS_TWO_PANE,isTwoPane);
        fragment.setArguments(bundle);
        return fragment;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.fragment_detail,container,false);
         textViewTitle=(TextView)view.findViewById(R.id.textViewTitle);
         imageViewPoster = (ImageView) view.findViewById(R.id.imageViewPosterDetail);
         textViewRating=(TextView) view.findViewById(R.id.textViewRating);
         textViewReleaseDate=(TextView) view.findViewById(R.id.textViewReleaseDate);
         textViewOverview=(TextView) view.findViewById(R.id.textViewOverview);
        Bundle args=getArguments();
        if(args!=null && args.containsKey(ARG_MOVIE)) {
            movieDetailed = args.getParcelable(ARG_MOVIE);
            isTwoPane=args.getBoolean(ARG_IS_TWO_PANE);
            bindData();
        }
        else {
            return null;
        }


        return view;

    }

    private void bindData() {



        if(movieDetailed!=null) {
            //textViewTitle.setText(movieDetailed.originalTitle);
            Picasso.with(getActivity()).load(Utils.
                    getCompletePosterPath(getActivity(),movieDetailed.posterPath)).into(imageViewPoster);
            textViewRating.setText(String.format(getString(R.string.rating_formatted),movieDetailed.userRating));
            textViewReleaseDate.setText(movieDetailed.releaseDate);
            textViewOverview.setText(movieDetailed.overview);
            if(!isTwoPane) {
                ((DetailInterface)getActivity()).setTitleInParent(movieDetailed.originalTitle);
            }
            else{
                if(textViewTitle!=null)
                {
                    textViewTitle.setVisibility(View.VISIBLE);
                    textViewTitle.setText(movieDetailed.originalTitle);
                }
            }
        }

    }

    public interface DetailInterface
    {
        void setTitleInParent(String title);
    }



}
