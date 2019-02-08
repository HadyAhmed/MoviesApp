package com.hadi.movies.fragment;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.hadi.movies.R;
import com.hadi.movies.adapter.MovieAdapter;
import com.hadi.movies.model.WebMovieResponse;
import com.hadi.movies.utils.network.WebServices;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class PopularFragment extends Fragment {


    private static final String TAG = PopularFragment.class.getSimpleName();
    private ProgressBar mProgressBar;
    private RecyclerView recyclerView;

    public PopularFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_movie, container, false);

        recyclerView = view.findViewById(R.id.movie_rv);
        mProgressBar = view.findViewById(R.id.poster_loading);

        WebServices webServices = WebServices.getMovies.create(WebServices.class);
        webServices.getPopularMovies(WebServices.KEY).enqueue(new Callback<WebMovieResponse>() {
            @Override
            public void onResponse(@NonNull Call<WebMovieResponse> call, @NonNull Response<WebMovieResponse> response) {
                if (response.body() != null) {
                    recyclerView.setAdapter(new MovieAdapter(getContext(), response.body().getMovies()));
                    hideProgressBar();
                }
            }

            @Override
            public void onFailure(@NonNull Call<WebMovieResponse> call, @NonNull Throwable t) {
                hideProgressBar();
            }
        });
        return view;
    }

    private void hideProgressBar() {
        if (mProgressBar.getVisibility() == View.VISIBLE) {
            mProgressBar.setVisibility(View.INVISIBLE);
        }
    }
}
