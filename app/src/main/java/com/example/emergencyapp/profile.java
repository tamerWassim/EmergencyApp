package com.example.emergencyapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class profile extends AppCompatActivity {

    TextView textView;

    EditText editText;



    FirebaseAuth mAuth;
    DatabaseReference databaseRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);



        textView = (TextView) findViewById(R.id.profile);

        databaseRef = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();

        if (mAuth.getCurrentUser() != null){
            databaseRef.child("Protected member").child(mAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    String str = "";
                    str += "first name: " + dataSnapshot.child("firstName").getValue() + "\n";
                    str += "last name: " + dataSnapshot.child("lastName").getValue() + "\n";
                    str += "gender: " + dataSnapshot.child("gender").getValue() + "\n";
                    str += "phone number: " + dataSnapshot.child("phoneNumber").getValue() + "\n";
                    str += "E-mail: " + dataSnapshot.child("email").getValue() + "\n";
                    str += "adress: " + dataSnapshot.child("adress").getValue() + "\n";
                    str += "pathologies: " + dataSnapshot.child("pathologies").getValue() + "\n";
                    str += "age: " + dataSnapshot.child("yearOfBith").getValue() + "\n";
                    str += "blood group: " + dataSnapshot.child("bloodGroup").getValue() + "\n";

                    textView.setText(str);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }



}

