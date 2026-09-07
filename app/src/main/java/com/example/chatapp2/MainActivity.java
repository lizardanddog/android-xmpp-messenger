package com.example.chatapp2;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;

import com.example.chatapp2.services.xmppService;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    public static SharedPreferences sharedPreferences;
    public static  SharedPreferences.Editor editor;
    public static final String sharedPrefFIle="com.code.example.onlyPath";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        xmppService xxx=new xmppService();
        Intent intent = new Intent(this, xmppService.class);
        xxx.getContext(this);
        xxx.onStartCommand(intent,0,0);
        sharedPreferences = getSharedPreferences(sharedPrefFIle, MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.putString("who", "Constantine");
        editor.apply();


        //startService(intent);

        ConstraintLayout layout= findViewById(androidx.constraintlayout.widget.R.id.constraint);

        xmppService.layout_width= 350;
               // layout.getWidth();
        xmppService.layout_height=450;
                //layout.getHeight();
        xmppService.active=2;
       try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


       getApppppSpecificAlbumStorageDir(this,"ChatApp2.Videos");
       getAppSpecificAlbumStorageDir(this,"ChatApp2.Pics");
        getAppAUdioSpecificAlbumStorageDir(this, "ChatApp2.audioFiles");

        Intent intent1 = new Intent(this, ChatListActivity.class);
        startActivity(intent1);

    }
    @Nullable
    File getAppSpecificAlbumStorageDir(Context context, String albumName) {
        // Get the pictures directory that's inside the app-specific directory on
        // external storage.
        //imageMsg.setFilename(""+ Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM)+"/pic1.jpg");
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), albumName);
        if (file == null || !file.mkdirs()) {
             Log.e("Catastrophe1", "Directory not created");
        }
        return file;
    }
    @Nullable
    File getApppppSpecificAlbumStorageDir(Context context, String albumName) {
        // Get the pictures directory that's inside the app-specific directory on
        // external storage.
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES), albumName);
        if (file == null || !file.mkdirs()) {
            Log.e("Catastrophe11", "Directory not created");
        }
        return file;
    }

    @Nullable
    File getAppAUdioSpecificAlbumStorageDir(Context context, String albumName) {
        // Get the pictures directory that's inside the app-specific directory on
        // external storage.
        File file = new File(context.getExternalFilesDir(
                Environment.DIRECTORY_MOVIES), albumName);
        if (file == null || !file.mkdirs()) {
            Log.e("Catastrophe111", "Directory not created");
        }
        return file;
    }
}