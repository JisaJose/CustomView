package com.example.customview.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Build;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;

import com.example.customview.R;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by jisajose on 2018-02-23.
 */

public class CustomView extends View {
    private static final int SQUARE_SIZE = 100;
    private Rect mRectSquare;
    private Paint mRectPaint;
    private int msquareColor;
    private int msquareSize;
    private Paint mPaintCircle;
    private float mCircleX, mCircleY;
    private Bitmap bitmapImage;
    private float mCircleRadius = 100f;

    public CustomView(Context context) {
        super(context);
        init(null);
    }

    public CustomView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public CustomView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    public CustomView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }


    private void init(@Nullable AttributeSet attrs) {
        mRectSquare = new Rect();
        mRectPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintCircle = new Paint();
        mPaintCircle.setAntiAlias(true);
        mPaintCircle.setColor(Color.GREEN);
        bitmapImage = BitmapFactory.decodeResource(getResources(), R.drawable.mm);

        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
                    getViewTreeObserver().removeOnGlobalLayoutListener(this);
                else
                    getViewTreeObserver().removeOnGlobalLayoutListener(this);
                int padding = 50;
                bitmapImage = getResizedBitmap(bitmapImage, getWidth() - padding, getHeight() - padding);
                new Timer().scheduleAtFixedRate(new TimerTask() {

                    @Override
                    public void run() {
                        int newWidth = bitmapImage.getWidth() - 50;
                        int newHieght = bitmapImage.getHeight() - 50;
                        if (newWidth <= 0 || newHieght <= 0) {
                            cancel();
                            return;
                        }
                        bitmapImage = getResizedBitmap(bitmapImage, newWidth, newHieght);
                        postInvalidate();
                    }
                }, 10001, 5001);
            }
        });
//        mRectPaint.setColor(Color.RED);
        if (attrs == null)
            return;
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.CustomView);
        msquareColor = typedArray.getColor(R.styleable.CustomView_square_color, Color.RED);
        msquareSize = typedArray.getDimensionPixelSize(R.styleable.CustomView_square_size, SQUARE_SIZE);
        mRectPaint.setColor(msquareColor);
        typedArray.recycle();
    }

    private Bitmap getResizedBitmap(Bitmap bitmap, int width, int height) {
        Matrix matrix = new Matrix();
        RectF source = new RectF(0, 0, bitmap.getWidth(), bitmap.getHeight());
        RectF destination = new RectF(0, 0, width, height);
        matrix.setRectToRect(source, destination, Matrix.ScaleToFit.CENTER);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

    public void swapColor() {
        mRectPaint.setColor(mRectPaint.getColor() == msquareColor ? Color.GREEN : msquareColor);
        postInvalidate();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        mRectSquare.left = 10;
        mRectSquare.right = mRectSquare.left + msquareSize;
        mRectSquare.top = 10;
        mRectSquare.bottom = mRectSquare.top + msquareSize;
        canvas.drawRect(mRectSquare, mRectPaint);
//        float cx,cy;
//        float radius=100f;
//        cx=getWidth() - radius -50f;
//        cy=mRectSquare.top + (mRectSquare.height( )/2);
        if (mCircleX == 0f || mCircleY == 0f) {
            mCircleX = getWidth() / 2;
            mCircleY = getHeight() / 2;

        }
        canvas.drawCircle(mCircleX, mCircleY, mCircleRadius, mPaintCircle);
        float imageX = (getWidth() - bitmapImage.getWidth() / 2);
        float imageY = (getHeight() - bitmapImage.getHeight() / 2);
        canvas.drawBitmap(bitmapImage, imageX, imageY, null);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        boolean value = super.onTouchEvent(event);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                float xValue = event.getX();
                float yValue = event.getY();
                if (mRectSquare.left < xValue && mRectSquare.right > yValue)
                    if (mRectSquare.top < yValue && mRectSquare.bottom > yValue) {
                        mCircleRadius += 10f;
                        postInvalidate();
                    }
                return true;
            }
            case MotionEvent.ACTION_MOVE: {
                float xValue = event.getX();
                float yValue = event.getY();
                double dx = Math.pow(xValue - mCircleX, 2);
                double dy = Math.pow(yValue - mCircleY, 2);
                if (dx + dy < Math.pow(mCircleRadius, 2)) {
//Touched
                    mCircleY = xValue;
                    mCircleY = yValue;
                    postInvalidate();
                }
                return true;
            }
        }
        return value;
    }
}
