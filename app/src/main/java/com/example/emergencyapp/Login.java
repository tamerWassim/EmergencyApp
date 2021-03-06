package com.example.emergencyapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity implements View.OnClickListener {

    FirebaseAuth mAuth;
    ProgressBar progressBar;
    Button signUp, login;
    private EditText userEmail, userPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        userEmail = findViewById(R.id.user_email);
        userPassword = findViewById(R.id.password);
        signUp = (Button) findViewById(R.id.sign_up);
        login = (Button) findViewById(R.id.login);

        findViewById(R.id.login).setOnClickListener(this);
        findViewById(R.id.sign_up).setOnClickListener(this);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);

        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mAuth.getCurrentUser() != null) {
            finish();
            startActivity(new Intent(Login.this, MainActivity.class));
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.sign_up:
                startActivity(new Intent(Login.this, SignUp.class));
                break;
            case R.id.login:
                userLogin();
                break;
        }
    }

    private void userLogin() {
        if (userEmail.getText().toString().trim().isEmpty()) {
            userEmail.setError("Email is required");
            userEmail.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(userEmail.getText().toString().trim()).matches()) {
            userEmail.setError("Please enter a valid email");
            userEmail.requestFocus();
            return;
        }

        if (userPassword.getText().toString().trim().isEmpty()) {
            userPassword.setError("Password is required");
            userPassword.requestFocus();
            return;
        }

        if (userPassword.getText().toString().trim().length() < 6) {
            userPassword.setError("Minimum lenght of password should be 6");
            userPassword.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        mAuth.signInWithEmailAndPassword(userEmail.getText().toString().trim(), userPassword.getText().toString().trim()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    finish();
                    Intent intent = new Intent(Login.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    Toast.makeText(getApplicationContext(), "your loged in perfect", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                }
                progressBar.setVisibility(View.GONE);
            }

        });
    }
}
