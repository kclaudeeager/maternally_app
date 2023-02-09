package com.example.navigationdrawerapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatViewHolder> {

    private static final int INCOMING_MESSAGE = 0;
    private static final int OUTGOING_MESSAGE = 1;

    private final List<Message> messages;
    private final String currentUserId;
     static int viewType;
    public ChatAdapter(List<Message> messages, String currentUserId) {
        this.messages = messages;
        this.currentUserId = currentUserId;
    }

    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view;
        if (viewType == INCOMING_MESSAGE) {
            view = inflater.inflate(R.layout.incoming_message, parent, false);
        } else {
            view = inflater.inflate(R.layout.outgoing_message, parent, false);
        }
        return new ChatViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder holder, int position) {
        Message message = messages.get(position);
        holder.bind(message);
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    @Override
    public int getItemViewType(int position) {
        Message message = messages.get(position);

        if (message.getSender().equals(currentUserId)) {
            this.viewType=OUTGOING_MESSAGE;
            return OUTGOING_MESSAGE;
        } else {
            this.viewType=INCOMING_MESSAGE;
            return INCOMING_MESSAGE;
        }
    }

    static class ChatViewHolder extends RecyclerView.ViewHolder {

        private final TextView messageText;
        private final TextView timestamp;

        public ChatViewHolder(@NonNull View itemView) {
            super(itemView);
          if(viewType==OUTGOING_MESSAGE){
              messageText = itemView.findViewById(R.id.outgoing_message_text);
              timestamp = itemView.findViewById(R.id.outgoing_message_timestamp);

          }
          else{
              messageText = itemView.findViewById(R.id.incoming_message_text);
              timestamp = itemView.findViewById(R.id.incoming_message_timestamp);
          }

        }

        public void bind(Message message) {
            String dateString = message.getTimestamp();
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ", Locale.getDefault());
            SimpleDateFormat outputFormat = new SimpleDateFormat("MMM dd, yyyy hh:mm a", Locale.getDefault());

            try {
                Date date = inputFormat.parse(dateString);
                Calendar calendar = Calendar.getInstance();
                assert date != null;
                calendar.setTime(date);
                outputFormat.setTimeZone(calendar.getTimeZone());
                String formattedDate = outputFormat.format(date);
                timestamp.setText(formattedDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            messageText.setText(message.getMessageText());
        }
    }
}

