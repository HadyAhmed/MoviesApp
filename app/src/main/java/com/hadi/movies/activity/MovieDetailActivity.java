package com.hadi.movies.activity;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
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
import com.hadi.movies.model.view_model.AddMovieViewModel;
import com.hadi.movies.model.view_model.MovieViewModelFactory;
import com.hadi.movies.utils.AppExecutors;
import com.hadi.movies.utils.database.MovieDatabase;
import com.hadi.movies.utils.network.NetworkUtils;
import com.hadi.movies.utils.network.WebServices;
import com.squareup.picasso.Picasso;

import java.net.URL;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieDetailActivity extends AppCompatActivity {

    public static final String MOVIE_KEY = "movie_key";
    public static final String MOVIE_DATABASE_KEY = "movie_from_database_key";

    private static final String TAG = MovieDetailActivity.class.getSimpleName();

    private ActivityDetailBinding detailBinding;
    private WebServices webServices = WebServices.getMovies.create(WebServices.class);
    private MovieDatabase database;
    private Movie mMovie;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        detailBinding = DataBindingUtil.setContentView(this, R.layout.activity_detail);
        database = MovieDatabase.getInstance(this);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        // Getting The Movie Object Passed By The Adapter
        Intent passedIntent = getIntent();
        mMovie = getIntent().getParcelableExtra(MOVIE_KEY);
        int movieId = getIntent().getIntExtra(MOVIE_DATABASE_KEY, 0);

        if (passedIntent != null && passedIntent.hasExtra(MOVIE_KEY)) {
            populateMovieDetail(mMovie);
        } else if (passedIntent != null && passedIntent.hasExtra(MOVIE_DATABASE_KEY)) {
            MovieViewModelFactory factory = new MovieViewModelFactory(database, movieId);
            final AddMovieViewModel viewModel = ViewModelProviders.of(this, factory).get(AddMovieViewModel.class);
            viewModel.getMovie().observe(this, new Observer<Movie>() {
                @Override
                public void onChanged(@Nullable Movie movie) {
                    populateMovieDetail(movie);
                    mMovie = movie;
                    viewModel.getMovie().removeObserver(this);
                }
            });
        } else {
            Toast.makeText(this, "Something Wrong Happened!", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    /**
     * method to populate the details from the {@link Movie} to the UI.
     *
     * @param movie is the passed mMovie object from {@link com.hadi.movies.adapter.MovieAdapter}
     *              or from the {@link FavoriteActivity} using local data storage.
     */
    private void populateMovieDetail(Movie movie) {
        showMovieObjectDetail(movie);
        showMovieTrailer(movie);
        showMovieReview(movie);
    }

    /**
     * this method will check for the movie in the database
     * and change the visibility for menu items in the toolbar.
     *
     * @param menu of the {@link MovieDetailActivity}
     */
    private void checkForDatabase(final Menu menu) {
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                final int movieId = database.movieDao().getMovieId(mMovie.getId());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (movieId == mMovie.getId()) {
                            menu.findItem(R.id.add_to_fav_item).setIcon(R.drawable.ic_favorite_check);
                        } else {
                            menu.findItem(R.id.add_to_fav_item).setIcon(R.drawable.ic_favorite_add);
                        }
                    }
                });
            }
        });

    }

    /**
     * this will request {@link com.hadi.movies.model.video.Result} and show up the
     * trailers if were exist and if not, they will appear the no trailer message
     *
     * @param movie instance of {@link Movie}
     */
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

    /**
     * this will request {@link com.hadi.movies.model.review.Result} and show up the
     * reviews if were exist and if not, they will appear the no review message
     *
     * @param movie instance of {@link Movie}
     */
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

    /**
     * this will populate {@link Movie} to the UI
     */
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.detail_menu, menu);
        checkForDatabase(menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_to_fav_item:
                AppExecutors.getInstance().diskIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        if (database.movieDao().getMovieId(mMovie.getId()) == mMovie.getId()) {
                            database.movieDao().removeMovie(mMovie);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    item.setIcon(R.drawable.ic_favorite_add);
                                    Toast.makeText(MovieDetailActivity.this, getString(R.string.remove_movie_msg), Toast.LENGTH_SHORT).show();
                                }
                            });
                        } else {
                            database.movieDao().insertMovie(mMovie);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    item.setIcon(R.drawable.ic_favorite_check);
                                    Toast.makeText(MovieDetailActivity.this, getString(R.string.add_movie_msg), Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
                });
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
