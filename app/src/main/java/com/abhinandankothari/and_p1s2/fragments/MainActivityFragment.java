package com.abhinandankothari.and_p1s2.fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.abhinandankothari.and_p1s2.R;
import com.abhinandankothari.and_p1s2.activities.DetailActivity;
import com.abhinandankothari.and_p1s2.activities.MainActivity;
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

public class MainActivityFragment extends Fragment {
    private GridLayoutManager gridLayoutManager;
    private RecyclerViewAdapter recyclerViewAdapter;

    @Bind(R.id.recycler_view)
    RecyclerView moviesView;
    FetchMoviesTask task;

    public MainActivityFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        ButterKnife.bind(this, rootView);
        gridLayoutManager = new GridLayoutManager(getActivity(), 2);
        moviesView.setHasFixedSize(true);
        moviesView.setLayoutManager(gridLayoutManager);
        addTouchListenerToMoviesList();
        return rootView;
    }

    private void addTouchListenerToMoviesList() {
        moviesView.addOnItemTouchListener(new MoviesViewTouchListener(getActivity(), new OnMoviesTouchListener() {
            @Override
            public void onItemClick(View childView, int position) {
                if (MainActivity.mTwoPane) {
                    PopulateDetailFragment(position);
                } else {
                    Intent movieIntent = new Intent(getActivity(), DetailActivity.class);
                    movieIntent.putExtra(Movie.TAG, recyclerViewAdapter.get(position));
                    startActivity(movieIntent);
                }
            }
        }));
    }

    private void PopulateDetailFragment(int position) {
        Bundle arguments = new Bundle();
        arguments.putParcelable(Movie.TAG, recyclerViewAdapter.get(position));
        DetailActivityFragment fragment = new DetailActivityFragment();
        fragment.setArguments(arguments);
        getFragmentManager().beginTransaction()
                .replace(R.id.movie_detail_container, fragment, MainActivity.MOVIEDETAILFRAGMENT_TAG)
                .commit();
    }

    @Override
    public void onResume() {
        super.onResume();
        task = new FetchMoviesTask();
        ConnectionDetector cd = new ConnectionDetector(getActivity());
        Boolean isInternetPresent = cd.isConnectingToInternet();
        if (isInternetPresent) {
            task.execute();
            if (MainActivity.mTwoPane) {
                DetailActivityFragment fragment = (DetailActivityFragment) getFragmentManager().findFragmentByTag(MainActivity.MOVIEDETAILFRAGMENT_TAG);
                if (fragment != null) {
                    getFragmentManager()
                            .beginTransaction()
                            .remove(fragment)
                            .commit();
                }
            }
        } else {
            Toast.makeText(getActivity(), "You are offline, Check your Internet Connection or Switch to Favourite Movies", Toast.LENGTH_LONG).show();
        }
    }

    public class FetchMoviesTask extends AsyncTask<Void, Void, List<Movie>> {
        @Override
        protected void onPostExecute(List<Movie> movieList) {
            super.onPostExecute(movieList);
            recyclerViewAdapter = new RecyclerViewAdapter(getActivity(), movieList);
            moviesView.setAdapter(recyclerViewAdapter);
        }

        @Override
        protected List<Movie> doInBackground(Void... params) {
            return fetchMoviesList();
        }
    }

    private List<Movie> fetchMoviesList() {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());
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
            Cursor cursor = getActivity().getContentResolver().query(Movie.CONTENT_URI, null, null, null, null);
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
}
