package com.abhinandankothari.and_p1s2.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.abhinandankothari.and_p1s2.contract.Movie;

public class MovieDBHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "popularmovies.db";

    public MovieDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_MOVIE_TABLE = "CREATE TABLE " + Movie.TABLE_NAME + " (" +
                Movie._ID + " INTERGER PRIMARY KEY, " +
                Movie.COLUMN_MOVIE_ID + " INTEGER NOT NULL, " +
                Movie.COLUMN_MOVIE_RATING + " TEXT NOT NULL, " +
                Movie.COLUMN_MOVIE_RELEASE_DATE + " TEXT NOT NULL, " +
                Movie.COLUMN_MOVIE_SYNOPSIS + " TEXT NOT NULL, " +
                Movie.COLUMN_MOVIE_TITLE + " TEXT NOT NULL, " +
                Movie.COLUMN_MOVIE_POSTER_PATH + " TEXT NOT NULL " +
                " )";
        db.execSQL(SQL_CREATE_MOVIE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + Movie.TABLE_NAME);
        onCreate(db);
    }
}
