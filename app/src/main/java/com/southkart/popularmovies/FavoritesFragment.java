package com.southkart.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.southkart.popularmovies.data.FavoritesAdapter;
import com.southkart.popularmovies.data.Movie;
import com.southkart.popularmovies.data.MovieAdapter;
import com.southkart.popularmovies.data.MovieContract;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FavoritesFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FavoritesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FavoritesFragment extends Fragment implements
        LoaderManager.LoaderCallbacks<Cursor>,
        FavoritesAdapter.FavoritesAdapterOnClickHandler{

    private static final String LOG_TAG = FavoritesFragment.class.getSimpleName();

    private static final int FAVORITE_MOVIE_LOADER = 1;

    public static final String[] MOVIES_PROJECTION = {
            MovieContract.MovieEntry._ID,
            MovieContract.MovieEntry.COLUMN_API_ID,
            MovieContract.MovieEntry.COLUMN_THUMBNAIL_URL,
    };

    private RecyclerView mRecyclerView;
    private int mPosition = RecyclerView.NO_POSITION;

    public static final String ID_MOVIE_ID = "movieID";
    public static final String ID_MOVIE_NAME = "movieName";
    public static final String ID_MOVIE_THUMBNAIL_URL = "movieThumbnailUrl";

    private FavoritesAdapter mFavoritesAdapter;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public FavoritesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FavoritesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FavoritesFragment newInstance(String param1, String param2) {
        FavoritesFragment fragment = new FavoritesFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_favorites, container, false);
    }

    private void loadData(){
        //  Get the reference of the Recycler View created during onCreate
        mRecyclerView = (RecyclerView) getView().findViewById(R.id.rv_favorites_list);

        // Set a Layout Manager to the RecyclerView
        GridLayoutManager layoutManager
                = new GridLayoutManager(getContext(), 3);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);

        // Instantiate the adapter and pass it a reference of a click handler
        mFavoritesAdapter = new FavoritesAdapter(getContext(),this);

        // Set the adapter to RecyclerView
        mRecyclerView.setAdapter(mFavoritesAdapter);

        LoaderManager loaderManager = getLoaderManager();
        loaderManager.initLoader(FAVORITE_MOVIE_LOADER, null, this);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        loadData();

    }

    @Override
    public void onStart() {
        super.onStart();


    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        loadData();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int loaderId, @Nullable Bundle args) {
        switch (loaderId) {
            case FAVORITE_MOVIE_LOADER:

                /* URI for all rows of movie data in our movies table */
                Uri moviesQueryUri = MovieContract.MovieEntry.CONTENT_URI;

                return new CursorLoader(getContext(),
                        moviesQueryUri,
                        MOVIES_PROJECTION,
                        null,
                        null,
                        null);
            default:
                throw new RuntimeException("Loader Not Implemented: " + loaderId);
        }
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        mFavoritesAdapter.swapCursor(data);
        if (mPosition == RecyclerView.NO_POSITION) mPosition = 0;
        mRecyclerView.smoothScrollToPosition(mPosition);

        if (data.getCount() != 0) {
        } else {
            Context context = getContext();
            String textToShow = "Database Empty";
            Toast.makeText(context, textToShow, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        mFavoritesAdapter.swapCursor(null);
    }

    @Override
    public void onClick(Movie currentMovie) {
        Intent movieDetailIntent = new Intent(getContext(), DetailActivity.class);
        movieDetailIntent.putExtra(ID_MOVIE_ID, currentMovie.getmId());
        movieDetailIntent.putExtra(ID_MOVIE_NAME, currentMovie.getmTitle());
        movieDetailIntent.putExtra(ID_MOVIE_THUMBNAIL_URL, currentMovie.getmThumbnailUrl());
        startActivity(movieDetailIntent);
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
