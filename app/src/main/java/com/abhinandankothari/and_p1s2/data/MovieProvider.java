package com.abhinandankothari.and_p1s2.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;

import com.abhinandankothari.and_p1s2.contract.Movie;

public class MovieProvider extends ContentProvider {
    public static final UriMatcher uriMatcher = buildUriMatcher();
    private MovieDBHelper movieDBHelper;
    static final int MOVIE = 100;
    static final int MOVIE_DETAILS = 101;
    public static final String moviesListSelection = Movie.TABLE_NAME;
    public static final String movieDetailsSelection = Movie.TABLE_NAME + "." + Movie.COLUMN_MOVIE_ID + " = ? ";


    private static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = Movie.CONTENT_AUTHORITY;
        matcher.addURI(authority, Movie.PATH_MOVIE, MOVIE);
        matcher.addURI(authority, Movie.PATH_MOVIE + "/#", MOVIE);
        return matcher;
    }

    @Override
    public boolean onCreate() {
        movieDBHelper = new MovieDBHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Cursor cursor;
        final int match = uriMatcher.match(uri);
        switch (match) {
            case MOVIE:
                cursor = movieDBHelper.getReadableDatabase().query(
                        Movie.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            case MOVIE_DETAILS:
                cursor = movieDBHelper.getReadableDatabase().query(
                        Movie.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            default:
                throw new UnsupportedOperationException("Unknown Uri:" + uri);

        }
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Override
    public String getType(Uri uri) {
        final int match = uriMatcher.match(uri);
        switch (match) {
            case MOVIE:
                return Movie.CONTENT_TYPE;
            case MOVIE_DETAILS:
                return Movie.CONTENT_ITEM_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown Uri:" + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final SQLiteDatabase db = movieDBHelper.getWritableDatabase();
        Uri returnUri;
        switch (uriMatcher.match(uri)) {
            case MOVIE_DETAILS:
                long _id = db.insert(Movie.TABLE_NAME, null, values);
                if (_id > 0) {
                    returnUri = Movie.buildMovieUri(_id);
                } else throw new android.database.SQLException("Unable to Inserter Database Row");
                break;
            default:
                throw new UnsupportedOperationException("Unknown Uri:" + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = movieDBHelper.getWritableDatabase();
        int deletedRows;
        if (null == selection) selection = "1";
        switch (uriMatcher.match(uri)) {
            case MOVIE_DETAILS:
                deletedRows = db.delete(Movie.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown Uri:" + uri);
        }
        if (deletedRows != 0) getContext().getContentResolver().notifyChange(uri, null);
        return deletedRows;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }
}
