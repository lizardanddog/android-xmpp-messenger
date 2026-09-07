package com.example.chatapp2.views;

import static java.util.Objects.isNull;

import android.animation.Animator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ImageDecoder;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.media.MediaPlayer;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.chatapp2.ChatBoxActivity;
import com.example.chatapp2.ChatListActivity;
import com.example.chatapp2.MainActivity;
import com.example.chatapp2.R;
import com.example.chatapp2.classes.RendererFactory;
import com.example.chatapp2.classes.messages;
import com.example.chatapp2.classes.movingObjects;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class chatBox extends View implements GestureDetector.OnGestureListener {

    // Variables for messages
    public ArrayList<messages> messagesList = new ArrayList<messages>(); // This list increments with SQL and new messages
    public ArrayList<messages> messagesListToLoad = new ArrayList<messages>(); // messages to load
    Paint textPaint;
    Paint rectPaint1;

    float Delta; // difference between two successive finger positions
    float Delta0; // Old difference between two successive finger positions
    float Delta1; // Maximum difference of positions to load messages
    float yOld; // Y axis of the first touched finger
    float yNew; // Relative to yOld
    public float contentBottomY; // Corresponds to the bottom of the lowest text
    float relativeTextPosition;
    int index; // Loop index for displaying messages
    boolean has_appeared; // boolean determining if the highest element of the list has appeared
    boolean isContentScrolled; // Flag for scrolled content
    boolean isContentScrolledWithKeyboard;
    float oldContentBottomY;
    float YT1;
    public float contentTopYOffset; // distance between contentBottomY and the top message in the list
    public int trueWidth;
    public int trueHeigth;
    public boolean firstmeasure;
    int msgClicked;

    public int viewSizeWithKeyboard;
    public ArrayList<messages> chargingMessages;


  public  float viewportHeightWithKeyboard; // Height after keyboard appearance
  public   float skinny_Height; // Previous height (with or without keyboard)
  float old_skinny_Height;
  float new_old_skinny_Height;
  public  int delta_height; // Is there a height difference?

  boolean topMessageInViewAtStart; // Presence of the first message at start
  boolean topMessageinViewAtStart2sBrother; // Flag for top message at keyboard start
  public boolean isScrollNeeded;
  GestureDetector gestureDetector ;

  boolean isLayoutFirst;

  ValueAnimator animator1;

  float KeyboardOnHeigth;

  float deltaAnimatorReste;
public implementChatbox implementChatbox;
public boolean isVocalPlaying;

//Bitmaps:
    //Bitmaps for playing audio
    Bitmap playbtn;
    Bitmap pausebtn;
    movingObjects playPauseObj;

    // Variables for renderer and vocal messages:
    private WaveformRenderer renderer;
    private byte[] waveform;
    private Paint vocalPaint1;
    private Paint vocalPaint2;
    private RectF vocalRect1;
    private RectF vocalSlidingRect;
    private Paint vocalSlidingPaint;

    public boolean iskeyboardToUp;

    Paint genericPaint; // Paint used for drawing bitmaps
   public int oldHeight;
   public int lastoldHeight;
   int finHeight;
   public boolean isKeyboardHere;

    public interface implementChatbox{
        void fetchSqlMsg();
        void chargeSqlMsg();
        void getScreenSize();
        void startPlaying(messages message);
        void stopPlaying(messages message);
        boolean onStopPlaying(messages message);
        int getVocalCurrentPosition();
        void launchImageDisplayer(int msgClicked33);
        void launchVideoDisplayer(int msgClicked34);
    }
    public interface WaveformRenderer {
        void render(messages vocalMessage, Canvas canvas, byte[] waveform);
    }

    public chatBox(Context context) {
        super(context);
        init(null);
        this.implementChatbox=null;
    }


    public chatBox(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
        this.implementChatbox=null;

    }

    public chatBox(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
        this.implementChatbox=null;
    }

    public chatBox(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs);
        this.implementChatbox=null;
    }
    public void setImplementChatbox(implementChatbox implementChatbox){
    this.implementChatbox=implementChatbox;
    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (implementChatbox!=null && firstmeasure){

            implementChatbox.getScreenSize();
            setMeasuredDimension(trueWidth,11*trueHeigth/13);
            oldHeight=11*trueHeigth/13;
            try {
                playbtn= ImageDecoder.decodeBitmap(ImageDecoder.createSource(getResources(), R.drawable.playbtn));
                pausebtn= ImageDecoder.decodeBitmap(ImageDecoder.createSource(getResources(), R.drawable.pausebtn));
                ArrayList<Bitmap> bmp= new ArrayList<>();
                bmp.add(playbtn);
                bmp.add(pausebtn);
                playPauseObj=new movingObjects(bmp, 0,0,0);

            } catch (IOException e) {
                e.printStackTrace();
            }
            firstmeasure=false;
        }else{
            if (iskeyboardToUp && !isKeyboardHere){

                if (Math.abs(View.MeasureSpec.getSize(heightMeasureSpec)) <= 11*trueHeigth/13){

                    setMeasuredDimension(widthMeasureSpec,heightMeasureSpec);
                    oldHeight=View.MeasureSpec.getSize(heightMeasureSpec);
                    lastoldHeight=oldHeight;
                }
                else{
                    setMeasuredDimension(trueWidth,11*trueHeigth/13);
                    finHeight=11*trueHeigth/13;
                }}
            else {

                if (Math.abs(View.MeasureSpec.getSize(heightMeasureSpec)) == finHeight || Math.abs(View.MeasureSpec.getSize(heightMeasureSpec))<=lastoldHeight ){

                    setMeasuredDimension(widthMeasureSpec,oldHeight);}
                else{
                    setMeasuredDimension(widthMeasureSpec,heightMeasureSpec);
                    oldHeight=View.MeasureSpec.getSize(heightMeasureSpec);

                }

            }

        }
}

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        if (isLayoutFirst){
            contentBottomY=getHeight()-10;
            if(implementChatbox!=null){
            implementChatbox.fetchSqlMsg();}
            index=messagesList.size()-1;
            relativeTextPosition=contentBottomY;
            while (relativeTextPosition>0 && index>=0) {
                // Recalculate message position relative to contentBottomY
                messagesList.get(index).rect.set(40 * (messagesList.get(index).getSender()) + (1 - messagesList.get(index).getSender()) * (3 * getWidth() / 4 -
                                messagesList.get(index).relativetextSize), (messagesList.get(index).YT - messagesList.get(index).YB) - 20 + relativeTextPosition,
                        (messagesList.get(index).relativetextSize + getWidth() / 4) * messagesList.get(index).getSender() + (1 - messagesList.get(index).getSender()) * (getWidth() - 40), relativeTextPosition);
                index--;
            }
            viewportHeightWithKeyboard=getHeight();
            skinny_Height=0;


        }
        skinny_Height=getHeight();

        if (Math.abs(contentTopYOffset)<=getHeight() && !isLayoutFirst){
            contentBottomY=getHeight()-10;
            Delta=0;
            setY(viewSizeWithKeyboard);
        }
        else if(!isLayoutFirst){
        if ((viewportHeightWithKeyboard==getHeight() || finHeight==getHeight()) && !isLayoutFirst){
            contentBottomY=contentBottomY+(skinny_Height-old_skinny_Height);
            topMessageinViewAtStart2sBrother=false;
        }
        else if(viewportHeightWithKeyboard!=getHeight() && !isLayoutFirst) {

            contentBottomY = contentBottomY + (skinny_Height - old_skinny_Height);
            YT1 = contentBottomY + contentTopYOffset;
            if (messagesList.size() - 1 >= 0) {
                if (isScrollNeeded && YT1 > (messagesList.get(0).YT - messagesList.get(0).YB)) {
                    Delta = getHeight();
                    isScrollNeeded = false;
                }
            }


            if (messagesList.get(0).rect.top > 10) {
                topMessageinViewAtStart2sBrother = true;
            } else {
                topMessageinViewAtStart2sBrother = false;
            }
        }
            setY(viewSizeWithKeyboard);
        }


        if (messagesList.size()>0){
            topMessageInViewAtStart=messagesList.get(0).rect.top>10;
        }
        invalidate();
        old_skinny_Height=skinny_Height;
        isLayoutFirst=false;
    }


    private void init(@Nullable AttributeSet set) {
        iskeyboardToUp=false;
        isKeyboardHere=false;
        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setColor(Color.BLACK);
        textPaint.setTextSize(50);
        rectPaint1 = new Paint(Paint.ANTI_ALIAS_FLAG);
        rectPaint1.setColor(Color.CYAN);

        genericPaint=new Paint();
        has_appeared=false;
        yOld=-1;
        YT1=-1;
        topMessageInViewAtStart=true;
        gestureDetector=new GestureDetector(this);
        isLayoutFirst=true;
        deltaAnimatorReste=0;
        isContentScrolled=false;
        isContentScrolledWithKeyboard=false;
        firstmeasure=true;
        topMessageinViewAtStart2sBrother=false;
        isScrollNeeded=false;
        contentTopYOffset=0;
        isVocalPlaying=false;

        RendererFactory rendererFactory = new RendererFactory();
        renderer= rendererFactory.createSimpleWaveformRenderer(Color.BLACK, Color.WHITE);
        vocalRect1 = new RectF();

        vocalPaint1 = new Paint();
        vocalPaint1.setColor(Color.DKGRAY);
        vocalPaint1.setAlpha(20);
        vocalPaint2 = new Paint();
        vocalPaint2.setColor(Color.DKGRAY);
        vocalPaint2.setStyle(Paint.Style.STROKE);;
        vocalPaint2.setStrokeWidth(5);
        vocalSlidingRect = new RectF();
        vocalSlidingPaint = new Paint();
        vocalSlidingPaint.setColor(Color.DKGRAY);
        vocalSlidingPaint.setAlpha(50);

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    protected void onDraw(Canvas canvas) {

        super.onDraw(canvas);
        contentBottomY=Math.max(getHeight()-10,contentBottomY+Delta);
        YT1=contentBottomY+contentTopYOffset;

        if(YT1<0){
            isContentScrolledWithKeyboard=true;
        }
            if (Math.abs(contentTopYOffset)>=getHeight()) {
            if (YT1>=10){
                contentBottomY=contentBottomY-YT1;
                YT1=contentBottomY+contentTopYOffset;
            }
                contentBottomY=Math.min(contentBottomY,contentBottomY-YT1);
            }


        relativeTextPosition=contentBottomY;
        index=messagesList.size()-1;

        while (relativeTextPosition>0 && index>=0){
            switch (messagesList.get(index).msgType) {

                case 0:
                messagesList.get(index).rect.set(40 * (messagesList.get(index).getSender()) + (1 - messagesList.get(index).getSender()) * (3 * getWidth() / 4 -
                                messagesList.get(index).relativetextSize), (messagesList.get(index).YT - messagesList.get(index).YB) - 20 + relativeTextPosition,
                        (messagesList.get(index).relativetextSize + getWidth() / 4) * messagesList.get(index).getSender() + (1 - messagesList.get(index).getSender()) * (getWidth() - 40), relativeTextPosition);
                break;
                case 1:

                    messagesList.get(index).rect.set(40 * (messagesList.get(index).getSender()) + (1 - messagesList.get(index).getSender()) * (getWidth() / 4),
                            (messagesList.get(index).YT - messagesList.get(index).YB) - 20 + relativeTextPosition,
                            (3*getWidth() / 4) * messagesList.get(index).getSender() + (1 - messagesList.get(index).getSender()) * (getWidth() - 40),
                            relativeTextPosition
                            );

                    break;

                case 2:
                    messagesList.get(index).rect.set(40 * (messagesList.get(index).getSender()) + (1 - messagesList.get(index).getSender()) * (3*getWidth() / 5),
                            (messagesList.get(index).YT - messagesList.get(index).YB) - 20 + relativeTextPosition,
                            (2*getWidth() / 5) * messagesList.get(index).getSender() + (1 - messagesList.get(index).getSender()) * (getWidth() - 40),
                            relativeTextPosition
                    );

                    break;
                case 3:
                    messagesList.get(index).rect.set(40 * (messagesList.get(index).getSender()) + (1 - messagesList.get(index).getSender()) * (3*getWidth() / 5),
                            (messagesList.get(index).YT - messagesList.get(index).YB) - 20 + relativeTextPosition,
                            (2*getWidth() / 5) * messagesList.get(index).getSender() + (1 - messagesList.get(index).getSender()) * (getWidth() - 40),
                            relativeTextPosition
                    );

                    break;
            }
            if(messagesList.get(index).rect.top<0 && !isContentScrolled && viewportHeightWithKeyboard==getHeight()){
                isContentScrolled=true;
            }
            if(messagesList.get(index).rect.top<0 && !isContentScrolledWithKeyboard && viewportHeightWithKeyboard!=getHeight()){

                isContentScrolledWithKeyboard=true;
            }
            if(index==0 && messagesList.get(index).rect.top <0 && topMessageInViewAtStart){
                topMessageInViewAtStart=false;

            }
            if(index==0 && !topMessageInViewAtStart){
                has_appeared=true;

                if(implementChatbox!=null){
                    implementChatbox.chargeSqlMsg();}

                messagesListToLoad.addAll(messagesList);
                messagesList=messagesListToLoad;
            }


            if (messagesList.get(index).rect.top<getHeight()){
                canvas.drawRoundRect(messagesList.get(index).rect, 24, 24, rectPaint1);
                switch(messagesList.get(index).msgType){
                    case 0:
                     for (int i = 0; i < messagesList.get(index).displayText.size(); i++) {
                        canvas.drawText(messagesList.get(index).displayText.get(i), 10+40*(messagesList.get(index).getSender())+(1-messagesList.get(index).getSender())*(3*getWidth()/4-
                            messagesList.get(index).relativetextSize), relativeTextPosition- 10 - messagesList.get(index).displayText.size() * 55 + 55 * (i + 1) , textPaint);
                     }
                     break;
                    case 1:
                        vocalRect1.set((messagesList.get(index).rect.left+messagesList.get(index).rect.width()*0.20f) ,
                                (messagesList.get(index).YT - messagesList.get(index).YB) - 10 + relativeTextPosition,
                                (messagesList.get(index).rect.width()*0.75f+messagesList.get(index).rect.left+messagesList.get(index).rect.width()*0.20f),
                                relativeTextPosition-10);
                        canvas.drawRoundRect(vocalRect1, 24, 24, vocalPaint1);
                        canvas.drawRoundRect(vocalRect1, 24, 24, vocalPaint2);
                        if (implementChatbox!=null && messagesList.get(index).isPlaying) {
                            vocalSlidingRect.set((messagesList.get(index).rect.left + messagesList.get(index).rect.width() * 0.20f),
                                    (messagesList.get(index).YT - messagesList.get(index).YB) - 10 + relativeTextPosition,
                                    (messagesList.get(index).rect.width() * 0.75f)*(implementChatbox.getVocalCurrentPosition()/messagesList.get(index).getVocalDuration()) + (messagesList.get(index).rect.left + messagesList.get(index).rect.width() * 0.20f),
                                    relativeTextPosition - 10);
                            canvas.drawRoundRect(vocalSlidingRect,24,24,vocalSlidingPaint);
                                                                                     }

                        if (!messagesList.get(index).isPlaying){
                        canvas.drawBitmap(playPauseObj.getBitmap().get(0),10+40*(messagesList.get(index).getSender())+(1-messagesList.get(index).getSender())*(getWidth()/4),relativeTextPosition-messagesList.get(index).rect.height()*0.5f-playbtn.getHeight()/2,genericPaint);
                                renderer.render(messagesList.get(index),canvas,null);
                                                            }
                        else{
                            canvas.drawBitmap(playPauseObj.getBitmap().get(1),10+40*(messagesList.get(index).getSender())+(1-messagesList.get(index).getSender())*(getWidth()/4),relativeTextPosition-messagesList.get(index).rect.height()*0.5f-playbtn.getHeight()/2,genericPaint);

                            }
                        break;

                    case 2:
                        canvas.drawBitmap(messagesList.get(index).iconBitmap, 2.5f + 40 * (messagesList.get(index).getSender()) + (1 - messagesList.get(index).getSender()) * (3 * getWidth() / 5),
                                relativeTextPosition - messagesList.get(index).rect.height() + messagesList.get(index).iconBitmap.getHeight() / 20,genericPaint);
                        if (messagesList.get(index).isLoading) {
                            canvas.drawBitmap(pausebtn, 2.5f + 40 * (messagesList.get(index).getSender()) + (1 - messagesList.get(index).getSender()) * (3 * getWidth() / 5),
                                    relativeTextPosition - messagesList.get(index).rect.height() + messagesList.get(index).iconBitmap.getHeight() / 20, genericPaint);
                                                            }
                        break;

                    case 3:

                            canvas.drawBitmap(messagesList.get(index).iconBitmap, 2.5f + 40 * (messagesList.get(index).getSender()) + (1 - messagesList.get(index).getSender()) * (3 * getWidth() / 5),
                                    relativeTextPosition - messagesList.get(index).rect.height() + messagesList.get(index).iconBitmap.getHeight() / 20, genericPaint);
                        if (messagesList.get(index).isLoading) {
                            canvas.drawBitmap(pausebtn, 2.5f + 40 * (messagesList.get(index).getSender()) + (1 - messagesList.get(index).getSender()) * (3 * getWidth() / 5),
                                    relativeTextPosition - messagesList.get(index).rect.height() + messagesList.get(index).iconBitmap.getHeight() / 20, genericPaint);
                        }


                        break;
                }}


            relativeTextPosition=(messagesList.get(index).YT - messagesList.get(index).YB) -20 + relativeTextPosition -15;
            index--;
            if(isVocalPlaying && implementChatbox!=null  && msgClicked<messagesList.size()){
                isVocalPlaying=implementChatbox.onStopPlaying(messagesList.get(msgClicked));
            }
            if (renderer != null && isVocalPlaying && msgClicked<messagesList.size()) {
                renderer.render(messagesList.get(msgClicked),canvas, waveform);
            }

        }

    }


    public void addNewMessage(messages newMessage){
    switch(newMessage.msgType){
        case 0:
        if (messagesList.size()==0){
            newMessage.setYT(50);
            newMessage.setYB(55+55*newMessage.displayText.size()+ newMessage.YT);
            messagesList.add(newMessage);

        }else{
            newMessage.setYT(messagesList.get(messagesList.size()-1).YB+50);
            newMessage.setYB(55+55*newMessage.displayText.size()+ newMessage.YT);
            messagesList.add(newMessage);}
            break;

        case 1:
            if (messagesList.size()==0){
                newMessage.setYT(50);
                newMessage.setYB(55+getWidth()/8);
                messagesList.add(newMessage);

            }else{
                newMessage.setYT(messagesList.get(messagesList.size()-1).YB+50);
                newMessage.setYB(55+getWidth()/8+ newMessage.YT);
                messagesList.add(newMessage);}
            break;

        case 2:
            if (messagesList.size()==0){
                newMessage.setYT(50);
                newMessage.setYB(55+getHeight()/4);
                messagesList.add(newMessage);
            }else{
                newMessage.setYT(messagesList.get(messagesList.size()-1).YB+50);
                newMessage.setYB(55+getHeight()/4+ newMessage.YT);
                messagesList.add(newMessage);}
            break;

        case 3:
            if (messagesList.size()==0){
                newMessage.setYT(50);
                newMessage.setYB(55+getHeight()/4);
                messagesList.add(newMessage);

            }else{
                newMessage.setYT(messagesList.get(messagesList.size()-1).YB+50);
                newMessage.setYB(55+getHeight()/4+ newMessage.YT);
                messagesList.add(newMessage);}

            break;

    }
    contentTopYOffset=contentTopYOffset+newMessage.YT - newMessage.YB - 20-15;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if (gestureDetector.onTouchEvent(event)) {
            return gestureDetector.onTouchEvent(event);
        }
        if(event.getPointerCount()==1){
        switch (event.getAction()) {

            case MotionEvent.ACTION_DOWN:
                yOld=-1;
                Delta=0;
                Delta0=0;
                return true;


            case MotionEvent.ACTION_MOVE:


            case MotionEvent.ACTION_UP:
                yOld=-1;
                Delta=0;
                Delta0=0;
                invalidate();
                break;
        }}
        return true;

    }


    @Override
    public boolean onDown(MotionEvent motionEvent) {
        Delta=0;
        if (!isNull(animator1)){
            animator1.cancel();
            invalidate();}
        return true;
    }

    @Override
    public void onShowPress(MotionEvent motionEvent) {
    }

    @Override
    public boolean onSingleTapUp(MotionEvent motionEvent) {
        Delta=0;

        msgClicked=index+1;
        RectF recty= new RectF();
        while (msgClicked<messagesList.size()){
        switch (messagesList.get(msgClicked).msgType){
            case 1:
               recty.set(messagesList.get(msgClicked).rect.left+10,
                   (messagesList.get(msgClicked).rect.top+messagesList.get(msgClicked).rect.height()/2-playbtn.getHeight()/2),
                  messagesList.get(msgClicked).rect.left+10+playbtn.getWidth(),
                   (messagesList.get(msgClicked).rect.top+messagesList.get(msgClicked).rect.height()/2+playbtn.getHeight()/2));
               break;
            case 2:
               recty.set(messagesList.get(msgClicked).rect.left,
                    (messagesList.get(msgClicked).rect.top),
                     messagesList.get(msgClicked).rect.right,
                    (messagesList.get(msgClicked).rect.bottom));
               break;
            case 3:
               recty.set(messagesList.get(msgClicked).rect.left,
                   (messagesList.get(msgClicked).rect.top),
                    messagesList.get(msgClicked).rect.right,
                    (messagesList.get(msgClicked).rect.bottom));
               break;
                                                  }



        if (recty.contains(motionEvent.getX(),motionEvent.getY())){
            switch (messagesList.get(msgClicked).msgType) {
                case 1:{
                    if (implementChatbox != null) {
                        if (!isVocalPlaying) {
                            implementChatbox.startPlaying(messagesList.get(msgClicked));
                            isVocalPlaying = true;
                            messagesList.get(msgClicked).isPlaying = true;
                        } else {
                            implementChatbox.stopPlaying(messagesList.get(msgClicked));
                            isVocalPlaying = false;
                            messagesList.get(msgClicked).isPlaying = false;
                               }
                                                    }
                    break;
                         }
                case 2:{
                        if (implementChatbox!=null && !messagesList.get(msgClicked).isLoading){
                            implementChatbox.launchImageDisplayer(msgClicked);
                                                                                              }
                    break;
                        }
                case 3:{
                        if (implementChatbox!=null && !messagesList.get(msgClicked).isLoading){
                            implementChatbox.launchVideoDisplayer(msgClicked);
                        }
                        break;
                        }
                                                          }
                break;
            }
           else if (messagesList.get(msgClicked).rect.top>getHeight()){

                break;
            }
             msgClicked++;

        }
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        if ((isContentScrolled || (isContentScrolledWithKeyboard && viewportHeightWithKeyboard!=getHeight()))){
            Delta=-v1;
            invalidate();
        }


        return true;
    }

    @Override
    public void onLongPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        if(isContentScrolled || (isContentScrolledWithKeyboard && viewportHeightWithKeyboard!=getHeight())) {
            float Delta_calc = (motionEvent1.getY() - motionEvent.getY());
            int upOrdown = (int) (Delta_calc / Math.abs(Delta_calc));
            if (!isNull(animator1)) {
                animator1.cancel();
            }

            Delta = Math.abs(v) / 5 * upOrdown + Delta_calc / 32;
            if (Math.abs(v) < 250 && Math.abs(Delta_calc) > getHeight() / 3) {
                v = 0;
                Delta = -10 * upOrdown;
            }
            animator1 = createAnimator(Delta, 0, setMovDuration(v));

            animator1.start();

        }

        return false;
    }

    private ValueAnimator createAnimator(float oldx,  float newx, int duration) {
        PropertyValuesHolder propertyX = PropertyValuesHolder.ofFloat("x" ,oldx, newx);
        ValueAnimator animator = new ValueAnimator();
        animator.setValues(propertyX);
        animator.setDuration(duration);
        animator.setInterpolator(new DecelerateInterpolator());
        deltaAnimatorReste=deltaAnimatorReste+Delta;

        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {

                Delta= (float) animator.getAnimatedValue("x");
                deltaAnimatorReste=deltaAnimatorReste-Delta;

                invalidate();
            }
        });

        return animator;
    }

    public int setMovDuration(float vitesse){
        return 750;
    }

    public void setWaveform(byte[] bytes) {
        this.waveform = Arrays.copyOf(bytes, bytes.length);
        invalidate();
    }
}
