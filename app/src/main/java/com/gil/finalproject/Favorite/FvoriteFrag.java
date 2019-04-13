package com.gil.finalproject.Favorite;


import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gil.finalproject.Book;
import com.gil.finalproject.LastSearchAdpter;
import com.gil.finalproject.R;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class FvoriteFrag extends Fragment {

    RecyclerView faveRV;
    List<Favorites> myFaveAL;
    LastSearchAdpter ArrayAdapter;
    public List<Book> faveBook = null;


    public FvoriteFrag() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_fvorite, container, false);
        faveRV = (RecyclerView) v.findViewById(R.id.favoriteRV);

        if (faveBook != null) {

            faveBook = Book.listAll(Book.class);
            faveRV.setLayoutManager(new GridLayoutManager(getActivity(), 1));
            LastSearchAdpter lastSearchAdpter = new LastSearchAdpter(getActivity(), faveBook);
            faveRV.setHasFixedSize(true);
            faveRV.setAdapter(lastSearchAdpter);
            lastSearchAdpter.notifyDataSetChanged();
        }

        Bundle recivreBundle = this.getArguments();
        if (recivreBundle != null) {
            String name = recivreBundle.getString("name");
            String adress = recivreBundle.getString("adress");

            myFaveAL = new ArrayList<Favorites>();
            myFaveAL.add(new Favorites(name, adress));

            for (int position = 0; position < myFaveAL.size(); position++) {
                String nameDB = myFaveAL.get(position).name;
                String adressDB = myFaveAL.get(position).adress;
                //   double latDB = myFaveAL.get(position).geometry.location.lat;
                //   double lngDB = myFaveAL.get(position).geometry.location.lng;
//creasting new list and all the objects into it
                List<Book> saveBooks = new ArrayList<>();
                saveBooks.add(new Book(nameDB, adressDB, 0, 0));
                Book.saveInTx(saveBooks);

                faveBook = Book.listAll(Book.class);
                ArrayList<Book> newBokks = new ArrayList<>();
                newBokks.addAll(faveBook);


                faveRV.setLayoutManager(new GridLayoutManager(getActivity(), 1));
                ArrayAdapter = new LastSearchAdpter(getActivity(), faveBook);
                faveRV.setHasFixedSize(true);
                faveRV.setAdapter(ArrayAdapter);
                ArrayAdapter.notifyDataSetChanged();
            }
        }
        faveBook = Book.listAll(Book.class);
        faveRV.setLayoutManager(new GridLayoutManager(getActivity(), 1));
        LastSearchAdpter lastSearchAdpter = new LastSearchAdpter(getActivity(), faveBook);
        faveRV.setHasFixedSize(true);
        faveRV.setAdapter(lastSearchAdpter);
        lastSearchAdpter.notifyDataSetChanged();

        return v;
    }

}
