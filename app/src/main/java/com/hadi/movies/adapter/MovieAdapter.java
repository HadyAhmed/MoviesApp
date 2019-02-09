package com.hadi.movies.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.hadi.movies.R;
import com.hadi.movies.activity.MovieDetailActivity;
import com.hadi.movies.model.movie.Movie;
import com.hadi.movies.utils.NetworkUtils;
import com.squareup.picasso.Picasso;

import java.net.URL;
import java.util.List;


public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {
    private final Context mContext;
    private List<Movie> mMovie;

    public MovieAdapter(Context mContext, List<Movie> mMovie) {
        this.mContext = mContext;
        this.mMovie = mMovie;
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.movie_item, viewGroup, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder viewHolder, int position) {
        final Movie currentMovie = mMovie.get(position);
        if (currentMovie != null) {
            URL imageUrl = NetworkUtils.buildURL(currentMovie.getPosterPath());
            if (imageUrl != null) {
                Picasso.with(mContext).load(imageUrl.toString()).into(viewHolder.moviePoster);
            }
            viewHolder.currentItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent movieDetailIntent = new Intent(mContext, MovieDetailActivity.class);
                    movieDetailIntent.putExtra(MovieDetailActivity.MOVIE_KEY, currentMovie);
                    mContext.startActivity(movieDetailIntent);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        if (0 == mMovie.size()) {
            return 0;
        } else {
            return mMovie.size();
        }
    }


    class MovieViewHolder extends RecyclerView.ViewHolder {
        ImageView moviePoster;
        View currentItem;

        MovieViewHolder(@NonNull View itemView) {
            super(itemView);
            moviePoster = itemView.findViewById(R.id.movie_poster_image);
            currentItem = itemView;
        }
    }
}
