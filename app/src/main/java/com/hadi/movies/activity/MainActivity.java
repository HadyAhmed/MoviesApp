package com.hadi.movies.activity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.hadi.movies.R;
import com.hadi.movies.adapter.MovieAdapter;
import com.hadi.movies.model.Movie;
import com.hadi.movies.utils.NetworkUtils;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static com.hadi.movies.utils.NetworkUtils.MOST_POPULAR_SORT;
import static com.hadi.movies.utils.NetworkUtils.TOP_RATED_SORT;


public class MainActivity extends AppCompatActivity {
    private RecyclerView moviesRecycler;
    private ProgressBar progressBar;
    private MovieAdapter adapter;
    private byte currentState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        moviesRecycler = findViewById(R.id.movies_recycler_view);

        // Start the application with most popular movies results
        showMostPopularMovies();
    }

    //method to show up movies with most popular sorting
    private void showMostPopularMovies() {
        new MoviesAsyncTask().execute(MOST_POPULAR_SORT);
    }

    //method to show up movies with top rating sorting
    private void showTopRatedMovies() {
        new MoviesAsyncTask().execute(TOP_RATED_SORT);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.movies_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.most_popular_item:
                if (!item.isChecked()) {
                    showMostPopularMovies();
                    currentState = MOST_POPULAR_SORT;
                    item.setChecked(true);
                }
                return true;
            case R.id.top_rated_item:
                if (!item.isChecked()) {
                    showTopRatedMovies();
                    currentState = TOP_RATED_SORT;
                    item.setChecked(true);
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Method to handle the error if result back equal null
     * could be no internet connection or another error
     */
    private void showErrorSnackBar() {
        View parentLayout = findViewById(R.id.movie_layout);
        Snackbar.make(parentLayout, getString(R.string.error_message), Snackbar.LENGTH_INDEFINITE)
                .setAction("Retry", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (currentState == MOST_POPULAR_SORT) {
                            showMostPopularMovies();
                        } else {
                            showTopRatedMovies();
                        }
                    }
                })
                .show();
    }

    class MoviesAsyncTask extends AsyncTask<Byte, Void, List<Movie>> {
        @Override
        protected void onPreExecute() {
            adapter = new MovieAdapter(MainActivity.this, new ArrayList<Movie>());
            moviesRecycler.setAdapter(adapter);
            progressBar = findViewById(R.id.progressBar);
            progressBar.setVisibility(View.VISIBLE);
            super.onPreExecute();
        }


        @Override
        protected List<Movie> doInBackground(Byte... paramValues) {
            URL url = NetworkUtils.buildURL(paramValues[0]);
            if (url != null) {
                Log.d(NetworkUtils.TAG, url.toString());
                try {
                    return NetworkUtils.fetchMoviesFromHttpResponse(url);
                } catch (IOException e) {
                    e.printStackTrace();
                    return null;
                }
            } else {
                return null;
            }
        }

        @Override
        protected void onPostExecute(List<Movie> movies) {
            super.onPostExecute(movies);
            progressBar.setVisibility(View.INVISIBLE);
            if (movies != null) {
                adapter = new MovieAdapter(MainActivity.this, movies);
                moviesRecycler.setAdapter(adapter);
                StaggeredGridLayoutManager layoutManager =
                        new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
                moviesRecycler.setLayoutManager(layoutManager);
            } else {
                showErrorSnackBar();
            }
        }
    }

}
