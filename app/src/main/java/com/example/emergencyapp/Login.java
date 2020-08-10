package com.example.emergencyapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Login extends AppCompatActivity {

    private TextView userName, password;
    private Button signUp, login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        userName = (TextView) findViewById(R.id.user_name);
        password = (TextView) findViewById(R.id.password);
        signUp = (Button) findViewById(R.id.sign_up);
        login = (Button) findViewById(R.id.login);

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, SignUp.class);
                startActivity(intent);

            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = userName.getText().toString();
                String pWord = password.getText().toString();

                Intent intent = new Intent(getBaseContext(), MainActivity.class);
                intent.putExtra("un", username);
                intent.putExtra("pw", pWord);
                startActivity(intent);


            }
        });
    }
}