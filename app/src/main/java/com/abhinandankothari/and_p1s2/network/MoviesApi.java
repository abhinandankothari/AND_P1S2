package com.abhinandankothari.and_p1s2.network;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface MoviesApi {
    @GET("discover/movie")
    Call<MoviesResponse> listOfMovies(@Query("api_key") String api_key,
                                      @Query("sort_by") String sortCriteria);

    @GET("movie/{id}/videos")
    Call<TrailersResponse> getVideos(@Path("id") int movieId,
                                     @Query("api_key") String api_key);

    @GET("movie/{id}/reviews")
    Call<ReviewsResponse> getReviews(@Path("id") int movieId,
                                      @Query("api_key") String api_key);

}
