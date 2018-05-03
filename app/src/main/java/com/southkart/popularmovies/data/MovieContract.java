package com.southkart.popularmovies.data;

import android.net.Uri;
import android.provider.BaseColumns;
import android.util.Log;

public class MovieContract {

    public static final String CONTENT_AUTHORITY = "com.southkart.popularmovies";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_MOVIES = "movies";

    public static final class MovieEntry implements BaseColumns {

        /* The base CONTENT_URI used to query the Movies table from the content provider */
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_MOVIES)
                .build();

        /* Used internally as the name of our weather table. */
        public static final String TABLE_NAME = "movies";

        /* Columns in the table */
        public static final String COLUMN_API_ID = "api_id";
        public static final String COLUMN_THUMBNAIL_URL = "thumbnail_url";

        public static Uri buildMovieUriWithId(String id) {
            return BASE_CONTENT_URI.buildUpon()
                    .appendPath(PATH_MOVIES)
                    .appendPath(id)
                    .build();
        }
    }
}