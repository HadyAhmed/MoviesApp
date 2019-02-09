package com.hadi.movies.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.hadi.movies.R;
import com.hadi.movies.adapter.ReviewAdapter;
import com.hadi.movies.adapter.VideoAdapter;
import com.hadi.movies.databinding.ActivityDetailBinding;
import com.hadi.movies.model.movie.Movie;
import com.hadi.movies.model.review.ReviewResponse;
import com.hadi.movies.model.video.Result;
import com.hadi.movies.model.video.VideoResponse;
import com.hadi.movies.utils.NetworkUtils;
import com.hadi.movies.utils.network.WebServices;
import com.squareup.picasso.Picasso;

import java.net.URL;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieDetailActivity extends AppCompatActivity {

    public static final String MOVIE_KEY = "movie_key";
    private static final String TAG = MovieDetailActivity.class.getSimpleName();
    private ActivityDetailBinding detailBinding;
    private WebServices webServices = WebServices.getMovies.create(WebServices.class);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        detailBinding = DataBindingUtil.setContentView(this, R.layout.activity_detail);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        // Getting The Movie Object Passed By The Adapter
        Intent passedIntent = getIntent();
        Bundle extraBundle = passedIntent.getExtras();
        Parcelable movieParc;
        // Handling the null object
        if (extraBundle != null) {
            movieParc = extraBundle.getParcelable(MOVIE_KEY);
            Movie movie = (Movie) movieParc;
            if (movie != null) {
                populateMovieDetail(movie);
            }
        } else {
            Toast.makeText(this, "Something Wrong Happened!", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    /**
     * Method to find view references and fetch the data withing it
     *
     * @param movie is the passed mMovie object from {@link com.hadi.movies.adapter.MovieAdapter}
     */
    private void populateMovieDetail(Movie movie) {
        showMovieObjectDetail(movie);
        showMovieTrailer(movie);
        showMovieReview(movie);
    }

    private void showMovieTrailer(Movie movie) {
        webServices.getMovieTrailer(movie.getId(), WebServices.KEY).enqueue(new Callback<VideoResponse>() {
            @Override
            public void onResponse(@NonNull Call<VideoResponse> call, @NonNull Response<VideoResponse> response) {
                if (response.body() != null) {
                    if (response.body().getResults().size() == 0) {
                        detailBinding.noTrailerTv.setVisibility(View.VISIBLE);
                    } else {
                        for (Result trailer : response.body().getResults()) {
                            Log.d(TAG, "onVideoResponse: " + trailer.getName());
                        }
                        detailBinding.trailerRv.setAdapter(new VideoAdapter(MovieDetailActivity.this, response.body().getResults()));
                        detailBinding.trailerRv.setNestedScrollingEnabled(false);
                        detailBinding.trailerRv.setHasFixedSize(true);
                        detailBinding.trailerRv.addItemDecoration(new DividerItemDecoration(MovieDetailActivity.this, DividerItemDecoration.VERTICAL));
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<VideoResponse> call, @NonNull Throwable t) {

            }
        });
    }

    private void showMovieReview(Movie movie) {
        webServices.getMovieReview(movie.getId(), WebServices.KEY).enqueue(new Callback<ReviewResponse>() {
            @Override
            public void onResponse(@NonNull Call<ReviewResponse> call, @NonNull Response<ReviewResponse> response) {
                if (response.body() != null) {
                    if (response.body().getResults().size() == 0) {
                        detailBinding.noReviewTv.setVisibility(View.VISIBLE);
                    } else {
                        detailBinding.reviewRv.setAdapter(new ReviewAdapter(response.body().getResults()));
                        detailBinding.reviewRv.setNestedScrollingEnabled(false);
                        detailBinding.reviewRv.setHasFixedSize(true);
                        detailBinding.reviewRv.addItemDecoration(new DividerItemDecoration(MovieDetailActivity.this, DividerItemDecoration.VERTICAL));
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ReviewResponse> call, @NonNull Throwable t) {

            }
        });
    }

    private void showMovieObjectDetail(Movie movie) {
        setTitle(movie.getTitle());
        URL imageUrl = NetworkUtils.buildURL(movie.getPosterPath());
        if (imageUrl != null) {
            Picasso.with(this).load(imageUrl.toString()).into(detailBinding.moviePoster);
        }
        detailBinding.movieRate.setText(String.valueOf(movie.getVoteAverage()));
        detailBinding.movieRate.append("/10");

        detailBinding.movieYear.setText(String.valueOf(movie.getReleaseDate()).substring(0, 4));

        detailBinding.movieOverview.setText(movie.getOverview());
    }

    @Override
    public void onBackPressed() {
        NavUtils.navigateUpFromSameTask(this);
    }
}
