package com.abhinandankothari.and_p1s2;

import android.app.Application;

import com.squareup.picasso.LruCache;
import com.squareup.picasso.Picasso;

public class PopularMoviesApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Picasso picasso = new Picasso.Builder(this)
                .indicatorsEnabled(BuildConfig.DEBUG)
                .loggingEnabled(BuildConfig.DEBUG)
                .memoryCache(new LruCache(1024 * 1024 * 30)) // 30 MB
                .build();
        Picasso.setSingletonInstance(picasso);
    }
}
