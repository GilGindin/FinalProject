package com.gil.finalproject;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.Toast;

import com.gil.finalproject.Favorite.FvoriteFrag;
import com.gil.finalproject.Location.LocationFrag;
import com.gil.finalproject.TextSearch.DetailsFragment;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

public class MainActivity extends AppCompatActivity implements myMapChnger, LocationListener {

    public static final int LOCATION_KEY_REQUEST = 15;
    private LocationManager locationManager;
    private DetailsFragment detailsFragment;
    private LocationFrag locationFragment;
    private boolean isOffline;
    private Location myLocation;
    static public double newLat;
    static public double newLng;
    private Switch myTextSwitch;
    private FragmentManager fragmentManager;
    private SensorManager sensorManager;
    private BroadcastReceiver receiver;
    private Button button_location;
    private Button button_search;
    private TextInputEditText editText_location;
    private TextInputEditText editText_name;


    //Action menu bar
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.action_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.favoriteFrag) {
            FvoriteFrag favoriteFrag = new FvoriteFrag();
            getFragmentManager().beginTransaction().addToBackStack("replacing to favorite")
                    .replace(R.id.map_coneiner, favoriteFrag).commit();
            return true;
        } else if (item.getItemId() == R.id.sharedPRef) {
            settingsFrag frag = new settingsFrag();
            getFragmentManager().beginTransaction().addToBackStack("fragSetting").replace(R.id.map_coneiner, frag).commit();

        } else if (item.getItemId() == R.id.exit) {

            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(1);
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.searching_layout);

        setButton();
        checkLocationPermission();
        runtimePermission();
        checkConnectivityManager();

    }

    private void checkConnectivityManager() {
        ConnectivityManager cm = (ConnectivityManager) MainActivity.this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        if (!isConnected || !locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            isOffline = true;
            List<Book> nlastSearches = Book.listAll(Book.class);
            RecyclerView lastRV = (RecyclerView) findViewById(R.id.myRV);
            lastRV.setLayoutManager(new GridLayoutManager(MainActivity.this, 1));
            RecyclerView.Adapter LastSearchAdpter = new Book.CustomArrayAdpter(MainActivity.this, nlastSearches);
            lastRV.setAdapter(LastSearchAdpter);
            Toast.makeText(MainActivity.this, "OFFLINE - please enable GPS and network providers ", Toast.LENGTH_SHORT).show();
        }
    }

    private void runtimePermission() {

// checking premission for gps location
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 17);
            }
            GilLocationResult();
        } else {
            //if there's location premission
            //trying to take the exact location by the GPS
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 3000, 500, this);
            myLocation = ((LocationManager) getSystemService(LOCATION_SERVICE)).getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (myLocation == null) {
                Log.e("Location", "not found");
            } else {
                Log.e("Locaion", myLocation.getLatitude() + " " + myLocation.getLongitude());
                newLat = myLocation.getLatitude();
                newLng = myLocation.getLongitude();

            }

        }
    }


    private void setButton() {
        detailsFragment = new DetailsFragment();
        locationFragment = new LocationFrag();
        editText_location = findViewById(R.id.editText_location);
        editText_name = findViewById(R.id.editText_name);
        myTextSwitch = (Switch) findViewById(R.id.switchBTN);
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        button_location = findViewById(R.id.button_location);
        button_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                locationManager = (LocationManager) MainActivity.this.getSystemService(Context.LOCATION_SERVICE);
                String textSearch = editText_name.getText().toString();
                locationFragment.textToSearch = textSearch;
                getSupportFragmentManager().beginTransaction().addToBackStack("replacing to location frag").replace(R.id.fragment_conteiner, locationFragment).commit();

            }
        });
        button_search = findViewById(R.id.button_search);
        button_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String textLocation = editText_name.getText().toString();

                detailsFragment.searchText(textLocation);
                //getSupportFragmentManager().beginTransaction().addToBackStack("details frag").replace(R.id.fragment_conteiner, detailsFragment).commit();
            }
        });
    }

    private boolean checkLocationPermission() {


        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(this)
                        .setTitle("GPS PROVIDER permission")
                        .setMessage("You need to allow Gps provider")
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(MainActivity.this,
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        LOCATION_KEY_REQUEST);
                            }
                        })
                        .create()
                        .show();


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        LOCATION_KEY_REQUEST);
            }
            return false;
        } else {
            return true;
        }
    }

    //on pause stop all the app updates , such as gps listiner
    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(receiver);
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {

            locationManager.removeUpdates(this);
        }

    }

    //if the the gps was offline
    //trying again to take the user previous location
    @Override
    protected void onResume() {
        super.onResume();
        receiver = new PowerConnectionReceiver();
        IntentFilter ifilter = new IntentFilter();
        ifilter.addAction(Intent.ACTION_POWER_CONNECTED);
        ifilter.addAction(Intent.ACTION_POWER_DISCONNECTED);
        registerReceiver(receiver, ifilter);

        onProviderEnabled(locationManager.GPS_PROVIDER);
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 3000, 500, this);
            locationManager = (LocationManager) MainActivity.this.getSystemService(Context.LOCATION_SERVICE);
            GilLocationResult();
        }

    }

    //change fragment methos
    //reciving lat and lng from MyAdpter where the user clicked in the list of the results
    @Override
    public void changeFragment(final double lat, final double lng) {

        MapFragment LocationFragment = new MapFragment();
        if (isTablet()) {
            getFragmentManager().beginTransaction().addToBackStack("add map").replace(R.id.largeMap, LocationFragment).commit();

        } else {

            getFragmentManager().beginTransaction().addToBackStack("replacing").replace(R.id.map_coneiner, LocationFragment).commit();
        }
        LocationFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {

                googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                LatLng latLang = new LatLng(lat, lng);
                Marker perth = googleMap.addMarker(new MarkerOptions()
                        .position(latLang)
                        .draggable(true));
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLang, 17));

            }
        });
    }

    @Override
    public void secondChangeFragment(String name, String adress) {

        FvoriteFrag frag = new FvoriteFrag();
        Bundle bundle = new Bundle();
        bundle.putString("name", name);
        bundle.putString("adress", adress);

        frag.setArguments(bundle);
        Toast.makeText(this, "saved to your favorite list", Toast.LENGTH_SHORT).show();
        getFragmentManager().beginTransaction().addToBackStack("replacing to favorite fragment").replace(R.id.fragment_conteiner, frag).commit();
    }


    @Override
    public void onLocationChanged(final Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 3000, 500, this);
            GilLocationResult();
        }
    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    //when getting premission to gps provider
    @SuppressLint("MissingPermission")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case LOCATION_KEY_REQUEST: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {
                        //get location
                        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 3000, 500, this);
                        Location loc = ((LocationManager) getSystemService(LOCATION_SERVICE)).getLastKnownLocation(LocationManager.GPS_PROVIDER);
                        if (loc == null) {

                        } else {
                            Log.e("/////////", loc.getLatitude() + " " + loc.getLongitude());
                        }
                    } else {
                        Toast.makeText(this, "please allow GPS provider...", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
    }

    //method for locatioin callabck
    public void GilLocationResult() {

        LocationCallback mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    Log.e("LocationUpdate", "No result :'( ");
                }
                for (Location location : locationResult.getLocations()) {
                    Log.e("new location", location.getLatitude() + " " + location.getLongitude());
                }
            }


        };
    }

    //method to check if the app running on tablet or phone
    public boolean isTablet() {
        boolean tablet;
        LinearLayout Xlarge = (LinearLayout) findViewById(R.id.largeMap);
        LinearLayout Large = (LinearLayout) findViewById(R.id.largeMap);
        if (Xlarge != null || Large != null) {
            tablet = true;
        } else {
            tablet = false;
        }
        return tablet;
    }
}



