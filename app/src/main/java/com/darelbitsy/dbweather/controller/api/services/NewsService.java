package com.darelbitsy.dbweather.controller.api.services;

import com.darelbitsy.dbweather.model.news.NewsResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Darel Bitsy on 18/02/17.
 */

public interface NewsService {
    @GET("/v1/articles")
    Call<NewsResponse> getNewsFromApiSync(
            @Query("source") String source,
            @Query("sortBy") String sortBy,
            @Query("apiKey") String apiKey
    );
}