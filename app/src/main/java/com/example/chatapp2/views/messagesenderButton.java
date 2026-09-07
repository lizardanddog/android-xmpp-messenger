package com.example.chatapp2.views;

import android.animation.Animator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ImageDecoder;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.DecelerateInterpolator;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.example.chatapp2.ChatBoxActivity;
import com.example.chatapp2.R;
import com.example.chatapp2.classes.RendererFactory;
import com.example.chatapp2.classes.messages;
import com.example.chatapp2.classes.movingObjects;

import java.io.IOException;
import java.util.ArrayList;

public class messagesenderButton extends View {

    // Bitmaps:
    Bitmap sendy;
    Bitmap micro;
    Bitmap iconDeleteSmall;
    Bitmap rounding;
    Bitmap actionIcon;

    // Moving Objects:
    movingObjects senderObject;
    movingObjects deleteIconObject;
    movingObjects sendySmallObject;
    movingObjects roundingObject;

    // Paints:
    Paint genericPaint;

    // Dimensions:
    int viewWidth;
    int viewHeight;

    // Status:
    public boolean isSendyOn;
    boolean isMicroOn;
    boolean isDeleteIconOn;
    boolean isMoving;
    public static String videoShortFilePath;

    // Interfaces:
    public interface messagesenderButtonInterface {
        void onMicroDown();
        void stopRecording();
        float sendAmplitude();
        Bitmap saveBitmap();
        boolean saveBitmapToFile() throws IOException, org.json.JSONException;
        Bitmap saveVideoBitmap() throws IOException;
        void launchRunnable(messages msg);
    }

    public messagesenderButtonInterface messagesenderButtonInterface;
    public chatBox chatbox;
    public CustomEditText customEditText;

    public messagesenderButton(Context context) {
        super(context);
        init(null);
    }

    public messagesenderButton(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public messagesenderButton(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(@Nullable AttributeSet set) {
        isSendyOn = false;
        isMicroOn = true;
        isDeleteIconOn = false;
        isMoving = false;
        genericPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

        Resources res = getResources();
        try {
            sendy = ImageDecoder.decodeBitmap(ImageDecoder.createSource(res, R.drawable.ic_launcher_foreground));
            micro = ImageDecoder.decodeBitmap(ImageDecoder.createSource(res, R.drawable.ic_launcher_foreground));
            iconDeleteSmall = ImageDecoder.decodeBitmap(ImageDecoder.createSource(res, R.drawable.ic_delete_small));
            rounding = ImageDecoder.decodeBitmap(ImageDecoder.createSource(res, R.drawable.ic_launcher_foreground));
            actionIcon = ImageDecoder.decodeBitmap(ImageDecoder.createSource(res, R.drawable.ic_delete_small));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        viewWidth = MeasureSpec.getSize(widthMeasureSpec);
        viewHeight = MeasureSpec.getSize(heightMeasureSpec);

        senderObject = new movingObjects(micro, viewWidth - micro.getWidth(), viewHeight * 0.5f - micro.getHeight() * 0.5f, 0);
        deleteIconObject = new movingObjects(iconDeleteSmall, -iconDeleteSmall.getWidth(), viewHeight * 0.5f - iconDeleteSmall.getHeight() * 0.5f, 0);
        sendySmallObject = new movingObjects(sendy, viewWidth - micro.getWidth(), viewHeight * 0.5f - micro.getHeight() * 0.5f, 0);
        roundingObject = new movingObjects(rounding, viewWidth - micro.getWidth(), viewHeight * 0.5f - micro.getHeight() * 0.5f, 0);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (isMoving || senderObject.isAnimated() || deleteIconObject.isAnimated()) {
            invalidate();
        }

        if (isDeleteIconOn || deleteIconObject.isAnimated()) {
            canvas.drawBitmap(deleteIconObject.getOneBitmap(), deleteIconObject.getX(), deleteIconObject.getY(), genericPaint);
        }

        canvas.drawBitmap(senderObject.getOneBitmap(), senderObject.getX(), senderObject.getY(), genericPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (isMicroOn) {
                    if (messagesenderButtonInterface != null) {
                        messagesenderButtonInterface.onMicroDown();
                    }
                    createBiAnimator(deleteIconObject, sendySmallObject, viewWidth / 100, sendySmallObject.getY(), 99 * viewWidth / 100, deleteIconObject.getY(), 200).start();
                    isDeleteIconOn = true;
                }
                break;

            case MotionEvent.ACTION_MOVE:
                if (isDeleteIconOn) {
                    senderObject.setX(x - senderObject.getOneBitmap().getWidth() * 0.5f);
                    senderObject.setY(y - senderObject.getOneBitmap().getHeight() * 0.5f);
                    invalidate();
                }
                break;

            case MotionEvent.ACTION_UP:
                if (isDeleteIconOn) {
                    if (x < viewWidth * 0.2f) {
                        if (messagesenderButtonInterface != null) {
                            messagesenderButtonInterface.stopRecording();
                        }
                    } else {
                        // Process message sending
                    }
                    isDeleteIconOn = false;
                    createAnimator(senderObject, viewWidth - micro.getWidth(), viewHeight * 0.5f - micro.getHeight() * 0.5f, 300).start();
                }
                break;
        }
        return true;
    }

    private ValueAnimator createAnimator(movingObjects target, float newx, float newy, int duration) {
        target.setAnimated(true);
        PropertyValuesHolder propertyX = PropertyValuesHolder.ofFloat("x", target.getX(), newx);
        PropertyValuesHolder propertyY = PropertyValuesHolder.ofFloat("y", target.getY(), newy);

        ValueAnimator animator = new ValueAnimator();
        animator.setValues(propertyX, propertyY);
        animator.setDuration(duration);
        animator.setInterpolator(new DecelerateInterpolator());

        animator.addUpdateListener(animation -> {
            target.setX((float) animator.getAnimatedValue("x"));
            target.setY((float) animator.getAnimatedValue("y"));
            if (target.getY() == newy && target.getX() == newx) {
                target.setAnimated(false);
            }
            invalidate();
        });
        return animator;
    }

    private ValueAnimator createBiAnimator(movingObjects obj1, movingObjects obj2, float newx1, float newy1, float newx2, float newy2, int duration) {
        obj1.setAnimated(true);
        PropertyValuesHolder propertyX1 = PropertyValuesHolder.ofFloat("x1", obj1.getX(), newx1);
        PropertyValuesHolder propertyY1 = PropertyValuesHolder.ofFloat("y1", obj1.getY(), newy1);

        obj2.setAnimated(true);
        PropertyValuesHolder propertyX2 = PropertyValuesHolder.ofFloat("x2", obj2.getX(), newx2);
        PropertyValuesHolder propertyY2 = PropertyValuesHolder.ofFloat("y2", obj2.getY(), newy2);

        ValueAnimator animator = new ValueAnimator();
        animator.setValues(propertyX1, propertyY1, propertyX2, propertyY2);
        animator.setDuration(duration);
        animator.setInterpolator(new DecelerateInterpolator());

        animator.addUpdateListener(animation -> {
            obj1.setX((float) animator.getAnimatedValue("x1"));
            obj1.setY((float) animator.getAnimatedValue("y1"));
            obj2.setX((float) animator.getAnimatedValue("x2"));
            obj2.setY((float) animator.getAnimatedValue("y2"));

            if (obj1.getY() == newy1 && obj1.getX() == newx1) {
                obj1.setAnimated(false);
                obj2.setAnimated(false);
            }
            invalidate();
        });
        return animator;
    }

    private ValueAnimator createVocalAnimator(movingObjects obj1, movingObjects obj2, movingObjects obj3, movingObjects obj4, float newx1, float newy1, float newx2, float newy2, float rayonFinal, float newx3, float newy3, float newx4, float newy4, int duration) {
        obj1.setAnimated(true);
        PropertyValuesHolder propertyX1 = PropertyValuesHolder.ofFloat("x1", obj1.getX(), newx1);
        PropertyValuesHolder propertyY1 = PropertyValuesHolder.ofFloat("y1", obj1.getY(), newy1);

        obj2.setAnimated(true);
        PropertyValuesHolder propertyX2 = PropertyValuesHolder.ofFloat("x2", obj2.getX(), newx2);
        PropertyValuesHolder propertyY2 = PropertyValuesHolder.ofFloat("y2", obj2.getY(), newy2);
        PropertyValuesHolder propertRayon = PropertyValuesHolder.ofFloat("rayon", obj2.getPayon(), rayonFinal);

        obj3.setAnimated(true);
        PropertyValuesHolder propertyX3 = PropertyValuesHolder.ofFloat("x3", obj3.getX(), newx3);
        PropertyValuesHolder propertyY3 = PropertyValuesHolder.ofFloat("y3", obj3.getY(), newy3);

        obj4.setAnimated(true);
        PropertyValuesHolder propertyX4 = PropertyValuesHolder.ofFloat("x4", obj4.getX(), newx4);
        PropertyValuesHolder propertyY4 = PropertyValuesHolder.ofFloat("y4", obj4.getY(), newy4);

        ValueAnimator animator = new ValueAnimator();
        animator.setValues(propertyX1, propertyY1, propertyX2, propertyY2, propertRayon, propertyX3, propertyY3, propertyX4, propertyY4);
        animator.setDuration(duration);
        animator.setInterpolator(new DecelerateInterpolator());

        animator.addUpdateListener(animation -> {
            obj1.setX((float) animator.getAnimatedValue("x1"));
            obj1.setY((float) animator.getAnimatedValue("y1"));
            obj2.setX((float) animator.getAnimatedValue("x2"));
            obj2.setY((float) animator.getAnimatedValue("y2"));
            obj2.setPayon((float) animator.getAnimatedValue("rayon"));
            obj3.setX((float) animator.getAnimatedValue("x3"));
            obj3.setY((float) animator.getAnimatedValue("y3"));
            obj4.setX((float) animator.getAnimatedValue("x4"));
            obj4.setY((float) animator.getAnimatedValue("y4"));

            invalidate();
        });
        return animator;
    }
}
