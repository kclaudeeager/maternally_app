package com.example.navigationdrawerapp;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class MessageListener {
    private Connection connection;
    private List<NewMessageListener> listeners = new ArrayList<>();

    public MessageListener(String host, String database, String user, String password) throws SQLException {
        String url = "jdbc:postgresql://" + host + "/" + database;
        connection = DriverManager.getConnection(url, user, password);

        // Listen for new messages in a separate thread
        new Thread(() -> {
            while (true) {
                try {
                    // Check for new messages every second
                    Thread.sleep(1000);
                    checkForNewMessages();
                } catch (InterruptedException | SQLException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void checkForNewMessages() throws SQLException {
        // Create a statement to query the messages table
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM messages ORDER BY timestamp DESC LIMIT 1");

        // Get the most recent message
        if (rs.next()) {
            // Extract the message details from the result set
            String sender = rs.getString("sender_id");
            String text = rs.getString("text");
            Timestamp timestamp = rs.getTimestamp("timestamp");

            // Notify all listeners of the new message
            for (NewMessageListener listener : listeners) {
                listener.onNewMessage(sender, text, timestamp);
            }
        }
    }

    public void addNewMessageListener(NewMessageListener listener) {
        listeners.add(listener);
    }

    public void removeNewMessageListener(NewMessageListener listener) {
        listeners.remove(listener);
    }

    public interface NewMessageListener {
        void onNewMessage(String sender, String text, Timestamp timestamp);
    }

    public void onNewMessage(Message message) {
        for (NewMessageListener listener : listeners) {
            listener.onNewMessage(message.getSender(), message.getMessageText(), Timestamp.valueOf(message.getTimestamp()));
        }
    }

}
