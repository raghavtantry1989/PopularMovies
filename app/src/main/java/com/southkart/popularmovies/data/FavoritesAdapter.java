package com.southkart.popularmovies.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.southkart.popularmovies.R;
import com.southkart.popularmovies.utilities.NetworkUtils;

public class FavoritesAdapter extends RecyclerView.Adapter<FavoritesAdapter.FavoritesAdapterViewHolder> {

    private Cursor mCursor;
    private Context mContext;

    private static final int KEY_1 = 1;
    private static final int KEY_2 = 2;

    final private FavoritesAdapterOnClickHandler mClickHandler;

    /**
     * The interface that receives onClick messages.
     */
    public interface FavoritesAdapterOnClickHandler {
        void onClick(Movie currentMovie);
    }

    public FavoritesAdapter(@NonNull Context context, FavoritesAdapterOnClickHandler clickHandler) {
        mContext = context;
        mClickHandler = clickHandler;
    }

    @NonNull
    @Override
    public FavoritesAdapter.FavoritesAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        int layoutIdForListItem = R.layout.movie_list_item;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately);
        return new FavoritesAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoritesAdapter.FavoritesAdapterViewHolder holder, int position) {
        // Indices for the title and thumbnail url
        int idIndex = mCursor.getColumnIndex(MovieContract.MovieEntry._ID);
        int thumbnailIndex = mCursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_THUMBNAIL_URL);

        mCursor.moveToPosition(position);

        // Determine the values of the wanted data
        int id = mCursor.getInt(idIndex);
        String thumbnail = mCursor.getString(thumbnailIndex);

        // Set Values
        String thumbnailUrlString = NetworkUtils.buildImageString(thumbnail);

//        holder.mMovieImageView.setTag(id);
//        holder.mMovieImageView.setTag(thumbnail);

        Glide.with(mContext).load(thumbnailUrlString)
                .into(holder.mMovieImageView);
    }

    @Override
    public int getItemCount() {
        if (null == mCursor) return 0;
        return mCursor.getCount();
    }

    public class FavoritesAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView mMovieImageView;

        public FavoritesAdapterViewHolder(View itemView) {
            super(itemView);
            mMovieImageView = (ImageView) itemView.findViewById(R.id.iv_movie_thumbnail);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            mCursor.moveToPosition(adapterPosition);

            // Indices for the id and api_id
            int idIndex = mCursor.getColumnIndex(MovieContract.MovieEntry._ID);
            int apiIdIndex = mCursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_API_ID);
            int thumbnailIndex = mCursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_THUMBNAIL_URL);

            // Determine the values of the wanted data
            int id = mCursor.getInt(idIndex);
            int apiId = mCursor.getInt(apiIdIndex);
            String thumbnail = mCursor.getString(thumbnailIndex);

            Movie movie = new Movie(Integer.toString(apiId),thumbnail);

            mClickHandler.onClick(movie);
        }
    }

    public void swapCursor(Cursor newCursor) {
        mCursor = newCursor;
        notifyDataSetChanged();
    }
}
