package com.southkart.popularmovies.utilities;

import android.net.Uri;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * Created by tantryr on 3/23/18.
 */

public final class NetworkUtils {
    public static final String LOG_TAG = NetworkUtils.class.getSimpleName();

    private static final String MOVIE_BASE_URL = "https://api.themoviedb.org/3/movie";
    private static final String YOUTUBE_BASE_URL = "https://www.youtube.com/watch";
    private static final String IMAGE_BASE_URL = "https://image.tmdb.org/t/p/";

    public static final String IMAGE_SIZE = "w185";

    private static final String API_PARAM = "api_key";
    private static final String API_KEY = "084e39feed1eced40afe8e9f9648caf7";

    private static final String LANGUAGE_PARAM = "language";
    private static final String LANGUAGE_VALUE = "en-US";

    private static final String PAGE_PARAM = "page";
    private static final String PAGE_VALUE = "1";

    private static final String YOUTUBE_PARAM = "v";

    /**
     * A static Function that will return a URL given a sort criteria
     *
     * @param sortCriteria The sort criteria can be Ascending or Descending
     * @return url  We will return a formatted URI to the caller
     */
    public static URL buildUrl(String sortCriteria) {

        /* Use URI Builder to create a URL which is properly formatted */
        Uri builtUri = Uri.parse(MOVIE_BASE_URL).buildUpon()
                .appendPath(sortCriteria)
                .appendQueryParameter(API_PARAM, API_KEY)
                .appendQueryParameter(LANGUAGE_PARAM, LANGUAGE_VALUE)
                .appendQueryParameter(PAGE_PARAM, PAGE_VALUE)
                .build();


        URL url = null;

        /* Convert the built URI to URL*/
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Log.d(LOG_TAG, "Built URI " + url);

        /* Return the URL to the caller */
        return url;
    }

    public static URL buildYoutubeUrl(String key) {

        /* Use URI Builder to create a URL which is properly formatted */
        Uri builtUri = Uri.parse(YOUTUBE_BASE_URL).buildUpon()
                .appendQueryParameter(YOUTUBE_PARAM, key)
                .build();


        URL url = null;

        /* Convert the built URI to URL*/
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Log.d(LOG_TAG, "Built URI " + url);

        /* Return the URL to the caller */
        return url;
    }

    public static URL buildMovieDetailUrl(String movieId) {

        /* Use URI Builder to create a URL which is properly formatted */
        Uri builtUri = Uri.parse(MOVIE_BASE_URL).buildUpon()
                .appendPath(movieId)
                .appendQueryParameter(API_PARAM, API_KEY)
                .appendQueryParameter(LANGUAGE_PARAM, LANGUAGE_VALUE)
                .build();

        URL url = null;

        /* Convert the built URI to URL*/
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Log.d(LOG_TAG, "Built URI " + url);

        /* Return the URL to the caller */
        return url;
    }

    public static URL buildMovieVideoUrl(String movieId) {

        /* Use URI Builder to create a URL which is properly formatted */
        Uri builtUri = Uri.parse(MOVIE_BASE_URL).buildUpon()
                .appendPath(movieId)
                .appendPath("videos")
                .appendQueryParameter(API_PARAM, API_KEY)
                .build();

        URL url = null;

        /* Convert the built URI to URL*/
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Log.d(LOG_TAG, "Built URI " + url);

        /* Return the URL to the caller */
        return url;
    }

    public static URL buildMovieReviewsUrl(String movieId) {

        /* Use URI Builder to create a URL which is properly formatted */
        Uri builtUri = Uri.parse(MOVIE_BASE_URL).buildUpon()
                .appendPath(movieId)
                .appendPath("reviews")
                .appendQueryParameter(API_PARAM, API_KEY)
                .build();

        URL url = null;

        /* Convert the built URI to URL*/
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Log.d(LOG_TAG, "Built URI " + url);

        /* Return the URL to the caller */
        return url;
    }

    public static URL buildImageURL(String imageId) {
        Uri builtUri = Uri.parse(IMAGE_BASE_URL).buildUpon()
                .appendPath(IMAGE_SIZE)
                .appendPath(imageId)
                .build();

        URL url = null;

        /* Convert the built URI to URL*/
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Log.d(LOG_TAG, "IMAGE URL " + url);

        /* Return the URL to the caller */
        return url;
    }

    public static String buildImageString(String imageId) {
        Uri builtUri = Uri.parse(IMAGE_BASE_URL).buildUpon()
                .appendPath(IMAGE_SIZE)
                .appendPath(imageId)
                .build();

        String url = builtUri.toString();

        /* Return the URL to the caller */
        return url;
    }


    /**
     * A Static function to perform the low level network operation
     *
     * @param url The URL from which we have to fetch the data
     * @return We return the data in String format to the caller
     */
    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }
}
