package com.example.chatapp2.classes;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class dataReceiver extends BroadcastReceiver {

    public String username;
    public String msg_body;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("Dataxxx")) {
            username = intent.getStringExtra("username");
            msg_body = intent.getStringExtra("body");
            Log.d("DataReceiver", "Received message from " + username + ": " + msg_body);
            updateCommunicationList(username, msg_body);
        }
    }

    public void updateCommunicationList(String username, String msg) {
        // Logic to update communication list
    }
}
