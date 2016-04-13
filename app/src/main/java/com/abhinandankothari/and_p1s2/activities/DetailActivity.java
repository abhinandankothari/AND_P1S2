package com.abhinandankothari.and_p1s2.activities;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.abhinandankothari.and_p1s2.R;
import com.abhinandankothari.and_p1s2.adapters.ReviewAdapter;
import com.abhinandankothari.and_p1s2.adapters.TrailerAdapter;
import com.abhinandankothari.and_p1s2.contract.Movie;
import com.abhinandankothari.and_p1s2.contract.Review;
import com.abhinandankothari.and_p1s2.contract.Trailer;
import com.abhinandankothari.and_p1s2.data.MovieDBHelper;
import com.abhinandankothari.and_p1s2.network.Api;
import com.abhinandankothari.and_p1s2.utility.ConnectionDetector;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

import static com.abhinandankothari.and_p1s2.contract.Movie.COLUMN_MOVIE_ID;
import static com.abhinandankothari.and_p1s2.contract.Movie.TABLE_NAME;
import static com.abhinandankothari.and_p1s2.contract.Movie.TAG;

public class DetailActivity extends AppCompatActivity {
    @Bind(R.id.movie_title)
    TextView movieTitle;
    @Bind(R.id.synopsis)
    TextView movieSynopsis;
    @Bind(R.id.rating)
    TextView movieRating;
    @Bind(R.id.release_date)
    TextView movieReleasDate;
    @Bind(R.id.img_thumb)
    ImageView movieThumb;
    @Bind(R.id.trailer_list)
    ListView trailerListView;
    @Bind(R.id.review_list)
    ListView reviewListView;
    @Bind(R.id.favourite)
    ToggleButton favouriteButton;

    Movie movie;

    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ButterKnife.bind(this);
        Bundle bundle = getIntent().getExtras();
        movie = bundle.getParcelable(TAG);
        movieTitle.setText(movie.getMovieTitle());
        movieRating.setText(movie.getMovieRating() + "/10");

        try {
            movieReleasDate.setText(movie.getYearFromReleaseDate());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        movieSynopsis.setText(movie.getMovieSynopsis());
        Picasso.with(this)
                .load(movie.getMoviePosterThumbUrl())
                .placeholder(R.drawable.poster)
                .into(movieThumb);

        addTouchListerToTrailersList();
        addTouchListerToFavouriteButton();
    }

    private boolean checkMovieInDatabase() {
        db = new MovieDBHelper(this).getWritableDatabase();
        return db.rawQuery("SELECT * FROM " + Movie.TABLE_NAME + " WHERE " + Movie.COLUMN_MOVIE_ID + "=?", new String[]{movie.getId() + ""}).getCount() > 0 ? true : false;
    }

    private void addTouchListerToFavouriteButton() {
        db = new MovieDBHelper(this).getWritableDatabase();
        favouriteButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    ContentValues movieValues = MovieDBHelper.createMovieValues(movie);
                    db.insert(TABLE_NAME, null, movieValues);
                } else {
                    if (db.delete(TABLE_NAME, COLUMN_MOVIE_ID + "=" + movie.getId(), null) > 0) {
                        showToast("Movie Un-favorited Successfully");
                    }
                }
            }
        });
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    private void addTouchListerToTrailersList() {
        trailerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Trailer itemValue = (Trailer) trailerListView.getItemAtPosition(position);
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(itemValue.getYoutubeVideoUrl())));
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        Bundle bundle = getIntent().getExtras();
        movie = bundle.getParcelable(TAG);
        if (checkMovieInDatabase()) favouriteButton.setChecked(true);
        FetchTrailersTask trailersTask = new FetchTrailersTask();
        FetchReviewsTask reviewsTask = new FetchReviewsTask();
        ConnectionDetector cd = new ConnectionDetector(getApplicationContext());
        Boolean isInternetPresent = cd.isConnectingToInternet();
        if (isInternetPresent) {
            trailersTask.execute(movie.getId());
            reviewsTask.execute(movie.getId());
        }
    }

    private List<Trailer> fetchTrailersList(int id) {
        Api api = new Api();
        List<Trailer> allVideos = new ArrayList<Trailer>();
        for (Trailer trailer : api.ListofTrailers(id)) {
            allVideos.add(trailer);
        }
        return allVideos;
    }

    private List<Review> fetchReviewsList(int id) {
        Api api = new Api();
        List<Review> allReviws = new ArrayList<Review>();
        int count = 0;
        for (Review review : api.ListofReivews(id)) {
            if (count < 5) {
                allReviws.add(review);
                count++;
            } else
                break;
        }
        return allReviws;
    }

    public class FetchTrailersTask extends AsyncTask<Integer, Void, List<Trailer>> {
        @Override
        protected void onPostExecute(List<Trailer> trailerList) {
            super.onPostExecute(trailerList);
            TrailerAdapter adapter = new TrailerAdapter(getApplicationContext(), trailerList);
            trailerListView.setAdapter(adapter);
        }

        @Override
        protected List<Trailer> doInBackground(Integer... params) {
            return fetchTrailersList(params[0]);
        }
    }

    public class FetchReviewsTask extends AsyncTask<Integer, Void, List<Review>> {
        @Override
        protected void onPostExecute(List<Review> reviewList) {
            super.onPostExecute(reviewList);
            ReviewAdapter adapter = new ReviewAdapter(getApplicationContext(), reviewList);
            reviewListView.setAdapter(adapter);
        }

        @Override
        protected List<Review> doInBackground(Integer... params) {
            return fetchReviewsList(params[0]);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        db.close();
    }
}
