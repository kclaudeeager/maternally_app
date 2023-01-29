package com.example.navigationdrawerapp;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class MessageService extends Service {
    private static final String TAG = "MessageService";
    Context context;
    String token;

    public MessageService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        token = intent.getStringExtra("token");
        context = this;
        new Thread(() -> {
            while (true) {
                listenForMessages();
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    Log.e(TAG, "Error in thread sleep: " + e.getMessage());
                }
            }
        }).start();
        Message message = (Message) intent.getSerializableExtra("message");
        if (message != null) {
            sendMessage(message);
        }
        return START_STICKY;
    }
    public MessageService(Context context) {
        this.context = context;
    }

    private void listenForMessages() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Urls.PARENT_URL + "messages/",
                response -> {
                    // Handle the response from the server
                    // Parse the JSON response and update the UI with the new messages
                },
                error -> {
                    // Handle any error that occurs while listening for new messages
                }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                String bearer = "Bearer " + getAccessToken(); // Replace with the actual access token
                headers.put("Authorization", bearer);
                return headers;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }

    // Helper function to get the access token
    private String getAccessToken() {
        // Add your code to get the access token
        return token;
    }

    public void sendMessage(Message message) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Urls.PARENT_URL + "messages/add",
                response -> {
                    // Handle the response from the server
                },
                error -> {
                    // Handle any error that occurs while sending the message
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("text", message.getMessageText());
                return params;
            }

            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                String bearer = "Bearer " + getAccessToken(); // Replace with the actual access token
                headers.put("Authorization", bearer);
                return headers;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }
    public interface MessageListener {
        void onSuccess(String response);
        void onError(VolleyError error);
    }
    public void getMessages(MessageListener messageListener) {
        // Handle the response from the server
        // Parse the JSON response and update the UI with the new messages
        // Handle any error that occurs while listening for new messages
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Urls.PARENT_URL + "messages/",
                messageListener::onSuccess,
                messageListener::onError) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                String bearer = "Bearer " + getAccessToken(); // Replace with the actual access token
                headers.put("Authorization", bearer);
                return headers;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }

}
