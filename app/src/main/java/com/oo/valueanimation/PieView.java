package com.oo.valueanimation;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.SweepGradient;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.OverScroller;

import static android.support.v4.widget.ViewDragHelper.INVALID_POINTER;

/**
 * Created by zhuxiaolong on 2018/1/30.
 */

public class PieView extends View {

    private final String TAG = getClass().getSimpleName();

    public PieView(Context context) {
        this(context, null);
    }

    public PieView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PieView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setWillNotDraw(false);
        init(context);

    }

    private OverScroller mOverScroller;
    private VelocityTracker mVelocityTracker;
    private Animation rotationAnimation;

    /**
     * ID of the active pointer. This is used to retain consistency during
     * drags/flings if multiple pointers are used.
     */
    private int mActivePointerId = INVALID_POINTER;

    private void init(Context context) {
        mOverScroller = new OverScroller(context);
        mVelocityTracker = VelocityTracker.obtain();
        rotationAnimation = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                super.applyTransformation(interpolatedTime, t);
                // TODO: 2018/1/30  根据差值 重绘圆
            }
        };

        mainPaint = new Paint();
        mainPaint.setColor(Color.rgb(100, 200, 250));
        mainPaint.setStyle(Paint.Style.FILL);
        mainPaint.setAntiAlias(true);


    }


    private float downY = 0;
    private float downX = 0;

    enum Quadrant {
        ONE, TWO, THREE, FORE
    }

    enum RotationOritation {
        CLOCKWISE, ANTICLOCKWISE
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mVelocityTracker.addMovement(event);
        int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                Log.i(TAG, "onTouchEvent: DOWN");
//                获取点击事件的 action
                int pointCount = event.getPointerCount();

                for (int p = 0; p < pointCount; p++) {
                    Log.i(TAG, "onTouchEvent: actionIndex = " + event.getActionIndex());
                    Log.i(TAG, "onTouchEvent: pointerId = " + event.getPointerId(p));
                    Log.i(TAG, "onTouchEvent: get pointerIndex by id = " + event.findPointerIndex(event.getPointerId(p)));
                }


                int pointId = event.getPointerId(0);
                downY = event.getY(pointId);
                downX = event.getX(pointId);
                break;
            case MotionEvent.ACTION_MOVE:
                Log.i(TAG, "onTouchEvent: MOVE");
                float curX = event.getX();
                float curY = event.getY();

                float diffX = curX - downX;
                float diffY = curY - downY;

                float diffXpercent = diffX / drawRect.width();
                float diffYpercent = diffY / drawRect.height();

                float targetDegree = (Math.abs(diffX) > Math.abs(diffY)) ? diffXpercent : diffYpercent * 360;
                setPieRotation((int) (lastDegree+targetDegree));


                break;
            case MotionEvent.ACTION_UP:
                Log.i(TAG, "onTouchEvent: UP");

                break;
            default:
                break;
        }


        return true;
    }


    private RectF drawRect;
    private RectF circleRectF;

    private Paint mainPaint;

    private float cx;
    private float cy;
    private float radius;

    private double startDegrees = 0;
    private double originDegrees = 0;

    private Matrix mMatrix;

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        Log.i(TAG, "onSizeChanged: " + w + " " + h);
        Log.i(TAG, "onSizeChanged: left " + getPaddingLeft() + " padding top" + getPaddingTop());
        int l = getPaddingLeft();
        int t = getPaddingTop();
        int r = l + w;
        int b = t + h;
        Log.i(TAG, "onSizeChanged: left= " + l + " top= " + t + "  right= " + r + " bottom= " + b);
        drawRect = new RectF(l, t, r, b);


        Log.i(TAG, "onSizeChanged: " + drawRect.toShortString());
        mMatrix = new Matrix();
        cx = drawRect.centerX();
        cy = drawRect.centerY();
        radius = (int) (Math.min(drawRect.width(), drawRect.height()) * 0.5f);
        circleRectF = new RectF(cx - radius, cy - radius, cx + radius, cy + radius);
        SweepGradient sweepGradient = new SweepGradient(cx, cy, Color.BLUE, Color.GREEN);
        sweepGradient.getLocalMatrix(mMatrix);
        mainPaint.setShader(sweepGradient);
        Log.i(TAG, "onSizeChanged: cx = " + cx + " cy = " + cy + " r = " + radius);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Log.i(TAG, "onDraw: startDegrees =  " + startDegrees);
        super.onDraw(canvas);
        mainPaint.getShader().setLocalMatrix(mMatrix);
        canvas.drawArc(circleRectF, 0, 360, true, mainPaint);
    }

    private int lastDegree = 0;

    public void setPieRotation(int degree) {
        mMatrix.postRotate(degree - lastDegree, cx, cy);
        lastDegree = degree;
        postInvalidateOnAnimation();
    }
}
