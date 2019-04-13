package com.gil.finalproject.TextSearch;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gil.finalproject.Book;
import com.gil.finalproject.R;
import com.gil.finalproject.RetrofitInstance;

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
public class DetailsFragment extends Fragment {

    RecyclerView myRV;
    EndPointClient service;
    String key = "AIzaSyCAU7syPLk4OKPV7UlInRBfVkCXOH7Xrdw ";
    MyAdpter adapter;
    Retrofit retrofit;
    String BASE_URL = "https://maps.googleapis.com/";
    String decodedQuery;
    public List<Book> lastBook = null;
    ProgressDialog dialog;

    public DetailsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_details, container, false);
        myRV = (RecyclerView) view.findViewById(R.id.myRV);
        myRV.setLayoutManager(new GridLayoutManager(getActivity(), 1));
        myRV.setHasFixedSize(true);

        return view;
    }

    //method that been call from mainACtivity and recive the query from the user
    public void searchText(String query) {
//fixing the query by decodequery
        try {
            decodedQuery = java.net.URLDecoder.decode(String.valueOf(query), "UTF-8");
        } catch (
                UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        //connect to the retrofit class with interface class
        service = RetrofitInstance.getRetrofitInstance().create(EndPointClient.class);
        //call for the results from the url by using retrofit
        Call<Myresults> myCall = service.getAllResults(decodedQuery, key);
        myCall.enqueue(new Callback<Myresults>() {
            @Override
            public void onResponse(Call<Myresults> call, Response<Myresults> response) {
                if (!response.isSuccessful()){
                    Log.d("", "onResponse:---------- "+response.code());
                    return;
                }
                Myresults resultWithInnerObjects = response.body();
                List<MyModels> alLPlacesResult = resultWithInnerObjects.results;

                //delete all the data
                Book.deleteAll(Book.class);
//running on the list values of the objects
                for (int position = 0; position < alLPlacesResult.size(); position++) {
                    String nameDB = alLPlacesResult.get(position).name;
                    String adressDB = alLPlacesResult.get(position).formatted_address;
                    double latDB = alLPlacesResult.get(position).geometry.location.lat;
                    double lngDB = alLPlacesResult.get(position).geometry.location.lng;
//creasting new list and all the objects into it
                    List<Book> books = new ArrayList<>();
                    books.add(new Book(nameDB, adressDB, latDB, lngDB));
                    Book.saveInTx(books);

                    lastBook = Book.listAll(Book.class);

                    ArrayList<Book> lastBook = new ArrayList<>();
                    lastBook.addAll(lastBook);

//set the results from the list into the recycler adpter

                    adapter = new MyAdpter(alLPlacesResult, getActivity());
                    myRV.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<Myresults> call, Throwable t) {
                //     dialog.dismiss();
                Log.d("msg", "t");
            }

        });
    }
}
