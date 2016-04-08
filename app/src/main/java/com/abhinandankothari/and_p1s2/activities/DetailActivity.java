package com.abhinandankothari.and_p1s2.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.abhinandankothari.and_p1s2.R;
import com.abhinandankothari.and_p1s2.adapters.TrailerAdapter;
import com.abhinandankothari.and_p1s2.contract.Movie;
import com.abhinandankothari.and_p1s2.contract.Trailer;
import com.abhinandankothari.and_p1s2.network.Api;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ButterKnife.bind(this);
        Bundle bundle = getIntent().getExtras();
        Movie movie = bundle.getParcelable(Movie.TAG);
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
        Movie movie = bundle.getParcelable(Movie.TAG);
        FetchTrailersTask task = new FetchTrailersTask();
        task.execute(movie.getId());
    }

    private List<Trailer> fetchTrailersList(int id) {
        Api api = new Api();
        List<Trailer> allVideos = new ArrayList<Trailer>();
        for (Trailer trailer : api.ListofTrailers(id)) {
            allVideos.add(trailer);
        }
        return allVideos;
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
}
