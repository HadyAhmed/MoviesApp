package com.hadi.movies.utils.network;

import com.google.gson.Gson;
import com.hadi.movies.model.WebMovieResponse;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface WebServices {

    String BASE_URL = "https://api.themoviedb.org/3/movie/";
    String POPULAR_MOVIES = "popular";
    String TOP_RATED_MOVIES = "top_rated";
    String API_KEY = "api_key";
    String KEY = "8d87808da715fcc1f5da7f793310967d";
    Retrofit getMovies =
            new Retrofit
                    .Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(new Gson()))
                    .build();

    @GET(POPULAR_MOVIES)
    Call<WebMovieResponse> getPopularMovies(@Query(API_KEY) String ley);

    @GET(TOP_RATED_MOVIES)
    Call<WebMovieResponse> getTopRatedMovies(@Query(API_KEY) String ley);
}
