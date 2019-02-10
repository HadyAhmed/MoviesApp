package com.hadi.movies.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.hadi.movies.databinding.FavoriteItemBinding;
import com.hadi.movies.interfaces.OnMovieClickHandler;
import com.hadi.movies.model.movie.Movie;
import com.hadi.movies.model.viewholder.FavoriteHolder;

import java.util.List;

public class MovieDatabaseAdapter extends RecyclerView.Adapter<FavoriteHolder> {
    private List<Movie> mMovieList;
    private LayoutInflater inflater;
    private OnMovieClickHandler onMovieClickHandler;

    public MovieDatabaseAdapter(OnMovieClickHandler movieClickHandler) {
        this.onMovieClickHandler = movieClickHandler;
    }

    @NonNull
    @Override
    public FavoriteHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        if (inflater == null) {
            inflater = LayoutInflater.from(viewGroup.getContext());
        }
        FavoriteItemBinding binding = FavoriteItemBinding.inflate(inflater, viewGroup, false);
        return new FavoriteHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoriteHolder favoriteHolder, int i) {
        final Movie currentMovie = mMovieList.get(i);
        favoriteHolder.setItemBinding(currentMovie);
        favoriteHolder.getItemBinding().setClickHandler(onMovieClickHandler);
    }

    @Override
    public int getItemCount() {
        if (mMovieList == null) {
            return 0;
        } else {
            return mMovieList.size();
        }
    }

    public List<Movie> getMovieList() {
        return mMovieList;
    }

    public void setMovieList(List<Movie> mMovieList) {
        this.mMovieList = mMovieList;
        notifyDataSetChanged();
    }
}
