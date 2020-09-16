package com.example.emergencyapp;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int REQUEST_CODE_LOCATION_PERMISSION = 1;
    final List<String> spinnerArraywilaya = new ArrayList<String>();
    final List<String> spinnerArraydayra = new ArrayList<String>();
    double latitude, longitude;
    String CurrentLocation;

    FirebaseAuth mAuth;
    Button emergencyCallBtn, mapBtn, firemanBtn, policemanBtn, accidentBtn;
    FusedLocationProviderClient fusedLocationProviderClient;
    DrawerLayout drawerLayout;
    DatabaseReference databaseRef;
    CheckBox deleteRescueAgentAccount;
    Spinner wilayaSpinner;
    Spinner dayraSpinner;

    private static void closeDrawer(DrawerLayout drawerLayout) {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            //when drawer is open
            drawerLayout.closeDrawer(GravityCompat.START);
        }
    }

    private static void openDrawer(DrawerLayout drawerLayout) {
        //open drawer layout
        drawerLayout.openDrawer(GravityCompat.START);
    }

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // firebase database and authentication
        mAuth = FirebaseAuth.getInstance();
        databaseRef = FirebaseDatabase.getInstance().getReference();

        // fast access to authorities
        firemanBtn = findViewById(R.id.fireButton);
        policemanBtn = findViewById(R.id.policeButton);
        accidentBtn = findViewById(R.id.AccidentButton);

        // S.O.S call Button
        emergencyCallBtn = findViewById(R.id.emergency_call);

        // location Button
        mapBtn = findViewById(R.id.mapButton);
        drawerLayout = findViewById(R.id.drawer_layout);

        // on click events
        emergencyCallBtn.setOnClickListener(this);
        firemanBtn.setOnClickListener(this);
        policemanBtn.setOnClickListener(this);
        accidentBtn.setOnClickListener(this);
        mapBtn.setOnClickListener(this);

        // get current location
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        // permissions to location
        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            getLocation();
        } else {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        //close drawer
        closeDrawer(drawerLayout);
        if (mAuth.getCurrentUser() == null) {
            finish();
            startActivity(new Intent(this, Login.class));
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mAuth.getCurrentUser() == null) {
            finish();
            startActivity(new Intent(this, Login.class));
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.emergency_call:
                btnSOSOnClick();
                break;
            case R.id.mapButton:
                btnMapOnClick();
                break;
            case R.id.fireButton:
                btnFireOnClick();
                break;
            case R.id.policeButton:
                btnPoliceOnClick();
                break;
            case R.id.AccidentButton:
                btnAccidentOnClick();
                break;
        }
    }

    //All main activity buttuns
    protected void btnMapOnClick() {
        startActivity(new Intent(getBaseContext(), MapFragment.class));
    }

    // fast access to Security authorities
    protected void btnFireOnClick() {
        // make phone call
        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
            Intent intent = new Intent(Intent.ACTION_CALL);
            intent.setData(Uri.parse("tel:01021"));
            startActivity(intent);
        } else {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CALL_PHONE}, 44);
        }
    }

    protected void btnPoliceOnClick() {
        // make phone call
        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
            Intent intent = new Intent(Intent.ACTION_CALL);
            intent.setData(Uri.parse("tel:01548"));
            startActivity(intent);
        } else {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CALL_PHONE}, 44);
        }
    }

    protected void btnAccidentOnClick() {
        // make phone call
        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
            Intent intent = new Intent(Intent.ACTION_CALL);
            intent.setData(Uri.parse("tel:01055"));
            startActivity(intent);
        } else {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CALL_PHONE}, 44);
        }
    }

    // S.O.S  call
    protected void btnSOSOnClick() {
        databaseRef = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();

        if (mAuth.getCurrentUser() != null) {
            databaseRef.child("Protected member").child(mAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                final String[] smsText = new String[1];
                final String[] wilaya = {""};
                final String[] dayra = {""};
                final String[] emergencyType = {""};

                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    //take user informations in SMS text
                    smsText[0] = "Emergengy SMS: " + "\n";
                    smsText[0] += "first name: " + dataSnapshot.child("firstName").getValue() + "\n";
                    smsText[0] += "last name: " + dataSnapshot.child("lastName").getValue() + "\n";
                    smsText[0] += "gender: " + dataSnapshot.child("gender").getValue() + "\n";
                    smsText[0] += "adress: " + dataSnapshot.child("adress").getValue() + "\n";
                    smsText[0] += "pathologies: " + dataSnapshot.child("pathologies").getValue() + "\n";
                    smsText[0] += "age: " + dataSnapshot.child("age").getValue() + "\n";
                    smsText[0] += "blood group: " + dataSnapshot.child("bloodGroup").getValue() + "\n \n";
                    smsText[0] += "phone number: 0" + dataSnapshot.child("phoneNumber").getValue() + "\n \n";
                    smsText[0] += "location:" + CurrentLocation + "\n";

                    final AlertDialog.Builder alertBuilder = new AlertDialog.Builder(MainActivity.this);
                    final View view = getLayoutInflater().inflate(R.layout.call_informations, null);
                    alertBuilder.setView(view);
                    final AlertDialog alertDialog = alertBuilder.create();

                    wilayaSpinner = view.findViewById(R.id.wilaya_call);
                    dayraSpinner = view.findViewById(R.id.dayra_call);

                    spinnerArraywilaya.clear();
                    fillWilaya();
                    wilayaSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            spinnerArraydayra.clear();
                            fillDayra(wilayaSpinner.getSelectedItem() + "");
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });

                    if (dataSnapshot.child("gender").getValue() == null) {
                        databaseRef.child("Rescue agent").child(mAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                            final String[] smsText = new String[1];
                            final String[] wilaya = {""};
                            final String[] dayra = {""};
                            final String[] emergencyType = {""};
                            String tel = "";

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
                                smsText[0] += "location:" + CurrentLocation + "\n";
                                tel = dataSnapshot.child("phoneNumber").getValue() + "";

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

                                        final String rescueAgentType;
                                        switch (emergencyType[0]) {
                                            case "Fire":
                                                rescueAgentType = "FIREMAN";
                                                break;
                                            case "Accident":
                                                rescueAgentType = "SOS_AGENT";
                                                break;
                                            case "Medical Emergency":
                                                rescueAgentType = "MEDICAL_FIELD";
                                                break;
                                            case "Robory":
                                                rescueAgentType = "POLICEMAN";
                                                break;
                                            case "Danger Threat":
                                                rescueAgentType = "GENDARM";
                                                break;
                                            default:
                                                rescueAgentType = "VOLUNTEER";
                                                break;
                                        }

                                        databaseRef.child("Rescue agent").addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                boolean exist = false;
                                                for (DataSnapshot ds : dataSnapshot.getChildren()) {

                                                    if (Objects.equals(ds.child("wilaya").getValue(), wilaya[0]) &&
                                                            Objects.equals(ds.child("dayra").getValue(), dayra[0]) &&
                                                            Objects.equals(ds.child("rescueType").getValue(), rescueAgentType) &&
                                                            !Objects.equals(ds.child("phoneNumber").getValue(), dataSnapshot.child(mAuth.getCurrentUser().getUid()).child("phoneNumber").getValue())) {
                                                        exist = true;
                                                        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED) {
                                                            SmsManager smsManager = SmsManager.getDefault();
                                                            smsManager.sendMultipartTextMessage("0" + ds.child("phoneNumber").getValue(), null, smsManager.divideMessage(smsText[0]), null, null);
                                                            smsText[0] = "";
                                                            Toast.makeText(MainActivity.this, "sms sent!", Toast.LENGTH_SHORT).show();
                                                        } else {
                                                            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.SEND_SMS}, 44);
                                                        }
                                                    }
                                                }
                                                if (!exist) {
                                                    Toast.makeText(getBaseContext(), "No Rescue Agent in This Area", Toast.LENGTH_SHORT).show();
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
                                Toast.makeText(getBaseContext(), "Check your internet connection", Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        alertDialog.show();
                        Button confirmCallBtn = view.findViewById(R.id.confirm_call);
                        confirmCallBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                wilaya[0] = wilayaSpinner.getSelectedItem() + "";
                                dayra[0] = dayraSpinner.getSelectedItem() + "";
                                Spinner spinner = view.findViewById(R.id.emergency_type_call);
                                emergencyType[0] = spinner.getSelectedItem() + "";

                                final String rescueAgentType;
                                switch (emergencyType[0]) {
                                    case "Fire":
                                        rescueAgentType = "FIREMAN";
                                        break;
                                    case "Accident":
                                        rescueAgentType = "SOS_AGENT";
                                        break;
                                    case "Medical Emergency":
                                        rescueAgentType = "MEDICAL_FIELD";
                                        break;
                                    case "Robory":
                                        rescueAgentType = "POLICEMAN";
                                        break;
                                    case "Danger Threat":
                                        rescueAgentType = "GENDARM";
                                        break;
                                    default:
                                        rescueAgentType = "VOLUNTEER";
                                        break;
                                }

                                databaseRef.child("Rescue agent").addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        boolean exist = false;
                                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                            if (Objects.equals(ds.child("wilaya").getValue(), wilaya[0]) &&
                                                    Objects.equals(ds.child("dayra").getValue(), dayra[0]) &&
                                                    Objects.equals(ds.child("rescueType").getValue(), rescueAgentType)) {
                                                exist = true;
                                                if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED) {
                                                    SmsManager smsManager = SmsManager.getDefault();
                                                    smsManager.sendMultipartTextMessage("0" + ds.child("phoneNumber").getValue(), null, smsManager.divideMessage(smsText[0]), null, null);
                                                    smsText[0] = "";
                                                    Toast.makeText(MainActivity.this, "sms sent!", Toast.LENGTH_SHORT).show();
                                                } else {
                                                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.SEND_SMS}, 44);
                                                }
                                            }
                                        }
                                        if (!exist) {
                                            Toast.makeText(getBaseContext(), "No Rescue Agent in This Area", Toast.LENGTH_SHORT).show();
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
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(getBaseContext(), "Check your internet connection", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
    // end .

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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_LOCATION_PERMISSION && grantResults.length > 0) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLocation();
            } else {
                Toast.makeText(this, "permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // get the current location
    @SuppressLint("MissingPermission")
    public void getLocation() {
        fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                Location location = task.getResult();
                if (location != null) {

                    try {
                        //initialize Geocoder
                        Geocoder geocoder = new Geocoder(MainActivity.this, Locale.getDefault());
                        //initialize address list
                        List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                        CurrentLocation = addresses.get(0).getCountryName() + "," + addresses.get(0).getLocality() + "," + addresses.get(0).getAdminArea() + "," + addresses.get(0).getAddressLine(0) + "";
                        // +addresses.get(0).getLatitude()+" "+addresses.get(0).getLongitude();
                        latitude = addresses.get(0).getLatitude();
                        longitude = addresses.get(0).getLongitude();

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    public void loginActivity() {
        mAuth.signOut();
        finish();
        startActivity(new Intent(this, Login.class));
    }

    // all menu options
    public void ClickMenu(View view) {
        openDrawer(drawerLayout);
    }

    public void ClickLogo(View view) {
        //close drawer
        closeDrawer(drawerLayout);
    }

    public void ClickProfile(View view) {
        //go to the profile activity
        Intent intent = new Intent(getApplicationContext(), profile.class);
        startActivity(intent);
    }

    public void ClickDeleteAccount(View view) {
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
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
                final String email = deleteInfo_Email.getText().toString().trim();
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

                ValueEventListener valueEventListener = new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {
                        final AuthCredential credential = EmailAuthProvider.getCredential(email, password);
                        user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(MainActivity.this, "Authentication complete", Toast.LENGTH_SHORT).show();

                                    if (Integer.parseInt(dataSnapshot.child("id").getValue().toString()) == 1) {
                                        databaseRef.child("Protected member").child(mAuth.getCurrentUser().getUid()).removeValue();
                                    } else
                                        databaseRef.child("Rescue agent").child(mAuth.getCurrentUser().getUid()).removeValue();

                                    user.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
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
                                }
                            }
                        });
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                };

                if (!deleteRescueAgentAccount.isChecked()) {
                    databaseRef.child("Protected member").child(user.getUid()).addValueEventListener(valueEventListener);
                } else {
                    databaseRef.child("Rescue agent").child(user.getUid()).addValueEventListener(valueEventListener);
                }
                deleteAlertDialog.dismiss();
            }
        });
    }

    public void ClickLogout(View view) {
        mAuth.signOut();
        finish();
        startActivity(new Intent(this, Login.class));
    }
}