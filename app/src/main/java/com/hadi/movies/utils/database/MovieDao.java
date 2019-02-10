package com.hadi.movies.utils.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.hadi.movies.model.movie.Movie;

import java.util.List;

@Dao
public interface MovieDao {
    @Query("SELECT * FROM movie")
    LiveData<List<Movie>> getAllMovies();

    @Query("SELECT id FROM movie WHERE id=:movieId")
    int getMovieId(int movieId);

    @Query("SELECT * FROM movie WHERE id =:movieId")
    LiveData<Movie> loadMovie(int movieId);

    @Insert
    void insertMovie(Movie movie);

    @Delete
    void removeMovie(Movie movie);

    @Query("DELETE FROM movie")
    void deleteAll();
}
