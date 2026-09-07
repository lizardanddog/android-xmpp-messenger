package com.example.chatapp2.classes;

import android.os.AsyncTask;
import android.util.Log;

import com.example.chatapp2.services.xmppService;

import org.jivesoftware.smack.AbstractXMPPConnection;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;
import org.jxmpp.jid.impl.JidCreate;

import java.util.ArrayList;

public class XMPPTask extends AsyncTask<String, Void, Boolean> {

    private static final String LOG_TAG = "XMPPTask";
    private AbstractXMPPConnection connection;

    @Override
    protected Boolean doInBackground(String... params) {
        try {
            XMPPTCPConnectionConfiguration config = XMPPTCPConnectionConfiguration.builder()
                    .setUsernameAndPassword("user", "password")
                    .setXmppDomain("localhost")
                    .setHost("192.168.1.50")
                    .setPort(5222)
                    .setSecurityMode(ConnectionConfiguration.SecurityMode.disabled)
                    .build();

            connection = new XMPPTCPConnection(config);
            connection.connect();
            connection.login();
            return true;
        } catch (Exception e) {
            Log.e(LOG_TAG, "XMPP connection failed", e);
            return false;
        }
    }
}
