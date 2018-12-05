package com.hadi.movies.activity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.hadi.movies.R;
import com.hadi.movies.adapter.MovieAdapter;
import com.hadi.movies.model.Movie;
import com.hadi.movies.utils.NetworkUtils;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    private RecyclerView moviesRecycler;
    private ProgressBar progressBar;
    private MovieAdapter adapter;
    private TextView errorMessageTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        moviesRecycler = findViewById(R.id.movies_recycler_view);
        errorMessageTv = findViewById(R.id.error_text_view);

        // Start the application with most popular movies results
        showMostPopularMovies();
    }

    //method to show up movies with most popular sorting
    private void showMostPopularMovies() {
        new MoviesAsyncTask().execute(NetworkUtils.SORT_BY_POPULAR);
    }

    //method to show up movies with top rating sorting
    private void showTopRatedMovies() {
        new MoviesAsyncTask().execute(NetworkUtils.SORT_BY_TOP_RATED);
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
                    item.setChecked(true);
                }
                return true;
            case R.id.top_rated_item:
                if (!item.isChecked()) {
                    showTopRatedMovies();
                    item.setChecked(true);
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    class MoviesAsyncTask extends AsyncTask<Byte, Void, List<Movie>> {

        @Override
        protected void onPreExecute() {
            adapter = new MovieAdapter(MainActivity.this, new ArrayList<Movie>());
            moviesRecycler.setAdapter(adapter);
            progressBar = findViewById(R.id.progressBar);
            progressBar.setVisibility(View.VISIBLE);
            errorMessageTv.setVisibility(View.INVISIBLE);
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
                errorMessageTv.setVisibility(View.INVISIBLE);
                adapter = new MovieAdapter(MainActivity.this, movies);
                moviesRecycler.setAdapter(adapter);
                GridLayoutManager layoutManager = new GridLayoutManager(MainActivity.this, 2);
                moviesRecycler.setLayoutManager(layoutManager);
            } else {
                errorMessageTv.setVisibility(View.VISIBLE);
                errorMessageTv.setText(getString(R.string.error_message));
            }
        }
    }

}
