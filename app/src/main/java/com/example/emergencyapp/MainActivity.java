package com.example.emergencyapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private TextView data;
    private Button help;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        help= findViewById(R.id.emergency);
        data = (TextView) findViewById(R.id.data);
        data.setText(getIntent().getStringExtra("un").toString() + "\n" +
                getIntent().getStringExtra("pw").toString());
    }

}
