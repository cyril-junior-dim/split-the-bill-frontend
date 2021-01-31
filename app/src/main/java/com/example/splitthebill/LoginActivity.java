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

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {
    EditText username, password;
    TextView signUp;
    TextView forgotPwd;
    ProgressBar progressBar;
    Button login;
    RequestQueue queue;

    private String email, pwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();

        if (SharedPrefManager.getInstance(this).isLoggedIn()) {
            finish();
            startActivity(new Intent(this, HomeActivity.class));
        }

        queue = Volley.newRequestQueue(this);

        // Input fields
        username = findViewById(R.id.emailAddress);
        password = findViewById(R.id.password);

        // Alternatives to login
        signUp = findViewById(R.id.goToSignUp);
        forgotPwd = findViewById(R.id.forgotPassword);

        // Confirm
        login = findViewById(R.id.login);

        // Progress bar
        progressBar = findViewById(R.id.loginProgressBar);
        progressBar.setVisibility(View.GONE);

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), RegisterActivity.class));            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = username.getText().toString().trim();
                pwd = password.getText().toString().trim();

                if(TextUtils.isEmpty(email)){
                    username.setError("Username or Email is required");
                    username.requestFocus();
                    return;
                }

                if(TextUtils.isEmpty(pwd)){
                    password.setError("Password is required");
                    password.requestFocus();
                    return;
                }
                userLogin();

                progressBar.setVisibility(View.VISIBLE);

            }
        });
    }

    private void userLogin() {
        JSONObject obj = new JSONObject();

        try {
            obj.put("password", email);
            obj.put("username", username);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, URLs.URL_LOGIN, obj,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        progressBar.setVisibility(View.GONE);

                        try {

                            //if no error in response
                            if (!response.getBoolean("error")) {
                                Toast.makeText(getApplicationContext(), response.getString("Success!"), Toast.LENGTH_SHORT).show();

                                //getting the user from the response
                                JSONObject userJson = response.getJSONObject("user");

                                //creating a new user object
                                User user = new User(
                                        userJson.getString("token"),
                                        userJson.getString("type")
                                );

                                //storing the user in shared preferences
                                SharedPrefManager.getInstance(getApplicationContext()).userLogin(user);
                                //starting the profile activity
                                finish();
                                startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                            } else {
                                Toast.makeText(getApplicationContext(), obj.getString("Failed"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                }) {
        };
        queue.add(jsonObjectRequest);
    }
}

