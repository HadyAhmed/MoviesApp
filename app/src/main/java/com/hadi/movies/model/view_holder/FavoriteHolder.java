package com.hadi.movies.model.view_holder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;

import com.hadi.movies.databinding.FavoriteItemBinding;
import com.hadi.movies.model.movie.Movie;

public class FavoriteHolder extends RecyclerView.ViewHolder {
    private FavoriteItemBinding itemBinding;

    public FavoriteHolder(@NonNull FavoriteItemBinding itemView) {
        super(itemView.getRoot());
        itemBinding = itemView;
    }

    public FavoriteItemBinding getItemBinding() {
        return itemBinding;
    }

    public void setItemBinding(Movie movie) {
        this.itemBinding.setMovieDetail(movie);
    }
}
