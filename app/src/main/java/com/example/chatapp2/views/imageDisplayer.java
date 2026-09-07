package com.example.chatapp2.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

public class imageDisplayer extends View {

    Bitmap bitmap;
    Paint paint;

    public imageDisplayer(Context context) {
        super(context);
        init();
    }

    public imageDisplayer(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    }

    public void setBitmap(String path, int screenWidth) {
        bitmap = BitmapFactory.decodeFile(path);
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (bitmap != null) {
            canvas.drawBitmap(bitmap, 0, 0, paint);
        }
    }
}
