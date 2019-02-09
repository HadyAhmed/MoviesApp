package com.hadi.movies.utils;

import android.net.Uri;

import com.hadi.movies.model.movie.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.net.ssl.HttpsURLConnection;

public final class NetworkUtils {
    public static final String TAG = NetworkUtils.class.getSimpleName();
    public static final byte MOST_POPULAR_SORT = 0;
    public static final byte TOP_RATED_SORT = 1;
    private static final String BASE_API = "https://api.themoviedb.org/3/movie";
    private static final String PARAM_TOP_RATED = "top_rated";
    private static final String PARAM_POPULAR = "popular";
    private static final String API_KEY_VALUE = "8d87808da715fcc1f5da7f793310967d";
    private static final String PARAM_LANGUAGE = "language";
    private static final String ENGLISH_LANGUAGE_VALUE = "en-US";
    private static final String PARAM_API_KEY = "api_key";
    private static final String BASE_IMAGE_API = "http://image.tmdb.org/t/p/w342";

    /**
     * Builds the URL used to query TheMoviesDb.
     *
     * @param sortBy The keyword that will be queried for movies to show up.
     * @return The URL to use to query the TheMoviesDb.
     */
    public static URL buildURL(byte sortBy /*Top Rated = 0, Popular = 1*/) {
        Uri buildUri = Uri.parse(BASE_API)
                .buildUpon()
                .appendPath(sortBy == MOST_POPULAR_SORT ? PARAM_POPULAR : PARAM_TOP_RATED)
                .appendQueryParameter(PARAM_API_KEY, API_KEY_VALUE)
                .appendQueryParameter(PARAM_LANGUAGE, ENGLISH_LANGUAGE_VALUE)
                .build();
        URL url;
        try {
            url = new URL(buildUri.toString());
            return url;
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Builds the URL used to query TheMoviesDb.
     *
     * @return The URL to use to query the TheMoviesDb.
     */
    public static URL buildURL(String imagePosterUrl) {
        Uri buildUri = Uri.parse(BASE_IMAGE_API + imagePosterUrl)
                .buildUpon()
                .build();
        URL url;
        try {
            url = new URL(buildUri.toString());
            return url;
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * This method returns the entire result from the HTTP response.
     *
     * @param url The URL to fetch the HTTP response from.
     * @return The contents of the HTTP response.
     * @throws IOException Related to network and stream reading
     */
    private static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpsURLConnection httpsURLConnection = (HttpsURLConnection) url.openConnection();
        httpsURLConnection.setRequestMethod("GET");
        httpsURLConnection.setConnectTimeout(15000);
        httpsURLConnection.setReadTimeout(10000);
        if (httpsURLConnection.getResponseCode() == 200) {
            try {
                InputStream in = httpsURLConnection.getInputStream();
                Scanner sc = new Scanner(in).useDelimiter("\\A");
                boolean hasNext = sc.hasNext();
                if (hasNext) {
                    return sc.next();
                } else {
                    return null;
                }
            } finally {
                httpsURLConnection.disconnect();
            }
        } else {
            return null;
        }
    }

    /**
     * Fetching the {@link List<Movie>} from the Responded JSON in the {@link HttpsURLConnection}
     * was opened
     *
     * @param url is the API used to fetch the JSON data
     * @return list with {@link Movie}
     * @throws IOException Related to network and json reading.
     */
    public static List<Movie> fetchMoviesFromHttpResponse(URL url) throws IOException {
        if (url == null) {
            return null;
        } else {
            // Getting the Whole Response JSON
            String responseJSON = getResponseFromHttpUrl(url);
            List<Movie> movieList = new ArrayList<>();
            if (responseJSON != null) {
                try {
                    JSONObject jsonRootObject = new JSONObject(responseJSON);
                    JSONArray resultArray = jsonRootObject.getJSONArray("results");
                    for (int i = 0; i < resultArray.length(); i++) {
                        JSONObject currentJson = resultArray.getJSONObject(i);
                        String movieOriginalTitle = currentJson.getString("original_title");
                        String moviePosterUrl = currentJson.getString("poster_path");
                        String moviePosterBackDropUrl = currentJson.getString("backdrop_path");
                        String movieOverView = currentJson.getString("overview");
                        double movieVoteAverage = currentJson.getDouble("vote_average");
                        String movieReleaseDate = currentJson.getString("release_date");
//                        movieList.add(new Movie(movieOriginalTitle, moviePosterUrl, moviePosterBackDropUrl, movieOverView, movieVoteAverage, movieReleaseDate));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return movieList;
        }
    }

}
