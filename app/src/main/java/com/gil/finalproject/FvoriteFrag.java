package com.gil.finalproject;


import android.os.Bundle;
import android.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class FvoriteFrag extends Fragment {

 ListView faveLV;
 ArrayList<MyModels> myFaveAL;
    CustomArrayAdpter ArrayAdapter;


    public FvoriteFrag() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
View v = inflater.inflate(R.layout.fragment_fvorite, container, false);
            Bundle recivreBundle = this.getArguments();
        if (recivreBundle != null) {
            String name = getArguments().getString("name" );
            String adress = getArguments().getString("adress");


        faveLV = (ListView) v.findViewById(R.id.faveLV);
        myFaveAL = new ArrayList<>();
        myFaveAL.add(new MyModels( name,adress , null));

        ArrayAdapter = new CustomArrayAdpter((FragmentActivity) getActivity(), R.layout.favorite_layout , faveLV);
        faveLV.setAdapter(ArrayAdapter);
    }









       /*

        //get the list from the database class <Book>
        newBooks = Book.listAll(Book.class);
        List<Book> allBooks = new ArrayList<>();
        Bundle bundle = new Bundle((ClassLoader) Book.listAll(Book.class));
        bundle.getStringArrayList(name);
        bundle.getStringArrayList(adress);
        allBooks.addAll(newBooks);
        //set the recycler layout manager
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        favoriteRV.setLayoutManager(mLayoutManager);
        //set recycler adpter
        LastSearchAdpter adpter = new LastSearchAdpter(getActivity() , newBooks);
        favoriteRV.setAdapter(adpter);
*/
        return v;
    }

}
