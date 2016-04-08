package com.abhinandankothari.and_p1s2.network;

import com.abhinandankothari.and_p1s2.contract.Trailer;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TrailersResponse {
    @SerializedName("id")
    public int movieId;
    @SerializedName("results")
    public List<Trailer> videos;
}
