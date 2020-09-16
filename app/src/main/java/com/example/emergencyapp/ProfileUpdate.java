package com.example.emergencyapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ProfileUpdate extends AppCompatActivity {

    EditText firstName, lastName, phoneNumber, adress, pathologies, YearOfBirth;
    Spinner BloodType, Gender;
    Button Update;
    String FN, LN, PN, AD, PA, BT, GN, YOB;
    int age;
    int PM;

    FirebaseAuth mAuth;
    DatabaseReference databaseRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_update);


        firstName = findViewById(R.id.updateFN);
        lastName = findViewById(R.id.updateLN);
        phoneNumber = findViewById(R.id.updatePN);
        adress = findViewById(R.id.updateAD);
        pathologies = findViewById(R.id.updatePA);
        Update = findViewById(R.id.UpdateProfile);
        YearOfBirth = findViewById(R.id.updateYOB);
        BloodType = findViewById(R.id.updateBT);
        Gender = findViewById(R.id.updateGN);

        databaseRef = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        fillBloodTypeSpiner();

        Update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mAuth.getCurrentUser() != null) {
                    if (firstName.getText().toString().trim().isEmpty()) {
                        firstName.setError(getString(R.string.input_error_firstName));
                        firstName.requestFocus();
                        return;
                    }

                    if (lastName.getText().toString().trim().isEmpty()) {
                        lastName.setError(getString(R.string.input_error_lastName));
                        lastName.requestFocus();
                        return;
                    }
                    if (phoneNumber.getText().toString().trim().isEmpty()) {
                        phoneNumber.setError(getString(R.string.input_error_phone));
                        phoneNumber.requestFocus();
                        return;
                    }
                    if (phoneNumber.getText().toString().trim().length() != 10) {
                        phoneNumber.setError(getString(R.string.input_error_phone_invalid));
                        phoneNumber.requestFocus();
                        return;
                    }
                    if (pathologies.getText().toString().trim().isEmpty()) {
                        pathologies.setError("if you don't have pathologies inter NONE ");
                        pathologies.requestFocus();
                        return;
                    }
                    if (adress.getText().toString().trim().isEmpty()) {
                        adress.setError(getString(R.string.input_adress_error));
                        adress.requestFocus();
                        return;
                    }
                    if (YearOfBirth.getText().toString().trim().isEmpty()) {
                        YearOfBirth.setError("enter your year of birth");
                        YearOfBirth.requestFocus();
                        return;
                    }
                    if (Integer.parseInt(YearOfBirth.getText().toString()) < 1910) {
                        YearOfBirth.setError("How Old Are You !!!");
                        YearOfBirth.requestFocus();
                        return;
                    }
                    if (Integer.parseInt(YearOfBirth.getText().toString()) > Calendar.getInstance().get(Calendar.YEAR)) {
                        YearOfBirth.setError("are you from the Future !");
                        YearOfBirth.requestFocus();
                        return;
                    }


                    ValueEventListener valueEventListener = new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            FN = firstName.getText().toString();
                            LN = lastName.getText().toString();
                            PN = phoneNumber.getText().toString();
                            AD = adress.getText().toString();
                            PA = pathologies.getText().toString();
                            YOB = YearOfBirth.getText().toString();
                            age = Calendar.getInstance().get(Calendar.YEAR) - Integer.parseInt(YOB);
                            GN = Gender.getSelectedItem().toString();

                            final BloodGroups blood;
                            switch (String.valueOf(BloodType.getSelectedItem())) {
                                case "A+":
                                    blood = BloodGroups.A_POS;
                                    break;
                                case "A-":
                                    blood = BloodGroups.A_NEG;
                                    break;
                                case "B+":
                                    blood = BloodGroups.B_POS;
                                    break;
                                case "B-":
                                    blood = BloodGroups.B_NEG;
                                    break;
                                case "AB+":
                                    blood = BloodGroups.AB_POS;
                                    break;
                                case "AB-":
                                    blood = BloodGroups.AB_NEG;
                                    break;
                                case "O+":
                                    blood = BloodGroups.O_POS;
                                    break;
                                case "O-":
                                    blood = BloodGroups.O_NEG;
                                    break;
                                default:
                                    blood = BloodGroups.NONE;
                            }
                            if (dataSnapshot.exists()) {

                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                                    PM = Integer.parseInt(dataSnapshot.child("id").getValue() + "");
                                    if (PM == 1) {
                                        databaseRef.child("Protected member").child(mAuth.getCurrentUser().getUid()).child("firstName").setValue(FN);
                                        databaseRef.child("Protected member").child(mAuth.getCurrentUser().getUid()).child("lastName").setValue(LN);
                                        databaseRef.child("Protected member").child(mAuth.getCurrentUser().getUid()).child("phoneNumber").setValue(PN);
                                        databaseRef.child("Protected member").child(mAuth.getCurrentUser().getUid()).child("adress").setValue(AD);
                                        databaseRef.child("Protected member").child(mAuth.getCurrentUser().getUid()).child("pathologies").setValue(PA);
                                        databaseRef.child("Protected member").child(mAuth.getCurrentUser().getUid()).child("age").setValue(age);
                                        databaseRef.child("Protected member").child(mAuth.getCurrentUser().getUid()).child("bloodGroup").setValue(blood);
                                        databaseRef.child("Protected member").child(mAuth.getCurrentUser().getUid()).child("gender").setValue(GN);
                                    } else if (PM == 2)
                                        databaseRef.child("Rescue agent").child(mAuth.getCurrentUser().getUid()).child("firstName").setValue(FN);
                                    databaseRef.child("Rescue agent").child(mAuth.getCurrentUser().getUid()).child("lastName").setValue(LN);
                                    databaseRef.child("Rescue agent").child(mAuth.getCurrentUser().getUid()).child("phoneNumber").setValue(PN);
                                    databaseRef.child("Rescue agent").child(mAuth.getCurrentUser().getUid()).child("adress").setValue(AD);
                                    databaseRef.child("Rescue agent").child(mAuth.getCurrentUser().getUid()).child("pathologies").setValue(PA);
                                    databaseRef.child("Rescue agent").child(mAuth.getCurrentUser().getUid()).child("age").setValue(age);
                                    databaseRef.child("Rescue agent").child(mAuth.getCurrentUser().getUid()).child("bloodGroup").setValue(blood);
                                    databaseRef.child("Rescue agent").child(mAuth.getCurrentUser().getUid()).child("gender").setValue(GN);
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    };

                    databaseRef.child("Protected member").child(mAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(valueEventListener);
                    databaseRef.child("Rescue agent").child(mAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(valueEventListener);
                }

                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                Toast.makeText(getBaseContext(), "account update succefuly", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fillBloodTypeSpiner() {
        final List<String> spinnerArray = new ArrayList<String>();
        spinnerArray.add("A+");
        spinnerArray.add("A-");
        spinnerArray.add("B+");
        spinnerArray.add("B-");
        spinnerArray.add("AB+");
        spinnerArray.add("AB-");
        spinnerArray.add("O+");
        spinnerArray.add("O-");

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getBaseContext(), android.R.layout.simple_spinner_item, spinnerArray);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        BloodType = (Spinner) findViewById(R.id.updateBT);
        BloodType.setAdapter(adapter);
    }
}