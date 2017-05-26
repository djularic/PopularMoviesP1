package com.jularic.dominik.popularmoviesp1;


import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.jularic.dominik.popularmoviesp1.adapters.AdapterMovies;
import com.jularic.dominik.popularmoviesp1.model.MovieList;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


/**
 * A simple {@link Fragment} subclass.
 */
public class MoviesFragment extends Fragment implements SharedPreferences.OnSharedPreferenceChangeListener {

    private static final String STATE_MOVIES = "movie_list" ;
    private Context mContext;
    TextView mTextViewNoConnection;
    RecyclerView mRecyclerView;
    AdapterMovies mAdapterMovies;
    GridLayoutManager mGridLayoutManager;
    private ProgressBar mProgressBar;
    private MovieList mMovieList;
    static int screenWidth;
    private final OkHttpClient mOkHttpClient = new OkHttpClient();
    private static boolean sortByPopularity;
    private String sortPreference;
    private static final String API_KEY = "YOUR_TMDB_API_KEY_GOES_HERE";
    private static final String URL_TMDB_POPULAR_MOVIES = "http://api.themoviedb.org/3/movie/popular?api_key=" + API_KEY;
    private static final String URL_TMDB_TOP_RATED_MOVIES = "http://api.themoviedb.org/3/movie/top_rated?api_key=" + API_KEY;
    private static int NUM_OF_COLUMNS_SMARTPHONE = 2;
    private SharedPreferences mSharedPreferences;

    public MoviesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_movies, container, false);

        mContext = getContext();

        //Getting the size of the screen
        WindowManager wm = (WindowManager) getActivity().getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        screenWidth = size.x/NUM_OF_COLUMNS_SMARTPHONE;


        if(getActivity()!=null){
            setupSharedPreferences();
            mProgressBar = (ProgressBar) rootView.findViewById(R.id.pb_fragment_movies);
            mRecyclerView = (RecyclerView) rootView.findViewById(R.id.rv_movies);
            mGridLayoutManager = new GridLayoutManager(getContext(), NUM_OF_COLUMNS_SMARTPHONE);
            mRecyclerView.setLayoutManager(mGridLayoutManager);
            mTextViewNoConnection = (TextView) rootView.findViewById(R.id.tv_network_availabilty);

            mTextViewNoConnection.setVisibility(View.VISIBLE);

            if(savedInstanceState != null ) {
                mMovieList = savedInstanceState.getParcelable(STATE_MOVIES);
                if(mMovieList != null){
                    mTextViewNoConnection.setVisibility(View.GONE);
                    mRecyclerView.setAdapter(new AdapterMovies(getContext(), mMovieList, screenWidth));
                }
                else{
                   mTextViewNoConnection.setVisibility(View.VISIBLE);
                }
             }
            else if(isNetworkAvailable()){
                try {
                    getDataFromTMDBApi(sortByPopularity);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                mTextViewNoConnection.setVisibility(View.GONE);
                mRecyclerView.setVisibility(View.VISIBLE);
            }
            else{
                //mTextViewNoConnection.setVisibility(View.VISIBLE);
            }

        }
        return rootView;
    }

    public void getDataFromTMDBApi(boolean sortByPopularity) throws Exception {

        String urlForParse;
        if(isNetworkAvailable()){
            mProgressBar.setVisibility(View.VISIBLE);
        }

        if(sortByPopularity == true) {
            urlForParse = URL_TMDB_POPULAR_MOVIES;
        }
        else{
            urlForParse = URL_TMDB_TOP_RATED_MOVIES;
        }

        Request request = new Request.Builder()
                .url(urlForParse)
                .build();

        final Handler mainHandler = new Handler(getContext().getMainLooper());

        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

                final MovieList movieList = new Gson().fromJson(response.body().string(), MovieList.class);
                mainHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        mMovieList = movieList;
                        mRecyclerView.setAdapter(new AdapterMovies(getContext(), movieList, screenWidth));
                        mProgressBar.setVisibility(View.GONE);
                    }
                });

            };

        });
    }

    @Override
    public void onStart(){
        super.onStart();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(STATE_MOVIES, mMovieList);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        PreferenceManager.getDefaultSharedPreferences(mContext).unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    public boolean isNetworkAvailable(){

       ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            return true;
        } else {
            return false;
        }
    }

    private void setupSharedPreferences(){
        //get the value of preferance fragment from SharedPreferences
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        sortPreference = mSharedPreferences.getString(getString(R.string.sp_key_preference_sort), getString(R.string.str_sort_most_popular));
        if(sortPreference.equals(getString(R.string.str_sort_most_popular))){
            getActivity().setTitle(getString(R.string.str_title_popular_movies));
            sortByPopularity = true;
        }
        else if (sortPreference.equals(getString(R.string.str_sort_top_rated))){
            getActivity().setTitle(getString(R.string.str_title_top_rated));
            sortByPopularity = false;
        }
        mSharedPreferences.registerOnSharedPreferenceChangeListener(this);

    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if(key.equals(getString(R.string.sp_key_preference_sort))){
            setupSharedPreferences();
                if(!isNetworkAvailable()){
                    mTextViewNoConnection.setVisibility(View.VISIBLE);
                    mMovieList = null;
                }
                try {
                    getDataFromTMDBApi(sortByPopularity);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                mRecyclerView.setAdapter(mAdapterMovies);


        }
    }
}
