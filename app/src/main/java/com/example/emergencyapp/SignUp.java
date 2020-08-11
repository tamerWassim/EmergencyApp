package com.example.emergencyapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.Toast;

import java.util.Calendar;

public class SignUp extends AppCompatActivity {

    NumberPicker day;
    NumberPicker month;
    NumberPicker year;

    Button sign;

    String val;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        sign = (Button) findViewById(R.id.sign);

        day = (NumberPicker) findViewById(R.id.day);
        day.setValue(1);
        day.setMinValue(1);
        day.setMaxValue(31);
        day.setWrapSelectorWheel(false);
        day.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                val = newVal + "/";
            }
        });

        month = (NumberPicker) findViewById(R.id.month);
        month.setValue(1);
        month.setMinValue(1);
        month.setMaxValue(12);
        month.setWrapSelectorWheel(false);
        month.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                val = newVal + "/";
            }
        });

        year = (NumberPicker) findViewById(R.id.year);
        year.setValue(2000);
        year.setMinValue(1940);
        year.setMaxValue(Calendar.getInstance().get(Calendar.YEAR));
        year.setWrapSelectorWheel(false);
        year.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                val = newVal + "";
            }
        });
        //val = day.getValue() + "";

        sign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast t = Toast.makeText(getBaseContext(), val, Toast.LENGTH_LONG);
                t.show();
            }
        });

    }
}
