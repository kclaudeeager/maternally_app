package com.example.navigationdrawerapp;

import static com.example.navigationdrawerapp.Urls.PARENT_URL;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class LoginActivity extends AppCompatActivity {
  EditText usernametxt;
  EditText passwordtxt;
  Button loginBtn;
  ProgressBar progressBar;
    Map<String,Object> UserInfo;
    String token;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        usernametxt=findViewById(R.id.email);
        passwordtxt=findViewById(R.id.password);
        loginBtn=findViewById(R.id.login);
        progressBar=findViewById(R.id.progress);


        loginBtn.setOnClickListener(view -> loginUser(usernametxt.getText().toString(),passwordtxt.getText().toString()));

    }

    private void loginUser(String email, String password) {
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please enter email and password", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            usernametxt.setError("Invalid email address");
            usernametxt.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        String url = PARENT_URL+"User/login";

        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("email", email);
            jsonBody.put("password", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonBody,
                response -> {
                    progressBar.setVisibility(View.INVISIBLE);
                    System.out.println("Response: "+response);
                    try {
                        UserInfo = new HashMap<>();
                        Iterator<String> keys = response.keys();
                        while (keys.hasNext()) {
                            String key = keys.next();
                            UserInfo.put(key, response.get(key));
                        }
                        token = Objects.requireNonNull(UserInfo.get("token")).toString();
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        intent.putExtra("token", token);
                        intent.putExtra("User", Objects.requireNonNull(UserInfo.get("User")).toString());
                        startActivity(intent);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }, error -> {
                   progressBar.setVisibility(View.INVISIBLE);
                    if (error instanceof NoConnectionError) {
                        Toast.makeText(getApplicationContext(), "Please check your internet connection", Toast.LENGTH_LONG).show();
                    } else if (error instanceof TimeoutError) {
                        Toast.makeText(getApplicationContext(), "Request Timeout. Please try again.", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "Server Error. Please try again.", Toast.LENGTH_LONG).show();
                    }
                     parseVolleyError(error);
                }
                );
        Volley.newRequestQueue(this).add(jsonObjectRequest);

    }

    public static List<Object> toList(JSONArray array) throws JSONException {
        List<Object> list = new ArrayList<Object>();
        for (int i = 0; i < array.length(); i++) {
            Object value = array.get(i);
            if (value instanceof JSONArray) {
                value = toList((JSONArray) value);
            } else if (value instanceof JSONObject) {
                value = toMap((JSONObject) value);
            }
            list.add(value);
        }
        return list;
    }
    public static Map<String, Object> toMap(JSONObject jsonobj)  throws JSONException {
        Map<String, Object> map = new HashMap<String, Object>();
        Iterator<String> keys = jsonobj.keys();
        while(keys.hasNext()) {
            String key = keys.next();
            Object value = jsonobj.get(key);
            if (value instanceof JSONArray) {
                value = toList((JSONArray) value);
            }
            else if (value instanceof JSONObject) {
                value = toMap((JSONObject) value);
            }
            map.put(key, value);
        }   return map;
    }
    public void parseVolleyError(VolleyError error){
        try {
            String responseBody=new String(error.networkResponse.data,"utf-8");
            JSONObject data=new JSONObject(responseBody);
            Log.i("ErrorRe: ",responseBody);
            String message=data.getString("message");
            Log.d("Status: ",data.getString("status"));
            if(data.getString("status").equalsIgnoreCase("500") && message.equalsIgnoreCase("")){
                message="Invalid email or password";
            }
            Log.i("Error: ",message);
            Toast.makeText(getApplicationContext(),"Error: "+message,Toast.LENGTH_LONG).show();
        } catch (UnsupportedEncodingException | JSONException | NullPointerException e) {
            e.printStackTrace();
        }
    }

}