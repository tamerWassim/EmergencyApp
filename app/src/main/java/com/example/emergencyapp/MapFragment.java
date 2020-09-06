package com.example.emergencyapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MapFragment extends AppCompatActivity implements OnMapReadyCallback {

   TextView textview;
    private LocationManager locationManager;
   // private String longiLocation, latutLocation;
    double latitude,longitude;
    String countryName;
    private static final int REQUEST_CODE_LOCATION_PERMISSION = 1;
    FusedLocationProviderClient fusedLocationProviderClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_map_fragment);
         textview  =findViewById(R.id.location_info);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        // Retrieve the content view that renders the map.

        // Get the SupportMapFragment and request notification
        // when the map is ready to be used.
        final SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);


        mapFragment.getMapAsync(this);
        getLocation();




    }
    @SuppressLint("MissingPermission")
    public void getLocation() {
        fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                Location location = task.getResult();
                if (location != null){

                    try {
                        //initialize Geocoder
                        Geocoder geocoder = new Geocoder(MapFragment.this,
                                Locale.getDefault() );
                        //initialize address list
                        List<Address> addresses = geocoder.getFromLocation(
                                location.getLatitude(),location.getLongitude(),1
                        );
                        Toast.makeText(getBaseContext(),addresses.get(0).getCountryName() +" "+addresses.get(0).getLocality()+" "+ addresses.get(0)
                                .getAddressLine(0)+" "+addresses.get(0).getLatitude()+" "+addresses.get(0).getLongitude()+
                                " "+addresses.get(0).getAdminArea(), Toast.LENGTH_LONG).show();
                        latitude=addresses.get(0).getLatitude();
                        longitude=addresses.get(0).getLongitude();
                        countryName = addresses.get(0).getCountryName()+"  " +addresses.get(0).getAdminArea();

                        textview.setText(addresses.get(0).getCountryName() +"  " +
                                " "+addresses.get(0).getLocality()+"" +
                                "  "+ addresses.get(0).getAddressLine(0)+" " +
                                " "+addresses.get(0).getLatitude()+" " +
                                "  "+addresses.get(0).getLongitude()+
                                "   "+addresses.get(0).getAdminArea());

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        LatLng position = new LatLng(latitude,longitude);
        MarkerOptions options = new MarkerOptions();
        options.position(position);
        googleMap.addMarker(options).setTitle("I'here");
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(position));
        //googleMap.animateCamera(CameraUpdateFactory.zoomTo(15));
        googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        googleMap.setMinZoomPreference(5);

    }


}