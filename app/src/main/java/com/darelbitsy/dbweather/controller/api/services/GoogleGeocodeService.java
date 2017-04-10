package com.darelbitsy.dbweather.controller.api.services;

import com.darelbitsy.dbweather.model.GoogleGeocoderApi.GoogleGeocodeResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Darel Bitsy on 20/02/17.
 * Google Geocode api Service
 */

public interface GoogleGeocodeService {
    @GET("/maps/api/geocode/json")
    Call<GoogleGeocodeResponse> getLocationNameWithLanguage (
            @Query("latlng")
            final String coordinates,
            @Query("language")
            final String language
    );

    @GET("maps/api/geocode/json")
    Call<GoogleGeocodeResponse> getLocationName (
            @Query("latlng")
            final String coordinates
    );
}
