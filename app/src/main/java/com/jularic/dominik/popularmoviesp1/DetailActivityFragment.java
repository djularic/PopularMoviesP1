package com.jularic.dominik.popularmoviesp1;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.jularic.dominik.popularmoviesp1.adapters.AdapterMovies;
import com.jularic.dominik.popularmoviesp1.model.Movie;
import com.squareup.picasso.Picasso;

public class DetailActivityFragment extends Fragment {

    private TextView mTvTitle;
    private ImageView mImPoster;
    private TextView mTvDate;
    private TextView mTvRating;
    private TextView mTvOverview;
    private int mPosterWidth;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);

        Intent intent = getActivity().getIntent();
        getActivity().setTitle(R.string.title_movie_details);
        Movie movie = intent.getParcelableExtra(AdapterMovies.MOVIE_DETAIL);
        mPosterWidth = intent.getIntExtra(AdapterMovies.POSTER_WIDTH,R.integer.width_defaul_value);
        mTvTitle = (TextView) rootView.findViewById(R.id.tv_title);
        mImPoster = (ImageView) rootView.findViewById(R.id.iv_poster);
        mTvDate = (TextView) rootView.findViewById(R.id.tv_date);
        mTvRating = (TextView) rootView.findViewById(R.id.tv_rating);
        mTvOverview = (TextView) rootView.findViewById(R.id.tv_overview);

        //Toast.makeText(getContext(), movie.getOverview(), Toast.LENGTH_SHORT).show();

        if(movie != null) {
            mTvTitle.setText(movie.getOriginal_title());

            Picasso.with(getContext())
                    .load(AdapterMovies.URL_TMDB_IMG_BASE_PATH_SRC + movie.getPoster_path())
                    .resize(mPosterWidth, (int) (mPosterWidth * 1.5))
                    .centerCrop()
                    .into(mImPoster);

            mTvDate.setText(movie.getRelease_date());
            mTvRating.setText(movie.getVote_average());
            mTvOverview.setText(movie.getOverview());
        }
        return rootView;
    }



}
