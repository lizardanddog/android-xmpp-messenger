package com.example.chatapp2.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

public class chatBar extends View {

    public interface chatBarInterface {
        void getScreenSize();
    }

    public chatBarInterface chatBarInterface;
    public int trueWidth;
    public int trueHeigth;

    Bitmap goBackButton;
    Bitmap userBitmap;
    Bitmap optionsButton;

    Paint iconPaint;
    TextPaint primaryTextPaint;
    TextPaint secondaryTextPaint;

    public chatBar(Context context) {
        super(context);
        init(null);
    }

    public chatBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public void setChatBarInterface(chatBarInterface chatBarInterface) {
        this.chatBarInterface = chatBarInterface;
    }

    private void init(@Nullable AttributeSet attrs) {
        iconPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        primaryTextPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        primaryTextPaint.setColor(Color.DKGRAY);
        primaryTextPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));

        secondaryTextPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        secondaryTextPaint.setColor(Color.GRAY);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (chatBarInterface != null) {
            chatBarInterface.getScreenSize();
        }
        setMeasuredDimension(trueWidth, trueHeigth / 13);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        primaryTextPaint.setTextSize(getHeight() / 3);
        secondaryTextPaint.setTextSize(getHeight() / 4);

        if (goBackButton != null) {
            canvas.drawBitmap(goBackButton, getWidth() / 100, getWidth() / 100, iconPaint);
        }
        if (userBitmap != null) {
            canvas.drawBitmap(userBitmap, getHeight() + getWidth() / 100, getWidth() / 100, iconPaint);
        }

        canvas.drawText("User Name", getHeight() * 2 + getWidth() / 100, getHeight() / 2, primaryTextPaint);
        canvas.drawText("Online", getHeight() * 2 + getWidth() / 100, getHeight() * 3 / 4, secondaryTextPaint);

        if (optionsButton != null) {
            canvas.drawBitmap(optionsButton, getWidth() - getHeight() * 3 / 4 - getWidth() / 100, getWidth() / 80, iconPaint);
        }
    }
}
