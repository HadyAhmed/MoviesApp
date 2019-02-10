package com.hadi.movies.activity;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.hadi.movies.R;
import com.hadi.movies.adapter.MovieDatabaseAdapter;
import com.hadi.movies.databinding.ActivityFavoriteBinding;
import com.hadi.movies.interfaces.OnMovieClickHandler;
import com.hadi.movies.model.movie.Movie;
import com.hadi.movies.model.view_model.MovieViewModel;
import com.hadi.movies.utils.AppExecutors;
import com.hadi.movies.utils.database.MovieDatabase;

import java.util.ArrayList;
import java.util.List;

public class FavoriteActivity extends AppCompatActivity implements OnMovieClickHandler {

    private static final String TAG = FavoriteActivity.class.getSimpleName() + "DatabaseChanges";
    private MovieDatabaseAdapter mAdapter;
    private MovieDatabase database;
    private ActivityFavoriteBinding favoriteBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        favoriteBinding = DataBindingUtil.setContentView(this, R.layout.activity_favorite);

        // Initial MovieDatabaseAdapter
        mAdapter = new MovieDatabaseAdapter(this, this);

        // Setup Recycler View
        favoriteBinding.favoriteRv.setAdapter(mAdapter);
        favoriteBinding.favoriteRv.addItemDecoration(new DividerItemDecoration(FavoriteActivity.this, DividerItemDecoration.VERTICAL));

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
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
                    }
                });
            }
        }).attachToRecyclerView(favoriteBinding.favoriteRv);

        database = MovieDatabase.getInstance(this);
        retrieveMovies();
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
                    favoriteBinding.emptyListView.setVisibility(View.VISIBLE);
                } else {
                    Log.d(TAG, "onChanged: Receiving data change from tha database");
                    // Setup Recycler View
                    mAdapter.setMovieList(movies);
                }
            }

        });
    }

    @Override
    public void onBackPressed() {
        NavUtils.navigateUpFromSameTask(this);
    }

    @Override
    public void onMovieClick(int movieId) {
        startActivity(new Intent(getApplicationContext(), MovieDetailActivity.class).putExtra(MovieDetailActivity.MOVIE_DATABASE_KEY, movieId));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.favorite_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.delete_all:
                AlertDialog.Builder askForDelete = new AlertDialog.Builder(this);
                askForDelete
                        .setTitle("Delete All")
                        .setMessage("Are you sure ?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                AppExecutors.getInstance().diskIO().execute(new Runnable() {
                                    @Override
                                    public void run() {
                                        database.movieDao().deleteAll();
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                mAdapter.setMovieList(new ArrayList<Movie>());
                                            }
                                        });
                                    }
                                });
                            }
                        })
                        .setNeutralButton("Cancel", null)
                        .show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
