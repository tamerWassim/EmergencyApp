package com.example.emergencyapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;

public class SignUp extends AppCompatActivity {

    FirebaseDatabase database;
    DatabaseReference databaseRef;

    //mutual fields
    EditText firstName, lastName, phoneNumber, password;
    Spinner genderSpinner;

    //protected member fields
    ProtectedMember protectedMember;
    EditText adress, pathologies, yearOfBith;
    Spinner bloodGroupSpinner;

    //rescue agent fields
    Spinner wilayaSpinner, dayraSpinner, rescueTypeSpinner;

    Button sign;
    RadioButton rescueAgent;
    RadioButton protectedMemberRadioButton;

    LinearLayout moreInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        database = FirebaseDatabase.getInstance();
        databaseRef = database.getReference();

        firstName = (EditText) findViewById(R.id.first_name);
        lastName = (EditText) findViewById(R.id.last_name);
        phoneNumber = findViewById(R.id.phone_number);
        password = (EditText) findViewById(R.id.pass_word);
        genderSpinner = findViewById(R.id.gender_spinner);

        sign = findViewById(R.id.sign);

        moreInfo = findViewById(R.id.more_informations);

        rescueAgent = findViewById(R.id.rescue_agent);
        rescueAgent.setChecked(false);

        protectedMemberRadioButton = findViewById(R.id.protected_member);
        protectedMemberRadioButton.setChecked(true);

        moreInfo.removeAllViews();
        LayoutInflater inflater = (LayoutInflater) getBaseContext().getSystemService(getBaseContext().LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.protcted_member_informations, null);

        moreInfo.addView(view);
        fillBloodGroupSpiner();

        adress = (EditText) findViewById(R.id.adress);
        pathologies = (EditText) findViewById(R.id.pathologies);
        yearOfBith = (EditText) findViewById(R.id.year_of_birth);
        bloodGroupSpinner = findViewById(R.id.blood_group_spinner);;


        rescueAgent.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (rescueAgent.isChecked()){
                    moreInfo.removeAllViews();
                    LayoutInflater inflater = (LayoutInflater) getBaseContext().getSystemService(getBaseContext().LAYOUT_INFLATER_SERVICE);
                    View view = inflater.inflate(R.layout.rescue_agent_informations, null);
                    moreInfo.addView(view);

                    adress = (EditText) findViewById(R.id.adress);
                    pathologies = (EditText) findViewById(R.id.pathologies);
                    yearOfBith = (EditText) findViewById(R.id.year_of_birth);
                    bloodGroupSpinner = findViewById(R.id.blood_group_spinner);

                    wilayaSpinner = findViewById(R.id.wilaya);
                    dayraSpinner = findViewById(R.id.dayra);
                    rescueTypeSpinner = findViewById(R.id.rescue_type);

                    fillRescueTypes();
                    fillWilaya();
                    fillDayra();
                }
            }
        });

        protectedMemberRadioButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (protectedMemberRadioButton.isChecked()){
                    moreInfo.removeAllViews();
                    LayoutInflater inflater = (LayoutInflater) getBaseContext().getSystemService(getBaseContext().LAYOUT_INFLATER_SERVICE);
                    View view = inflater.inflate(R.layout.protcted_member_informations, null);
                    moreInfo.addView(view);
                    fillBloodGroupSpiner();

                    firstName = (EditText) findViewById(R.id.first_name);
                    lastName = (EditText) findViewById(R.id.last_name);
                    phoneNumber = findViewById(R.id.phone_number);
                    password = (EditText) findViewById(R.id.pass_word);
                }
            }
        });

        sign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createNewMember();
            }
        });

    }

    private void  createNewMember(){
        if (protectedMemberRadioButton.isChecked()){

            BloodGroups blood;

            switch (String.valueOf(bloodGroupSpinner.getSelectedItem())){
                case "A+" : blood = BloodGroups.A_POS; break;
                case "A-" : blood = BloodGroups.A_NEG; break;
                case "B+" : blood = BloodGroups.B_POS; break;
                case "B-" : blood = BloodGroups.B_NEG; break;
                case "AB+" : blood = BloodGroups.AB_POS; break;
                case "AB-" : blood = BloodGroups.AB_NEG; break;
                case "O+" : blood = BloodGroups.O_POS; break;
                case "O-" : blood = BloodGroups.O_NEG; break;
                default: blood = BloodGroups.NONE;
            }

             protectedMember = new ProtectedMember(
                    firstName.getText() + "",
                    lastName.getText() + "",
                    genderSpinner.getSelectedItem() + "",
                    adress.getText() + "",
                     Integer.parseInt(yearOfBith.getText() + ""),
                     Integer.parseInt(phoneNumber.getText() + ""),
                    password.getText() + "",
                    pathologies.getText() + "",
                    blood);

            databaseRef.child("Protected member").push().setValue(protectedMember);

            Toast.makeText(getBaseContext(), protectedMember.toString(), Toast.LENGTH_LONG).show();
        }
    }

    private void fillBloodGroupSpiner(){

        List<String> spinnerArray =  new ArrayList<String>();
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
        bloodGroupSpinner = (Spinner) findViewById(R.id.blood_group_spinner);
        bloodGroupSpinner.setAdapter(adapter);
    }

    private void fillWilaya(){
        final List<String> spinnerArray =  new ArrayList<String>();

        databaseRef.child("wilaya").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    spinnerArray.add(ds.getKey());
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getBaseContext(), android.R.layout.simple_spinner_item, spinnerArray);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        wilayaSpinner = (Spinner) findViewById(R.id.wilaya);
        wilayaSpinner.setAdapter(adapter);
    }

    private void fillDayra(){
    }

    private void fillRescueTypes(){
        List<String> spinnerArray =  new ArrayList<String>();

        spinnerArray.add("Volunteer");
        spinnerArray.add("SOS Agent");
        spinnerArray.add("Doctor");
        spinnerArray.add("Nurse");
        spinnerArray.add("Fireman");
        spinnerArray.add("Policeman");
        spinnerArray.add("Gendarm");

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getBaseContext(), android.R.layout.simple_spinner_item, spinnerArray);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        rescueTypeSpinner = (Spinner) findViewById(R.id.rescue_type);
        rescueTypeSpinner.setAdapter(adapter);
    }
}
