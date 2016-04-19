package com.abhinandankothari.and_p1s2.network;

import android.util.Log;

import com.abhinandankothari.and_p1s2.Config;
import com.abhinandankothari.and_p1s2.contract.Movie;
import com.abhinandankothari.and_p1s2.contract.Review;
import com.abhinandankothari.and_p1s2.contract.Trailer;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.GsonConverterFactory;
import retrofit2.Retrofit;

public class Api {

    public static final String API_URL = "http://api.themoviedb.org/3/";
    public String sortCriteria;
    Retrofit retrofit;
    MoviesApi m;

    public Api() {
        retrofit = new Retrofit.Builder()
                .baseUrl(API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        m = retrofit.create(MoviesApi.class);
    }

    public List<Movie> ListofMovies(String sort) {
        Call<MoviesResponse> call;
        if (sort.equals("Popularity")) {
            call = m.listOfPopularMovies(Config.API_KEY);
        } else
            call = m.listOfTopRatedMovies(Config.API_KEY);

        try {
            MoviesResponse moviesResponse = call.execute().body();
            if (moviesResponse.movies != null) return moviesResponse.movies;
            else throw new IOException("API Key invalid or Internet Connection Turned Off");
        } catch (IOException ex) {
            Log.e("ERROR", "Unable to get Data");
            return null;
        }
    }

    public List<Trailer> ListofTrailers(int id) {
        Call<TrailersResponse> call = m.getVideos(id, Config.API_KEY);
        try {
            TrailersResponse trailerResponse = call.execute().body();
            return trailerResponse.videos;
        } catch (IOException ex) {
            Log.e("ERROR", "Unable to get Data");
            return null;
        }
    }

    public List<Review> ListofReivews(int id) {
        Call<ReviewsResponse> call = m.getReviews(id, Config.API_KEY);
        try {
            ReviewsResponse reviewsResponse = call.execute().body();
            return reviewsResponse.reviews;
        } catch (IOException ex) {
            Log.e("ERROR", "Unable to get Data");
            return null;
        }
    }
}
