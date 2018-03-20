package com.gil.finalproject;


import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.io.UnsupportedEncodingException;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * A simple {@link Fragment} subclass.
 */
public class DetailsFragment extends Fragment {

    private RecyclerView myRV;
    private EndPointClient service;
    String key ="AIzaSyBQKf7VwlWtSsDHKdyFfVI5AvGSZS1dlW8";
    MyAdpter adapter;
    Retrofit retrofit ;
    String BASE_URL = "https://maps.googleapis.com";
    String decodedQuery;


    public DetailsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_details, container, false);
            myRV = (RecyclerView) view.findViewById(R.id.myRV);

             return view;
    }


    public void searchText(String query) {

        try {
            decodedQuery = java.net.URLDecoder.decode(String.valueOf(query), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        retrofit = new Retrofit.Builder().baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        service = retrofit.create(EndPointClient.class);

         Call<Myresults> myCall = service.getAllResults(decodedQuery , key);
        myCall.enqueue(new Callback<Myresults>() {
            @Override
            public void onResponse(Call<Myresults> call, Response<Myresults> response) {
                Myresults resultWithInnerObjects=  response.body();
                List<MyModels> alLPlacesResult = resultWithInnerObjects.results;

                myRV.setLayoutManager(new GridLayoutManager(getActivity() , 1));
                adapter = new MyAdpter( alLPlacesResult , getActivity());
                myRV.setHasFixedSize(true);
                myRV.setAdapter(adapter);
               adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<Myresults> call, Throwable t) {
                Log.d("msg" , "t");
            }

        });

    }



}
