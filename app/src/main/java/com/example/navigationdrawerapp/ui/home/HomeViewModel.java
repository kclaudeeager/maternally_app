package com.example.navigationdrawerapp.ui.home;

import static com.example.navigationdrawerapp.MainActivity.token;
import static com.example.navigationdrawerapp.Urls.PARENT_URL;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.navigationdrawerapp.ui.kids.Callback;

import java.util.HashMap;
import java.util.Map;

public class HomeViewModel extends ViewModel {

    private final MutableLiveData<String> mText;
    private final MutableLiveData<String> childText;
    private final MutableLiveData<String> tipsText;
    public HomeViewModel() {
        mText = new MutableLiveData<>();
        childText=new MutableLiveData<>();
        tipsText=new MutableLiveData<>();
        //mText.setValue("This is home fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
    public LiveData<String> getChildNumber(){
        return childText;
    }
    public LiveData<String> getTipsNumber(){
        return tipsText;
    }

 public void retrieveBabiesData(int motherId, final String token,Context context) {
        RequestQueue queue = Volley.newRequestQueue(context);
        String url = "http://197.243.25.120:8080/api/v1/Baby/mother/" + motherId;

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET, url, null,
                response -> {childText.setValue(""+response.length());
                },
                error ->{

                    Log.e("VolleyError", error.toString());
                }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + token);
                return headers;
            }
        };

        queue.add(jsonArrayRequest);
    }


    public void   getTipsLength(Context context,final String token) {
        RequestQueue queue = Volley.newRequestQueue(context);
         final String url = PARENT_URL + "HealthTips/all";
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET, url, null,
                response -> {
                    System.out.println("Response>>"+response);
                    tipsText.setValue(""+response.length());
                },
                error ->{
                    Log.e("VolleyError here", error.toString());
                }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + token);
                return headers;
            }
        };

        queue.add(jsonArrayRequest);
    }

    public static void retrieveMotherData(String phoneNumber, Context context, Callback callback) {
        RequestQueue queue = Volley.newRequestQueue(context);
        String url = PARENT_URL+"mothers/phone/" + phoneNumber;
        ProgressDialog dialog = new ProgressDialog(context);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET, url, null,
                callback::onSuccess,
                error -> {
                    Log.e("VolleyError", error.toString());
                    callback.onError(error.toString());
                }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + token);
                return headers;
            }
        };

        queue.add(jsonObjectRequest);
    }
}