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
import com.example.chatapp2.views.chatBox;

import java.io.IOException;

public class flipView extends View implements GestureDetector.OnGestureListener {
    boolean isfirstLayout;
    Bitmap flip;
    flipInterface flipInterface;
    public int trueWidth;
    public int trueHeigth;
    public GestureDetector gestureDetector;



    public interface flipInterface{
        void onClick1();
    }

    public flipView(Context context) {
        super(context);
        isfirstLayout=true;
        flipInterface=null;
    }

    public flipView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        isfirstLayout=true;
        flipInterface=null;

    }

    public flipView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        isfirstLayout=true;
        flipInterface=null;
    }

    public flipView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        isfirstLayout=true;
        flipInterface=null;
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

            try {
                flip= ImageDecoder.decodeBitmap(ImageDecoder.createSource(getResources(), R.drawable.flip));
                flip=Bitmap.createScaledBitmap(flip, 175,175,false);

            } catch (IOException e) {
                e.printStackTrace();
            }
            gestureDetector=new GestureDetector(this);
        }
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if (gestureDetector.onTouchEvent(event)) {
            return gestureDetector.onTouchEvent(event);
        }
        return true;}
    public void setFlipInterface(flipInterface flipInterface){
        this.flipInterface=flipInterface;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawBitmap(flip,0,0,new Paint());

        super.onDraw(canvas);
    }

    @Override
    public boolean onDown(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent motionEvent) {
        Log.i("flip","flip++");
        if(flipInterface!=null){

            flipInterface.onClick1();
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
}
