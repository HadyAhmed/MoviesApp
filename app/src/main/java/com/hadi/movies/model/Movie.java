package com.hadi.movies.model;

/*
original title
movie poster image thumbnail
A plot synopsis (called overview in the api)
user rating (called vote_average in the api)
release date
 */

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

public class Movie implements Parcelable {
    public static final Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel source) {
            return new Movie(source);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };
    private final String movieOriginalTitle;
    private String moviePosterUrl;
    private String moviePosterBackDropUrl;
    private String movieOverView;
    private double movieVoteAverage;
    private String movieReleaseDate;

    public Movie(String movieOriginalTitle, String moviePosterUrl, String moviePosterBackDropUrl, String movieOverView, double movieVoteAverage, String movieReleaseDate) {
        this.movieOriginalTitle = movieOriginalTitle;
        this.moviePosterUrl = moviePosterUrl;
        this.moviePosterBackDropUrl = moviePosterBackDropUrl;
        this.movieOverView = movieOverView;
        this.movieVoteAverage = movieVoteAverage;
        this.movieReleaseDate = movieReleaseDate;
    }

    private Movie(Parcel in) {
        this.movieOriginalTitle = in.readString();
        this.moviePosterUrl = in.readString();
        this.moviePosterBackDropUrl = in.readString();
        this.movieOverView = in.readString();
        this.movieVoteAverage = in.readDouble();
        this.movieReleaseDate = in.readString();
    }

    public String getMovieOriginalTitle() {
        return movieOriginalTitle;
    }

    public String getMoviePosterUrl() {
        return moviePosterUrl;
    }

    public String getMoviePosterBackDropUrl() {
        return moviePosterBackDropUrl;
    }

    public String getMovieOverView() {
        return movieOverView;
    }

    public double getMovieVoteAverage() {
        return movieVoteAverage;
    }

    public String getMovieReleaseDate() {
        return movieReleaseDate;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.movieOriginalTitle);
        dest.writeString(this.moviePosterUrl);
        dest.writeString(this.moviePosterBackDropUrl);
        dest.writeString(this.movieOverView);
        dest.writeDouble(this.movieVoteAverage);
        dest.writeString(this.movieReleaseDate);
    }

    @NonNull
    @Override
    public String toString() {
        return "Movie{" +
                "movieOriginalTitle='" + movieOriginalTitle + '\'' +
                ", moviePosterUrl='" + moviePosterUrl + '\'' +
                ", moviePosterBackDropUrl='" + moviePosterBackDropUrl + '\'' +
                ", movieOverView='" + movieOverView + '\'' +
                ", movieVoteAverage=" + movieVoteAverage +
                ", movieReleaseDate='" + movieReleaseDate + '\'' +
                '}';
    }
}
