package com.gil.finalproject;

import com.orm.SugarRecord;

/**
 * Created by gil on 08/03/2018.
 */

public class Book extends SugarRecord {

    String name;
    String edition;

    public Book() {
    }

    public Book(String name, String edition) {
        this.name = name;
        this.edition = edition;
    }
}
