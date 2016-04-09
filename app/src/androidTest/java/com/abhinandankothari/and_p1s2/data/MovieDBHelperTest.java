package com.abhinandankothari.and_p1s2.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.abhinandankothari.and_p1s2.contract.Movie;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static junit.framework.TestCase.assertFalse;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class MovieDBHelperTest {

    Context context;

    @Before
    public void setUp() throws Exception {
        context = InstrumentationRegistry.getTargetContext();
        deleteTheDatabase();
    }

    private void deleteTheDatabase() {
        context.deleteDatabase(MovieDBHelper.DATABASE_NAME);

    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testCreateDb() throws Throwable {

        final HashSet<String> tableNameHashSet = new HashSet<String>();
        tableNameHashSet.add(Movie.TABLE_NAME);
        deleteTheDatabase();
        SQLiteDatabase db = new MovieDBHelper(
                this.context).getWritableDatabase();
        assertEquals(true, db.isOpen()
        );
        Cursor c = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);

        assertTrue("Error: This means that the database has not been created correctly",
                c.moveToFirst()

        );
        do {
            tableNameHashSet.remove(c.getString(0));
        } while (c.moveToNext());
        assertTrue("Error: Your database was created without movie table",
                tableNameHashSet.isEmpty()

        );
        c = db.rawQuery("PRAGMA table_info(" + Movie.TABLE_NAME + ")",
                null);
        assertTrue("Error: This means that we were unable to query the database for table information.",
                c.moveToFirst()

        );


        // Build a HashSet of all of the column names we want to look for
        final HashSet<String> movieColumnHashSet = new HashSet<String>();
        movieColumnHashSet.add(Movie._ID);
        movieColumnHashSet.add(Movie.COLUMN_MOVIE_ID);
        movieColumnHashSet.add(Movie.COLUMN_MOVIE_RATING);
        movieColumnHashSet.add(Movie.COLUMN_MOVIE_RELEASE_DATE);
        movieColumnHashSet.add(Movie.COLUMN_MOVIE_SYNOPSIS);
        movieColumnHashSet.add(Movie.COLUMN_MOVIE_TITLE);
        movieColumnHashSet.add(Movie.COLUMN_MOVIE_POSTER_PATH);

        int columnNameIndex = c.getColumnIndex("name");
        do {
            String columnName = c.getString(columnNameIndex);
            movieColumnHashSet.remove(columnName);
        }

        while (c.moveToNext());

        assertTrue("Error: The database doesn't contain all of the required movie entry columns",
                movieColumnHashSet.isEmpty()
        );
        db.close();
    }

    @Test
    public void testMovieTable() throws Exception {
        MovieDBHelper movieDBHelper = new MovieDBHelper(context);
        SQLiteDatabase db = movieDBHelper.getWritableDatabase();
        ContentValues testValues = createMovieValues();
        long movieRowId;
        movieRowId = db.insert(Movie.TABLE_NAME, null, testValues);
        assertNotEquals(movieRowId, -1);

        Cursor cursor = db.query(Movie.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null);
        assertEquals(cursor.getCount(), 1);
        assertTrue(cursor.moveToFirst());
        validateCurrentRecord("Error: Movie Query Validation Failed", cursor, createMovieValues());
        assertFalse(cursor.moveToNext());
        cursor.close();
        db.close();
    }

    static ContentValues createMovieValues() {
        ContentValues testValues = new ContentValues();
        testValues.put(Movie.COLUMN_MOVIE_ID, 12345);
        testValues.put(Movie.COLUMN_MOVIE_POSTER_PATH, "https://img.jpeg");
        testValues.put(Movie.COLUMN_MOVIE_RATING, "5.9");
        testValues.put(Movie.COLUMN_MOVIE_SYNOPSIS, "Awesome Movie");
        testValues.put(Movie.COLUMN_MOVIE_TITLE, "BestMovieEver");
        testValues.put(Movie.COLUMN_MOVIE_RELEASE_DATE, "2015-12-12");
        return testValues;
    }

    static void validateCurrentRecord(String error, Cursor valueCursor, ContentValues expectedValues) {
        Set<Map.Entry<String, Object>> valueSet = expectedValues.valueSet();
        for (Map.Entry<String, Object> entry : valueSet) {
            String columnName = entry.getKey();
            int idx = valueCursor.getColumnIndex(columnName);
            assertFalse("Column '" + columnName + "' not found. " + error, idx == -1);
            String expectedValue = entry.getValue().toString();
            assertEquals("Value '" + entry.getValue().toString() +
                    "' did not match the expected value '" +
                    expectedValue + "'. " + error, expectedValue, valueCursor.getString(idx));
        }
    }
}