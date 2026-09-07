package com.example.chatapp2.classes;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageDecoder;
import android.os.AsyncTask;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.chatapp2.R;

import java.io.IOException;
import java.util.ArrayList;

public class LoadBitmapTask extends AsyncTask<Void, Void, Void> {
    Resources res;
    public Bitmap senderButton;
    public Bitmap micButton;
    public Bitmap lock0;
    public Bitmap lock1;
    public Bitmap lock4;
    public Bitmap lock3;
    boolean last;
    public Bitmap locky;
    public Bitmap pbackground;
    public Bitmap pb1;
    public Bitmap pb2;
    public Bitmap pb3;
    public Bitmap pb4;
    public Bitmap background0;
    public Bitmap background1;
    public Bitmap background3;
    public Bitmap background4;

    public LoadBitmapTask(Resources res){
        this.res=res;
        last=false;
    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    public void decodeMsGSenderBitmap(int trueHeigth, int trueWidth) throws IOException {
        senderButton= ImageDecoder.decodeBitmap(ImageDecoder.createSource(res, R.drawable.send));
        micButton=ImageDecoder.decodeBitmap(ImageDecoder.createSource(res,R.drawable.mic));
        lock0=ImageDecoder.decodeBitmap(ImageDecoder.createSource(res,R.drawable.lock0small));
        lock1=ImageDecoder.decodeBitmap(ImageDecoder.createSource(res,R.drawable.lock1small));
        lock4=ImageDecoder.decodeBitmap(ImageDecoder.createSource(res,R.drawable.lock2small));
        lock3=ImageDecoder.decodeBitmap(ImageDecoder.createSource(res,R.drawable.lock3small));
        locky=ImageDecoder.decodeBitmap(ImageDecoder.createSource(res,R.drawable.locky));
        pbackground=ImageDecoder.decodeBitmap(ImageDecoder.createSource(res, R.drawable.pbackgoundsmall));

        senderButton =Bitmap.createScaledBitmap(senderButton,
          trueHeigth/18, trueHeigth/18, false);
        micButton =Bitmap.createScaledBitmap(micButton,
                trueHeigth/18, trueHeigth/18, false);
        lock0=Bitmap.createScaledBitmap(lock0,trueHeigth/25, trueHeigth/25, false);
        lock1=Bitmap.createScaledBitmap(lock1,trueHeigth/25, trueHeigth/25, false);
        lock4=Bitmap.createScaledBitmap(lock4,trueHeigth/25, trueHeigth/25, false);
        lock3=Bitmap.createScaledBitmap(lock3,trueHeigth/25, trueHeigth/25, false);
        pb1=ImageDecoder.decodeBitmap(ImageDecoder.createSource(res,R.drawable.pb0small));
        pb2=ImageDecoder.decodeBitmap(ImageDecoder.createSource(res,R.drawable.pb1small));
        pb3=ImageDecoder.decodeBitmap(ImageDecoder.createSource(res,R.drawable.pb2small));
        pb4=ImageDecoder.decodeBitmap(ImageDecoder.createSource(res,R.drawable.pb3small));
        background0=ImageDecoder.decodeBitmap(ImageDecoder.createSource(res, R.drawable.background0small));
        background1=ImageDecoder.decodeBitmap(ImageDecoder.createSource(res,R.drawable.background1small));
        background3=ImageDecoder.decodeBitmap(ImageDecoder.createSource(res, R.drawable.background3small));
        background4=ImageDecoder.decodeBitmap(ImageDecoder.createSource(res, R.drawable.background4small));
        pb1=Bitmap.createScaledBitmap(pb1,trueHeigth/15, trueHeigth/15, false);
        pb2=Bitmap.createScaledBitmap(pb2,trueHeigth/15, trueHeigth/15, false);
        pb3=Bitmap.createScaledBitmap(pb3,trueHeigth/15, trueHeigth/15, false);
        pb4=Bitmap.createScaledBitmap(BitmapFactory.decodeResource(res, R.drawable.pb3small),trueHeigth/15, trueHeigth/15, false);

        background0=Bitmap.createScaledBitmap(background0,trueWidth, trueWidth/2, false);
        background1=Bitmap.createScaledBitmap(background1,trueWidth, trueWidth/2, false);
        background3=Bitmap.createScaledBitmap(background3,trueWidth, trueWidth/2, false);
        background4=Bitmap.createScaledBitmap(background4,trueWidth, trueWidth/2, false);
        pbackground=Bitmap.createScaledBitmap(pbackground,trueWidth/2, trueWidth/2, false);


    }

    @Override
    protected Void doInBackground(Void... voids) {
        while (!last){
            //code is running
        }
        return null;
    }
}
