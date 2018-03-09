package com.gil.finalproject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by gil on 11/02/2018.
 */

public class MyAdpter extends RecyclerView.Adapter<MyAdpter.MyHolder> {


    private List<MyModels> allResults;
    Context context;
    String urlPhoto = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference=";
    String key ="&key=AIzaSyCYU6DaLO1HG8wXJcbPBu84qVz1G4bge5M";

    public MyAdpter(List<MyModels> allResults, Activity context) {
        this.allResults = allResults;
        this.context = context;
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(context).inflate(R.layout.single_item, null);
        MyHolder mySingle = new MyHolder(v);
        return mySingle;
    }

    @Override
    public void onBindViewHolder(final MyHolder holder, final int position) {

        MyModels current = allResults.get(position);
        holder.bindData(current);
    }

    @Override
    public int getItemCount() {
        return allResults.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {

        TextView tv;
        TextView tv1;
        //TextView tv2;
        ImageView modelsIV;
        View myView;

        public MyHolder(View itemView) {
            super(itemView);
            myView = itemView;

        }
        public void bindData(final MyModels currentModel) {

            tv = (TextView) itemView.findViewById(R.id.nameTV);
            tv.setText(currentModel.name);
            tv1 = (TextView) itemView.findViewById(R.id.adressTV);
            tv1.setText(currentModel.formatted_address);
            modelsIV = (ImageView) itemView.findViewById(R.id.modelIV);
            if(currentModel.photos != null) {
                String photoString = urlPhoto + currentModel.photos.get(0).photo_reference + key;
                Picasso.with(context).load(photoString).into(modelsIV);
            }

            myView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    double lat = currentModel.geometry.location.lat ;
                    double lng = currentModel.geometry.location.lng;
                    myMapChnger changer = (myMapChnger) context;
                    changer.changeFragment(lat , lng);
                    Book book3 = new Book (currentModel.name , currentModel.formatted_address , currentModel.geometry , currentModel.photos);
                    book3.save();



                }

            });
            myView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {

                   // Intent intent = new Intent(context , Favorits_Activity.class);
                  //  intent.putExtra("name" , currentModel.name);
                    //intent.putExtra("adress" , currentModel.formatted_address);
                    //context.startActivity(intent);

                    return true;
                }
            });

        }
    }
}
