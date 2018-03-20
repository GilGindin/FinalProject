package com.gil.finalproject;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by gil on 11/02/2018.
 */

public interface EndPointClient {


    @GET("/maps/api/place/textsearch/json")
    Call<Myresults> getAllResults(@Query("query") String query, @Query("key") String key);

    @GET("https://maps.googleapis.com/maps/api/place/nearbysearch/json")
    Call<Myresults>getAllGeometry (@Query("location") String locationsData , @Query("query") String searchText ,@Query("radius") String radiusNum, @Query("key") String key);

}
