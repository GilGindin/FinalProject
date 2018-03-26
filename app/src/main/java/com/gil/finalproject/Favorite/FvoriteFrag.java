package com.gil.finalproject.Favorite;


import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gil.finalproject.R;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class FvoriteFrag extends Fragment {

     RecyclerView faveRV;
     List<Favorites> myFaveAL;
    CustomArrayAdpter ArrayAdapter;


    public FvoriteFrag() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
View v = inflater.inflate(R.layout.fragment_fvorite, container, false);
        faveRV = (RecyclerView) v.findViewById(R.id.favoriteRV) ;

            Bundle recivreBundle = this.getArguments();
        if (recivreBundle != null) {
            String name = recivreBundle.getString("name" );
            String adress = recivreBundle.getString("adress");

        myFaveAL = new ArrayList<Favorites>();
        myFaveAL.add(new Favorites( name,adress));

            faveRV.setLayoutManager(new GridLayoutManager(getActivity(), 1));
            ArrayAdapter = new CustomArrayAdpter(getActivity() , myFaveAL);
            faveRV.setHasFixedSize(true);
            faveRV.setAdapter(ArrayAdapter);
            ArrayAdapter.notifyDataSetChanged();

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
