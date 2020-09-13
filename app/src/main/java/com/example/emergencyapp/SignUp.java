package com.example.emergencyapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
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

public class SignUp extends AppCompatActivity implements View.OnClickListener {

    FirebaseDatabase database;
    DatabaseReference databaseRef;
    FirebaseAuth mAuth;

    //mutual fields
    EditText firstName, lastName, phoneNumber, password, Email;
    Spinner genderSpinner;
    EditText adress, pathologies, yearOfbirth;
    Spinner bloodGroupSpinner;

    //protected member fields
    ProtectedMember protectedMember;

    //rescue agent fields
    RescueAgent rescueAgent;
    Spinner wilayaSpinner, dayraSpinner, rescueTypeSpinner;

    Button sign , backToLogIn;
    CheckBox rescueAgentVelonteer;
    LinearLayout moreInfo;
    ProgressBar progressBar;
    final List<String> spinnerArraywilaya = new ArrayList<String>();
    final List<String> spinnerArraydayra = new ArrayList<String>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        database = FirebaseDatabase.getInstance();
        databaseRef = database.getReference();
        mAuth = FirebaseAuth.getInstance();

        firstName = (EditText) findViewById(R.id.first_name);
        lastName = (EditText) findViewById(R.id.last_name);
        phoneNumber = findViewById(R.id.phone_number);
        Email = findViewById(R.id.Email);
        password = (EditText) findViewById(R.id.pass_word);
        genderSpinner = findViewById(R.id.gender_spinner);
        adress = findViewById(R.id.adress);
        pathologies = findViewById(R.id.pathologies);
        yearOfbirth = findViewById(R.id.yearOfBirth);
        bloodGroupSpinner = findViewById(R.id.blood_group_spinner);
        fillBloodGroupSpiner();

        moreInfo = findViewById(R.id.more_informations);

        rescueAgentVelonteer = findViewById(R.id.rescue_agent);
        rescueAgentVelonteer.setChecked(false);

        sign = findViewById(R.id.sign);
        backToLogIn = findViewById(R.id.back_to_login);
        backToLogIn.setOnClickListener(this);
        findViewById(R.id.sign).setOnClickListener(this);
        progressBar = findViewById(R.id.progressbar);
        progressBar.setVisibility(View.GONE);

        rescueAgentVelonteer.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (rescueAgentVelonteer.isChecked()) {
                    LayoutInflater inflater = (LayoutInflater) getBaseContext().getSystemService(getBaseContext().LAYOUT_INFLATER_SERVICE);
                    View view = inflater.inflate(R.layout.rescue_agent_informations, null);
                    moreInfo.addView(view);

                    wilayaSpinner = findViewById(R.id.wilaya);
                    dayraSpinner = findViewById(R.id.dayra);
                    rescueTypeSpinner = findViewById(R.id.rescue_type);

                    fillRescueTypes();
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
                } else {
                    moreInfo.removeAllViews();
                }
            }
        });
    }


    private void createNewMember() {

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


        if (!Patterns.EMAIL_ADDRESS.matcher(Email.getText().toString().trim()).matches()) {
            Email.setError(getString(R.string.input_error_email_invalid));
            Email.requestFocus();
            return;
        }

        if (password.getText().toString().trim().isEmpty()) {
            password.setError(getString(R.string.input_error_password));
            password.requestFocus();
            return;
        }

        if (password.getText().toString().trim().length() < 6) {
            password.setError(getString(R.string.input_error_password_length));
            password.requestFocus();
            return;
        }

        if (adress.getText().toString().trim().isEmpty()) {
            adress.setError(getString(R.string.input_adress_error));
            adress.requestFocus();
            return;
        }

        if (yearOfbirth.getText().toString().trim().isEmpty()) {
            yearOfbirth.setError("enter your year of birth");
            yearOfbirth.requestFocus();
            return;
        }
        if (Integer.parseInt(yearOfbirth.getText().toString())<1910) {
            yearOfbirth.setError("How Old Are You !!!");
            yearOfbirth.requestFocus();
            return;
        }
        if (Integer.parseInt(yearOfbirth.getText().toString()) > Calendar.getInstance().get(Calendar.YEAR)) {
            yearOfbirth.setError("are you from the Future !");
            yearOfbirth.requestFocus();
            return;
        }
        if (pathologies.getText().toString().trim().isEmpty()) {
            pathologies.setError("if you don't have pathologies inter NONE ");
            pathologies.requestFocus();
            return;
        }



        final BloodGroups blood;
        switch (String.valueOf(bloodGroupSpinner.getSelectedItem())) {
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
        final int age = Calendar.getInstance().get(Calendar.YEAR)  - Integer.parseInt(yearOfbirth.getText()+"") ;
        progressBar.setVisibility(View.VISIBLE);
        if (!rescueAgentVelonteer.isChecked()) {
            mAuth.createUserWithEmailAndPassword(Email.getText() + "", password.getText() + "")
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                protectedMember = new ProtectedMember(
                                        firstName.getText() + "",
                                        lastName.getText() + "",
                                        genderSpinner.getSelectedItem() + "",
                                        adress.getText() + "",
                                        Integer.parseInt(age+"") ,
                                        Integer.parseInt(phoneNumber.getText() + ""),
                                        Email.getText() + "",
                                        password.getText() + "",
                                        pathologies.getText() + "",
                                        blood);
                               // Toast.makeText(getBaseContext(), protectedMember.toString(), Toast.LENGTH_SHORT).show();

                                databaseRef.child("Protected member")
                                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                        .setValue(protectedMember).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        progressBar.setVisibility(View.GONE);
                                        if (task.isSuccessful()) {
                                            Toast.makeText(SignUp.this, "register success", Toast.LENGTH_LONG).show();
                                            finish();
                                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                            startActivity(intent);
                                        } else {
                                            if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                                                //display a failure message
                                                Toast.makeText(getApplicationContext(), "You are already registered", Toast.LENGTH_SHORT).show();

                                            } else {
                                                Toast.makeText(SignUp.this, "register failed", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    }


                                });

                            } else {
                                Toast.makeText(SignUp.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        } else { //volenteer as rescue agent
            mAuth.createUserWithEmailAndPassword(Email.getText() + "", password.getText() + "")
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                final RescueTypes rescueType;
                                switch (String.valueOf(rescueTypeSpinner.getSelectedItem())) {
                                    case "Volunteer":
                                        rescueType = RescueTypes.VOLUNTEER;
                                        break;
                                    case "SOS Agent":
                                        rescueType = RescueTypes.SOS_AGENT;
                                        break;
                                    case "Medical Field":
                                        rescueType = RescueTypes.MEDICAL_FIELD;
                                        break;
                                    case "Fireman":
                                        rescueType = RescueTypes.FIREMAN;
                                        break;
                                    case "Policeman":
                                        rescueType = RescueTypes.POLICEMAN;
                                        break;
                                    case "Gendarm":
                                        rescueType = RescueTypes.GENDARM;
                                        break;
                                    default:
                                        rescueType = RescueTypes.VOLUNTEER;
                                }

                                rescueAgent = new RescueAgent(
                                        firstName.getText() + "",
                                        lastName.getText() + "",
                                        genderSpinner.getSelectedItem() + "",
                                        adress.getText() + "",
                                        Integer.parseInt(age+""),
                                        Integer.parseInt(phoneNumber.getText() + ""),
                                        Email.getText() + "",
                                        password.getText() + "",
                                        pathologies.getText() + "",
                                        blood,
                                        wilayaSpinner.getSelectedItem() + "",
                                        dayraSpinner.getSelectedItem() + "",
                                        rescueType);
                              //  Toast.makeText(getBaseContext(), rescueAgent.toString(), Toast.LENGTH_LONG).show();

                                databaseRef.child("Rescue agent")
                                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                        .setValue(rescueAgent).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        progressBar.setVisibility(View.GONE);
                                        if (task.isSuccessful()) {
                                            Toast.makeText(SignUp.this, "register success", Toast.LENGTH_LONG).show();
                                            finish();
                                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                            startActivity(intent);
                                        } else {
                                            if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                                                //display a failure message
                                                Toast.makeText(getApplicationContext(), "You are already registered", Toast.LENGTH_SHORT).show();

                                            } else {
                                                Toast.makeText(SignUp.this, "register failed", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    }


                                });

                            } else {
                                Toast.makeText(SignUp.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }



    private void fillBloodGroupSpiner() {

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
        bloodGroupSpinner = (Spinner) findViewById(R.id.blood_group_spinner);
        bloodGroupSpinner.setAdapter(adapter);
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

    public  void fillRescueTypes() {
        List<String> spinnerArray = new ArrayList<String>();

        spinnerArray.add("Volunteer");
        spinnerArray.add("SOS Agent");
        spinnerArray.add("Medical Field");
        spinnerArray.add("Fireman");
        spinnerArray.add("Policeman");
        spinnerArray.add("Gendarm");

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getBaseContext(), android.R.layout.simple_spinner_item, spinnerArray);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        rescueTypeSpinner = (Spinner) findViewById(R.id.rescue_type);
        rescueTypeSpinner.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sign:
                createNewMember();
                break;
            case R.id.back_to_login :
                startActivity(new Intent(getApplicationContext(),Login.class));
                finish();
        }

    }

}
