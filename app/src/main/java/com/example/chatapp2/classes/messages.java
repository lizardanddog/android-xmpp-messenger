package com.example.chatapp2.classes;

import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.RectF;

import java.util.ArrayList;

public class messages {

    public String text;
    public int sender;
    public Paint textPaint;
    public Paint rectPaint1;
    public ArrayList<String> displayText;
    public int relativetextSize; // Width of the message bubble
    public float YT; // Top Y coordinate
    public float YB; // Bottom Y coordinate
    public RectF rect;
    public int msgType; // 0: text, 1: audio, 2: image, 3: video
    public String filename;
    public boolean isPlaying;
    public boolean isLoading;
    public int vocalDuration = -1;
    public Bitmap iconBitmap;
    public Runnable mFileAndIconLoader;

    public messages(String text, int sender, Paint textPaint, Paint rectPaint1, int textWidth) {
        this.text = text;
        this.sender = sender;
        this.textPaint = textPaint;
        this.rectPaint1 = rectPaint1;
        this.displayText = formatText(text, textWidth);
        this.rect = new RectF();
        this.msgType = 0;
    }

    public messages(int sender, Paint rectPaint1, String filename) {
        this.sender = sender;
        this.rectPaint1 = rectPaint1;
        this.filename = filename;
        this.msgType = 1;
        this.rect = new RectF();
    }

    public messages(int sender, Paint rectPaint1, CharSequence text, Bitmap iconBitmap) {
        this.sender = sender;
        this.rectPaint1 = rectPaint1;
        this.text = text.toString();
        this.iconBitmap = iconBitmap;
        this.rect = new RectF();
    }

    private ArrayList<String> formatText(String text, int textWidth) {
        ArrayList<String> formatted = new ArrayList<>();
        // Simple text wrapping logic
        int start = 0;
        while (start < text.length()) {
            int end = Math.min(start + 20, text.length());
            formatted.add(text.substring(start, end));
            start = end;
        }
        this.relativetextSize = Math.min(text.length() * 30, textWidth);
        return formatted;
    }

    public int getSender() {
        return sender;
    }

    public void setYT(float YT) {
        this.YT = YT;
    }

    public void setYB(float YB) {
        this.YB = YB;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public void setPlaying(boolean playing) {
        isPlaying = playing;
    }

    public void setMsgType(int msgType) {
        this.msgType = msgType;
    }

    public void setVocalDuration(int vocalDuration) {
        this.vocalDuration = vocalDuration;
    }

    public int getVocalDuration() {
        return vocalDuration;
    }

    public void setmFileAndIconLoader(Runnable loader) {
        this.mFileAndIconLoader = loader;
    }
}
