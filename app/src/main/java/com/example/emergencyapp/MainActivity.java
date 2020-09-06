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

import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;

import com.firebase.ui.auth.ui.phone.CountryListSpinner;
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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ConcurrentHashMap;

public class MainActivity extends AppCompatActivity implements View.OnClickListener /*LocationListener*/ {

    final List<String> spinnerArraywilaya = new ArrayList<String>();
    final List<String> spinnerArraydayra = new ArrayList<String>();

    private static final int REQUEST_CODE_LOCATION_PERMISSION = 1;
    Button emergencyCallBtn ,mapBtn,firemanBtn,policemanBtn,accidentBtn;
    double latitude,longitude;
    FusedLocationProviderClient fusedLocationProviderClient;
    DrawerLayout drawerLayout;
    DatabaseReference databaseRef;
    FirebaseAuth mAuth;
    private LocationManager locationManager;
    private String longiLocation, latutLocation;
//    EditText deleteInfo_Email,deleteInfo_password;
//    Button conform;
    CheckBox deleteRescueAgentAccount;

    Spinner wilayaSpinner;
    Spinner dayraSpinner;




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

    private static void closeDrawer(DrawerLayout drawerLayout) {
        //close drawer layout
        // check condition
        if (drawerLayout.isDrawerOpen(GravityCompat.START)){
            //when drawer is open
            // close drawer
            drawerLayout.closeDrawer(GravityCompat.START);
        }
    }

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        databaseRef = FirebaseDatabase.getInstance().getReference();

//        deleteInfo_Email = findViewById(R.id.delete_user_email);
//        deleteInfo_password = findViewById(R.id.delete_user_password);
//        conform = findViewById(R.id.delete_confirm);
        drawerLayout =findViewById(R.id.drawer_layout);
        emergencyCallBtn = findViewById(R.id.emergency_call);
        mapBtn =findViewById(R.id.mapButton);
        firemanBtn = findViewById(R.id.fireButton);
        policemanBtn = findViewById(R.id.policeButton);
        accidentBtn = findViewById(R.id.AccidentButton);

        emergencyCallBtn.setOnClickListener(this);
        firemanBtn.setOnClickListener(this);
        policemanBtn.setOnClickListener(this);
        accidentBtn.setOnClickListener(this);
        mapBtn.setOnClickListener(this);
        //fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
    }

    private static void openDrawer(DrawerLayout drawerLayout) {
        //open drawer layout
        drawerLayout.openDrawer(GravityCompat.START);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.emergency_call:
                btnSOSOnClick();
                break;
            case R.id.mapButton :
                btnMapOnClick();
                break;
            case R.id.fireButton :
                btnFireOnClick();
                break;
            case R.id.policeButton :
                btnPoliceOnClick();
                break;
            case R.id.AccidentButton :
                btnAccidentOnClick();
                break;
        }
    }

    // the menu
    public void ClickMenu(View view){
        //open drawer
         openDrawer(drawerLayout);
    }

    public void Clicklogo(View view){
        //close drawer
        closeDrawer(drawerLayout);
    }

    public void ClickProfile(View view){

        //go to the profile activity
       Intent intent = new Intent(getApplicationContext(),profile.class);
       startActivity(intent);

    }

    public void ClickDeleteAccount(View view) {

        final   FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final AlertDialog.Builder deleteAlertBuilder = new AlertDialog.Builder(MainActivity.this);
        view = getLayoutInflater().inflate(R.layout.delete_acount_information, null);
        deleteAlertBuilder.setView(view);
        final AlertDialog deleteAlertDialog = deleteAlertBuilder.create();
        deleteAlertDialog.show();
        final EditText deleteInfo_Email = view.findViewById(R.id.delete_user_email);
        final EditText deleteInfo_password = view.findViewById(R.id.delete_user_password);
        deleteRescueAgentAccount = view.findViewById(R.id.rescue_agent_delete_account);






        final Button conform = view.findViewById(R.id.delete_confirm);
        conform.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String email =deleteInfo_Email.getText().toString().trim();
                final String password = deleteInfo_password.getText().toString().trim();



                if (deleteInfo_Email.getText().toString().trim().isEmpty()) {
                    deleteInfo_Email.setError("enter your correct email");
                    deleteInfo_Email.requestFocus();
                    return;
                }
                if (!Patterns.EMAIL_ADDRESS.matcher(deleteInfo_Email.getText().toString().trim()).matches()) {
                    deleteInfo_Email.setError("Please enter a valid email");
                    deleteInfo_Email.requestFocus();
                    return;
                }
                if (deleteInfo_password.getText().toString().trim().isEmpty()) {
                    deleteInfo_password.setError("Password is required");
                    deleteInfo_password.requestFocus();
                    return;
                }
                if (deleteInfo_password.getText().toString().trim().length() < 6) {
                    deleteInfo_password.setError("Minimum lenght of password should be 6");
                    deleteInfo_password.requestFocus();
                    return;
                }


                ValueEventListener valueEventListener = new ValueEventListener(){
                    @Override
                    public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {

//                        String useremail = dataSnapshot.child("email").getValue()+"";
//                        String userpassword = dataSnapshot.child("password").getValue()+"";
                        final AuthCredential credential = EmailAuthProvider
                                .getCredential(email, password);
//                        Toast.makeText(MainActivity.this, email+"  "+password,
//                                Toast.LENGTH_SHORT).show();




                     //   if(useremail.equals(email)  &&  userpassword.equals(password) ){

                            user.reauthenticate(credential)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                // Log.e("TAG", "onComplete: authentication complete");
                                                Toast.makeText(MainActivity.this, "Authentication complete",
                                                        Toast.LENGTH_SHORT).show();

                                                if(Integer.parseInt(dataSnapshot.child("id").getValue().toString()) == 1){
                                                   databaseRef.child("Protected member").child(mAuth.getCurrentUser().getUid()).removeValue();
                                                  //  Toast.makeText(MainActivity.this, "account delete",Toast.LENGTH_SHORT).show();

                                                }else //if(Integer.parseInt(dataSnapshot.child("id").getValue().toString()) == 2) {
                                                    databaseRef.child("Rescue agent").child(mAuth.getCurrentUser().getUid()).removeValue();
                                                 //   Toast.makeText(MainActivity.this, "account delete",Toast.LENGTH_SHORT).show();
//



                                               // Toast.makeText(getApplicationContext(), "account deleted", Toast.LENGTH_SHORT).show();
                                                user.delete()
                                                        .addOnCompleteListener (new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                if (task.isSuccessful()) {

                                                                   Toast.makeText(getApplicationContext(), "account deleted", Toast.LENGTH_SHORT).show();
                                                                    loginActivity();

                                                                } else {
                                                                    Log.e("TAG", "User account deletion unsucessful.");
                                                                    Toast.makeText(getApplicationContext(), "User account deletion unsucessful", Toast.LENGTH_SHORT).show();

                                                                }
                                                            }
                                                        });


                                            } else {
                                              // Toast.makeText(MainActivity.this, "Authentication failed",Toast.LENGTH_SHORT).show();
                                            }
                                        }


                                    });
                        }




                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }



                };



                if(!deleteRescueAgentAccount.isChecked()){
                    databaseRef.child("Protected member").child(user.getUid()).addValueEventListener(valueEventListener);


                }else {
                    databaseRef.child("Rescue agent").child(user.getUid()).addValueEventListener(valueEventListener);

                }
                deleteAlertDialog.dismiss();
            }


        });

       // if(databaseRef.child("Protected member").child(mAuth.getCurrentUser().getUid()).equals(null)){

     //   }else if (databaseRef.child("Rescue agent").child(mAuth.getCurrentUser().getUid()).equals(null)){

      //  }


    }

    public void loginActivity(){
        mAuth.signOut();
        finish();
        startActivity(new Intent(this, Login.class));
    }

    public void ClickLogout(View view){

        mAuth.signOut();
        finish();
        startActivity(new Intent(this, Login.class));

    }

    public void ClickHelp(View view){

        // go to the help page
        startActivity(new Intent(MainActivity.this,Help.class));

    }

    public void ClickMore(){

    }

    @Override
    protected void onPause() {
        super.onPause();
        //close drawer
        closeDrawer(drawerLayout);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mAuth.getCurrentUser() == null) {
            finish();
            startActivity(new Intent(this, Login.class));
        }
    }

    protected void btnSOSOnClick() {
        databaseRef = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();

        final ArrayList<String> agentsTel = new ArrayList<>();

        if (mAuth.getCurrentUser() != null) {
            databaseRef.child("Protected member").child(mAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                final String tel = "";
                final String[] smsText = new String[1];
                final String[] wilaya = {""};
                final String[] dayra = {""};
                final String[] emergencyType = {""};

                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    smsText[0] = "Emergengy SMS: " + "\n";
                    smsText[0] += "first name: " + dataSnapshot.child("firstName").getValue() + "\n";
                    smsText[0] += "last name: " + dataSnapshot.child("lastName").getValue() + "\n";
                    smsText[0] += "gender: " + dataSnapshot.child("gender").getValue() + "\n";
                    smsText[0] += "adress: " + dataSnapshot.child("adress").getValue() + "\n";
                    smsText[0] += "pathologies: " + dataSnapshot.child("pathologies").getValue() + "\n";
                    smsText[0] += "age: " + dataSnapshot.child("age").getValue() + "\n";
                    smsText[0] += "blood group: " + dataSnapshot.child("bloodGroup").getValue() + "\n \n";
                    smsText[0] += "phone number: 0" + dataSnapshot.child("phoneNumber").getValue() + "\n";


                    final AlertDialog.Builder alertBuilder = new AlertDialog.Builder(MainActivity.this);
                    final View view = getLayoutInflater().inflate(R.layout.call_informations, null);
                    alertBuilder.setView(view);
                    final AlertDialog alertDialog = alertBuilder.create();
                    alertDialog.show();

                    wilayaSpinner = view.findViewById(R.id.wilaya_call);
                    dayraSpinner = view.findViewById(R.id.dayra_call);

                    spinnerArraywilaya.clear();
                    fillWilaya();
                    wilayaSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            //Toast.makeText(getBaseContext(), "hhhhhhh", Toast.LENGTH_SHORT).show();
                            spinnerArraydayra.clear();
                            fillDayra(wilayaSpinner.getSelectedItem() + "");
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });

                    Button confirmCallBtn = view.findViewById(R.id.confirm_call);
                    confirmCallBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            wilaya[0] = wilayaSpinner.getSelectedItem() + "";
                            dayra[0] = dayraSpinner.getSelectedItem() + "";
                            Spinner spinner = view.findViewById(R.id.emergency_type_call);
                            emergencyType[0] = spinner.getSelectedItem() + "";
                            //Toast.makeText(getBaseContext(), emergencyType[0], Toast.LENGTH_SHORT).show();

                            final String rescueAgentType;
                            switch (emergencyType[0]) {
                                case "incendie":
                                    rescueAgentType = "FIREMAN";
                                    break;
                                case "accident":
                                    rescueAgentType = "SOS_AGENT";
                                    break;
                                case "urgence madical":
                                    rescueAgentType = "DOCTOR";
                                    break;
                                default:
                                    rescueAgentType = "VOLUNTEER";
                                    break;
                            }

                            databaseRef.child("Rescue agent").addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                        if (ds.child("wilaya").getValue().equals(wilaya[0]) &&
                                                ds.child("dayra").getValue().equals(dayra[0]) &&
                                                ds.child("rescueType").getValue().equals(rescueAgentType)) {
                                            agentsTel.add("0" + ds.child("phoneNumber").getValue());

                                            if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED) {
                                                SmsManager smsManager = SmsManager.getDefault();
                                                smsManager.sendMultipartTextMessage("0" + ds.child("phoneNumber").getValue(), null, smsManager.divideMessage(smsText[0]), null, null);
                                                smsText[0] = "";
                                                Toast.makeText(MainActivity.this, "sms sent!", Toast.LENGTH_SHORT).show();
                                            } else {
                                                ActivityCompat.requestPermissions(MainActivity.this,
                                                        new String[]{Manifest.permission.SEND_SMS}, 44);
                                            }
                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                            alertDialog.dismiss();
                        }
                    });
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }

    protected void btnMapOnClick(){
        startActivity(new Intent(getBaseContext(),MapFragment.class));
    }

    protected void btnFireOnClick(){
        // make phone call
        if (ContextCompat.checkSelfPermission(getApplicationContext(),Manifest.permission.CALL_PHONE)
                == PackageManager.PERMISSION_GRANTED){
            Intent intent = new Intent(Intent.ACTION_CALL);
            intent.setData(Uri.parse("tel:01021"));
            startActivity(intent);
        }else {
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.CALL_PHONE},44);
        }
    }

    protected void btnPoliceOnClick(){
        // make phone call
        if (ContextCompat.checkSelfPermission(getApplicationContext(),Manifest.permission.CALL_PHONE)
                == PackageManager.PERMISSION_GRANTED){
            Intent intent = new Intent(Intent.ACTION_CALL);
            intent.setData(Uri.parse("tel:01548"));
            startActivity(intent);

        }else {
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.CALL_PHONE},44);
        }
    }

    protected void btnAccidentOnClick(){
        // make phone call
        if (ContextCompat.checkSelfPermission(getApplicationContext(),Manifest.permission.CALL_PHONE)
                == PackageManager.PERMISSION_GRANTED){
            Intent intent = new Intent(Intent.ACTION_CALL);
            intent.setData(Uri.parse("tel:01055"));
            startActivity(intent);

        }else {
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.CALL_PHONE},44);
        }
    }

    private void fillWilaya() {

        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    spinnerArraywilaya.add(ds.getKey() + "");
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getBaseContext(), android.R.layout.simple_spinner_item, spinnerArraywilaya);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                wilayaSpinner.setAdapter(adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

        databaseRef.child("wilaya").addValueEventListener(valueEventListener);
    }

    private void fillDayra(String mWilaya) {

        databaseRef.child("wilaya").child(mWilaya).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    spinnerArraydayra.add(ds.getKey());
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getBaseContext(), android.R.layout.simple_spinner_item, spinnerArraydayra);

                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                dayraSpinner.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}