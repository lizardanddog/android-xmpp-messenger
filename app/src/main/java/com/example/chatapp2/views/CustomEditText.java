package com.example.chatapp2.views;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ImageDecoder;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.chatapp2.ChatBoxActivity;
import com.example.chatapp2.R;

import java.io.IOException;

public class CustomEditText extends androidx.appcompat.widget.AppCompatEditText {
    Drawable mClearButtonImage;
    public Drawable scaleEmojiImage;
    Bitmap bitmapEmoji;
    public Drawable otherFeatures;
    Bitmap otherFeaturesBitmap;
    Bitmap bitmapEmojiDark;
    Drawable scaleEmojiDark;
    Bitmap otherFeaturesBitmapdark;
    Drawable otherFeaturesdark;
    messagesenderButton messagesenderbutton;
    otherFeaturesView otherFeaturesView;
    ImplementCustomEditText implementCustomEditText;

    // Booleans of firsts:
    boolean firstMeasure;



   public int trueWidth;
   public int trueHeigth;


    @RequiresApi(api = Build.VERSION_CODES.P)
    public CustomEditText(@NonNull Context context) throws IOException {
        super(context);
        this.implementCustomEditText=null;
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    public CustomEditText(@NonNull Context context, @Nullable AttributeSet attrs) throws IOException {
        super(context, attrs);
        this.implementCustomEditText=null;
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    public CustomEditText(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) throws IOException {
        super(context, attrs, defStyleAttr);
        this.implementCustomEditText=null;
        init();
    }

    public interface ImplementCustomEditText{
        public void getScreenSize();
    }
    public void ImplementCustomEditText(ImplementCustomEditText implementCustomEditText){
        this.implementCustomEditText=implementCustomEditText;
    }
    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        if (implementCustomEditText!=null){
            implementCustomEditText.getScreenSize();
            setMeasuredDimension(trueWidth-trueHeigth/13,trueHeigth/18);
        }
    if (firstMeasure){
        Resources res=getResources();
        try {
            bitmapEmoji = ImageDecoder.decodeBitmap(ImageDecoder.createSource(res, R.drawable.emojismall));
            bitmapEmojiDark=ImageDecoder.decodeBitmap(ImageDecoder.createSource(res, R.drawable.emojidarksmall));
            otherFeaturesBitmap= ImageDecoder.decodeBitmap(ImageDecoder.createSource(res, R.drawable.optionsmall));
            otherFeaturesBitmapdark= ImageDecoder.decodeBitmap(ImageDecoder.createSource(res, R.drawable.optionsmalldark));

        } catch (IOException e) {
            e.printStackTrace();
        }

        scaleEmojiImage=new BitmapDrawable(res, Bitmap.createScaledBitmap(bitmapEmoji, (int) (trueHeigth/19), (int) (trueHeigth/19), true));
        scaleEmojiDark=new BitmapDrawable(res, Bitmap.createScaledBitmap(bitmapEmojiDark, (int) (trueHeigth/19), (int) (trueHeigth/19), true));
        otherFeatures=new BitmapDrawable(res, Bitmap.createScaledBitmap(otherFeaturesBitmap, (int) (trueHeigth/19), (int) (trueHeigth/19) ,true));
        otherFeaturesdark=new BitmapDrawable(res, Bitmap.createScaledBitmap(otherFeaturesBitmapdark, (int) (trueHeigth/19), (int) (trueHeigth/19), true));

        setCompoundDrawablesRelativeWithIntrinsicBounds
                (scaleEmojiImage,                      // Start of text.
                        null,               // Above text.
                        otherFeatures,  // End of text.
                        null);


        firstMeasure=false;
    }
if (ChatBoxActivity.stateOfActivity!=0){
    setCompoundDrawablesRelativeWithIntrinsicBounds
            (scaleEmojiImage,                      // Start of text.
                    null,               // Above text.
                    null,  // End of text.
                    null);
}


    }

    @Override
    protected void onAttachedToWindow() {

        super.onAttachedToWindow();
        ConstraintLayout parentActivity = (ConstraintLayout)  this.getParent();
        messagesenderbutton = (messagesenderButton) parentActivity.findViewById(R.id.messagesenderButton);
        otherFeaturesView=(com.example.chatapp2.views.otherFeaturesView)  parentActivity.findViewById(R.id.otherFeaturesView);
    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    private void init() {
        long t1=System.nanoTime();

        firstMeasure=true;

        setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                if ((getCompoundDrawablesRelative()[2] != null)) {
                    float clearButtonStart; // Used for LTR languages
                    float clearButtonEnd;  // Used for RTL languages
                    boolean isOtherFeaturesButtonClicked = false;
                    boolean isEmojibuttonClicked = false;
                    // TODO: Detect the touch in RTL or LTR layout direction.
                    // TODO: Check for actions if the button is tapped.

                    clearButtonStart = (getWidth() - getPaddingEnd()
                            - otherFeatures.getIntrinsicWidth());
                    if (event.getX() > clearButtonStart && event.getX()<getWidth()-getPaddingEnd() && ChatBoxActivity.stateOfActivity==0) {
                        isOtherFeaturesButtonClicked = true;
                    }
                    if (event.getX() > getPaddingStart() && event.getX() < getPaddingStart()+scaleEmojiImage.getIntrinsicWidth()){
                        isEmojibuttonClicked=true;
                    }

                    if (isOtherFeaturesButtonClicked ) {
                        if (event.getAction() == MotionEvent.ACTION_DOWN) {
                            // Switch to the black version of clear button.
                            setCompoundDrawablesRelativeWithIntrinsicBounds
                                    (scaleEmojiImage,                      // Start of text.
                                            null,               // Above text.
                                            otherFeaturesdark,  // End of text.
                                            null);
                        }
                        // Check for ACTION_UP.
                        if (event.getAction() == MotionEvent.ACTION_UP) {
                            setCompoundDrawablesRelativeWithIntrinsicBounds
                                    (scaleEmojiImage,                      // Start of text.
                                            null,               // Above text.
                                            otherFeatures,  // End of text.
                                            null);
                            otherFeaturesView.entryAnimation();
                            return true;
                        }
                    }
                    else if(isEmojibuttonClicked) {
                        if (event.getAction() == MotionEvent.ACTION_DOWN) {
                            // Switch to the black version of clear button.
                            setCompoundDrawablesRelativeWithIntrinsicBounds
                                    (scaleEmojiDark,                      // Start of text.
                                            null,               // Above text.
                                            otherFeatures,  // End of text.
                                            null);
                        }
                        // Check for ACTION_UP.
                        if (event.getAction() == MotionEvent.ACTION_UP) {
                            setCompoundDrawablesRelativeWithIntrinsicBounds
                                    (scaleEmojiImage,                      // Start of text.
                                            null,               // Above text.
                                            otherFeatures,  // End of text.
                                            null);
                            return true;
                        }
                    }
                    else{
                        return false;
                    }

                }


                return false;




            }
        });

        addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String text = getText().toString();
                if (text.equals("") && ChatBoxActivity.stateOfActivity==0){
                    showClearButton();
                    messagesenderbutton.senderObject.setCase(0);
                    messagesenderbutton.invalidate();
                }else{
                    hideClearButton();
                    messagesenderbutton.senderObject.setCase(1);
                    messagesenderbutton.invalidate();}

            }

            @Override
            public void afterTextChanged(Editable editable) {


            }
        });
        // setBackgroundResource(android.R.color.transparent);
       // requestFocus();

        long t2=System.nanoTime();
        long time=t2-t1;
        Log.i("AAAcustomeditInit",""+time);
    }

    private void showClearButton() {
        setCompoundDrawablesRelativeWithIntrinsicBounds
                (scaleEmojiImage,                      // Start of text.
                        null,               // Above text.
                        otherFeatures,  // End of text.
                        null);              // Below text.
    }
    private void hideClearButton() {
        setCompoundDrawablesRelativeWithIntrinsicBounds
                (scaleEmojiImage,             // Start of text.
                        null,      // Above text.
                        null,      // End of text.
                        null);     // Below text.
    }


}
