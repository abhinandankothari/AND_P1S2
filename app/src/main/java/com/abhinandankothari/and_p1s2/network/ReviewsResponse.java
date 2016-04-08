package com.abhinandankothari.and_p1s2.network;

import com.abhinandankothari.and_p1s2.contract.Review;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ReviewsResponse {
    @SerializedName("id")
    public int movieId;
    @SerializedName("page")
    public int page;
    @SerializedName("results")
    public List<Review> reviews;
}
