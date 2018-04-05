package com.gil.finalproject;

import android.annotation.SuppressLint;
import android.app.FragmentManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.hardware.SensorEventListener;
import android.hardware.SensorListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.BatteryManager;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.SearchView;
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

    SearchView searchView;
    LocationManager locationManager;
    DetailsFragment detailsFragment;
    public boolean isOffline;
   public  Location myLocation;
    static public double newLat;
    static public double newLng;
    Switch myTextSwitch;
    FragmentManager fragmentManager;
    SensorManager sensorManager;


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
                    .replace(R.id.theMainLayout,favoriteFrag).commit();
            return true;
        }
        else if(item.getItemId() == R.id.sharedPRef){


        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        detailsFragment = new DetailsFragment();
        myTextSwitch  = (Switch) findViewById(R.id.switchBTN);
        searchView = (SearchView) findViewById(R.id.searchSV);
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        sensorManager= (SensorManager) getSystemService(SENSOR_SERVICE);





// checking premission for gps location
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 17);
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


//search view
        // the main query from the user to the app
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(final String newText) {
                //checking if the user have internet
                ConnectivityManager cm = (ConnectivityManager) MainActivity.this.getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
                boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
                locationManager = (LocationManager) MainActivity.this.getSystemService(Context.LOCATION_SERVICE);

                ///OFFLINE mode
                // taking the list from the last results who was searching by the user
                if (!isConnected || !locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    isOffline = true;
                    List<Book> nlastSearches = Book.listAll(Book.class);
                    RecyclerView lastRV = (RecyclerView) findViewById(R.id.myRV);
                    lastRV.setLayoutManager(new GridLayoutManager(MainActivity.this, 1));
                    RecyclerView.Adapter LastSearchAdpter = new LastSearchAdpter(MainActivity.this, nlastSearches);
                    lastRV.setAdapter(LastSearchAdpter);
                    Toast.makeText(MainActivity.this, "OFFLINE - enable GPS and network providers ", Toast.LENGTH_SHORT).show();
                } else {
// if online , searching the query with minimum 3 chars to search


                    if (newText.length() > 4) {
                        Boolean switchState = myTextSwitch.isChecked();
                        myTextSwitch.setChecked(false);

//checking if the switch button is on or off
                        myTextSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                                if(isChecked){

//method that excist in detalis fragment
                                    LocationFrag locationFragment = new LocationFrag();
                                    locationFragment.textToSearch=newText;
                                    getFragmentManager().beginTransaction().addToBackStack("replacing to location frag").replace(R.id.theMainLayout, locationFragment).commit();
                                  //  locationFragment.searchText(newText);
                                }
                            }
                        });
                            detailsFragment.searchText(newText);

                    }
                }
                return true;
                //////////////////////////////////////////////////////////////////////////////////////////////////////////
            }
        });
        //replacing to detalis fragment
        fragmentManager = getFragmentManager();
        getFragmentManager().beginTransaction().addToBackStack("details frag").replace(R.id.theMainLayout, detailsFragment).commit();



    }
    BroadcastReceiver batteryPower = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int status = intent.getIntExtra(BatteryManager.EXTRA_STATUS , -1);
            boolean isCharge = status == BatteryManager.BATTERY_STATUS_CHARGING ||
                    status == BatteryManager.BATTERY_STATUS_FULL;
            if(isCharge){

                Toast.makeText(context, "Battery charging..", Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(context, "disconnecting charge..", Toast.LENGTH_SHORT).show();
            }
        }
    };
    BroadcastReceiver changingConnection = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals(intent.ACTION_POWER_CONNECTED)){

                Toast.makeText(context, "battery charge", Toast.LENGTH_SHORT).show();
            }else if(intent.getAction().equals(intent.ACTION_POWER_DISCONNECTED)){

                Toast.makeText(context, "disconnected charging..", Toast.LENGTH_SHORT).show();

            }
        }
    };
//on pause stop all the app updates , such as gps listiner
    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(changingConnection);
        unregisterReceiver(batteryPower);
        locationManager.removeUpdates(this);
        //onsavedinstancestate
    }

////////////////////////////////////////////////////////////
    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
    }

    //if the the gps was offline
    //trying again to take the user previous location
    @Override
    protected void onResume() {
        super.onResume();
    IntentFilter filtter = new IntentFilter(Intent.ACTION_BATTERY_LOW);
        filtter.addAction(Intent.ACTION_BATTERY_OKAY);
        registerReceiver(batteryPower, filtter);
        registerReceiver(changingConnection, filtter);
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
        if(isTablet()){
            getFragmentManager().beginTransaction().addToBackStack("add map").replace(R.id.largeMap , LocationFragment).commit();

        }
        else {

            getFragmentManager().beginTransaction().addToBackStack("replacing").replace(R.id.theMainLayout, LocationFragment).commit();
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
        bundle.putString("name" ,name);
        bundle.putString("adress" , adress);

        frag.setArguments(bundle);
        Toast.makeText(this, "saved to your favorite list", Toast.LENGTH_SHORT).show();
        getFragmentManager().beginTransaction().addToBackStack("replacing to favorite fragment").replace(R.id.theMainLayout , frag).commit();
       // getFragmentManager().beginTransaction().addToBackStack("replace to favorite").replace(R.id.theMainLayout , frag).commit();
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

        if (requestCode == 15) {

            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //get location
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 3000, 500, this);
                Location loc = ((LocationManager) getSystemService(LOCATION_SERVICE)).getLastKnownLocation(LocationManager.GPS_PROVIDER);
                if (loc == null) {
                    Log.e("PSY", "not found");
                } else {
                    Log.e("/////////", loc.getLatitude() + " " + loc.getLongitude());
                }
            } else {
                Toast.makeText(this, "please allow GPS provider...", Toast.LENGTH_SHORT).show();
            }
        }
    }
    //method for locatioin callabck
    public void GilLocationResult () {

        LocationCallback mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                  Log.e("LocationUpdate","No result :'( ");
                }
                for (Location location : locationResult.getLocations()) {
                    Log.e("new location",location.getLatitude() + " " + location.getLongitude());
                }
            }


        };
    }

    public boolean isTablet(){
      boolean tablet;
        LinearLayout Xlarge =(LinearLayout) findViewById(R.id.largeMap);
        LinearLayout Large = (LinearLayout) findViewById(R.id.largeMap);
        if(Xlarge != null || Large != null){
            tablet = true;
        }else {
            tablet= false;
        }
        return tablet;
      }
     }

