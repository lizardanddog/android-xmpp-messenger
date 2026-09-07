package com.example.chatapp2.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

import com.example.chatapp2.classes.communicationCard;

import java.util.ArrayList;

public class chatList extends View implements GestureDetector.OnGestureListener {

    public ArrayList<communicationCard> communicationList = new ArrayList<>();
    public float contentBottomY;
    float oldContentBottomY;
    float Delta;
    GestureDetector gestureDetector;

    public chatList(Context context) {
        super(context);
        init();
    }

    public chatList(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        gestureDetector = new GestureDetector(getContext(), this);
        contentBottomY = 0;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // Simplified drawing logic for chat list
        Paint paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setTextSize(40);
        for (int i = 0; i < communicationList.size(); i++) {
            canvas.drawText("Conversation " + i, 50, 100 + i * 150 + contentBottomY, paint);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return gestureDetector.onTouchEvent(event);
    }

    @Override
    public boolean onDown(MotionEvent motionEvent) {
        return true;
    }

    @Override
    public void onShowPress(MotionEvent motionEvent) {}

    @Override
    public boolean onSingleTapUp(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        contentBottomY -= v1;
        invalidate();
        return true;
    }

    @Override
    public void onLongPress(MotionEvent motionEvent) {}

    @Override
    public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        return false;
    }
}
