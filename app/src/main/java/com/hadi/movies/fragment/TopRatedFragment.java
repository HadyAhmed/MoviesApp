package com.hadi.movies.fragment;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hadi.movies.adapter.MovieAdapter;
import com.hadi.movies.databinding.FragmentMoviesBinding;
import com.hadi.movies.model.movie.Movie;
import com.hadi.movies.model.movie.WebMovieResponse;
import com.hadi.movies.utils.network.WebServices;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.support.constraint.Constraints.TAG;

public class TopRatedFragment extends Fragment {


    private static final String MOVIE_STATE = "movie_list_key";
    private MovieAdapter mAdapter;
    private ArrayList<Movie> mMovieList;

    public TopRatedFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        FragmentMoviesBinding moviesBinding = FragmentMoviesBinding.inflate(inflater, container, false);
        mAdapter = new MovieAdapter(getContext());

        moviesBinding.movieRv.setAdapter(mAdapter);

        // Check for fragment life cycle state
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(MOVIE_STATE)) {
                List<Movie> movieList = savedInstanceState.getParcelableArrayList(MOVIE_STATE);
                if (movieList == null) {
                    fetchMovies();
                } else {
                    mAdapter.setMovie(movieList);
                    mMovieList = (ArrayList<Movie>) mAdapter.getMovie();
                }
            }
        } else {
            fetchMovies();
        }
        return moviesBinding.getRoot();
    }

    /**
     * this method will fetch the data from the server using {@link retrofit2.Retrofit}
     */
    private void fetchMovies() {
        WebServices webServices = WebServices.getMoviesResponse.create(WebServices.class);
        Log.d(TAG, "fetchMovies: " + webServices.getTopRatedMovies(WebServices.KEY).request().url());
        webServices.getTopRatedMovies(WebServices.KEY).enqueue(new Callback<WebMovieResponse>() {
            @Override
            public void onResponse(@NonNull Call<WebMovieResponse> call, @NonNull Response<WebMovieResponse> response) {
                if (response.body() != null) {
                    mAdapter.setMovie(response.body().getMovies());
                    mMovieList = (ArrayList<Movie>) mAdapter.getMovie();
                }
            }

            @Override
            public void onFailure(@NonNull Call<WebMovieResponse> call, @NonNull Throwable t) {
            }
        });
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(MOVIE_STATE, mMovieList);
    }
}
