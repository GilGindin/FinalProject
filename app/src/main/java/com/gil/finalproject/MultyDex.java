package com.gil.finalproject;

import android.content.Context;

import com.orm.SugarApp;
import com.orm.SugarContext;
import com.orm.SugarRecord;

/**
 * Created by gil on 03/04/2018.
 */

public class MultyDex extends SugarApp {


    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        //android.support.multidex.MultyDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        SugarContext.init(this);
    }

    @Override
    public void onTerminate() {
        SugarContext.terminate();
        super.onTerminate();
    }
}
