package com.example.chatapp2.classes;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;

import java.util.ArrayList;

public class movingObjects {
    ArrayList<Bitmap> bitmap;
    Bitmap oneBitmap;
    float x;
    float y;
    float xc;
    float yc;
    float payon; // rayon or radius
    int Case;
    boolean isAnimated;
    RectF oval;
    Paint paint;
    boolean hasPayon;

    public movingObjects(ArrayList<Bitmap> bitmap, float x, float y, int Case) {
        this.bitmap = bitmap;
        this.x = x;
        this.y = y;
        this.Case=Case;
        this.isAnimated=false;
        this.hasPayon=false;
    }


    public movingObjects(Bitmap oneBitmap, float x, float y, boolean isAnimated) {
        this.oneBitmap = oneBitmap;
        this.x = x;
        this.y = y;
        this.isAnimated = isAnimated;
        this.hasPayon=false;
    }

    public Bitmap getOneBitmap() {
        return oneBitmap;
    }

    public void setOneBitmap(Bitmap oneBitmap) {
        this.oneBitmap = oneBitmap;
    }

    public float getXc() {
        return xc;
    }

    public void setXc(float xc) {
        this.xc = xc;
    }

    public float getYc() {
        return yc;
    }

    public void setYc(float yc) {
        this.yc = yc;
    }

    public movingObjects(float x, float y, float payon) {
        this.x = x;
        this.xc=x+payon/2;
        this.yc=y+payon/2;
        this.payon=payon;
        this.y = y;
        this.oval = new RectF(x,y,x+payon,y+payon);
        this.isAnimated=false;
        this.paint = new Paint();
        this.paint.setColor(Color.WHITE);
        this.hasPayon=true;
    }

    public void setXY(float newX, float newY){
        this.x=newX;
        this.y=newY;
        this.xc=newX+payon/2;
        this.yc=newY+payon/2;
        this.oval.set(newX,newY,newX+payon,newY+payon);

    }

    public float getPayon() {
        return payon;
    }

    public void setPayon(float payon) {
        this.payon = payon;
        this.oval.set(x,y,x+payon,y+payon);
    }

    public RectF getOval() {
        return oval;
    }

    public void setOval(RectF oval) {
        this.oval = oval;
    }

    public Paint getPaint() {
        return paint;
    }

    public void setPaint(Paint paint) {
        this.paint = paint;
    }

    public boolean isAnimated() {
        return isAnimated;
    }

    public void setAnimated(boolean animated) {
        isAnimated = animated;
    }

    public int getCase() {
        return Case;
    }

    public void setCase(int aCase) {
        Case = aCase;
    }

    public ArrayList<Bitmap> getBitmap() {
        return bitmap;
    }

    public void setBitmap(ArrayList<Bitmap> bitmap) {
        this.bitmap = bitmap;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
        if(hasPayon){
            this.oval.set(x,y,x+payon,y+payon);
        }
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
        if(hasPayon){
            this.oval.set(x,y,x+payon,y+payon);
        }
    }

    public Bitmap returnBitmapByCase(){
        return bitmap.get(Case);
    }
}
