package com.hadi.movies.utils.network;

import com.google.gson.Gson;
import com.hadi.movies.model.movie.WebMovieResponse;
import com.hadi.movies.model.review.ReviewResponse;
import com.hadi.movies.model.video.VideoResponse;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface WebServices {
    // URL
    String BASE_URL = "https://api.themoviedb.org/3/movie/";
    // PATHS
    String POPULAR_MOVIES = "popular";
    String TOP_RATED_MOVIES = "top_rated";
    String REVIEWS = "reviews";
    String VIDEOS = "videos";

    String API_KEY = "api_key";
    String KEY = "8d87808da715fcc1f5da7f793310967d";
    Retrofit getMoviesResponse =
            new Retrofit
                    .Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(new Gson()))
                    .build();

    @GET(TOP_RATED_MOVIES)
    Call<WebMovieResponse> getTopRatedMovies(@Query(API_KEY) String key);

    @GET(POPULAR_MOVIES)
    Call<WebMovieResponse> getPopularMovies(@Query(API_KEY) String key);

    @GET("{movieId}/" + REVIEWS)
    Call<ReviewResponse> getMovieReview(@Path("movieId") int id, @Query(API_KEY) String key);

    @GET("{movieId}/" + VIDEOS)
    Call<VideoResponse> getMovieTrailer(@Path("movieId") int id, @Query(API_KEY) String key);
}
