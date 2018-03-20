package com.gil.finalproject;

import android.content.Context;

import com.orm.SugarRecord;
import com.orm.dsl.Table;

import java.util.List;

/**
 * Created by gil on 08/03/2018.
 */

public class Book extends SugarRecord {


    String name;
    String adress;
    double lat;
    double lng;



    public Book() {
    }

    public Book( String name, String adress, double lat, double lng) {
        this.name = name;
        this.adress = adress;
        this.lat = lat;
        this.lng = lng;
    }




}
