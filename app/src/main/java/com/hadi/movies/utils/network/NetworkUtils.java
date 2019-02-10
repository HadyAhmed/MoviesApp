package com.hadi.movies.utils.network;

import android.net.Uri;

import java.net.MalformedURLException;
import java.net.URL;

public final class NetworkUtils {
    private static final String BASE_IMAGE_API = "http://image.tmdb.org/t/p/w342";

    /**
     * @return The URL to use for Movie Image Poster.
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

}
