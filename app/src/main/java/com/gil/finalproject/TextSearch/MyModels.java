package com.gil.finalproject.TextSearch;


import java.util.List;

/**
 * Created by gil on 11/02/2018.
 */

public class MyModels {

       public   String name;
       public   String formatted_address;
       public MyLocation geometry;
       public List<PhotoObj> photos;

    public MyModels(String name, String formatted_address , MyLocation geometry ) {
        this.name = name;
        this.formatted_address = formatted_address;
        this.geometry = geometry;

    }


}
