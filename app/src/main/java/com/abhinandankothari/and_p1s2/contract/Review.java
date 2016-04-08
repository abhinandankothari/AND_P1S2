package com.abhinandankothari.and_p1s2.contract;

import com.google.gson.annotations.SerializedName;

public class Review {
    @SerializedName("id")
    public String id;
    @SerializedName("author")
    public String name;
    @SerializedName("content")
    public String content;
    @SerializedName("url")
    public String url;

    public Review(String id, String name, String content, String url) {
        this.id = id;
        this.name = name;
        this.content = content;
        this.url = url;
    }

    public String getAuthorName() {
        return name;
    }

    public String getContent() {
        return content;
    }
}
