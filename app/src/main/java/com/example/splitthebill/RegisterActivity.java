package com.example.splitthebill;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {
    ProgressBar progressBar;
    TextView signIn;
    EditText rUsername, rEmail, rPassword, rConfPwd;
    String username, email, password;
    Button register;
    RequestQueue queue;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getSupportActionBar().hide();

        //if the user is already logged in we will directly start the MainActivity (profile) activity
        if (SharedPrefManager.getInstance(this).isLoggedIn()) {
            finish();
            startActivity(new Intent(this, HomeActivity.class));
            return;
        }

        queue = Volley.newRequestQueue(this);

        // Text Inputs
        rUsername = findViewById(R.id.rUsername);
        rEmail = findViewById(R.id.rEmail);
        rPassword = findViewById(R.id.rPassword);
        rConfPwd = findViewById(R.id.rConfirmPwd);

        // Progress Bar
        progressBar = findViewById(R.id.registerProgress);
        progressBar.setVisibility(View.GONE);

        // Clickables
        signIn = findViewById(R.id.goToSignIn);
        register = findViewById(R.id.register);

        // Listeners
        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = rEmail.getText().toString().trim();
                username = rUsername.getText().toString().trim();
                password = rPassword.getText().toString().trim();

                if(TextUtils.isEmpty(username)){
                    rUsername.setError("Username required");
                    rUsername.requestFocus();
                    return;
                }

                if(TextUtils.isEmpty(email)){
                    rEmail.setError("Email is required");
                    rEmail.requestFocus();
                    return;
                }

                if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    rEmail.setError("Enter a valid email");
                    rEmail.requestFocus();
                    return;
                }

                if(TextUtils.isEmpty(password)){
                    rPassword.setError("Password is required");
                    rPassword.requestFocus();
                    return;
                }

                if(password.length() < 6){
                    rPassword.setError("Password must be at least 6 characters long");
                    rPassword.requestFocus();
                    return;
                }

                if(!rPassword.getText().toString().trim().equals(rConfPwd.getText().toString().trim())){
                    rConfPwd.setError("Passwords do not match");
                    rConfPwd.requestFocus();
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);
                registerUser(email, username, password);
            }
        });


    }

    private void registerUser(String email, String username, String password) {
        JSONObject obj = new JSONObject();

        try {
            obj.put("username", username);
            obj.put("password", password);
            obj.put("email", email);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, URLs.URL_REGISTER, obj,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(getApplicationContext(), "Success!", Toast.LENGTH_SHORT).show();

                        progressBar.setVisibility(View.GONE);

                        //creating a new user object
                        try {
                            User user = new User(
                                    response.getString("token"),
                                    response.getString("type")
                            );

                            SharedPrefManager.getInstance(getApplicationContext()).userLogin(user);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        // finish();
                        startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                    }
                },
                new Response.ErrorListener() {
                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println(new String(error.networkResponse.data, StandardCharsets.UTF_8));
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {
        };

        queue.add(jsonObjectRequest);
    }
}
