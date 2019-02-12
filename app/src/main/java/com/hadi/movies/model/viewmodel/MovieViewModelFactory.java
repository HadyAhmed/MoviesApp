package com.hadi.movies.model.viewmodel;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.hadi.movies.utils.database.MovieDatabase;

public class MovieViewModelFactory extends ViewModelProvider.NewInstanceFactory {
    private final MovieDatabase mDatabase;
    private final int mMovieId;

    public MovieViewModelFactory(MovieDatabase mDatabase, int mMovieId) {
        this.mDatabase = mDatabase;
        this.mMovieId = mMovieId;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new AddMovieViewModel(mDatabase, mMovieId);
    }
}
