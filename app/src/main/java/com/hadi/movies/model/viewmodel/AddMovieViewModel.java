package com.hadi.movies.model.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.hadi.movies.model.movie.Movie;
import com.hadi.movies.utils.database.MovieDatabase;

public class AddMovieViewModel extends ViewModel {
    private LiveData<Movie> mMovie;

    public AddMovieViewModel(MovieDatabase database, int movieId) {
        mMovie = database.movieDao().loadMovie(movieId);
    }

    public LiveData<Movie> getMovie() {
        return mMovie;
    }
}
