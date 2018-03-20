package com.gil.finalproject;


import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * A simple {@link Fragment} subclass.
 */
public class LocationFrag extends Fragment {


    private RecyclerView locationRV;
    private EndPointClient service;
    Retrofit retrofit ;
    String BASE_URL = "https://maps.googleapis.com";
    String key ="AIzaSyBQKf7VwlWtSsDHKdyFfVI5AvGSZS1dlW8";
    String decodedQuery;
    MyAdpter adapter;
    ProgressDialog dialog;
    LocationManager locationManager;
    Location lastKnowLoc;

    public LocationFrag() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v =         inflater.inflate(R.layout.fragment_location, container, false);
        locationRV = (RecyclerView) v.findViewById(R.id.locationRV);

        return v;
    }

    public void searchText(String query){

        try {
            decodedQuery = java.net.URLDecoder.decode(String.valueOf(query), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        retrofit = new Retrofit.Builder().baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        service = retrofit.create(EndPointClient.class);

        String locationsData = null;
        if (ActivityCompat.checkSelfPermission(getActivity(),android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED){

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(
                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 15);
            }
        } else {

        lastKnowLoc = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            locationsData = "" + lastKnowLoc.getLatitude() + "," + lastKnowLoc.getLongitude();
    }

        Call<Myresults> newCall = service.getAllGeometry(locationsData , query , "500" , key);
        newCall.enqueue(new Callback<Myresults>() {
            @Override
            public void onResponse(Call<Myresults> call, Response<Myresults> response) {
                Myresults resultWithInnerObjects=  response.body();
                List<MyModels> locationResults = resultWithInnerObjects.results;
                locationRV.setLayoutManager(new GridLayoutManager(getActivity() , 1));
                adapter = new MyAdpter( locationResults , getActivity());
                locationRV.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<Myresults> call, Throwable t) {

            }
        });







    }




}
