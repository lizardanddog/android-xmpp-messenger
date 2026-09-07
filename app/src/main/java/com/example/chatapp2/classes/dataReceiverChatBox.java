package com.example.chatapp2.classes;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageDecoder;
import android.graphics.Paint;
import android.media.ThumbnailUtils;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.util.Size;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.chatapp2.R;
import com.example.chatapp2.views.chatBox;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class dataReceiverChatBox extends BroadcastReceiver {

    public String username;
    public String msg_body;
    public String msg_type;
    public chatBox chatbox;
    public String conversation;
    private Bitmap actionIconBitmap;
    private String msgId;
    String videoLastFilePath;



    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    public void onReceive(Context context, Intent intent) {
        Resources res = chatbox.getResources();
        try {

            actionIconBitmap=ImageDecoder.decodeBitmap(ImageDecoder.createSource(res, R.drawable.ic_delete_small));

        } catch (IOException e) {
            e.printStackTrace();

        }
        Log.d("DataReceiver","Action: "+intent.getAction());
        if (intent.getAction().equals("Dataxxx")&&intent.getStringExtra("username").equals(conversation)) {

            username = intent.getStringExtra("username");
            msg_body = intent.getStringExtra("body");
            msg_type = intent.getStringExtra("msgType");
            if (msg_type.length() > 1) {
                String[] parts = msg_type.split("/",3);
                msgId= parts[1];
                videoLastFilePath= parts[2];

                msg_type= parts[0];
                Log.d("DataReceiver","Received message ID: "+msgId+" type: "+msg_type);
                parts=null;
            }

            switch(msg_type) {
                case "0":
                        chatbox.addNewMessage(new messages(msg_body, 0, new Paint(), new Paint(), chatbox.getWidth() * 2 / 3))
                    ;
                    chatbox.contentBottomY = chatbox.getHeight() - 10;
                    Log.d("DataReceiver","Processing text message");
break;
                case "1":
                    downloadAudioMessage(msgId);
                    Log.d("DataReceiver","Processing audio message");
                    break;
                case "2":
                    downloadImageMessage(msgId);
                    Log.d("DataReceiver","Processing image message");
break;
                case "3":
                    downloadVideoMessage(msgId);
                    Log.d("DataReceiver","Processing video message");
break;
            }

                  chatbox.invalidate();

        }}

    public void chatFinder(chatBox chatbox, String conversation){
        this.chatbox=chatbox;
        this.conversation=conversation;
    }

    public void downloadAudioMessage(String msgIdd){
        String url = "http://192.168.1.50:6661/api/v22/users?msgId="+msgIdd;


        RequestQueue queue = Volley.newRequestQueue(chatbox.getContext());


        VolleyMultipartRequest request = new VolleyMultipartRequest(Request.Method.GET, url, new com.android.volley.Response.Listener<NetworkResponse>() {
            @Override
            public void onResponse(NetworkResponse response) {
                Log.d("DataReceiver","Audio download successful");
                File file = new File (""+Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM)+"/"+videoLastFilePath+".3gp");
                messages vocalMsg=new messages(1,new Paint(),"Audio message received");
                vocalMsg.setMsgType(1);
                vocalMsg.setFilename(""+Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM)+"/"+videoLastFilePath+".3gp");
                FileOutputStream out = null;
                try {
                    out = new FileOutputStream(file);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                try {
                    out.write(response.data);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                chatbox.addNewMessage(vocalMsg);
                chatbox.invalidate();



                // on below line we are setting this string s to our text view.

            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(chatbox.getContext(), "Fail to get response = " + error, Toast.LENGTH_SHORT).show();
            }
        }) ;
        queue.add(request);
        // below line is to make
        // a json object request.

    }


    public void downloadVideoMessage(String msgIdd){

     //   String videoLastFilePath="CHA-"+System.nanoTime()+"-"+msgIdd;

        Size previewSize=new Size((int) (chatbox.getWidth()*2/5.0-40-5), (int) (chatbox.getHeight()/4.0-5));
        String url = "http://192.168.1.50:6661/api/v22/users?msgId="+msgIdd;
        RequestQueue queue = Volley.newRequestQueue(chatbox.getContext());


                    VolleyMultipartRequest videoRequest = new VolleyMultipartRequest(Request.Method.GET, url, new Response.Listener<NetworkResponse>() {
                        @RequiresApi(api = Build.VERSION_CODES.Q)
                        @Override
                        public void onResponse(NetworkResponse response) {
                            File file = new File (""+Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM)+"/"+videoLastFilePath+".mp4");




                            messages imageMsg=new messages(1,new Paint(),"Video message", actionIconBitmap);
                            imageMsg.setMsgType(3);
                            imageMsg.setFilename(""+Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM)+"/"+videoLastFilePath+".mp4");
                            imageMsg.setmFileAndIconLoader(new Runnable() {
                                                               @Override
                                                               public void run() {
                                                                   FileOutputStream out = null;
                                                                   try {
                                                                       out = new FileOutputStream(file);
                                                                   } catch (FileNotFoundException e) {
                                                                       e.printStackTrace();
                                                                   }
                                                                   try {
                                                                       out.write(response.data);
                                                                   } catch (IOException e) {
                                                                       e.printStackTrace();
                                                                   }
                                                                   try {
                                                                       out.close();
                                                                   } catch (IOException e) {
                                                                       e.printStackTrace();
                                                                   }

                                                                   Size previewSize=new Size((int) (chatbox.getWidth()*2/5.0-40-5), (int) (chatbox.getHeight()/4.0-5));
                                                                   try {
                                                                       Bitmap thumb= ThumbnailUtils.createVideoThumbnail(new File(""+Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM)+"/"+videoLastFilePath+".mp4"), previewSize,null);
                                                                       imageMsg.iconBitmap=thumb;
                                                                       chatbox.addNewMessage(imageMsg);
                                                                   } catch (IOException e) {
                                                                       e.printStackTrace();
                                                                   }


                                                                   chatbox.invalidate();

                                                                   //postBitmap(bitmap10, mFile);


                                                                   // } catch (IOException e) {
                                                                   //e.printStackTrace();
                                                                   //  }
                                                               }
                                                           }
                            );
                            imageMsg.mFileAndIconLoader.run();

                        }
                    }, new com.android.volley.Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                            Toast.makeText(chatbox.getContext(), "Fail to get response = " + error, Toast.LENGTH_SHORT).show();
                        }
                    });{

                    }


                    // on below line we are setting this string s to our text view.
        Log.d("DataReceiver","Request queue size: "+queue.getCache().toString()) ;
                    queue.add(videoRequest);

            }


        // below line is to make
        // a json object request.




    public void downloadImageMessage(String msgIdd){
        //String videoLastFilePath="CHA-"+System.nanoTime()*999+"-"+msgIdd;
       File mFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM), videoLastFilePath+".jpg");
        String url = "http://192.168.1.50:6661/api/v22/users?msgId="+msgIdd;


        RequestQueue queue = Volley.newRequestQueue(chatbox.getContext());
        VolleyMultipartRequest  VolleymultipartRequest= new  VolleyMultipartRequest(Request.Method.GET, url, new Response.Listener<NetworkResponse>() {
                        @Override
                        public void onResponse(NetworkResponse response) {

                            messages imageMsg=new messages(0,new Paint(),"Image message", actionIconBitmap);
                            imageMsg.setMsgType(2);

                            imageMsg.setFilename(""+ Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM)+"/"+videoLastFilePath+".jpg");
                            imageMsg.setmFileAndIconLoader(new Runnable() {
                                                               @Override
                                                               public void run() {
                                                                   FileOutputStream out = null;
                                                                   try {
                                                                       out = new FileOutputStream(mFile);
                                                                   } catch (FileNotFoundException e) {
                                                                       e.printStackTrace();
                                                                   }
                                                                   try {
                                                                       out.write(response.data);
                                                                   } catch (IOException e) {
                                                                       e.printStackTrace();
                                                                   }
                                                                   try {
                                                                       out.close();
                                                                   } catch (IOException e) {
                                                                       e.printStackTrace();
                                                                   }

                                                                   //Create small image to show in chatbox
                                                                   imageMsg.iconBitmap= Bitmap.createScaledBitmap(BitmapFactory.decodeFile(""+Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM)+"/"+videoLastFilePath+".jpg"),chatbox.getWidth()*2/5-40-5,chatbox.getHeight()/4-5,false);


                                                                   //Save fat image :)
                                                                   Bitmap bitmap98=BitmapFactory.decodeFile(imageMsg.filename);

                                                                   chatbox.invalidate();
                                                                   bitmap98=null;
                                                               }
                                                           }
                            );
                            imageMsg.mFileAndIconLoader.run();
                            chatbox.addNewMessage(imageMsg);
                            chatbox.contentBottomY = chatbox.getHeight() - 10;
                            chatbox.invalidate();









                        }
                    }, new com.android.volley.Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                            Toast.makeText(chatbox.getContext(), "Fail to get response = " + error, Toast.LENGTH_SHORT).show();
                        }
                    });{

        }


        // on below line we are setting this string s to our text view.
        Log.d("DataReceiver","Request queue size: "+queue.getCache().toString()) ;
        queue.add(VolleymultipartRequest);




    }
    private void postBitmap(Bitmap bitmap, File file) throws IOException {
        OutputStream fOut = null;
        Integer counter = 0;
        fOut = new FileOutputStream(file);
        bitmap.compress(Bitmap.CompressFormat.JPEG, 85, fOut); // saving the Bitmap to a file compressed as a JPEG with 85% compression rate
        fOut.flush();
        fOut.close();
        MediaStore.Images.Media.insertImage(chatbox.getContext().getContentResolver(),file.getAbsolutePath(),file.getName(),file.getName());
    }




}
