package com.example.chatapp2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

public class ChatListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_list);

        Intent msgIntent = new Intent("msgintent");
        msgIntent.putExtra("body", "Welcome to ChatApp");
        sendBroadcast(msgIntent);
    }
}
