package com.gil.finalproject;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by gil on 22/03/2018.
 */

public class CustomArrayAdpter extends ArrayAdapter<String> {

    Context activity;
    int favorite_layout;
    List<String> faveLV;




    public CustomArrayAdpter(FragmentActivity activity, int favorite_layout, ListView faveLV) {
        super(activity , favorite_layout , (List<String>) faveLV);
        this.activity = activity;
        this.favorite_layout = favorite_layout;
        this.faveLV = (List<String>) faveLV;

    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View v = LayoutInflater.from(activity).inflate(favorite_layout , null);

        String currentString = faveLV.get(position);

        TextView nameFave = (TextView) v.findViewById(R.id.faveTV);
        TextView adressFve = (TextView) v.findViewById(R.id.faveTV2);

        LinearLayout myFaveLayout = (LinearLayout) v.findViewById(R.id.myFaveLayout);

        nameFave.setText(currentString);
        adressFve.setText(currentString);




        return v;
    }
}
