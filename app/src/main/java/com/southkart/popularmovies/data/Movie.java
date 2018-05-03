package com.southkart.popularmovies.data;

import java.io.Serializable;
import java.util.ArrayList;

public class Movie implements Serializable {
    private String mId;
    private String mTitle;
    private String mThumbnailUrl;
    private String mSynopsis;
    private double mUserRating;
    private String mReleaseData;
    private String mRuntime;
    private boolean is_favorite;
    private ArrayList<String> mVideoKeys;
    private ArrayList<String> mReviews;


    public Movie(String mId, String mThumbnailUrl) {
        this.mId = mId;
        this.mThumbnailUrl = mThumbnailUrl;
    }

    public Movie(String mTitle, String mThumbnailUrl, String mSynopsis, double mUserRating, String mReleaseData, String mRuntime) {
        this.mTitle = mTitle;
        this.mThumbnailUrl = mThumbnailUrl;
        this.mSynopsis = mSynopsis;
        this.mUserRating = mUserRating;
        this.mReleaseData = mReleaseData;
        this.mRuntime = mRuntime;
    }

    public String getmId() {
        return mId;
    }

    public String getmTitle() {
        return mTitle;
    }

    public String getmThumbnailUrl() {
        return mThumbnailUrl;
    }

    public String getmSynopsis() {
        return mSynopsis;
    }

    public String getmUserRating() {
        return Double.toString(mUserRating);
    }

    public String getmReleaseData() {
        return mReleaseData;
    }

    public String getmRuntime() {
        return mRuntime;
    }

    public ArrayList<String> getmVideoKeys() {
        return mVideoKeys;
    }

    public void setmVideoKeys(ArrayList<String> mVideoKeys) {
        this.mVideoKeys = mVideoKeys;
    }

    public ArrayList<String> getmReviews() {
        return mReviews;
    }

    public void setmReviews(ArrayList<String> mReviews) {
        this.mReviews = mReviews;
    }

    public boolean isIs_favorite() {
        return is_favorite;
    }

    public void setIs_favorite(boolean is_favorite) {
        this.is_favorite = is_favorite;
    }

}