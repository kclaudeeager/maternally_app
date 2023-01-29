package com.example.navigationdrawerapp;

import static com.example.navigationdrawerapp.MainActivity.userDataJs;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.VolleyError;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.Objects;

public class ChatActivity extends AppCompatActivity {
    private RecyclerView chatListView;
    private EditText messageEditText;
    private Button sendButton;
    private MessageService messageService;

    private ArrayList<Message> messages;
    private ChatAdapter chatAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        // Initialize views
        chatListView = findViewById(R.id.chat_list_view);
        messageEditText = findViewById(R.id.message_edit_text);
        sendButton = findViewById(R.id.send_button);

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

        // Get messages from the server
        getMessagesFromServer();

        // Add send button click listener
        sendButton.setOnClickListener(view -> {
            String messageText = messageEditText.getText().toString();
            if (!messageText.isEmpty()) {
                // Create new message
                Message message = new Message();
                try {
                    message.setMessageText(messageText);
                    messages.add(message);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                // Notify the adapter to update the RecyclerView
                chatAdapter.notifyDataSetChanged();

                // Clear message edit text
                messageEditText.setText("");

                // Send the message to the server
                sendMessage(message);
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {// app icon in action bar clicked; go to parent
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void getMessagesFromServer() {
        messageService.getMessages(new MessageService.MessageListener() {
            @Override
            public void onSuccess(String response) {
                // Clear the current messages list
                ChatActivity.this.messages.clear();
                System.out.println("onSuccess response: "+response);
                // Add the new messages to the list
                ChatActivity.this.messages.addAll(messages);

                // Notify the adapter to update the RecyclerView
                chatAdapter.notifyDataSetChanged();
            }

            @Override
            public void onError(VolleyError error) {

            }
        });
    }

    private void sendMessage(Message message) {
        Intent serviceIntent = new Intent(this, MessageService.class);
        serviceIntent.putExtra("message", message);
        startService(serviceIntent);
    }




}