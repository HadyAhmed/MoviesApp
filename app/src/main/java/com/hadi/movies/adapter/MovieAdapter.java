package com.hadi.movies.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hadi.movies.R;
import com.hadi.movies.activity.MovieDetailActivity;
import com.hadi.movies.model.movie.Movie;
import com.hadi.movies.model.viewholder.MovieViewHolder;
import com.hadi.movies.utils.network.NetworkUtils;
import com.squareup.picasso.Picasso;

import java.net.URL;
import java.util.List;


public class MovieAdapter extends RecyclerView.Adapter<MovieViewHolder> {
    private Context mContext;
    private List<Movie> mMovie;

    public MovieAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public List<Movie> getMovie() {
        return mMovie;
    }

    public void setMovie(List<Movie> mMovie) {
        this.mMovie = mMovie;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new MovieViewHolder(LayoutInflater.from(mContext).inflate(R.layout.movie_item, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder viewHolder, int position) {
        final Movie currentMovie = mMovie.get(position);
        if (currentMovie != null) {
            URL imageUrl = NetworkUtils.buildURL(currentMovie.getPosterPath());
            if (imageUrl != null) {
                Picasso.with(mContext).load(imageUrl.toString()).into(viewHolder.getMoviePoster());
            }
            viewHolder.getCurrentItem().setOnClickListener(new View.OnClickListener() {
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
        if (mMovie == null) {
            return 0;
        } else {
            return mMovie.size();
        }
    }
}
