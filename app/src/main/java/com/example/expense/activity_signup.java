package com.example.expense;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class activity_signup extends AppCompatActivity {
    private EditText email;
    private EditText password;
    private EditText retypePassword;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        EditText firstName = findViewById(R.id.signInFirstname);
        EditText lastName = findViewById(R.id.signInLastName);
        email = findViewById(R.id.signInAddress);
        password = findViewById(R.id.signInPassword);
        retypePassword = findViewById(R.id.retypePassword);
        Button registerButton = findViewById(R.id.btn_register);
        TextView loginButton = findViewById(R.id.loginNow);

        mAuth = FirebaseAuth.getInstance();

        registerButton.setOnClickListener(v -> {
            String emailText = email.getText().toString().trim();
            String passwordText = password.getText().toString().trim();
            String retypePasswordText = retypePassword.getText().toString().trim();

            if (TextUtils.isEmpty(emailText) || TextUtils.isEmpty(passwordText)) {
                Toast.makeText(activity_signup.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }
            if (!passwordText.equals(retypePasswordText)) {
                Toast.makeText(activity_signup.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                return;
            }

            mAuth.createUserWithEmailAndPassword(emailText, passwordText)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(activity_signup.this, "Registration Successful!", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(activity_signup.this, LoginActivity.class));
                            finish();
                        } else {
                            Toast.makeText(activity_signup.this, "Registration Failed: " + Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        });

        loginButton.setOnClickListener(v -> {
            startActivity(new Intent(activity_signup.this, LoginActivity.class));
        });
    }
}
