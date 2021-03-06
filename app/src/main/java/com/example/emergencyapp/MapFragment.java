package com.example.emergencyapp;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

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

    Double latitude = 0.0;
    Double longitude = 0.0;
    SupportMapFragment mapFragment;
    TextView positionInfo;
    String adress;

    FusedLocationProviderClient fusedLocationProviderClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_location);

        // Retrieve the content view that renders the map.
        // Get the SupportMapFragment and request notification
        positionInfo = findViewById(R.id.positionInfo);

        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getApplicationContext());
            getLocation();
            // when the map is ready to be used.
            mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        } else {
            ActivityCompat.requestPermissions(MapFragment.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
        }


    }

    @Override
    protected void onResume() {
        super.onResume();
        getLocation();
        mapFragment.getMapAsync(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        getLocation();
        mapFragment.getMapAsync(this);
    }

    @SuppressLint("MissingPermission")
    public void getLocation() {
        fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                Location location = task.getResult();
                if (location != null) {
                    try {
                        //initialize Geocoder
                        Geocoder geocoder = new Geocoder(MapFragment.this, Locale.getDefault());
                        //initialize address list
                        List<Address> addresses = geocoder.getFromLocation(
                                location.getLatitude(), location.getLongitude(), 1);
                        positionInfo.setText(addresses.get(0).getCountryName() + " , " + addresses.get(0).getAdminArea() + " , " + addresses.get(0).getLocality() + "\n " +
                                "" + addresses.get(0).getAddressLine(0) + "\n" +
                                "" + addresses.get(0).getLatitude() + " , " + addresses.get(0).getLongitude() + " ");
                        adress = addresses.get(0).getAdminArea() + " " + addresses.get(0).getLocality() + " " + addresses.get(0).getCountryName() + " ";
                        latitude = addresses.get(0).getLatitude();
                        longitude = addresses.get(0).getLongitude();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        LatLng position = new LatLng(latitude, longitude);
        MarkerOptions options = new MarkerOptions();
        options.position(position);
        googleMap.addMarker(options).setTitle(adress);
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(position));
        googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        googleMap.setMinZoomPreference(4);
    }
}