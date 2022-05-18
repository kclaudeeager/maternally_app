package com.example.navigationdrawerapp;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpResponse;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.navigationdrawerapp.ui.home.HomeFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                loginUser(usernametxt.getText().toString(),passwordtxt.getText().toString());
            }
        });

    }
    private void loginUser(String email, String password){
        progressBar.setVisibility(View.VISIBLE);
        Login(email,password);
//        progressBar.setVisibility(View.VISIBLE);
//        String realPhone="0789728209";
//        String pass="123";
//        if(realPhone.equals(phone)&& pass.equals(password)){
//            Intent mainActivityIntent=new Intent(LoginActivity.this,MainActivity.class);
//            mainActivityIntent.putExtra("phone",phone);
//            startActivity(mainActivityIntent);
//            progressBar.setVisibility(View.GONE);
//           // System.out.println(new StringBuilder().append("User found: ").append(getDecodedJwt("eyJhbGciOiJIUzI1NiJ9.eyJpYXQiOjE2NTI4NzIzNDcsImV4cCI6MTY1Mjg3NTk0NywidXNlcklkIjoyLCJlbWFpbCI6Im11aG9yYWtleWVAZ21haWwuY29tIiwiZmlyc3ROYW1lIjoiTXVob3Jha2V5ZSIsImxhc3ROYW1lIjoiSGVyIGxhc3ROYW1lNCIsInJvbGUiOiJOT05FIn0.BGEfHioRGJs7-JfxxLRH5qVUXSTzVvBhRpEcTicK4co")).toString());
//            finish();
//        }
//        else {
//            Toast.makeText(getApplicationContext(),"Invalid username or password",Toast.LENGTH_SHORT);
//            System.out.println("Invalid username or password");
//            progressBar.setVisibility(View.INVISIBLE);
//            return;
//        }


    }

    synchronized private void Login(String email,String password){
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(true);
        progressDialog.setMessage("Please wait for loading....");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();

        new Thread() {
            public void run() {
                try {
                    RequestQueue requestQueue = Volley.newRequestQueue(LoginActivity.this);
                    int method=1;
                    String url = "https://maternally-health-backend.herokuapp.com/api/v1/User/login";

                    JSONObject js = new JSONObject();
                    try {
                        js.put("email",email);
                        js.put("password",password);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    // Make request for JSONObject
                    JsonObjectRequest jsonObjReq = new JsonObjectRequest(
                            method, url, js,
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {

                                    Log.i("Response: ",response.toString());
                                    UserInfo=new HashMap<>();
                                    try {
                                        if(!response.has("error")) {
                                            Toast.makeText(LoginActivity.this, " Well Logedin",Toast.LENGTH_LONG).show();
                                            UserInfo = toMap(response);
                                            token = UserInfo.get("token").toString();
                                           Log.i("User details:", UserInfo.get("User").toString());
                                            JSONObject userJson = new JSONObject(response.get("User").toString());
                                            Log.i("userJson:", userJson.toString());
                                             Object user=(UserInfo.get("User")).getClass();
                                            Log.d("token: ", token);
                                            usernametxt.setText("");
                                            passwordtxt.setText("");
                                            //String role = userJson.get("role").toString();
                                            //Log.i("Role ", role);
//                                            switch (role) {
//
//                                                case "MOTHER": {
                                                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                                    intent.putExtra("token", token);
                                                    intent.putExtra("User",response.get("User").toString());
                                                     intent.putExtra("phone",password);
                                                    startActivity(intent);
//                                                }
//                                                break;
//                                                default:{
//                                                    Intent intent = new Intent(LoginActivity.this, HomeFragment.class);
//                                                    intent.putExtra("UserInfo", response.toString());

                                                    startActivity(intent);
//
//                                                }
//                                                break;
//
//                                            }
                                        }
                                        else {
                                            String message=response.getString("error");
                                            Toast.makeText(getApplicationContext(),"Error: "+message,Toast.LENGTH_LONG).show();
                                            usernametxt.setText("");
                                            passwordtxt.setText("");
                                            progressBar.setVisibility(View.GONE);
                                        }
                                        progressDialog.dismiss();
                                        progressBar.setVisibility(View.GONE);

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            if(error!=null) {
                                System.out.println("Error occured : "+error);
                                parseVollyError(error);
                            }
                            usernametxt.setText("");
                            passwordtxt.setText("");
                            progressBar.setVisibility(View.GONE);
                            progressDialog.dismiss();
                        }
                    }) {

                        /**
                         * Passing some request headers
                         */
                        @Override
                        public Map<String, String> getHeaders() throws AuthFailureError {
                            HashMap<String, String> headers = new HashMap<String, String>();
                            headers.put("Content-Type", "application/json; charset=utf-8");
                            return headers;
                        }

                    };

                    // Adding request to request queue
                    Volley.newRequestQueue(LoginActivity.this).add(jsonObjReq).setRetryPolicy(new DefaultRetryPolicy(0,-1,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT))
                            .setShouldCache(false);

                }

                catch (Exception e) {
                    Log.e("tag", ""+e.getMessage());
                }
                // dismiss the progress dialog


                // finish();
            }
        }.start();

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
            } else if (value instanceof JSONObject) {
                value = toMap((JSONObject) value);
            }
            map.put(key, value);
        }   return map;
    }
    public void parseVollyError(VolleyError error){
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
        } catch (UnsupportedEncodingException | JSONException e) {
            e.printStackTrace();
        }
        catch (NullPointerException nullPointerException){
            nullPointerException.printStackTrace();
        }
    }
}