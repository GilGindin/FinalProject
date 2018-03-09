package com.gil.finalproject;

import android.content.Context;

import com.orm.SugarRecord;
import com.orm.dsl.Table;

import java.util.List;

/**
 * Created by gil on 08/03/2018.
 */

public class Book extends SugarRecord {

    int ID;
    String name;
    String adress;
    MyLocation geometry;
    List<PhotoObj> photo;


    public Book() {
    }

    public Book( String name, String adress, MyLocation geometry, List<PhotoObj> photo) {
        this.name = name;
        this.adress = adress;
        this.geometry = geometry;
        this.photo = photo;
    }


}
