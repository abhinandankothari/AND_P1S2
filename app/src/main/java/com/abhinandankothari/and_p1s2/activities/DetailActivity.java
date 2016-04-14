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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

}
