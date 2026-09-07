package com.example.chatapp2.fragments.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ImageDecoder;
import android.graphics.Paint;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.example.chatapp2.R;

import java.io.IOException;

public class flashview extends View implements GestureDetector.OnGestureListener {
    boolean isfirstLayout;
    Bitmap flash1;
    Bitmap flash2;
    Bitmap flash3;
    public int flashcase;
    public static final int flash_off=0;
    public static final int flash_on=1;
    public static final int flash_auto=3;
    public flashinterface flashinterface;
    public int trueWidth;
    public int trueHeigth;

    public interface flashinterface{
        void onClick1();
    }
    public void setFlashInterface(flashinterface flashinterface){
        this.flashinterface=flashinterface;
    }

    public flashview(Context context) {
        super(context);
        isfirstLayout=true;
        flashcase=flashview.flash_off;
        flashinterface=null;
    }

    public flashview(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        isfirstLayout=true;
        flashcase=flashview.flash_off;
        flashinterface=null;
    }

    public flashview(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        isfirstLayout=true;
        flashcase=flashview.flash_off;
        flashinterface=null;
    }

    public flashview(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        isfirstLayout=true;
        flashcase=flashview.flash_off;
        flashinterface=null;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        setMeasuredDimension(175, 175);

    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if(isfirstLayout){
            gestureDetector=new GestureDetector(this);
            try {

                flash1= ImageDecoder.decodeBitmap(ImageDecoder.createSource(getResources(), R.drawable.noflash));
                flash1=Bitmap.createScaledBitmap(flash1, 175,175, false);
                flash2= ImageDecoder.decodeBitmap(ImageDecoder.createSource(getResources(), R.drawable.flash));
                flash2=Bitmap.createScaledBitmap(flash2, 175,175, false);
                flash3= ImageDecoder.decodeBitmap(ImageDecoder.createSource(getResources(), R.drawable.autoflash));
                flash3=Bitmap.createScaledBitmap(flash3, 175,175, false);

            } catch (IOException e) {
                e.printStackTrace();
            }
            isfirstLayout=false;
        }
    }
    protected void onDraw(Canvas canvas) {
        switch(flashcase){
            case flashview.flash_off:
                canvas.drawBitmap(flash1,0,0,new Paint());
                break;
            case flashview.flash_on:
               // flashcase=flashview.flash_auto;
                canvas.drawBitmap(flash2,0,0,new Paint());
                break;
            case flashview.flash_auto:
               // flashcase=flashview.flash_off;
                canvas.drawBitmap(flash3,0,0,new Paint());
                break;
        }

        super.onDraw(canvas);
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if (gestureDetector.onTouchEvent(event)) {
            return gestureDetector.onTouchEvent(event);
        }
        return true;}

    @Override
    public boolean onDown(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent motionEvent) {
        Log.i("EHEHEHEHE", "aaaaa");
        switch(flashcase){
            case flashview.flash_off:
                flashcase=flashview.flash_on;
                break;
            case flashview.flash_on:
                flashcase=flashview.flash_auto;
                break;
            case flashview.flash_auto:
                flashcase=flashview.flash_off;
                break;

        }
        if(flashinterface!=null){
            flashinterface.onClick1();
        }
            invalidate();

        return false;
    }

    @Override
    public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        return false;
    }
    public GestureDetector gestureDetector;
}
