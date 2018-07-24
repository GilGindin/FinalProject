package com.gil.finalproject;

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

import java.util.List;

/**
 * Created by gil on 11/03/2018.
 */

public class LastSearchAdpter extends RecyclerView.Adapter<LastSearchAdpter.MyLastSearchHolder> {
    Context context;
    private List<Book> MyLastResults;
    public float [] nearDistanceResults = new float[10];
    public String nPreference = "kilometre";
    public double roundedNDis;

    public LastSearchAdpter(Context context, List<Book> myLastResults) {
        this.context = context;
        MyLastResults = myLastResults;
    }

    @Override
    public MyLastSearchHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View myV = LayoutInflater.from(context).inflate(R.layout.single_item ,parent , false);
        MyLastSearchHolder singleItem = new MyLastSearchHolder(myV);
        return singleItem;
    }

    @Override
    public void onBindViewHolder(MyLastSearchHolder holder, int position) {

        Book currentBook = MyLastResults.get(position);
        holder.onBindData(currentBook);

    }


    @Override
    public int getItemCount() {
       if(MyLastResults != null) {
            return MyLastResults.size();
        }
        return 0;
    }
    public class MyLastSearchHolder extends RecyclerView.ViewHolder {

        TextView tv;
        TextView tv1;
        TextView tv2;
        View myView;


        public MyLastSearchHolder(View itemView) {
            super(itemView);
            myView = itemView;
        }
        public void onBindData(final Book curent){

            double currentLat = curent.lat;
            double currentLng = curent.lng;

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
            tv.setText(curent.name);
            tv1 = (TextView) itemView.findViewById(R.id.adressTV);
            tv1.setText(curent.adress);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    double lat = curent.lat ;
                    double lng = curent.lng;
                    myMapChnger changer = (myMapChnger) context;
                    changer.changeFragment(lat , lng);
                }
            });

        }
    }
}
