package com.example.emergencyapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.snapshot.ChildKey;

import java.lang.invoke.ConstantCallSite;
import java.util.ArrayList;

public class profile extends AppCompatActivity {



    TextView firstName,lastName,gender,phoneNumber,adress,pathologies,age,bloodGroup;
    Button Edit ;

    int PM ;



    FirebaseAuth mAuth;
    DatabaseReference databaseRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);


        firstName = findViewById(R.id.FN);
        lastName = findViewById(R.id.LN);
        gender = findViewById(R.id.GE);
        phoneNumber = findViewById(R.id.PN);
        adress = findViewById(R.id.AD);
        pathologies = findViewById(R.id.PA);
        age = findViewById(R.id.AG);
        bloodGroup = findViewById(R.id.BG);
        Edit = findViewById(R.id.EditProfile);


         databaseRef = FirebaseDatabase.getInstance().getReference();
         mAuth = FirebaseAuth.getInstance();

         if (mAuth.getCurrentUser() != null) {


            ValueEventListener valueEventListener = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    if (dataSnapshot.exists()) {
                        final ValueEventListener valueEventListener2 = new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                                firstName.setText(dataSnapshot.child("firstName").getValue().toString());
                                lastName.setText(dataSnapshot.child("lastName").getValue().toString());
                                gender.setText(dataSnapshot.child("gender").getValue().toString());
                                phoneNumber.setText(dataSnapshot.child("phoneNumber").getValue().toString());
//                    str += "E-mail: " + dataSnapshot.child("email").getValue() + "\n";
                                adress.setText(dataSnapshot.child("adress").getValue().toString());

                                pathologies.setText(dataSnapshot.child("pathologies").getValue().toString());
                                age.setText(dataSnapshot.child("age").getValue().toString());
                                bloodGroup.setText(dataSnapshot.child("bloodGroup").getValue().toString());

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        };
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                                PM = Integer.parseInt(dataSnapshot.child("id").getValue() + "");
                                if (PM == 1) {
                                    databaseRef.child("Protected member").child(mAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(valueEventListener2);
                                }else if(PM == 2) databaseRef.child("Rescue agent").child(mAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(valueEventListener2);

                            // RA = Integer.parseInt(dataSnapshot.child("id").getValue() + "");
//                            path.setText(t);

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


         Edit.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {



             }
         });

//        //textView.setText(databaseRef.child(mAuth.getCurrentUser().getUid()).child("email").+"");
//        //textView.setText(mAuth.getCurrentUser().getUid());
//
//        //databaseRef.child(databaseRef.child(mAuth.getCurrentUser().getUid()).getKey()).getParent();
//
//
////        if (mAuth.getCurrentUser() != null){
////
////            databaseRef.child("Protected member").child(mAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
////                @Override
////                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
////
////                   String str = dataSnapshot.child("email").getValue()+"  ";
////
////                   // String parent = dataSnapshot.getKey(String.class);
////
//////                     if(dataSnapshot.child("rescueType").getValue().equals(null)){
//////
//////
//////                      }
//////                     str+= ;
////
////                    textView.setText(str);
////                  //  databaseRef.child("Protected member").child(mAuth.getCurrentUser().getUid()).removeValue();
////
////                    //deleteDatabase(dataSnapshot.toString());
////                }
////
////                @Override
////                public void onCancelled(@NonNull DatabaseError databaseError) {
////
////                }
////
////            });
//
//
//        ValueEventListener valueEventListener = new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                if (dataSnapshot.exists()) {
////                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
//                        ArrayList<String> tamer = new ArrayList<>();
//                         tamer.add(dataSnapshot.getValue().toString() );
//
//                        textView.setText(tamer+"  ");
//                    }
//                }
////            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        };
////
////
//        Query query = databaseRef.child("Protected member").orderByChild("email").equalTo("tamer@gmail.com");
//
//        query.addValueEventListener(valueEventListener);

    }
}




