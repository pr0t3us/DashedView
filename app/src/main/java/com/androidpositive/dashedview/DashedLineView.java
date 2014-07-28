package com.androidpositive.dashedview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.util.AttributeSet;
import android.view.View;

/**
 * @author Serhio
 */
public class DashedLineView extends View {
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


    public DashedLineView(Context context) {
        super(context);
        init(context);
    }

    public DashedLineView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DashedLineView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        readAttributes(context, attrs, defStyle);
        init(context);
    }

    private void readAttributes(Context context, AttributeSet attrs, int defStyle) {
        if (attrs != null) {
            TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.DashedLineView, defStyle, 0);
            try {
                mOrientation = Orientation.fromId(a.getInteger(R.styleable.DashedLineView_orientation, 0));
                /* get line position, default are left */
                mLinePosition = Stick.fromId(a.getInteger(R.styleable.DashedLineView_stick, 0));
                /* read color or set white as default */
                mColor = a.getColor(R.styleable.DashedLineView_initialColor, android.R.color.white);
                mLineLength = a.getDimension(R.styleable.DashedLineView_dashedLineLength, 4f);
                mLineWidth = a.getDimension(R.styleable.DashedLineView_dashedLineWidth, 4f);
                mSpaceLength = a.getDimension(R.styleable.DashedLineView_dashedSpaceLength, 2f);
            } finally {
                a.recycle();
            }
        }

    }

    private void init(Context context) {
        mDensity = ScreenUtils.getScreenDensity(context);
        mLinePaint = new Paint();
        mLinePaint.setStyle(Paint.Style.STROKE);
        mLinePaint.setStrokeWidth(mDensity * mLineWidth);
        //set your own color
        mLinePaint.setColor(mColor);
        mLinePath = new Path();
        //array is ON and OFF distances in px (4px line then 2px space)
        mEffects = new DashPathEffect(new float[]{mLineLength, mSpaceLength, mLineLength, mSpaceLength}, 0);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mLinePaint.setPathEffect(mEffects);

        if (mOrientation == Orientation.HORIZONTAL) {
            // horizontal
            if (mLinePosition == Stick.RIGHT) {
                mFromY = mToY = getMeasuredHeight();
            } else if (mLinePosition == Stick.CENTER) {
                mFromY = mToY = getMeasuredHeight() / 2;
            }
            mToX = getMeasuredWidth();
        } else {
            // vertical
            if (mLinePosition == Stick.RIGHT) {
                mFromX = mToX = getMeasuredWidth();
            } else if (mLinePosition == Stick.CENTER) {
                mFromX = mToX = getMeasuredWidth() / 2;
            }
            mToY = getMeasuredHeight();
        }
        mLinePath.moveTo(mFromX, mFromY);
        mLinePath.lineTo(mToX, mToY);
        canvas.drawPath(mLinePath, mLinePaint);
    }

    private enum Orientation {
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

    private enum Stick {
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