package com.southkart.popularmovies.utilities;

import android.util.Log;

import com.southkart.popularmovies.data.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by tantryr on 3/23/18.
 */

public final class JsonUtils {

    private static final String LOG_TAG = JsonUtils.class.getSimpleName();

    public static ArrayList<Movie> getMoviesListFromJSON(String resultsJSONString) throws JSONException {
        ArrayList<Movie> movies = new ArrayList<>();

        JSONObject root = new JSONObject(resultsJSONString);
        JSONArray results = root.getJSONArray("results");

        for (int index = 0; index < results.length(); index++) {
            // Get the reference of the movie object
            JSONObject movieObject = results.getJSONObject(index);

            // Get value of each movie
            String apiId = Integer.toString(movieObject.getInt("id"));
            String thumbnailUrl = movieObject.getString("poster_path").substring(1);

            // Create a movie object from the collected data
            Movie movie = new Movie(apiId, thumbnailUrl);
            movies.add(movie);
        }
        return movies;
    }

    public static Movie getMovieDetailsFromJSON(String resultsJSONString) throws JSONException {
        Movie movie = null;
        JSONObject root = new JSONObject(resultsJSONString);

        String title = root.getString("title");
        String thumbnailUrl = root.getString("poster_path").substring(1);
        String synopsis = root.getString("overview");
        double userRatings = root.getDouble("vote_average");
        String releaseData = root.getString("release_date");
        String runTime = Integer.toString(root.getInt("runtime"));

        // Create a movie object from the collected data
        movie = new Movie(title, thumbnailUrl, synopsis, userRatings, releaseData, runTime);

        return movie;
    }

    public static Movie getMovieVideoFromJSON(String resultsJSONString, Movie movie) throws JSONException {

        JSONObject root = new JSONObject(resultsJSONString);
        JSONArray results = root.getJSONArray("results");
        ArrayList<String> videoKey = new ArrayList<String>();

        for (int index = 0; index < results.length(); index++) {
            // Get the reference of the movie object
            JSONObject movieObject = results.getJSONObject(index);

            // Get value of each movie
            videoKey.add(movieObject.getString("key"));
        }

        movie.setmVideoKeys(videoKey);
        return movie;
    }

    public static Movie getMovieReviewsFromJSON(String resultsJSONString, Movie movie) throws JSONException {

        JSONObject root = new JSONObject(resultsJSONString);
        JSONArray results = root.getJSONArray("results");
        ArrayList<String> reviews = new ArrayList<String>();

        for (int index = 0; index < results.length(); index++) {
            // Get the reference of the movie object
            JSONObject reviewObject = results.getJSONObject(index);

            // Get value of each movie
            reviews.add(reviewObject.getString("content"));
        }

        movie.setmReviews(reviews);
        return movie;
    }


}
