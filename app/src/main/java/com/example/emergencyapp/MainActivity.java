package com.example.emergencyapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationProvider;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.SmsManager;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import com.google.android.gms.location.FusedLocationProviderClient;

import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.MapView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements View.OnClickListener /*LocationListener*/ {

    Button emergencyCall ,map;
    private LocationManager locationManager;
    private String longiLocation, latutLocation;
    double latitude,longitude;
    private static final int REQUEST_CODE_LOCATION_PERMISSION = 1;
    FusedLocationProviderClient fusedLocationProviderClient;



    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        emergencyCall = findViewById(R.id.emergency_call);
        map =findViewById(R.id.mapButton);

        emergencyCall.setOnClickListener(this);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);


        map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,MapFragment.class);
                startActivity(intent);
            }
        });


    }

//
//    // the location
//
//
//    void getLocation() {
//        try {
//            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
//            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 5, this);
//
//        } catch (SecurityException e) {
//            e.printStackTrace();
//        }
//    }
//
//    @Override
//    public void onLocationChanged(Location location) {
//
//        data.setText(location.getLatitude() + " " + location.getLongitude());
//        longiLocation = location.getLongitude() + "";
//        latutLocation = location.getLatitude() + "";
//
//
//    }
//
//
//    @Override
//    public void onProviderDisabled(String provider) {
//        Toast.makeText(MainActivity.this, "Please Enable GPS and Internet", Toast.LENGTH_SHORT).show();
//    }
//
//    @Override
//    public void onStatusChanged(String provider, int status, Bundle extras) {
//
//    }
//
//    @Override
//    public void onProviderEnabled(String provider) {
//
//    }
//

//    //end of the location
//



//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        if (requestCode == REQUEST_CODE_LOCATION_PERMISSION && grantResults.length > 0 ){
//              if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
//                   getLocation();
//              }else {
//                  Toast.makeText(this,"permission denied",Toast.LENGTH_SHORT).show();
//              }
//        }
//    }

    @SuppressLint("MissingPermission")
    public void getLocation() {
          fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                Location location = task.getResult();
                if (location != null){

                    try {
                        //initialize Geocoder
                        Geocoder geocoder = new Geocoder(MainActivity.this,
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

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }
    public double geoLat(){
        return latitude;
    }

    public double geoLon(){
        return longitude;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.emergency_call:
                String tel = "0674209174";
                String smsText = "i need help!";
/*
                // make phone call
                if (ContextCompat.checkSelfPermission(getApplicationContext(),Manifest.permission.CALL_PHONE)
                        == PackageManager.PERMISSION_GRANTED){
                    Intent intent = new Intent(Intent.ACTION_CALL);
                    intent.setData(Uri.parse("tel:" + tel));
                    startActivity(intent);

                }else {
                    ActivityCompat.requestPermissions(MainActivity.this,
                            new String[]{Manifest.permission.CALL_PHONE},44);
                }

*/

/*
                //send sms
                if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED) {
                    SmsManager smsManager = SmsManager.getDefault();
                    smsManager.sendTextMessage(tel, null, smsText, null, null);
                    Toast.makeText(MainActivity.this, "sms sent!", Toast.LENGTH_SHORT).show();
                } else {
                    ActivityCompat.requestPermissions(MainActivity.this,
                            new String[]{Manifest.permission.SEND_SMS}, 44);
                }
*/

                break;
        }


    }
}