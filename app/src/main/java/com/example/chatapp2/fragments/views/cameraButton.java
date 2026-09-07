package com.example.chatapp2.fragments.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ImageDecoder;
import android.graphics.Paint;
import android.graphics.RectF;
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

public class cameraButton extends View implements GestureDetector.OnGestureListener {
    boolean isfirstLayout;
    Bitmap backdrop;
    public cameraButtonInterface cameraButtonInterface;
    public int trueWidth=3000;
    public int trueHeigth=3000;

    public interface cameraButtonInterface{
        void onClick1();
        void getMeasurements();
    }

    public cameraButton(Context context) {
        super(context);
        cameraButtonInterface=null;
        isfirstLayout=true;
    }

    public cameraButton(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        cameraButtonInterface=null;
        isfirstLayout=true;
    }

    public cameraButton(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        cameraButtonInterface=null;
        isfirstLayout=true;
    }

    public cameraButton(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        cameraButtonInterface=null;
        isfirstLayout=true;
    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if(isfirstLayout){
            gestureDetector=new GestureDetector(this);
            try {
                Log.i("decoded","yes");
                backdrop= ImageDecoder.decodeBitmap(ImageDecoder.createSource(getResources(), R.drawable.backdrop));
                backdrop= Bitmap.createScaledBitmap(backdrop,175,175,false);

            } catch (IOException e) {
                e.printStackTrace();
            }
            isfirstLayout=false;
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

            setMeasuredDimension(175, 175);

        }
    public void setCameraButtonInterface(cameraButtonInterface cameraButtonInterface){
        this.cameraButtonInterface=cameraButtonInterface;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Log.i("drawn","yes"+getY()+"/"+getX());
        canvas.drawBitmap(backdrop,0,0,new Paint());
        Log.i("lalala",""+backdrop.getWidth()+"/"+backdrop.getHeight());
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
        if(cameraButtonInterface!=null){
            cameraButtonInterface.onClick1();
        }
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
