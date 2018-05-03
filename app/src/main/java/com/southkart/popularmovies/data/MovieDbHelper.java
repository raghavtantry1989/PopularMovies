package com.southkart.popularmovies.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.southkart.popularmovies.data.MovieContract.MovieEntry;

public class MovieDbHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "movies.db";

    private static final int DATABASE_VERSION = 5;

    //  Constructor that accepts a context and call through to the superclass constructor
    public MovieDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String SQL_CREATE_MOVIE_TABLE =

                "CREATE TABLE " + MovieEntry.TABLE_NAME + " (" +

                        MovieEntry._ID                         + " INTEGER PRIMARY KEY AUTOINCREMENT, " +

                        MovieEntry.COLUMN_API_ID               + " INTEGER NOT NULL, "                  +

                        MovieEntry.COLUMN_THUMBNAIL_URL        + " TEXT NOT NULL, "                     +

                        " UNIQUE (" + MovieEntry.COLUMN_API_ID + ") ON CONFLICT REPLACE);";

        /*
         * Execute the SQL with the execSQL method of our SQLite database object.
         */
        sqLiteDatabase.execSQL(SQL_CREATE_MOVIE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MovieEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
