package com.hadi.movies.model.viewholder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.hadi.movies.R;

public class MovieViewHolder extends RecyclerView.ViewHolder {
    private ImageView moviePoster;
    private View currentItem;

    public MovieViewHolder(@NonNull View itemView) {
        super(itemView);
        moviePoster = itemView.findViewById(R.id.movie_poster_image);
        currentItem = itemView;
    }

    public ImageView getMoviePoster() {
        return moviePoster;
    }

    public View getCurrentItem() {
        return currentItem;
    }
}