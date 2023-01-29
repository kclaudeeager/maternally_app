package com.example.navigationdrawerapp;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MessageDAO {
    public void insertMessage(Message message) throws SQLException {
        try (Connection connection = JDBCUtil.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement("INSERT INTO message(text) VALUES (?)")) {
                statement.setString(1, message.getMessageText());
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Message> getAll() {
        List<Message> messages = new ArrayList<>();
        try (Connection connection = JDBCUtil.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM message")) {
                try (ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {
                        Message message = new Message();
                        message.setMessageText(resultSet.getString("text"));
                        messages.add(message);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return messages;
    }
}
