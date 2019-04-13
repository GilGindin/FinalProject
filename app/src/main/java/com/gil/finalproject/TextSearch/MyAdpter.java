package com.gil.finalproject.TextSearch;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;
import android.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.gil.finalproject.Book;
import com.gil.finalproject.MainActivity;
import com.gil.finalproject.R;
import com.gil.finalproject.myMapChnger;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gil on 11/02/2018.
 */

public class MyAdpter extends RecyclerView.Adapter<MyAdpter.MyHolder> {

   public List<Book> lastBook = null;
     List<MyModels> allResults;
    Context context;
    String urlPhoto = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference=";
    String key ="&key=AIzaSyCAU7syPLk4OKPV7UlInRBfVkCXOH7Xrdw";
    public float [] nearDistanceResults = new float[10];
    public String nPreference = "kilometre";
    public double roundedNDis;

    //constractur for the adpter
    public MyAdpter(List<MyModels> allResults, Activity context) {
        this.allResults = allResults;
        this.context = context;
    }


    //inflate the ciew of the adpter
    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_item, parent , false);
        MyHolder mySingle = new MyHolder(v);
        return mySingle;
    }

    //binding the view with the list by binddata method
    @Override
    public void onBindViewHolder(final MyHolder holder, final int position) {

        MyModels current = allResults.get(position);
        holder.bindData(current);
    }
//get all item that are in the list
    @Override
    public int getItemCount() {

        return allResults.size();
    }
//the holder class
    public class MyHolder extends RecyclerView.ViewHolder {

        TextView tv;
        TextView tv1;
        TextView tv2;
        ImageView modelsIV;
        View myView;

        public MyHolder(View itemView) {
            super(itemView);
            myView = itemView;
//the method that bind the view with the objects
        }
        public void bindData(final MyModels currentModel) {

            double currentLat = currentModel.geometry.location.lat;
            double currentLng = currentModel.geometry.location.lng;

            Location.distanceBetween(MainActivity.newLat , MainActivity.newLng ,currentLat , currentLng , nearDistanceResults );
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
            nPreference = sharedPreferences.getString("list_preference_units" , "kilometre");
            tv2 =(TextView) itemView.findViewById(R.id.radiusTV);
            if(nPreference.equals("kilometre")){
                roundedNDis = (double)Math.round( (nearDistanceResults[0]/1000 ) * 100d) / 100d;
                tv2.setText(roundedNDis + " kilometre " );
            }else {

                roundedNDis = (double)Math.round( (((nearDistanceResults[0]*0.621371)/1000 ) ) * 100d) / 100d;
                tv2.setText(roundedNDis + " miles " );
            }

            tv = (TextView) itemView.findViewById(R.id.nameTV);
            tv.setText(currentModel.name);
            tv1 = (TextView) itemView.findViewById(R.id.adressTV);
            tv1.setText(currentModel.formatted_address);
            modelsIV = (ImageView) itemView.findViewById(R.id.modelIV);
            if(currentModel.photos != null) {
                String photoString = urlPhoto + currentModel.photos.get(0).photo_reference + key;
                Picasso.with(context).load(photoString).into(modelsIV);
            }
//by clicking on item in the list it takes his lat,lng and place it on the google map by the mapchanger class interface
            //mapchanger interfcae class is a class that switching fragments
            myView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    double lat = currentModel.geometry.location.lat ;
                    double lng = currentModel.geometry.location.lng;
                    myMapChnger changer = (myMapChnger) context;
                    changer.changeFragment(lat , lng);
//delete all the data
                    Book.deleteAll(Book.class);
//running on the list values of the objects
                    for (int position = 0; position < allResults.size(); position++) {
                        String nameDB = allResults.get(position).name;
                        String adressDB = allResults.get(position).formatted_address;
                        double latDB = allResults.get(position).geometry.location.lat;
                        double lngDB = allResults.get(position).geometry.location.lng;
//creasting new list and all the objects into it
                        List<Book> books = new ArrayList<>();
                        books.add(new Book(nameDB , adressDB , latDB , lngDB));
                        Book.saveInTx(books);

                        lastBook = Book.listAll(Book.class);

                        ArrayList<Book> lastBook = new ArrayList<>();
                        lastBook.addAll(lastBook);

                    }
//by clicking long click moving the value of the obejcts to favorite frsgment
                }

            });
            myView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {


                    myMapChnger changer = (myMapChnger) context;
                    changer.secondChangeFragment(currentModel.name , currentModel.formatted_address);
                    return true;
                }
            });

        }
    }
}
