package com.example.emergencyapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.Spinner;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements View.OnClickListener /*LocationListener*/ {

    Button emergencyCall ,map;

    private LocationManager locationManager;
    private String longiLocation, latutLocation;
    double latitude,longitude;
    private static final int REQUEST_CODE_LOCATION_PERMISSION = 1;
    FusedLocationProviderClient fusedLocationProviderClient;
    DrawerLayout drawerLayout;

    DatabaseReference databaseRef;
    FirebaseAuth mAuth;



    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        drawerLayout =findViewById(R.id.drawer_layout);


        emergencyCall = findViewById(R.id.emergency_call);
        map =findViewById(R.id.mapButton);

       emergencyCall.setOnClickListener(this);
        //fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
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

//    @SuppressLint("MissingPermission")
//    public void getLocation() {
//          fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
//            @Override
//            public void onComplete(@NonNull Task<Location> task) {
//                Location location = task.getResult();
//                if (location != null){
//
//                    try {
//                        //initialize Geocoder
//                        Geocoder geocoder = new Geocoder(MainActivity.this,
//                                Locale.getDefault() );
//                        //initialize address list
//                        List<Address> addresses = geocoder.getFromLocation(
//                                location.getLatitude(),location.getLongitude(),1
//                        );
//                        Toast.makeText(getBaseContext(),addresses.get(0).getCountryName() +" "+addresses.get(0).getLocality()+" "+ addresses.get(0)
//                        .getAddressLine(0)+" "+addresses.get(0).getLatitude()+" "+addresses.get(0).getLongitude()+
//                                " "+addresses.get(0).getAdminArea(), Toast.LENGTH_LONG).show();
//                        latitude=addresses.get(0).getLatitude();
//                        longitude=addresses.get(0).getLongitude();
//
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//        });
//    }
//    public double geoLat(){
//        return latitude;
//    }
//
//    public double geoLon(){
//        return longitude;
//    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.emergency_call:
                makeAnEmergencyCall();
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

                break;
        }
    }



    // the menu
    public void ClickMenu(View view){
        //open drawer
         openDrawer(drawerLayout);
    }

    private static void openDrawer(DrawerLayout drawerLayout) {
        //open drawer layout
        drawerLayout.openDrawer(GravityCompat.START);
    }

    public void Clicklogo(View view){
        //close drawer
        closeDrawer(drawerLayout);
    }

    private static void closeDrawer(DrawerLayout drawerLayout) {
        //close drawer layout
        // check condition
        if (drawerLayout.isDrawerOpen(GravityCompat.START)){
            //when drawer is open
            // close drawer
            drawerLayout.closeDrawer(GravityCompat.START);
        }
    }

    public void ClickProfile(View view){

        //go to the profile activity
       Intent intent = new Intent(getApplicationContext(),profile.class);
       startActivity(intent);

    }

    public void ClickDeleteAccount(View view){
//        // alert dialog
//       // AlertDialog.Builder builder=new AlertDialog.Builder;
//        AlertDialog.Builder builder=new AlertDialog.Builder(activity);
//        //set the title
//        builder.setTitle("Delete Account");
//        //set msg
//        builder.setMessage("are you sure you want to delete your account ?");
//        //yes
//        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                // log out from app and redirect to log in activity
//                Intent intent =new Intent(MainActivity.this,MapFragment.class);
//                startActivity(intent);
//                // System.exit(0);
//            }
//        });
//        // NO
//        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                dialog.dismiss();
//            }
//        });
//        builder.show();
      //  Toast.makeText(getApplicationContext(),"it work",Toast.LENGTH_SHORT).show();
        mAuth.signOut();
        finish();
        startActivity(new Intent(this, Login.class));

    }

    public void ClickLogout(View view){
//        // alert dialog
//        AlertDialog.Builder builder=new AlertDialog.Builder(activity);
//        //set the title
//        builder.setTitle("Logout");
//        //set msg
//        builder.setMessage("are you sure you want to logout ?");
//        //yes
//        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                // log out from app and redirect to log in activity
//              Intent intent =new Intent(MainActivity.this,MapFragment.class);
//              startActivity(intent);
//              // System.exit(0);
//            }
//        });
//         // NO
//        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                dialog.dismiss();
//            }
//        });
//        builder.show();
        mAuth.signOut();
        finish();
        startActivity(new Intent(this, Login.class));

    }

    public void ClickHelp(View view){

        // go to the help page
    }

    @Override
    protected void onPause() {
        super.onPause();
        //close drawer
        closeDrawer(drawerLayout);
    }
    protected void makeAnEmergencyCall() {
        databaseRef = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();

        final String tel = "";
        final String[] smsText = new String[1];
        final String[] wilaya = {""};
        final String[] dayra = {""};
        final String[] emergencyType = {""};
        smsText[0] = "Emergengy Call: ";

        final ArrayList<String> agentsTel = new ArrayList<>();

        if (mAuth.getCurrentUser() != null) {
            databaseRef.child("Protected member").child(mAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    smsText[0] += "first name: " + dataSnapshot.child("firstName").getValue() + "\n";
                    smsText[0] += "last name: " + dataSnapshot.child("lastName").getValue() + "\n";
                    smsText[0] += "gender: " + dataSnapshot.child("gender").getValue() + "\n";
                    smsText[0] += "adress: " + dataSnapshot.child("adress").getValue() + "\n";
                    smsText[0] += "pathologies: " + dataSnapshot.child("pathologies").getValue() + "\n";
                    smsText[0] += "age: " + dataSnapshot.child("yearOfBith").getValue() + "\n";
                    smsText[0] += "blood group: " + dataSnapshot.child("bloodGroup").getValue() + "\n \n";
                    smsText[0] += "phone number: 0" + dataSnapshot.child("phoneNumber").getValue() + "\n";
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            final AlertDialog.Builder alertBuilder = new AlertDialog.Builder(MainActivity.this);
            final View view = getLayoutInflater().inflate(R.layout.call_informations, null);
            alertBuilder.setView(view);
            final AlertDialog alertDialog = alertBuilder.create();
            alertDialog.show();

            Button confirmCallBtn = view.findViewById(R.id.confirm_call);
            confirmCallBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Spinner spinner = view.findViewById(R.id.wilaya_call);
                    wilaya[0] = spinner.getSelectedItem() + "";
                    spinner = view.findViewById(R.id.dayra_call);
                    dayra[0] = spinner.getSelectedItem() + "";
                    spinner = view.findViewById(R.id.emergency_type_call);
                    emergencyType[0] = spinner.getSelectedItem() + "";
                    //Toast.makeText(getBaseContext(), emergencyType[0], Toast.LENGTH_SHORT).show();

                    switch (emergencyType[0]){
                        case "incendie":
                            //Toast.makeText(getBaseContext(), emergencyType[0] , Toast.LENGTH_SHORT).show();
                            databaseRef.child("Rescue agent").addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                        if (ds.child("wilaya").getValue().equals(wilaya[0]) &&
                                              ds.child("dayra").getValue().equals(dayra[0])){
                                              agentsTel.add("0" + ds.child("phoneNumber").getValue());
                          //                     Toast.makeText(getBaseContext(), agentsTel.get(0) , Toast.LENGTH_SHORT).show();

                                            if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED) {
                                                SmsManager smsManager = SmsManager.getDefault();
                                                smsManager.sendTextMessage("0" + ds.child("phoneNumber").getValue(), null, smsText[0], null, null);
                                                Toast.makeText(MainActivity.this, "sms sent!", Toast.LENGTH_SHORT).show();
                                            } else {
                                                ActivityCompat.requestPermissions(MainActivity.this,
                                                        new String[]{Manifest.permission.SEND_SMS}, 44);
                                            }
                                          //   Toast.makeText(getBaseContext(), agentsTel.get(0) , Toast.LENGTH_SHORT).show();
                                      }
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                            break;
                        case "accident":
                            break;
                        case "urgence madical":
                            break;
                    }

                    //send sms
                    for (String telNB : agentsTel){

                    }

                    alertDialog.dismiss();
                }
            });
        }
    }
}