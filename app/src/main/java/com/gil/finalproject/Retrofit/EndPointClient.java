package com.gil.finalproject.Retrofit;

import com.gil.finalproject.Retrofit.Myresults;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by gil on 11/02/2018.
 */

public interface EndPointClient {


    @GET("/maps/api/place/textsearch/json")
    Call<Myresults> getAllResults(@Query("query") String query, @Query("key") String key);


}
