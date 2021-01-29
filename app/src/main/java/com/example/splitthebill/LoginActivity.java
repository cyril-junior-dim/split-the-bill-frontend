package com.example.splitthebill;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

public class LoginActivity extends AppCompatActivity {
    EditText username, password;
    TextView signUp;
    TextView forgotPwd;
    ProgressBar progressBar;
    Button login;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();

        if (SharedPrefManager.getInstance(this).isLoggedIn()) {
            finish();
            startActivity(new Intent(this, HomeActivity.class));
        }

        progressBar = findViewById(R.id.loginProgressBar);
        username = findViewById(R.id.emailAddress);
        password = findViewById(R.id.password);
        signUp = findViewById(R.id.goToSignUp);
        forgotPwd = findViewById(R.id.forgotPassword);
        login = findViewById(R.id.login);

        progressBar.setVisibility(View.GONE);

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), RegisterActivity.class));            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = username.getText().toString().trim();
                String pwd = password.getText().toString().trim();

                if(TextUtils.isEmpty(email)){
                    username.setError("Username or Email is required");
                    return;
                }

                if(TextUtils.isEmpty(pwd)){
                    password.setError("Password is required");
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);

            }
        });

    }
}