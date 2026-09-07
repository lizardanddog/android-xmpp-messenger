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
import android.graphics.RectF;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.example.chatapp2.R;
import com.example.chatapp2.classes.movingObjects;

import java.io.IOException;

public class otherFeaturesView extends View {

    public int screenWidth;
    public int screenHeight;
    implementOtherFeaturesView implementOtherFeaturesView;
    boolean firstMeasure;
    Bitmap camera;
    Bitmap galerie;
    Bitmap sendMoney;
    Bitmap game;
    RectF greenRound;
    Paint greenPaint;
    movingObjects cameraObject;
    movingObjects galerieObject;
    movingObjects sendMoneyObject;
    movingObjects gameObject;
    Paint cor;
    boolean isVisible;
    boolean animationSens; // Sens de l'animation, aller ou retour
    int animation_state; //Indique ou l'on se trouve dans l'animation d'apparition/de disparition

    public otherFeaturesView(Context context) {
        super(context);
        implementOtherFeaturesView=null;
        init(null);
    }

    public otherFeaturesView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        implementOtherFeaturesView=null;
        init(attrs);
    }

    public otherFeaturesView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        implementOtherFeaturesView=null;
        init(attrs);
    }

    public otherFeaturesView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        implementOtherFeaturesView=null;
        init(attrs);
    }
    public interface implementOtherFeaturesView{
        void getScreenSize();
        void onCameraClick();
    }

    public void setImplementOtherFeaturesView(implementOtherFeaturesView implementOtherFeaturesView){
        this.implementOtherFeaturesView=implementOtherFeaturesView;
    }

    private void init(@Nullable AttributeSet set) {
        firstMeasure=true;
        cor= new Paint();
        isVisible=false;
        animationSens=true;
        greenRound=new RectF();
        greenPaint=new Paint();
        greenPaint.setColor(Color.BLACK);
        greenPaint.setAlpha(150);

    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        if (implementOtherFeaturesView!=null){
            implementOtherFeaturesView.getScreenSize();
            setMeasuredDimension(screenWidth-screenHeight/13,screenHeight/9);
        }

        if (firstMeasure){
            Resources res=getResources();
            try {
                camera = ImageDecoder.decodeBitmap(ImageDecoder.createSource(res, R.drawable.emojismall));
                galerie=ImageDecoder.decodeBitmap(ImageDecoder.createSource(res, R.drawable.emojidarksmall));
                sendMoney= ImageDecoder.decodeBitmap(ImageDecoder.createSource(res, R.drawable.optionsmall));
                game= ImageDecoder.decodeBitmap(ImageDecoder.createSource(res, R.drawable.optionsmalldark));

            } catch (IOException e) {
                e.printStackTrace();
            }
         camera=   Bitmap.createScaledBitmap(camera, (int) (screenHeight/19), (int) (screenHeight/19), true);
         galerie=   Bitmap.createScaledBitmap(galerie, (int) (screenHeight/19), (int) (screenHeight/19), true);
         sendMoney=   Bitmap.createScaledBitmap(sendMoney, (int) (screenHeight/19), (int) (screenHeight/19), true);
         game=   Bitmap.createScaledBitmap(game, (int) (screenHeight/19), (int) (screenHeight/19), true);

         cameraObject = new movingObjects(camera,screenWidth-screenHeight/13-5-screenHeight/19,
                 screenHeight/18+5,false);
         galerieObject=new movingObjects(galerie,screenWidth-screenHeight/13-5-screenHeight/19,
                 screenHeight/18+5,false);
         sendMoneyObject=new movingObjects(sendMoney,screenWidth-screenHeight/13-5-screenHeight/19,
                 screenHeight/18+5,false);
         gameObject=new movingObjects(game,screenWidth-screenHeight/13-5-screenHeight/19,
                 screenHeight/18+5,false);

         greenRound.set(screenWidth-screenHeight/13-5-screenHeight/19,
                 screenHeight/18+5,screenWidth-screenHeight/13-5,screenHeight/18+5+screenHeight/19);

         firstMeasure=false;
        }
        }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        if(isVisible){
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:

                if (isObjectClicked(cameraObject,event.getX(),event.getY())){
                    greenRound.set(cameraObject.getX(),cameraObject.getY(),cameraObject.getX()+cameraObject.getOneBitmap().getWidth(),
                            cameraObject.getY()+cameraObject.getOneBitmap().getHeight());
                }
                else if (isObjectClicked(sendMoneyObject,event.getX(),event.getY())){
                    greenRound.set(sendMoneyObject.getX(),sendMoneyObject.getY(),sendMoneyObject.getX()+sendMoneyObject.getOneBitmap().getWidth(),
                            sendMoneyObject.getY()+sendMoneyObject.getOneBitmap().getHeight());
                }
                else if (isObjectClicked(gameObject,event.getX(),event.getY())){
                    greenRound.set(gameObject.getX(),gameObject.getY(),gameObject.getX()+gameObject.getOneBitmap().getWidth(),
                            gameObject.getY()+gameObject.getOneBitmap().getHeight());
                }
                else if (isObjectClicked(galerieObject,event.getX(),event.getY())){
                    greenRound.set(galerieObject.getX(),galerieObject.getY(),galerieObject.getX()+galerieObject.getOneBitmap().getWidth(),
                            galerieObject.getY()+galerieObject.getOneBitmap().getHeight());
                }
                invalidate();
                return true;

            case MotionEvent.ACTION_UP:

                if (isObjectClicked(cameraObject,event.getX(),event.getY())){
                    if(implementOtherFeaturesView!=null){
                        objectClicked=1;
                        entryAnimation();

                    }
                }


                greenRound.set(0,0,0,0);
                invalidate();
                return true;
        }}return true;
    }
int objectClicked=0; // 0 is nothing; 1 is Camera Etc.
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(isVisible){

        canvas.drawBitmap(gameObject.getOneBitmap(),gameObject.getX(),gameObject.getY(),cor);
        canvas.drawBitmap(sendMoneyObject.getOneBitmap(),sendMoneyObject.getX(),sendMoneyObject.getY(),cor);
        canvas.drawBitmap(galerieObject.getOneBitmap(),galerieObject.getX(),galerieObject.getY(),cor);
        canvas.drawBitmap(cameraObject.getOneBitmap(),cameraObject.getX(),cameraObject.getY(),cor);
        canvas.drawRoundRect(greenRound,100,100,greenPaint);
            }

       }


    private ValueAnimator createAnimator(movingObjects cameraObject1,movingObjects galerieObject1, movingObjects sendMoneyObject1, movingObjects gameObject1, float v1, float v2,
                                         float v3, float v4, float v5, int duration) {

        PropertyValuesHolder propertyV = PropertyValuesHolder.ofFloat("v" ,v1, v2);
        PropertyValuesHolder propertyV2 = PropertyValuesHolder.ofFloat("v2" ,galerieObject1.getX(), v3);
        PropertyValuesHolder propertyV3 = PropertyValuesHolder.ofFloat("v3" ,sendMoneyObject1.getX(), v4);
        PropertyValuesHolder propertyV4 = PropertyValuesHolder.ofFloat("v4" ,gameObject1.getX(), v5);


        ValueAnimator animator = new ValueAnimator();
        animator.setValues(propertyV, propertyV2, propertyV3, propertyV4);
        animator.setDuration(duration);
        animator.setInterpolator(new AccelerateInterpolator());

        //animator.setInterpolator(new LinearInterpolator());

        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                switch(animation_state){

                    case 0:
                        cameraObject1.setY( (float) animator.getAnimatedValue("v"));
                        Log.i("sabaka",""+cameraObject1.getY());
                        cameraObject1.setAnimated(true);

                        break;
                    case 1:
                        galerieObject1.setX((float) animator.getAnimatedValue("v2"));
                        galerieObject1.setAnimated(true);
                        sendMoneyObject1.setX((float) animator.getAnimatedValue("v3"));
                        sendMoneyObject1.setAnimated(true);
                        gameObject1.setX( (float) animator.getAnimatedValue("v4"));
                        gameObject1.setAnimated(true);
                        break;

//                    case 2:
//                        sendMoneyObject1.setX(galerieObject1.getX() -(float) animator.getAnimatedValue("v"));
//                       break;
//                    case 3:
//                        gameObject1.setX( sendMoneyObject1.getX()-(float) animator.getAnimatedValue("v"));
//                        break;

                }
                invalidate();
            }

        });

        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                if (animationSens){
                switch(animation_state){
                    case 0:
                      galerieObject1.setX(cameraObject1.getX());
                      galerieObject1.setY(cameraObject1.getY());
                        sendMoneyObject1.setX(galerieObject1.getX());
                        sendMoneyObject1.setY(galerieObject1.getY());
                        gameObject1.setX( sendMoneyObject1.getX());
                        gameObject1.setY( sendMoneyObject1.getY());
                        cameraObject1.setAnimated(false);
                        animation_state=1;
                      animator.start();
                        break;

                    case 1:
                        animationSens=false;
                        gameObject1.setAnimated(false);
                        sendMoneyObject1.setAnimated(false);
                        galerieObject1.setAnimated(false);
                }}
                else{
                    switch(animation_state) {
                    case 0:
                        animationSens=true;
                        cameraObject.setAnimated(false);

                        if (objectClicked==1){
                            objectClicked=0;
                            if(implementOtherFeaturesView!=null){
                                isVisible=false;
                                implementOtherFeaturesView.onCameraClick();
                            }
                        }


                        break;
                    case 1:

                        sendMoneyObject1.setX(screenWidth-screenHeight/13-5-screenHeight/19);
                        sendMoneyObject1.setY(screenHeight/18+5);
                        galerieObject1.setX(screenWidth-screenHeight/13-5-screenHeight/19);
                        galerieObject1.setY(screenHeight/18+5);
                        gameObject1.setX( screenWidth-screenHeight/13-5-screenHeight/19);
                        gameObject1.setY( screenHeight/18+5);
                        animation_state = 0;
                        gameObject1.setAnimated(false);
                        sendMoneyObject1.setAnimated(false);
                        galerieObject1.setAnimated(false);

                        animator.start();
                        break;

                }


                }

            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
        return animator;
    }

    public void entryAnimation(){
        isVisible=true;
        if (animationSens && !cameraObject.isAnimated() && !gameObject.isAnimated()){
        createAnimator(cameraObject,galerieObject,sendMoneyObject,gameObject,cameraObject.getY(), cameraObject.getY()-(screenHeight/18.0f+5f), galerieObject.getX()-galerieObject.getOneBitmap().getWidth()*1.10f,sendMoneyObject.getX()-galerieObject.getOneBitmap().getWidth()*2.20f,gameObject.getX()-galerieObject.getOneBitmap().getWidth()*3.30f,125).start();}
        else if (!cameraObject.isAnimated() && !gameObject.isAnimated()){
            createAnimator(cameraObject,galerieObject,sendMoneyObject,gameObject,cameraObject.getY(),screenHeight/18+5, cameraObject.getX(), cameraObject.getX(),cameraObject.getX(),125).start();
        }
    }

    public boolean isObjectClicked(movingObjects object,float x, float y){

        if (x>object.getX() && x<object.getX()+object.getOneBitmap().getWidth() && y>object.getY() && y<object.getY()+object.getOneBitmap().getHeight()){
            Log.i("I love escort munich","help");
            return true;
        }
        else{
            return false;
        }

    }


}
