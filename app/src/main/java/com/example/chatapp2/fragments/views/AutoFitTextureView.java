package com.example.chatapp2.fragments.views;

import android.content.Context;
import android.graphics.Rect;
import android.hardware.camera2.CameraAccessException;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ScaleGestureDetector;
import android.view.TextureView;

public class AutoFitTextureView extends TextureView {

    private int mRatioWidth = 0;
    private int mRatioHeight = 0;
    Rect cameraZoomRect;
    float maxZoomFactor;
    private ScaleGestureDetector mScaleDetector;
    private float mScaleFactor = 1.f;

    public interface getZoomCaracteristics {
        Rect giveRectZoom() throws CameraAccessException;
        float giveMaxZoom() throws CameraAccessException;
        void previewRequestINT(Rect rect);
        void captureSession() throws CameraAccessException;
    }

    public Rect cameraZoomRect1;
    private getZoomCaracteristics getZoomCaracteristics;

    private boolean isFirstMaxZoom;

    public void setGetZoomCaracteristics(getZoomCaracteristics getZoomCaracteristics) {
        this.getZoomCaracteristics = getZoomCaracteristics;
    }

    public AutoFitTextureView(Context context) {
        this(context, null);
        this.isFirstMaxZoom = true;
    }

    public AutoFitTextureView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        this.isFirstMaxZoom = true;
    }

    public AutoFitTextureView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.isFirstMaxZoom = true;
        mScaleDetector = new ScaleGestureDetector(context, new ScaleListener());
    }

    public void setAspectRatio(int width, int height) {
        if (width < 0 || height < 0) {
            throw new IllegalArgumentException("Size cannot be negative.");
        }
        mRatioWidth = width;
        mRatioHeight = height;
        requestLayout();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        if (0 == mRatioWidth || 0 == mRatioHeight) {
            setMeasuredDimension(width, height);
        } else {
            if (width < height * mRatioWidth / mRatioHeight) {
                setMeasuredDimension(width, width * mRatioHeight / mRatioWidth);
            } else {
                setMeasuredDimension(height * mRatioWidth / mRatioHeight, height);
            }
        }
    }

    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleListener {
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            mScaleFactor *= detector.getScaleFactor();

            if (getZoomCaracteristics != null && isFirstMaxZoom) {
                try {
                    maxZoomFactor = Math.min(3f, getZoomCaracteristics.giveMaxZoom());
                    isFirstMaxZoom = false;
                } catch (CameraAccessException e) {
                    e.printStackTrace();
                }
            }

            mScaleFactor = Math.max(1.0f, Math.min(mScaleFactor, maxZoomFactor));

            try {
                cameraZoomRect = getZoomCaracteristics.giveRectZoom();
                cameraZoomRect1 = getZoomCaracteristics.giveRectZoom();

                float eventualWidth = cameraZoomRect.width() / mScaleFactor;

                if (eventualWidth < cameraZoomRect1.width() / 3.0) {
                    cameraZoomRect.set((int) (0.5f * (2 * cameraZoomRect1.width() / 3.0)),
                            (int) (0.5f * (2 * cameraZoomRect1.height() / 3)),
                            (int) (0.5f * (4 * cameraZoomRect1.width() / 3)),
                            (int) (0.5f * (4 * cameraZoomRect1.height() / 3)));
                } else {
                    cameraZoomRect.set((int) (0.5f * (cameraZoomRect1.width() - cameraZoomRect.width() / mScaleFactor)),
                            (int) (0.5f * (cameraZoomRect1.height() - cameraZoomRect.height() / mScaleFactor)),
                            (int) (0.5f * (cameraZoomRect1.width() + cameraZoomRect.width() / mScaleFactor)),
                            (int) (0.5f * (cameraZoomRect1.height() + cameraZoomRect.height() / mScaleFactor)));
                }

                if (eventualWidth > cameraZoomRect1.width()) {
                    cameraZoomRect.set(cameraZoomRect1);
                } else {
                    cameraZoomRect.set((int) (0.5f * (cameraZoomRect1.width() - cameraZoomRect.width() / mScaleFactor)),
                            (int) (0.5f * (cameraZoomRect1.height() - cameraZoomRect.height() / mScaleFactor)),
                            (int) (0.5f * (cameraZoomRect1.width() + cameraZoomRect.width() / mScaleFactor)),
                            (int) ((0.5f * (cameraZoomRect1.height() + cameraZoomRect.height() / mScaleFactor))));
                }

                getZoomCaracteristics.previewRequestINT(cameraZoomRect);
                getZoomCaracteristics.captureSession();

            } catch (CameraAccessException e) {
                e.printStackTrace();
            }

            return true;
        }
    }
}
