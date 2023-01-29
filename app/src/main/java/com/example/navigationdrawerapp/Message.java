package com.example.navigationdrawerapp;

import java.io.Serializable;

public class Message implements Serializable {
    private String sender;
    private String messageText;
    private long timeSent;
    String senderId;
    String timestamp;

    public Message(String sender, String messageText, String timestamp) {
        this.sender = sender;
        this.messageText = messageText;
        this.timestamp = timestamp;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    public void setTimeSent(long timeSent) {
        this.timeSent = timeSent;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
    public Message(){

    }
    public Message(String sender, String messageText, long timeSent) {
        this.sender = sender;
        this.messageText = messageText;
        this.timeSent = timeSent;
    }

    public String getSender() {
        return sender;
    }

    public String getMessageText() {
        return messageText;
    }

    public long getTimeSent() {
        return timeSent;
    }
}
