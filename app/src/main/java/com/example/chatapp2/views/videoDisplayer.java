package com.example.chatapp2.views;

import static java.util.Objects.isNull;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.graphics.RectF;
import android.media.MediaPlayer;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.VideoView;

import com.example.chatapp2.R;

import java.io.IOException;


public class videoDisplayer extends VideoView implements GestureDetector.OnGestureListener {
    boolean isfirstLayout;
    int videoState; //0: firstPauseBeforeStart ; 1: playing ; 2 OnPause


    public videoDisplayer(Context context) {
        super(context);
        isfirstLayout=true;
        videoState=0;
        setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                videoState=0;
                start();
                pause();
            }
        });
        setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                start();
                pause();
            }
        });
    }

    public videoDisplayer(Context context, AttributeSet attrs) {
        super(context, attrs);
        isfirstLayout=true;
        videoState=0;
        setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                videoState=0;
                start();
                pause();
            }
        });
        setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                start();
                pause();
            }
        });
    }

    public videoDisplayer(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        isfirstLayout=true;
        videoState=0;
        setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                videoState=0;
                start();
                pause();
            }
        });
        setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                start();
                pause();
            }
        });
    }

    public videoDisplayer(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        isfirstLayout=true;
        videoState=0;
        setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                videoState=0;
                start();
                pause();
            }
        });
        setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                start();
                pause();
            }
        });
    }
    int videoWWW=0;
    int videoHHH=0;
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(videoWWW,videoHHH);
        i++;
    }
    int i=0;
    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if(isfirstLayout){
            gestureDetector=new GestureDetector(this);
            isfirstLayout=false;
        }
        if (i==2){
            setY(getY()+getHeight()*0.5f);}

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (gestureDetector.onTouchEvent(event)) {
            return gestureDetector.onTouchEvent(event);
        }
        return true;
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
        Log.i("videostate",""+videoState);
        switch (videoState){
            case 0:
                start();
                videoState=1;
                break;
            case 1:
                pause();
                videoState=2;
                break;
            case 2:
                start();
                videoState=1;
                break;
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

    public void setWWWHHH(int WWW, int HHH){
        this.videoHHH=HHH;
        this.videoWWW=WWW;
    }

}
