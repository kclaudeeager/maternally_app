package com.example.navigationdrawerapp;

import static com.example.navigationdrawerapp.MainActivity.token;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MessageService extends Service {
    private static final String TAG = MessageService.class.getSimpleName();
    private static final String MESSAGE_SENT_ACTION = "com.example.MESSAGE_SENT_ACTION";
    private static final String NEW_MESSAGE_ACTION = "com.example.NEW_MESSAGE_ACTION";

    Context context;


    public MessageService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.i("iBind intent",intent.toString());
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {


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
                    Log.d("Incoming",response);
                    Intent intent = new Intent("new_message");
                    intent.putExtra("message", response);
                    context.sendBroadcast(intent);
                },
                error -> {
                    // Handle any error that occurs while listening for new messages
                    Log.e("error", String.valueOf(error));
                }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                String bearer = "Bearer " +token; // Replace with the actual access token
                headers.put("Authorization", bearer);
                return headers;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }


    public void sendMessage(Message message) {
        JSONObject requestBody = new JSONObject();
        try {
            requestBody.put("text", message.getMessageText());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Urls.PARENT_URL + "messages/add", requestBody,
                response -> {
                    // Handle the response from the server
                    Log.i("Sent..", response.toString());
                },
                error -> {
                    // Handle any error that occurs while sending the message
                    Log.e("SendingError", error.toString());
                }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                String bearer = "Bearer " + token; // Replace with the actual access token
                headers.put("Authorization", bearer);
                headers.put("Content-Type", "application/json");
                return headers;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(jsonObjectRequest);
    }

    public interface MessageListener {
        void onSuccess(String response) throws JSONException;
        void onError(VolleyError error);
    }
    public void getMessages(MessageListener messageListener) {
        // Handle the response from the server
        // Parse the JSON response and update the UI with the new messages
        // Handle any error that occurs while listening for new messages
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Urls.PARENT_URL + "messages/",
                response -> {
                    try {
                        messageListener.onSuccess(response);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                messageListener::onError) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                String bearer = "Bearer " +token; // Replace with the actual access token
                headers.put("Authorization", bearer);
                return headers;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }

}
