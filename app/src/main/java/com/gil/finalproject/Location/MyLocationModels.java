package com.gil.finalproject.Location;

import java.util.List;

/**
 * Created by gil on 26/03/2018.
 */

public class MyLocationModels {

    public String name;
    public String vicinity;
    public MyLocationGeometry geometry;
    public List<MyPhotoLocation> photos;

    public MyLocationModels(String name, String vicinity, MyLocationGeometry geometry) {
        this.name = name;
        this.vicinity = vicinity;
        this.geometry = geometry;
    }
}
