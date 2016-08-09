package com.ab.popularmovies.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ab.popularmovies.R;
import com.ab.popularmovies.Utils;
import com.ab.popularmovies.adapters.ReviewsAdapter;
import com.ab.popularmovies.adapters.TrailersAdapter;
import com.ab.popularmovies.model.Movie;
import com.ab.popularmovies.model.MovieDbApiResponse;
import com.ab.popularmovies.model.Review;
import com.ab.popularmovies.model.ReviewResponse;
import com.ab.popularmovies.model.Trailer;
import com.ab.popularmovies.model.TrailerResponse;
import com.ab.popularmovies.net.ApiBuilder;
import com.ab.popularmovies.net.MovieDbService;
import com.squareup.picasso.Picasso;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by q4J1X056 on 08-08-2016.
 */
public class DetailFragment extends Fragment {
    private static final String LOG_TAG = DetailFragment.class.getSimpleName() ;
    private TextView textViewTitle;
    private ImageView imageViewPoster;
    private TextView textViewRating;
    private TextView textViewReleaseDate;
    private TextView textViewOverview;
    private LinearLayout linearLayoutTrailers;
    private RecyclerView recyclerViewTrailers;
    private LinearLayout linearLayoutReviews;
    private RecyclerView recyclerViewReviews;
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
         linearLayoutTrailers=(LinearLayout)view.findViewById(R.id.linearLayoutTrailer);
         recyclerViewTrailers=(RecyclerView)linearLayoutTrailers.findViewById(R.id.recyclerViewTrailers);
         linearLayoutReviews=(LinearLayout)view.findViewById(R.id.linearLayoutReviews);
         recyclerViewReviews=(RecyclerView)linearLayoutReviews.findViewById(R.id.recyclerViewReviews);
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
            getTrailers(movieDetailed.id);
            getReviews(movieDetailed.id);
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


    void getTrailers(String id)
    {
        MovieDbService movieDbService = ApiBuilder.getClient(getActivity()).create(MovieDbService.class);
        Call<TrailerResponse> call = movieDbService.getTrailers(id, getString(R.string.moviedb_api_key));
        Log.e(LOG_TAG, "Call to fetch trailers");
        call.enqueue(new Callback<TrailerResponse>() {
            @Override
            public void onResponse(Call<TrailerResponse> call, Response<TrailerResponse> response) {
                int statusCode = response.code();
                Log.e(LOG_TAG, " trailers status code:"+statusCode);
                List<Trailer> trailers = response.body().listTrailer;
                if (trailers != null && !trailers.isEmpty()) {
                    linearLayoutTrailers.setVisibility(View.VISIBLE);
                    TrailersAdapter trailersAdapter=new TrailersAdapter(getActivity(),trailers);
                    recyclerViewTrailers.setHasFixedSize(true);

                    // use a linear layout manager
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
                    linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
                    recyclerViewTrailers.setLayoutManager(linearLayoutManager);
                    recyclerViewTrailers.setAdapter(trailersAdapter);
                }
            }

            @Override
            public void onFailure(Call<TrailerResponse> call, Throwable t) {
                // Log error here since request failed
                Log.e(LOG_TAG, t.toString());
            }
        });

    }


    void getReviews(String id)
    {
        MovieDbService movieDbService = ApiBuilder.getClient(getActivity()).create(MovieDbService.class);
        Call<ReviewResponse> call = movieDbService.getReviews(id, getString(R.string.moviedb_api_key));
        Log.e(LOG_TAG, "Call to fetch reviews");
        call.enqueue(new Callback<ReviewResponse>() {
            @Override
            public void onResponse(Call<ReviewResponse> call, Response<ReviewResponse> response) {
                int statusCode = response.code();
                Log.e(LOG_TAG, " reviews status code:"+statusCode);
                List<Review> reviews = response.body().listReview;
                if (reviews != null && !reviews.isEmpty()) {
                    linearLayoutReviews.setVisibility(View.VISIBLE);
                    ReviewsAdapter reviewsAdapter=new ReviewsAdapter(getActivity(),reviews);
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
                    recyclerViewReviews.setLayoutManager(linearLayoutManager);
                    recyclerViewReviews.setAdapter(reviewsAdapter);
                }
            }

            @Override
            public void onFailure(Call<ReviewResponse> call, Throwable t) {
                // Log error here since request failed
                Log.e(LOG_TAG, t.toString());
            }
        });

    }



}
