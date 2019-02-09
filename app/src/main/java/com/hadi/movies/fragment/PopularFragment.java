package com.hadi.movies.fragment;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hadi.movies.adapter.MovieAdapter;
import com.hadi.movies.databinding.FragmentMovieBinding;
import com.hadi.movies.model.movie.WebMovieResponse;
import com.hadi.movies.utils.network.WebServices;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class PopularFragment extends Fragment {


    private static final String TAG = PopularFragment.class.getSimpleName();
    private FragmentMovieBinding movieBinding;

    public PopularFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        movieBinding = FragmentMovieBinding.inflate(inflater, container, false);
        fetchMovies();
        return movieBinding.getRoot();
    }

    private void fetchMovies() {
        WebServices webServices = WebServices.getMovies.create(WebServices.class);
        webServices.getPopularMovies(WebServices.KEY).enqueue(new Callback<WebMovieResponse>() {
            @Override
            public void onResponse(@NonNull Call<WebMovieResponse> call, @NonNull Response<WebMovieResponse> response) {
                if (response.body() != null) {
                    movieBinding.movieRv.setAdapter(new MovieAdapter(getContext(), response.body().getMovies()));
                    hideProgressBar();
                }
            }

            @Override
            public void onFailure(@NonNull Call<WebMovieResponse> call, @NonNull Throwable t) {
                hideProgressBar();
            }
        });
    }

    private void hideProgressBar() {
        if (movieBinding.posterLoading.getVisibility() == View.VISIBLE) {
            movieBinding.posterLoading.setVisibility(View.INVISIBLE);
        }
    }
}
