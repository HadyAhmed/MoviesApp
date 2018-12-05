package com.hadi.movies.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.hadi.movies.R;
import com.hadi.movies.model.Movie;
import com.hadi.movies.utils.NetworkUtils;

import java.net.URL;

public class MovieDetailActivity extends AppCompatActivity {

    public static final String MOVIE_KEY = "movie_key";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        // Getting The Movie Object Passed By The Adapter
        Intent passedIntent = getIntent();
        Bundle extraBundle = passedIntent.getExtras();
        Parcelable movieParc;
        // Handling the null object
        if (extraBundle != null) {
            movieParc = extraBundle.getParcelable(MOVIE_KEY);
            Movie movie = (Movie) movieParc;
            if (movie != null) {
                showMovieDetail(movie);
            }
        } else {
            Toast.makeText(this, "Something Wrong Happened!", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    /**
     * Method to find view references and fetch the data withing it
     *
     * @param movie is the passed movie object from {@link com.hadi.movies.adapter.MovieAdapter}
     */
    private void showMovieDetail(Movie movie) {
        ImageView moviePoster = findViewById(R.id.movie_backdrop);
        TextView movieTitle = findViewById(R.id.movie_original_title_detail);
        TextView movieDate = findViewById(R.id.movie_release_date_detail);
        TextView movieRate = findViewById(R.id.movie_rate_detail);
        TextView movieOverview = findViewById(R.id.movie_overview_detail);

        setTitle(movie.getMovieOriginalTitle());
        URL imageUrl = NetworkUtils.buildURL(movie.getMoviePosterBackDropUrl());
        Glide.with(this).load(imageUrl).into(moviePoster);
        movieTitle.setText(movie.getMovieOriginalTitle());
        movieDate.setText(movie.getMovieReleaseDate());
        movieRate.setText(String.valueOf(movie.getMovieVoteAverage()));
        movieOverview.setText(movie.getMovieOverView());
    }
}
