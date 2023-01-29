package com.example.navigationdrawerapp;

import static com.example.navigationdrawerapp.MainActivity.userDataJs;

import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONException;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;


public class ChatActivity extends AppCompatActivity {
    private RecyclerView chatListView;
    private EditText messageEditText;
    private Button sendButton;
    private MessageDAO messageDao;

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
            chatAdapter = new ChatAdapter(messages,userDataJs.getString("id"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        chatListView.setAdapter(chatAdapter);

        // Get messages from database
        getMessagesFromDatabase();
        messageDao = new MessageDAO();
        // Add send button click listener
        sendButton.setOnClickListener(view -> {
            String messageText = messageEditText.getText().toString();
            if (!messageText.isEmpty()) {
                // Create new message
                Message message = new Message();
                try {
                    int userId=userDataJs.getInt("id");
                    message.setSenderId(String.valueOf(userId));
                    message.setMessageText(messageText);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        message.setTimestamp(Timestamp.valueOf(String.valueOf(LocalDateTime.now())).toString());
                    }

                    messageDao.insertMessage(message);
                    messages.add(message);
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (SQLException e) {
                    e.printStackTrace();
                }

                messages.add(message);
                chatAdapter.notifyDataSetChanged();

                // Clear message edit text
                messageEditText.setText("");

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
        if (item.getItemId() == android.R.id.home) {// app icon in action bar clicked; goto parent activity.
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void getMessagesFromDatabase() {
     messageDao = new MessageDAO();
        messages.clear();
        messages.addAll(messageDao.getAll());
        chatAdapter.notifyDataSetChanged();
        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                messages.clear();
                messages.addAll(messageDao.getAll());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        chatAdapter.notifyDataSetChanged();
                    }
                });
            }
        }, 0, 5000);
    }




}