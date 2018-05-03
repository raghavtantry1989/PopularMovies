package com.southkart.popularmovies.data;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.southkart.popularmovies.R;
import com.southkart.popularmovies.utilities.NetworkUtils;

import java.net.URL;
import java.util.ArrayList;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieAdapterViewHolder> {

    private static final String LOG_TAG = MovieAdapter.class.getSimpleName();

    private ArrayList<Movie> mMoviesList;
    private Context mContext;

    final private MovieAdapterOnClickHandler mClickHandler;

    /**
     * The interface that receives onClick messages.
     */
    public interface MovieAdapterOnClickHandler {
        void onClick(Movie movie);
    }

    public MovieAdapter(@NonNull Context context, MovieAdapterOnClickHandler clickHandler) {
        mContext = context;
        mClickHandler = clickHandler;
    }

    @NonNull
    @Override
    public MovieAdapter.MovieAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        int layoutIdForListItem = R.layout.movie_list_item;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately);
        return new MovieAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieAdapter.MovieAdapterViewHolder holder, int position) {
        Movie currentMovie = mMoviesList.get(position);

        String thumbnailUrlPath = currentMovie.getmThumbnailUrl();
        URL thumbnailUrl = NetworkUtils.buildImageURL(thumbnailUrlPath);
        Log.d(LOG_TAG, thumbnailUrl.toString());

        Glide.with(mContext).load(thumbnailUrl.toString())
                .into(holder.mMovieImageView);
    }

    public class MovieAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView mMovieImageView;

        public MovieAdapterViewHolder(View itemView) {
            super(itemView);
            mMovieImageView = (ImageView) itemView.findViewById(R.id.iv_movie_thumbnail);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            Movie currentMovie = mMoviesList.get(adapterPosition);
            mClickHandler.onClick(currentMovie);
        }
    }

    @Override
    public int getItemCount() {
        if (null == mMoviesList) return 0;
        Log.d(LOG_TAG, Integer.toString(mMoviesList.size()));
        return mMoviesList.size();
    }

    public void setMovieData(ArrayList<Movie> movieList) {
        mMoviesList = movieList;
        notifyDataSetChanged();
    }
}
