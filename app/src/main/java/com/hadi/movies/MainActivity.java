package com.hadi.movies;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    public static final String API_KEY = "8d87808da715fcc1f5da7f793310967d";
    public static final String TOP_RATED = "https://api.themoviedb.org/3/movie/top_rated?api_key=8d87808da715fcc1f5da7f793310967d&language=en-US";
    public static final String MOST_POPULAR = "https://api.themoviedb.org/3/movie/popular?api_key=8d87808da715fcc1f5da7f793310967d&language=en-US";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
