package com.gil.finalproject;

import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.RadioButton;
import android.widget.SearchView;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;

public class MainActivity extends AppCompatActivity implements myMapChnger, LocationListener {

    SearchView searchView;
    RadioButton radioButton;
    LocationManager locationManager;
    DetailsFragment detailsFragment;

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.action_menu, menu);
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()== R.id.listFragment)
        {
            getFragmentManager().beginTransaction().addToBackStack("replacing")
                    .replace(R.id.theMainLayout, new DetailsFragment()).commit();
        }
        return true;
    }

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        detailsFragment = new DetailsFragment();
        searchView = (SearchView) findViewById(R.id.searchSV);
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED){
            requestPermissions(new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION} , 17);

        }else {
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER , 600000 , 500 , this);
        //Location myLocation = ((LocationManager) getSystemService(LOCATION_SERVICE)).getLastKnownLocation(LocationManager.GPS_PROVIDER);
          //  Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        }



                  searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {

                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    if(newText.length()>3) {
                        detailsFragment.searchText(newText);
                    }
                    return true;
                }
            });

        getFragmentManager().beginTransaction().replace(R.id.theMainLayout ,detailsFragment).commit();

    }
    @Override
    protected void onPause() {
        super.onPause();
        locationManager.removeUpdates(this);
    }

    @Override
    public void changeFragment(final double lat, final double lng) {

         MapFragment LocationFragment = new MapFragment();
        getFragmentManager().beginTransaction().addToBackStack("replacing").replace(R.id.theMainLayout ,LocationFragment).commit();

        LocationFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {

                googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                LatLng latLang = new LatLng(lat, lng);
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLang, 16));
            }
        });
    }

    @Override
    public void onLocationChanged(final Location location) {

    }
    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
