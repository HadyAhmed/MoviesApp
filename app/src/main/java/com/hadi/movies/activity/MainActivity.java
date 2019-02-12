package com.hadi.movies.activity;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;

import com.hadi.movies.R;
import com.hadi.movies.adapter.MovieAdapter;
import com.hadi.movies.databinding.ActivityMainBinding;
import com.hadi.movies.model.movie.Movie;
import com.hadi.movies.model.movie.WebMovieResponse;
import com.hadi.movies.model.viewmodel.MovieViewModel;
import com.hadi.movies.utils.network.WebServices;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private static final String TAG = MainActivity.class.getSimpleName() + "Tag";
    // Constants for application State keys
    private static final String MOVIES_KEY = "movies_list_key";
    private static final String RECYCLER_VIEW_STATE = "recycler_view_key";
    private ActivityMainBinding mainBinding;
    private MovieAdapter mAdapter;
    // Variable that holds the data for the activity life cycle changes
    private ArrayList<Movie> movieArrayList;
    private Parcelable recyclerViewState;

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(MOVIES_KEY)) {
                movieArrayList = savedInstanceState.getParcelableArrayList(MOVIES_KEY);
                recyclerViewState = savedInstanceState.getParcelable(RECYCLER_VIEW_STATE);
                mAdapter.setMovie(movieArrayList);
                if (mainBinding.moviesRv.getLayoutManager() != null) {
                    mainBinding.moviesRv.getLayoutManager().onRestoreInstanceState(recyclerViewState);
                }
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        mainBinding.categorySpinner.setOnItemSelectedListener(this);

        mAdapter = new MovieAdapter(this);

        mainBinding.moviesRv.setAdapter(mAdapter);

        mainBinding.moviesRv.setItemViewCacheSize(15);
        mainBinding.moviesRv.setHasFixedSize(true);

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // TODO: 2019-02-12 Trigger Before Selecting On Item That the app is connected to the internet
        switch (position) {
            case 0:
                fetchMovies(WebServices.TOP_RATED_MOVIES);
                break;
            case 1:
                fetchMovies(WebServices.POPULAR_MOVIES);
            default:
                // TODO: 2019-02-12 Add Items in the main view
                retrieveMovies();
                break;
        }
    }


    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.movies_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.show_fav_item:
                startActivity(new Intent(this, FavoriteActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * this method will fetch the data from the server using {@link retrofit2.Retrofit}
     *
     * @param category is the category type of {@link Movie} to attach the {@link java.net.URL} client to server.
     */
    private void fetchMovies(String category) {
        showProgressBar();
        WebServices webServices = WebServices.getMoviesResponse.create(WebServices.class);
        Log.d(TAG, "fetchMovies: " + webServices.getMovies(WebServices.POPULAR_MOVIES, WebServices.KEY).request().url());
        webServices.getMovies(category, WebServices.KEY).enqueue(new Callback<WebMovieResponse>() {
            @Override
            public void onResponse(@NonNull Call<WebMovieResponse> call, @NonNull Response<WebMovieResponse> response) {
                if (response.body() != null) {
                    movieArrayList = (ArrayList<Movie>) response.body().getMovies();
                    mAdapter.setMovie(response.body().getMovies());
                    hideProgressBar();
                }
            }

            @Override
            public void onFailure(@NonNull Call<WebMovieResponse> call, @NonNull Throwable t) {
                hideProgressBar();
            }
        });
    }

    /**
     * This method will retrieve all the movies within the database and observe changes in it.
     */
    private void retrieveMovies() {
        MovieViewModel viewModel = ViewModelProviders.of(this).get(MovieViewModel.class);
        viewModel.getMovies().observe(this, new Observer<List<Movie>>() {
            @Override
            public void onChanged(@Nullable List<Movie> movies) {
                if (movies != null && movies.size() == 0) {
                    Log.d(TAG, "receiving movies from database: Empty List");
//                    favoriteBinding.emptyListView.setVisibility(View.VISIBLE);
                } else {
                    // Setup Recycler View
                    mAdapter.setMovie(movies);
                    mainBinding.moviesRv.setAdapter(mAdapter);
                }
            }

        });
    }

    /**
     * Show the {@link android.widget.ProgressBar} if it's INVISIBLE
     */
    private void showProgressBar() {
        if (mainBinding.posterLoading.getVisibility() == View.INVISIBLE) {
            mainBinding.posterLoading.setVisibility(View.VISIBLE);
        }
    }

    /**
     * Hide the {@link android.widget.ProgressBar} if it's VISIBLE
     */
    private void hideProgressBar() {
        if (mainBinding.posterLoading.getVisibility() == View.VISIBLE) {
            mainBinding.posterLoading.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        movieArrayList = (ArrayList<Movie>) mAdapter.getMovie();
        outState.putParcelableArrayList(MOVIES_KEY, movieArrayList);
        if (mainBinding.moviesRv.getLayoutManager() != null) {
            recyclerViewState = mainBinding.moviesRv.getLayoutManager().onSaveInstanceState();
            outState.putParcelable(RECYCLER_VIEW_STATE, recyclerViewState);
        }
    }

}
