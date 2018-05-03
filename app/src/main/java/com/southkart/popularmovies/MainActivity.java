package com.southkart.popularmovies;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.southkart.popularmovies.data.Movie;
import com.southkart.popularmovies.data.MovieAdapter;
import com.southkart.popularmovies.utilities.JsonUtils;
import com.southkart.popularmovies.utilities.NetworkUtils;
import com.southkart.popularmovies.utilities.PreferenceUtils;

import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<ArrayList<Movie>>,
        MovieAdapter.MovieAdapterOnClickHandler,
        FavoritesFragment.OnFragmentInteractionListener,
        SharedPreferences.OnSharedPreferenceChangeListener {

    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    // Private static boolean flag for preference updates and initialize it to false
    private static boolean PREFERENCES_HAVE_BEEN_UPDATED = false;

    private static final int POPULAR_MOVIE_LOADER = 1;
    public static final String ID_MOVIE_ID = "movieID";
    public static final String ID_MOVIE_NAME = "movieName";
    public static final String ID_MOVIE_THUMBNAIL_URL = "movieThumbnailUrl";

    private RecyclerView mRecyclerView;
    private Fragment mFragment;
    private String mSortOrder;

    private TextView mErrorMessageView;
    private MovieAdapter mMovieAdapter;
    private ProgressBar mLoadingIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get Error Message Text View Reference
        mErrorMessageView = (TextView) findViewById(R.id.tv_error_message_display);

        // Reference of the ProgressBar
        mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicator);

        //  Get the reference of the Recycler View created during onCreate
        mRecyclerView = (RecyclerView) findViewById(R.id.rv_movie_list);

        int spanCount = getApplicationContext().getResources().getInteger(R.integer.span_count);

        // Set a Layout Manager to the RecyclerView
        GridLayoutManager layoutManager
                = new GridLayoutManager(this, spanCount);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);

        // Instantiate the adapter and pass it a reference of a click handler
        mMovieAdapter = new MovieAdapter(this, this);

        // Set the adapter to RecyclerView
        mRecyclerView.setAdapter(mMovieAdapter);

        mFragment = getSupportFragmentManager().findFragmentById(R.id.favorites_fragment);

        setPreference();

        /*
         * Register MainActivity as an OnPreferenceChangedListener to receive a callback when a
         * SharedPreference has changed.
         */
        PreferenceManager.getDefaultSharedPreferences(this)
                .registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (PREFERENCES_HAVE_BEEN_UPDATED) {
            setPreference();
            setUpViewsVisibility();
            triggerLoaderReload();
            PREFERENCES_HAVE_BEEN_UPDATED = false;
        }
    }

    private void setPreference() {
        mSortOrder = PreferenceUtils.getPreferredSortOrder(this);
        setUpViewsVisibility();
        triggerLoaderReload();
    }

    private void setUpViewsVisibility() {
        if (mSortOrder.equalsIgnoreCase("favorites")) {
            hideRecyclerView();
            showFavoritesFragmentView();
        } else {
            showRecyclerView();
            hideFavoritesFragmentView();
        }
    }

    private void triggerLoaderReload() {
        if (!mSortOrder.equalsIgnoreCase("favorites")) {
            getSupportLoaderManager().restartLoader(POPULAR_MOVIE_LOADER, null, this);
        }
    }


    // Override onDestroy and unregister MainActivity as a SharedPreferenceChangedListener
    @Override
    protected void onDestroy() {
        super.onDestroy();

        /* Unregister MainActivity as an OnPreferenceChangedListener to avoid any memory leaks. */
        PreferenceManager.getDefaultSharedPreferences(this)
                .unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        /* Use AppCompatActivity's method getMenuInflater to get a handle on the menu inflater */
        MenuInflater inflater = getMenuInflater();
        /* Use the inflater's inflate method to inflate our menu layout to this menu */
        inflater.inflate(R.menu.main, menu);
        /* Return true so that the menu is displayed in the Toolbar */
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            Intent startSettingsActivity = new Intent(this, SettingsActivity.class);
            startActivity(startSettingsActivity);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        PREFERENCES_HAVE_BEEN_UPDATED = true;

    }

    private void hideErrorMessage() {
        if (mErrorMessageView.getVisibility() == View.VISIBLE) {
            mErrorMessageView.setVisibility(View.INVISIBLE);
        }
    }

    private void showErrorMessageView() {
        if (mErrorMessageView.getVisibility() == View.INVISIBLE) {
            mErrorMessageView.setVisibility(View.VISIBLE);
        }
    }

    private void hideRecyclerView() {
        if (mRecyclerView.getVisibility() == View.VISIBLE) {
            mRecyclerView.setVisibility(View.INVISIBLE);
        }
    }

    private void showRecyclerView() {
        if (mRecyclerView.getVisibility() == View.INVISIBLE) {
            mRecyclerView.setVisibility(View.VISIBLE);
        }
    }

    private void hideFavoritesFragmentView() {
        if (mFragment.getView().getVisibility() == View.VISIBLE) {
            mFragment.getView().setVisibility(View.INVISIBLE);
        }
    }

    private void showFavoritesFragmentView() {
        if (mFragment.getView().getVisibility() == View.INVISIBLE) {
            mFragment.getView().setVisibility(View.VISIBLE);
        }
    }

    @SuppressLint("StaticFieldLeak")
    @NonNull
    @Override
    public Loader<ArrayList<Movie>> onCreateLoader(int id, @Nullable Bundle args) {
        return new android.support.v4.content.AsyncTaskLoader<ArrayList<Movie>>(this) {

            /* This ArrayList will hold and help cache our movie data */
            ArrayList<Movie> movieList = null;

            /**
             * Subclasses of AsyncTaskLoader must implement this to take care of loading their data.
             */
            @Override
            protected void onStartLoading() {
                if (movieList != null) {
                    deliverResult(movieList);
                } else {
                    mLoadingIndicator.setVisibility(View.VISIBLE);
                    forceLoad();
                }
            }


            @Nullable
            @Override
            public ArrayList<Movie> loadInBackground() {

                if (mSortOrder == null || TextUtils.isEmpty(mSortOrder)) {
                    return null;
                } else {

                    // Build a URL with the sort criteria that was selected
                    URL movieURL = NetworkUtils.buildUrl(mSortOrder);

                    try {
                        // Fetch the json data
                        String jsonMovieResponse = NetworkUtils.getResponseFromHttpUrl(movieURL);
                        Log.d(LOG_TAG, jsonMovieResponse);
                        // Parse the json data
                        movieList = JsonUtils.getMoviesListFromJSON(jsonMovieResponse);

                        return movieList;

                    } catch (Exception e) {
                        e.printStackTrace();
                        return null;
                    }
                }
            }

            /**
             * Sends the result of the load to the registered listener.
             *
             * @param data The result of the load
             */
            public void deliverResult(ArrayList<Movie> data) {
                movieList = data;
                super.deliverResult(data);
            }
        };
    }

    @Override
    public void onLoadFinished(@NonNull Loader<ArrayList<Movie>> loader, ArrayList<Movie> results) {
        mLoadingIndicator.setVisibility(View.INVISIBLE);
        if (results != null && !results.equals("")) {
            hideErrorMessage();
            mMovieAdapter.setMovieData(results);
        } else {
            showErrorMessageView();
            hideRecyclerView();
            hideFavoritesFragmentView();
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<ArrayList<Movie>> loader) {
        mMovieAdapter.setMovieData(null);
    }

    @Override
    public void onClick(Movie movie) {
        Intent movieDetailIntent = new Intent(MainActivity.this, DetailActivity.class);
        movieDetailIntent.putExtra(ID_MOVIE_ID, movie.getmId());
        Log.d("MovieTitle",movie.getmId());
        movieDetailIntent.putExtra(ID_MOVIE_NAME, movie.getmTitle());

        movieDetailIntent.putExtra(ID_MOVIE_THUMBNAIL_URL, movie.getmThumbnailUrl());
        startActivity(movieDetailIntent);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
