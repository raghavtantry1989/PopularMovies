package com.southkart.popularmovies.utilities;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.preference.PreferenceManager;

import com.southkart.popularmovies.R;

public class PreferenceUtils {

    public static String getPreferredSortOrder(Context context) {
        // Return the user's preferred sort order
        SharedPreferences prefs = PreferenceManager
                .getDefaultSharedPreferences(context);
        String keyForSortOrder = context.getString(R.string.pref_sort_order_key);
        String defaultSortOrder = context.getString(R.string.pref_sort_order_value_most_popular);
        return prefs.getString(keyForSortOrder, defaultSortOrder);
    }
}
