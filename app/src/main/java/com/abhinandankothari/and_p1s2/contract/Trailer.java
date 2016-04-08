package com.abhinandankothari.and_p1s2.contract;

import com.google.gson.annotations.SerializedName;

public class Trailer {
    public static final String YOUTUBE_BASE_URL = "https://www.youtube.com/watch?v=";

    @SerializedName("id")
    public String id;
    @SerializedName("name")
    public String name;
    @SerializedName("key")
    public String key;
    @SerializedName("site")
    public String site;
    @SerializedName("size")
    public int size;
    @SerializedName("type")
    public String type;

    public Trailer(String id, String name, String key, String site, int size, String type) {
        this.id = id;
        this.name = name;
        this.key = key;
        this.site = site;
        this.size = size;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public String getKey() {
        return key;
    }

    public String getYoutubeVideoUrl() {
        return YOUTUBE_BASE_URL + this.getKey();
    }
}
