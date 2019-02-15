package com.hadi.movies.fragment;


import android.app.Activity;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hadi.movies.R;
import com.hadi.movies.activity.MovieDetailActivity;
import com.hadi.movies.adapter.MovieDatabaseAdapter;
import com.hadi.movies.databinding.FavoriteLayoutBinding;
import com.hadi.movies.interfaces.OnFavMovieClickHandler;
import com.hadi.movies.model.movie.Movie;
import com.hadi.movies.model.viewmodel.MovieViewModel;
import com.hadi.movies.utils.AppExecutors;
import com.hadi.movies.utils.database.MovieDatabase;

import java.util.ArrayList;
import java.util.List;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

/**
 * A simple {@link Fragment} subclass.
 */
public class FavoriteFragment extends Fragment implements OnFavMovieClickHandler {

    private static final String TAG = "FavoriteFragment";
    private MovieDatabaseAdapter mAdapter;
    private MovieDatabase database;
    private FavoriteLayoutBinding favoriteBinding;
    private Context mContext;
    private Activity mActivity;

    public FavoriteFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        favoriteBinding = FavoriteLayoutBinding.inflate(inflater, container, false);
        mContext = getContext();
        mActivity = getActivity();
        // Initial MovieDatabaseAdapter
        mAdapter = new MovieDatabaseAdapter(this);
        // Setup Recycler View
        favoriteBinding.favoriteRv.setAdapter(mAdapter);
        favoriteBinding.favoriteRv.addItemDecoration(new DividerItemDecoration(mContext.getApplicationContext(), DividerItemDecoration.VERTICAL));

        setupRecyclerViewActions();

        database = MovieDatabase.getInstance(mContext.getApplicationContext());
        retrieveMovies();

        return favoriteBinding.getRoot();
    }

    /**
     * where i defines it's {@link ItemTouchHelper} and add the onSwiped action to delete by swiping right or left
     * and the on child draw to draw canvas behind the item when swiping.
     */
    private void setupRecyclerViewActions() {
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull final RecyclerView.ViewHolder viewHolder, int i) {
                AppExecutors.getInstance().diskIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        int position = viewHolder.getAdapterPosition();
                        List<Movie> movies = mAdapter.getMovieList();
                        database.movieDao().removeMovie(movies.get(position));
                        mActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mAdapter.setMovieList(new ArrayList<Movie>());
                            }
                        });
                    }
                });
            }

            @Override
            public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                // Adding on swipe canvas for each item in the recycler view.
                new RecyclerViewSwipeDecorator
                        .Builder(mContext, c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                        .addBackgroundColor(ContextCompat.getColor(mContext, R.color.colorAccent))
                        .addActionIcon(R.drawable.ic_favorite_delete)
                        .create()
                        .decorate();
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        }).attachToRecyclerView(favoriteBinding.favoriteRv);
    }

    @Override
    public void onMovieClick(int movieId) {
        startActivity(new Intent(getContext(), MovieDetailActivity.class).putExtra(MovieDetailActivity.MOVIE_DATABASE_KEY, movieId));
    }

    /**
     * This method will retrieve all the movies within the database and observe changes in it.
     */
    private void retrieveMovies() {
        MovieViewModel viewModel = ViewModelProviders.of(this).get(MovieViewModel.class);
        viewModel.getMovies().observe(this, new Observer<List<Movie>>() {
            @Override
            public void onChanged(@Nullable List<Movie> movies) {
                if (movies == null || movies.size() == 0) {
                    Log.d(TAG, "Database is empty");
                    favoriteBinding.emptyListView.setVisibility(View.VISIBLE);
                    mAdapter.setMovieList(movies);
                } else {
                    favoriteBinding.emptyListView.setVisibility(View.INVISIBLE);
                    Log.d(TAG, "Database has: " + movies.size() + " Movies");
                    mAdapter.setMovieList(movies);
                }
            }

        });
    }

}
