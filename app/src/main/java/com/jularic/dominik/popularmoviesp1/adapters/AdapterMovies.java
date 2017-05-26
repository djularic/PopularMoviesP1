package com.jularic.dominik.popularmoviesp1.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.jularic.dominik.popularmoviesp1.DetailActivity;
import com.jularic.dominik.popularmoviesp1.R;
import com.jularic.dominik.popularmoviesp1.model.Movie;
import com.jularic.dominik.popularmoviesp1.model.MovieList;
import com.squareup.picasso.Picasso;

/**
 * Created by Dominik on 19.5.2017..
 */

public class AdapterMovies extends RecyclerView.Adapter<AdapterMovies.MovieHolder> {

    private LayoutInflater mInflater;
    private Context mContext;
    private MovieList mMovieList;
    private int mWidth;
    public static final String URL_TMDB_IMG_BASE_PATH_SRC= "http://image.tmdb.org/t/p/w185/";
    public static final String MOVIE_DETAIL = "movie_detail";
    public static final String POSTER_WIDTH = "poster_width";
    private static final double LANDSCAPE_IMAGE_DECREASE = 1.5;

    public AdapterMovies(Context context, MovieList movieList, int x){
        mContext = context;
        mWidth = x;
        mMovieList = movieList;
        mInflater =  LayoutInflater.from(context);
    }
    @Override
    public MovieHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.row_movie, parent, false);
        MovieHolder holder = new MovieHolder(view, mContext, mMovieList, mWidth);
        return holder;
    }

    @Override
    public void onBindViewHolder(MovieHolder holder, int position) {

        Picasso.with(mContext)
                .load(URL_TMDB_IMG_BASE_PATH_SRC + mMovieList.getMovieByListPosistion(position).getPoster_path())
                .resize(mWidth, (int)(mWidth*1.5))
                .centerCrop()
                .placeholder(R.drawable.default_placeholder_img)
                .error(R.drawable.error_img)
                .into(holder.mImageView);

    }

    @Override
    public int getItemCount() {
        return mMovieList.getItemsCount();
    }

    public static class MovieHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView mImageView;
        MovieList mMovieList;
        Context mContext;
        int mPosterWidth;

        public MovieHolder(View itemView, Context context, MovieList movieList, int posterWidth) {
            super(itemView);
            mMovieList = movieList;
            mContext = context;
            mPosterWidth = posterWidth;
            itemView.setOnClickListener(this);
            mImageView = (ImageView) itemView.findViewById(R.id.iv_movie);

        }

        @Override
        public void onClick(View view) {

            int position = getAdapterPosition();
            Movie movie = mMovieList.getMovieByListPosistion(position);
            Intent intentOpenDetailsFragment = new Intent (mContext,DetailActivity.class);
            intentOpenDetailsFragment.putExtra(MOVIE_DETAIL, movie);
            if(view.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
                //Decrease the width of poster if device is in Landscape mode
                //Because of Landscape->imgClick->Landscape->Portrait mode then shows too large img so that
                //ratings and date can not be shown
                mPosterWidth = (int)(mPosterWidth/LANDSCAPE_IMAGE_DECREASE);
            }
            intentOpenDetailsFragment.putExtra(POSTER_WIDTH, mPosterWidth);
            mContext.startActivity(intentOpenDetailsFragment);
            //Toast.makeText(itemView.getContext(), "msg ms"+getAdapterPosition(), Toast.LENGTH_SHORT).show();
        }
    }

}
