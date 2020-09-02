package com.example.emergencyapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapFragment extends AppCompatActivity implements OnMapReadyCallback {

     EditText textview1 ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


         textview1 =findViewById(R.id.editTextTextMultiLine);

        // Retrieve the content view that renders the map.
        setContentView(R.layout.activity_map_fragment);
        // Get the SupportMapFragment and request notification
        // when the map is ready to be used.
        final SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);


        mapFragment.getMapAsync(this);


    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        LatLng position = new LatLng(36.1940491,8.158600);
        MarkerOptions options = new MarkerOptions();
        options.position(position);
        googleMap.addMarker(options);
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(position));
        googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        googleMap.setMinZoomPreference(4);

    }


}