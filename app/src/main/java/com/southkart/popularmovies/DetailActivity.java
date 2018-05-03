package com.southkart.popularmovies;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.speech.tts.TextToSpeech;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.southkart.popularmovies.data.Movie;
import com.southkart.popularmovies.data.MovieAdapter;
import com.southkart.popularmovies.data.MovieContract;
import com.southkart.popularmovies.data.TrailerAdapter;
import com.southkart.popularmovies.utilities.JsonUtils;
import com.southkart.popularmovies.utilities.NetworkUtils;

import java.net.URL;
import java.util.ArrayList;

public class DetailActivity extends AppCompatActivity implements
        TrailerAdapter.TrailerAdapterOnClickHandler,
        LoaderManager.LoaderCallbacks<Movie> {

    public static final String LOG_TAG = DetailActivity.class.getSimpleName();

    private String mMovieId;
    private String mMovieThumbnailUrlKey;

    private ImageView mMovieThumbnail;
    private TextView mMovieReleaseDate;
    private TextView mMovieRunTime;
    private TextView mMovieRatings;
    private TextView mMovieSynopsis;
    private RecyclerView mRecyclerView;
    private TextView mMovieTrailerUrl;
    private TrailerAdapter mTrailerAdapter;
    private ProgressBar mLoadingIndicator;
    private TextView mErrorMessageView;
    private LinearLayout mLinearLayoutWrapper;
    private TextView mReviews;
    private Button mButton;
    private TextView mMarkedIndicator;

    private static final int MOVIE_DETAIL_LOADER = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        setTitle("");

        // Set setDisplayHomeAsUpEnabled to true on the support ActionBar
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();

        if (intent.hasExtra(MainActivity.ID_MOVIE_ID)) {
            String movieId = intent.getStringExtra(MainActivity.ID_MOVIE_ID);
            mMovieId = movieId;
        }

        if (intent.hasExtra(MainActivity.ID_MOVIE_THUMBNAIL_URL)) {
            mMovieThumbnailUrlKey = intent.getStringExtra(MainActivity.ID_MOVIE_THUMBNAIL_URL);
        }

        mMovieThumbnail = (ImageView) findViewById(R.id.iv_poster);
        mMovieReleaseDate = (TextView) findViewById(R.id.tv_release_date);
        mMovieRunTime = (TextView) findViewById(R.id.tv_run_time);
        mMovieRatings = (TextView) findViewById(R.id.tv_ratings);
        mMovieSynopsis = (TextView) findViewById(R.id.tv_plot_synopsis);
        mRecyclerView = (RecyclerView) findViewById(R.id.rv_movie_trailer_list);
        mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_details_loading_indicator);
        mErrorMessageView = (TextView) findViewById(R.id.tv__details_error_message_display);
        mLinearLayoutWrapper = (LinearLayout) findViewById(R.id.movie_data_wrapper);
        mReviews = (TextView) findViewById(R.id.tv_reviews);
        mButton = (Button) findViewById(R.id.btn_make_favorite);
        mMarkedIndicator = (TextView) findViewById(R.id.tv_marked_indicator);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setHasFixedSize(true);

        // Instantiate the adapter and pass it a reference of a click handler
        mTrailerAdapter = new TrailerAdapter(this, this);

        // Set the adapter to RecyclerView
        mRecyclerView.setAdapter(mTrailerAdapter);

        getSupportLoaderManager().initLoader(MOVIE_DETAIL_LOADER, null, this);
    }

    @SuppressLint("StaticFieldLeak")
    @NonNull
    @Override
    public Loader<Movie> onCreateLoader(int id, @Nullable Bundle args) {
        return new android.support.v4.content.AsyncTaskLoader<Movie>(this) {

            /* This String array will hold and help cache our weather data */
            Movie movie = null;

            /**
             * Subclasses of AsyncTaskLoader must implement this to take care of loading their data.
             */
            @Override
            protected void onStartLoading() {
                if (movie != null) {
                    deliverResult(movie);
                } else {
                    mLoadingIndicator.setVisibility(View.VISIBLE);
                    forceLoad();
                }
            }

            @Nullable
            @Override
            public Movie loadInBackground() {
                try {
                    // Build the URL
                    URL movieURL = NetworkUtils.buildMovieDetailUrl(mMovieId);
                    Log.d(LOG_TAG, movieURL.toString());

                    // Fetch the json data
                    String jsonMovieResponse = NetworkUtils.getResponseFromHttpUrl(movieURL);

                    // Parse the json data
                    movie = JsonUtils.getMovieDetailsFromJSON(jsonMovieResponse);

                    // Build Movie Video URL
                    URL movieVideoUrl = NetworkUtils.buildMovieVideoUrl(mMovieId);
                    Log.d(LOG_TAG, movieVideoUrl.toString());

                    // Fetch json data
                    String jsonMovieVideoResponse = NetworkUtils.getResponseFromHttpUrl(movieVideoUrl);
                    Log.d(LOG_TAG, jsonMovieVideoResponse);
                    movie = JsonUtils.getMovieVideoFromJSON(jsonMovieVideoResponse, movie);

                    // Build Movie Review URL
                    URL movieReviewUrl = NetworkUtils.buildMovieReviewsUrl(mMovieId);

                    // Fetch json data
                    String jsonMovieReviewsResponse = NetworkUtils.getResponseFromHttpUrl(movieReviewUrl);

                    movie = JsonUtils.getMovieReviewsFromJSON(jsonMovieReviewsResponse, movie);

                    return movie;

                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }

            /**
             * Sends the result of the load to the registered listener.
             *
             * @param data The result of the load
             */
            public void deliverResult(Movie data) {
                movie = data;
                super.deliverResult(data);
            }
        };
    }

    private void showMovieDataView() {
        mErrorMessageView.setVisibility(View.INVISIBLE);
        mLinearLayoutWrapper.setVisibility(View.VISIBLE);
    }

    private void showErrorMessageView() {
        mErrorMessageView.setVisibility(View.VISIBLE);
        mLinearLayoutWrapper.setVisibility(View.INVISIBLE);
    }


    @Override
    public void onLoadFinished(@NonNull Loader<Movie> loader, Movie movie) {
        mLoadingIndicator.setVisibility(View.INVISIBLE);

        if (movie != null && !movie.equals("")) {
            showMovieDataView();

            URL thumbnailUrl = NetworkUtils.buildImageURL(movie.getmThumbnailUrl());
            Glide.with(this).load(thumbnailUrl.toString())
                    .into(mMovieThumbnail);
            setTitle(movie.getmTitle());
            mMovieReleaseDate.setText(movie.getmReleaseData());
            mMovieRunTime.setText(movie.getmRuntime());
            mMovieRatings.setText(movie.getmUserRating());
            mMovieSynopsis.setText(movie.getmSynopsis());
            mTrailerAdapter.setVideoKeys(movie.getmVideoKeys());

            ArrayList<String> reviews = movie.getmReviews();
            if(reviews.size() > 0){
                for(int index=0; index< reviews.size(); index++){
                    mReviews.append(reviews.get(index));
                }
            }else {
                mReviews.setText(R.string.no_reviews);
            }
        } else {
            showErrorMessageView();
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Movie> loader) {

    }

    @Override
    public void onClick(String videoKey) {
        String youtubeKey = NetworkUtils.buildYoutubeUrl(videoKey).toString();
        Intent webIntent = new Intent(Intent.ACTION_VIEW,
                Uri.parse(videoKey));

        if (webIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(webIntent);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        /* Use AppCompatActivity's method getMenuInflater to get a handle on the menu inflater */
        MenuInflater inflater = getMenuInflater();
        /* Use the inflater's inflate method to inflate our menu layout to this menu */
        inflater.inflate(R.menu.detail, menu);
        /* Return true so that the menu is displayed in the Toolbar */
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
        }

        if (id == R.id.action_settings) {
            Intent startSettingsActivity = new Intent(this, SettingsActivity.class);
            startActivity(startSettingsActivity);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void markAsFavorite(View view) {
        ContentValues value = new ContentValues();
        value.put(MovieContract.MovieEntry.COLUMN_API_ID,mMovieId);
        value.put(MovieContract.MovieEntry.COLUMN_THUMBNAIL_URL,mMovieThumbnailUrlKey);
        Uri uri = getContentResolver().insert(MovieContract.MovieEntry.CONTENT_URI,value);

        if(uri != null){
            Context context = DetailActivity.this;
            String textToShow = "Movie set a as favorite";
            Toast.makeText(context, textToShow, Toast.LENGTH_SHORT).show();
            mButton.setVisibility(View.INVISIBLE);
            mMarkedIndicator.setVisibility(View.VISIBLE);
        }
    }
}
