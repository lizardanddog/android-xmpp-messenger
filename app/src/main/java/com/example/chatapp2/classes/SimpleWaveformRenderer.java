package com.example.chatapp2.classes;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.Log;

import androidx.annotation.ColorInt;

import com.example.chatapp2.views.chatBox;
import com.example.chatapp2.views.messagesenderButton;

public class SimpleWaveformRenderer implements chatBox.WaveformRenderer {
    @ColorInt
    private final int backgroundColour;
    private final Paint foregroundPaint;
    private final Path waveformPath;
    private static final int Y_FACTOR = 0xFF;
    private static final float HALF_FACTOR = 0.5f;


    static SimpleWaveformRenderer newInstance(@ColorInt int backgroundColour, @ColorInt int foregroundColour) {
        Paint paint = new Paint();
        paint.setColor(foregroundColour);
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(5);
        Path waveformPath = new Path();
        return new SimpleWaveformRenderer(backgroundColour, paint, waveformPath);
    }


    SimpleWaveformRenderer(@ColorInt int backgroundColour, Paint foregroundPaint, Path waveformPath) {
        this.backgroundColour = backgroundColour;
        this.foregroundPaint = foregroundPaint;
        this.waveformPath = waveformPath;
    }



    @Override
    public void render(messages vocalMessage, Canvas canvas, byte[] waveform) {
      //  canvas.drawColor(backgroundColour);
        float width = vocalMessage.rect.width()*0.75f;
        float height = vocalMessage.rect.height()*0.9f;
        waveformPath.reset();
        if (waveform != null) {
            renderWaveform(waveform, width, height, vocalMessage.rect.left+vocalMessage.rect.width()*0.20f,
                    vocalMessage.rect.bottom);
        } else {
            Log.i("crochet","krk");

            renderBlank(width, height,vocalMessage.rect.left+vocalMessage.rect.width()*0.20f,
                    vocalMessage.rect.bottom);
        }
        canvas.drawPath(waveformPath, foregroundPaint);
    }

    private void renderWaveform(byte[] waveform, float width, float height, float x, float y) {
        float xIncrement = width / (float) (waveform.length);
        float yIncrement = height / Y_FACTOR;
        int halfHeight = (int) (y-height * HALF_FACTOR);
        waveformPath.moveTo(x, halfHeight);
        for (int i = 1; i < waveform.length; i++) {
            float yPosition = waveform[i] > 0 ? height - (yIncrement * waveform[i]) : (-yIncrement * waveform[i]);
            Log.i("see",""+yPosition);
            waveformPath.lineTo(x+xIncrement * i, y-yPosition);
        }
        //waveformPath.lineTo(width, halfHeight);
    }


    private void renderBlank(float width, float height, float x, float y1) {
        int y = (int) (y1-height * HALF_FACTOR);
        waveformPath.moveTo(x, y);
        waveformPath.lineTo(x+width, y);
        Log.i("alba",""+x);
    }

}
