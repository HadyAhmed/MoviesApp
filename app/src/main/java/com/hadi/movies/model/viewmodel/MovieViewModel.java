package com.hadi.movies.model.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.hadi.movies.model.movie.Movie;
import com.hadi.movies.utils.database.MovieDatabase;

import java.util.List;

public class MovieViewModel extends AndroidViewModel {

    private LiveData<List<Movie>> movies;

    public MovieViewModel(@NonNull Application application) {
        super(application);
        movies = MovieDatabase.getInstance(application.getApplicationContext()).movieDao().getAllMovies();
    }

    public LiveData<List<Movie>> getMovies() {
        return movies;
    }
}
