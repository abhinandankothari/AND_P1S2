package com.abhinandankothari.and_p1s2.network;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface MoviesApi {
    @GET("movie/popular")
    Call<MoviesResponse> listOfPopularMovies(@Query("api_key") String api_key);

    @GET("movie/top_rated")
    Call<MoviesResponse> listOfTopRatedMovies(@Query("api_key") String api_key);

    @GET("movie/{id}/videos")
    Call<TrailersResponse> getVideos(@Path("id") int movieId,
                                     @Query("api_key") String api_key);

    @GET("movie/{id}/reviews")
    Call<ReviewsResponse> getReviews(@Path("id") int movieId,
                                     @Query("api_key") String api_key);

}
