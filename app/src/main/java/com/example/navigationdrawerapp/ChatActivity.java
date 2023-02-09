package com.example.navigationdrawerapp;

import static com.example.navigationdrawerapp.MainActivity.token;
import static com.example.navigationdrawerapp.MainActivity.userDataJs;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

public class ChatActivity extends AppCompatActivity {
    private EditText messageEditText;
    private MessageService messageService;
    RecyclerView chatListView;
    private ArrayList<Message> messages;
    private ChatAdapter chatAdapter;
    private final BroadcastReceiver messageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Message message = (Message) intent.getSerializableExtra("message");
            messages.add(message);
            chatAdapter.notifyDataSetChanged();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        // Initialize views
        chatListView = findViewById(R.id.chat_list_view);
        messageEditText = findViewById(R.id.message_edit_text);
        Button sendButton = findViewById(R.id.send_button);

        // Initialize messages list and adapter
        messages = new ArrayList<>();
        try {
            chatAdapter = new ChatAdapter(messages, userDataJs.getString("id"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        chatListView.setAdapter(chatAdapter);

        // Initialize the MessageService instance
        messageService = new MessageService(this);
        Intent serviceIntent = new Intent(this, MessageService.class);
        Log.d("token",token);
        serviceIntent.putExtra("token", token);
        startService(serviceIntent);
        registerReceiver(messageReceiver, new IntentFilter("new_message"));

        // Get messages from the server
        getMessagesFromServer();

        // Add send button click listener
        sendButton.setOnClickListener(view -> {
            String messageText = messageEditText.getText().toString();
            if (!messageText.isEmpty()) {
                Message message=new Message();
                message.setMessageText(messageText);
                messageService.sendMessage(message);
                messageEditText.setText("");
                try {
                    messages.add(new Message(userDataJs.getString("id"),messageText,""+ new Date()));
                    try {
                        chatAdapter = new ChatAdapter(messages, userDataJs.getString("id"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    chatListView.setAdapter(chatAdapter);
                    chatListView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
                    chatAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(messageReceiver);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void getMessagesFromServer() {
        messageService.getMessages(new MessageService.MessageListener() {

            @Override
            public void onSuccess(String response) throws JSONException {
                JSONArray responseArray=new JSONArray(response);
                //Log.i("Messages",responseArray.toString());
                for (int i = 0; i < responseArray.length(); i++) {
                    try {

                        JSONObject message = responseArray.getJSONObject(i);

                        messages.add(new Message(message.getString("sender_id"),message.getString("text"),message.getString("createdAt")));

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                try {
                    chatAdapter = new ChatAdapter(messages, userDataJs.getString("id"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                chatListView.setAdapter(chatAdapter);
                chatListView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
                chatAdapter.notifyDataSetChanged();

            }

            @Override
            public void onError(VolleyError error) {
                // Handle error
            }
        });
    }

}

