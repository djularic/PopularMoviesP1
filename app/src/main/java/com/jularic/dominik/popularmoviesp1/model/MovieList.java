package com.jularic.dominik.popularmoviesp1.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Dominik on 19.5.2017..
 */

public class MovieList implements Parcelable {

    @SerializedName("results")
    private ArrayList<Movie> movieList;

    protected MovieList(Parcel in) {
        movieList = in.createTypedArrayList(Movie.CREATOR);
    }

    public static final Creator<MovieList> CREATOR = new Creator<MovieList>() {
        @Override
        public MovieList createFromParcel(Parcel in) {
            return new MovieList(in);
        }

        @Override
        public MovieList[] newArray(int size) {
            return new MovieList[size];
        }
    };

    public ArrayList<Movie> getMovietList() {
        return movieList;
    }

    public Movie getMovieByListPosistion(int position) {
        return movieList.get(position);
    }

    public int getItemsCount() {
        return movieList.size();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeTypedList(movieList);
    }
}
