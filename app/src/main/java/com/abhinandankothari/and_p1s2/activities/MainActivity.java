package com.abhinandankothari.and_p1s2.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.abhinandankothari.and_p1s2.R;
import com.abhinandankothari.and_p1s2.adapters.RecyclerViewAdapter;
import com.abhinandankothari.and_p1s2.contract.Movie;
import com.abhinandankothari.and_p1s2.listeners.MoviesViewTouchListener;
import com.abhinandankothari.and_p1s2.listeners.OnMoviesTouchListener;
import com.abhinandankothari.and_p1s2.network.Api;
import com.abhinandankothari.and_p1s2.utility.ConnectionDetector;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {
    private GridLayoutManager gridLayoutManager;
    private RecyclerViewAdapter recyclerViewAdapter;

    @Bind(R.id.recycler_view)
    RecyclerView moviesView;
    @Bind(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        gridLayoutManager = new GridLayoutManager(this, 2);
        moviesView.setHasFixedSize(true);
        moviesView.setLayoutManager(gridLayoutManager);

        addTouchListenerToMoviesList();
    }

    private void addTouchListenerToMoviesList() {
        moviesView.addOnItemTouchListener(new MoviesViewTouchListener(this, new OnMoviesTouchListener() {
            @Override
            public void onItemClick(View childView, int position) {
                Intent movieIntent = new Intent(MainActivity.this, DetailActivity.class);
                movieIntent.putExtra(Movie.TAG, recyclerViewAdapter.get(position));
                startActivity(movieIntent);
            }
        }));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        FetchMoviesTask task = new FetchMoviesTask();
        ConnectionDetector cd = new ConnectionDetector(getApplicationContext());
        Boolean isInternetPresent = cd.isConnectingToInternet();
        if (isInternetPresent) {
            task.execute();
        } else {
            Toast.makeText(this, "You are offline, Check your Internet Connection or Switch to Favourite Movies", Toast.LENGTH_LONG);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent settings = new Intent(this, SettingsActivity.class);
            startActivity(settings);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private List<Movie> fetchMoviesList() {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        String sort = sharedPref.getString("sort", "Popularity");
        List<Movie> allItems = new ArrayList<Movie>();
        if (!sort.equalsIgnoreCase("My Favourites")) {
            Api movies = new Api();
            for (Movie movie : movies.ListofMovies(sort)) {
                allItems.add(new Movie(movie.getId(), movie.getMovieTitle(), movie.getMoviePosterThumbUrl(), movie.getMovieSynopsis(),
                        movie.getMovieRating(), movie.getMovieReleaseDate()));
            }
            return allItems;
        } else {
            //Content Provider
            Cursor cursor = getContentResolver().query(Movie.CONTENT_URI, null, null, null, null);
            int i;
            for (i = 0; i < cursor.getCount(); i++) {
                cursor.moveToPosition(i);
                allItems.add(i, new Movie(
                        cursor.getInt(cursor.getColumnIndexOrThrow(Movie.COLUMN_MOVIE_ID)),
                        cursor.getString(cursor.getColumnIndex(Movie.COLUMN_MOVIE_TITLE)),
                        cursor.getString(cursor.getColumnIndex(Movie.COLUMN_MOVIE_POSTER_PATH)),
                        cursor.getString(cursor.getColumnIndexOrThrow(Movie.COLUMN_MOVIE_SYNOPSIS)),
                        cursor.getString(cursor.getColumnIndexOrThrow(Movie.COLUMN_MOVIE_RATING)),
                        cursor.getString(cursor.getColumnIndexOrThrow(Movie.COLUMN_MOVIE_RELEASE_DATE))));
            }
            cursor.close();
            return allItems;
        }
    }

    public class FetchMoviesTask extends AsyncTask<Void, Void, List<Movie>> {
        @Override
        protected void onPostExecute(List<Movie> movieList) {
            super.onPostExecute(movieList);
            recyclerViewAdapter = new RecyclerViewAdapter(getApplicationContext(), movieList);
            moviesView.setAdapter(recyclerViewAdapter);
        }

        @Override
        protected List<Movie> doInBackground(Void... params) {
            return fetchMoviesList();
        }
    }
}
