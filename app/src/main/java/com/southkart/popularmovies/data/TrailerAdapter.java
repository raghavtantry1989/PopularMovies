package com.southkart.popularmovies.data;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.southkart.popularmovies.R;
import com.southkart.popularmovies.utilities.NetworkUtils;

import java.net.URL;
import java.util.ArrayList;

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.TrailerAdapterViewHolder> {

    private static final String LOG_TAG = TrailerAdapter.class.getSimpleName();

    private ArrayList<String> videoKeys;
    private Context mContext;

    final private TrailerAdapter.TrailerAdapterOnClickHandler mClickHandler;

    public interface TrailerAdapterOnClickHandler {
        void onClick(String videoKey);
    }

    public TrailerAdapter(@NonNull Context context, TrailerAdapter.TrailerAdapterOnClickHandler clickHandler) {
        mContext = context;
        mClickHandler = clickHandler;
    }

    @NonNull
    @Override
    public TrailerAdapter.TrailerAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        int layoutIdForListItem = R.layout.trailer_list_item;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately);
        return new TrailerAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TrailerAdapter.TrailerAdapterViewHolder holder, int position) {
        String trailerCount =  mContext.getString(R.string.trailer_word) + " " +position;
        holder.mTrailerIdTextView.setText(trailerCount);
    }

    @Override
    public int getItemCount() {
        if (null == videoKeys) return 0;
        return videoKeys.size();
    }

    public class TrailerAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView mTrailerIdTextView;

        public TrailerAdapterViewHolder(View itemView) {
            super(itemView);
            mTrailerIdTextView = (TextView) itemView.findViewById(R.id.tv_trailer_id);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            String trailerKey = videoKeys.get(adapterPosition);
            mClickHandler.onClick(trailerKey);
        }
    }

    public void setVideoKeys(ArrayList<String> videoKeys) {
        this.videoKeys = videoKeys;
        notifyDataSetChanged();
    }
}
