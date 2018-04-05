package com.gil.finalproject.Location;

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
 * Created by gil on 26/03/2018.
 */

public class LocationAdpter extends RecyclerView.Adapter<LocationAdpter.MyHolder> {

    String urlPhoto = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference=";
    String key ="&key=AIzaSyBQKf7VwlWtSsDHKdyFfVI5AvGSZS1dlW8";
    Context context;
    List<MyLocationModels> allLocation;
    public List<Book> lastBook = null;
    public float [] nearDistanceResults = new float[10];
    public String nPreference = "kilometre";
    public double roundedNDis;

    public LocationAdpter(Context context, List<MyLocationModels> allLocation) {
        this.context = context;
        this.allLocation = allLocation;
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_item, parent , false);
        LocationAdpter.MyHolder mySingle = new LocationAdpter.MyHolder(v);
        return mySingle;

    }

    @Override
    public void onBindViewHolder(MyHolder holder, int position) {

        MyLocationModels current = allLocation.get(position);
        holder.bindData(current);
    }



    @Override
    public int getItemCount() {
        return allLocation.size();
    }
    public class MyHolder extends RecyclerView.ViewHolder{

        TextView tv;
        TextView tv1;
        TextView tv2;
        ImageView modelsIV;
        View myView;

        public MyHolder(View itemView) {
            super(itemView);
            myView = itemView;
        }
        public void bindData(final MyLocationModels currentModel) {

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
            tv1.setText(currentModel.vicinity);
            modelsIV = (ImageView) itemView.findViewById(R.id.modelIV);
            if (currentModel.photos != null) {
                String photoString = urlPhoto + currentModel.photos.get(0).photo_reference + key;
                Picasso.with(context).load(photoString).into(modelsIV);

                //delete all the data
                Book.deleteAll(Book.class);
//running on the list values of the objects
                for (int position = 0; position < allLocation.size(); position++) {
                    String nameDB = allLocation.get(position).name;
                    String adressDB = allLocation.get(position).vicinity;
                    double latDB = allLocation.get(position).geometry.location.lat;
                    double lngDB = allLocation.get(position).geometry.location.lng;
//creasting new list and all the objects into it
                    List<Book> books = new ArrayList<>();
                    books.add(new Book(nameDB , adressDB , latDB , lngDB));
                    Book.saveInTx(books);

                    lastBook = Book.listAll(Book.class);

                    ArrayList<Book> lastBook = new ArrayList<>();
                    lastBook.addAll(lastBook);
            }
            myView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    double lat = currentModel.geometry.location.lat ;
                    double lng = currentModel.geometry.location.lng;
                    myMapChnger changer = (myMapChnger) context;
                    changer.changeFragment(lat , lng);
                }

            });
                myView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        myMapChnger changer = (myMapChnger) context;
                        changer.secondChangeFragment(currentModel.name , currentModel.vicinity);
                        return true;
                    }
                });
        }
    }}
}
