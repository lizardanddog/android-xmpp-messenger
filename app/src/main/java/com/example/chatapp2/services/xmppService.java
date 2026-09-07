package com.example.chatapp2.services;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

public class xmppService extends Service {

    @Override
    public void onCreate() {
        super.onCreate();
        IntentFilter filter = new IntentFilter("msgintent");
        registerReceiver(new dataReceiverForXmppService(), filter);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public static class dataReceiverForXmppService extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String body = intent.getStringExtra("body");
            Log.d("XmppService", "Received broadcast: " + body);
        }
    }
}
