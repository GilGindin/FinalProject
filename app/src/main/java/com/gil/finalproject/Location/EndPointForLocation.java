package com.gil.finalproject.Location;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by gil on 26/03/2018.
 */

public interface EndPointForLocation {

    @GET("/maps/api/place/nearbysearch/json")
    Call<MyLocationResults> getAllGeometry (@Query("location") String location , @Query("radius") String radius, @Query("keyword") String keyword, @Query("key") String key);

}
//https://maps.googleapis.com/maps/api/place/nearbysearch/json