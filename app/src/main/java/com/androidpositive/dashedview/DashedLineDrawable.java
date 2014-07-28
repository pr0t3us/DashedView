package com.androidpositive.dashedview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;

/**
 * @author Serhio
 */
public class DashedLineDrawable extends Drawable {
    private float mDensity;
    private Paint mLinePaint;
    private Path mLinePath;
    private PathEffect mEffects;
    private Orientation mOrientation = Orientation.HORIZONTAL;
    private Stick mLinePosition = Stick.LEFT;
    private int mColor = android.R.color.white;

    private float mFromX = 0;
    private float mFromY = 0;
    private float mToX = 0;
    private float mToY = 0;

    //Dimensions in px
    private float mLineLength = 4f;
    private float mLineWidth = 4f;
    private float mSpaceLength = 2f;


    public DashedLineDrawable(Context context) {
        init(context);
    }

    public Orientation getOrientation() {
        return mOrientation;
    }

    public void setOrientation(Orientation orientation) {
        this.mOrientation = orientation;
    }

    public Stick getLinePosition() {
        return mLinePosition;
    }

    public void setLinePosition(Stick linePosition) {
        this.mLinePosition = linePosition;
    }

    public int getColor() {
        return mColor;
    }

    public void setColor(int color) {
        this.mColor = color;
    }

    public float getLineLength() {
        return mLineLength;
    }

    public void setLineLength(float lineLength) {
        this.mLineLength = lineLength;
    }

    public float getLineWidth() {
        return mLineWidth;
    }

    public void setLineWidth(float lineWidth) {
        this.mLineWidth = lineWidth;
    }

    public float getSpaceLength() {
        return mSpaceLength;
    }

    public void setSpaceLength(float spaceLength) {
        this.mSpaceLength = spaceLength;
    }

    private void init(Context context) {
        mDensity = ScreenUtils.getScreenDensity(context);
        mLinePaint = new Paint();
        mLinePaint.setStyle(Paint.Style.STROKE);
        mLinePaint.setStrokeWidth(mDensity * mLineWidth);
        
        mLinePath = new Path();
        //array is ON and OFF distances in px (4px line then 2px space)
        mEffects = new DashPathEffect(new float[]{mLineLength, mSpaceLength, mLineLength, mSpaceLength}, 0);

    }

    @Override
    public void draw(Canvas canvas) {
        mLinePaint.setPathEffect(mEffects);
        mLinePaint.setColor(mColor);

        if (mOrientation == Orientation.HORIZONTAL) {
            // horizontal
            if (mLinePosition == Stick.RIGHT) {
                mFromY = mToY = copyBounds().height();
            } else if (mLinePosition == Stick.CENTER) {
                mFromY = mToY = copyBounds().height() / 2;
            }
            mToX = copyBounds().width();
        } else {
            // vertical
            if (mLinePosition == Stick.RIGHT) {
                mFromX = mToX = copyBounds().width();
            } else if (mLinePosition == Stick.CENTER) {
                mFromX = mToX = copyBounds().width() / 2;
            }
            mToY = copyBounds().height();
        }
        mLinePath.moveTo(mFromX, mFromY);
        mLinePath.lineTo(mToX, mToY);
        canvas.drawPath(mLinePath, mLinePaint);
    }

    @Override
    public void setAlpha(int alpha) {
        mLinePaint.setAlpha(alpha);
    }

    @Override
    public void setColorFilter(ColorFilter cf) {
        mLinePaint.setColorFilter(cf);
    }

    @Override
    public int getOpacity() {
        return PixelFormat.OPAQUE;
    }

    public enum Orientation {
        HORIZONTAL(0), VERTICAL(1);
        int id;

        Orientation(int id) {
            this.id = id;
        }

        static Orientation fromId(int id) {
            for (Orientation f : values()) {
                if (f.id == id) return f;
            }
            throw new IllegalArgumentException();
        }
    }

    public enum Stick {
        LEFT(0), CENTER(1), RIGHT(2);
        int id;

        Stick(int id) {
            this.id = id;
        }

        static Stick fromId(int id) {
            for (Stick f : values()) {
                if (f.id == id) return f;
            }
            throw new IllegalArgumentException();
        }
    }
}