package com.oo.valueanimation;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Shader;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.OverScroller;
import android.widget.Scroller;

/**
 * Created by zhuxiaolong on 2018/1/29.
 */

public class ScrollerView extends View {
    private final String TAG = getClass().getSimpleName();
    private Object mMaximumVelocity;

    public ScrollerView(Context context) {
        this(context, null);
    }

    public ScrollerView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ScrollerView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }


    private VelocityTracker mVelocityTracker;
    private Scroller mScroller;
    private OverScroller mOverScroller;


    private void init(Context context) {
        final ViewConfiguration configuration = ViewConfiguration.get(context);
        mVelocityTracker = VelocityTracker.obtain();
        mScroller = new Scroller(context);
        mMaximumVelocity = configuration.getScaledMaximumFlingVelocity();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int[] colors={Color.RED,Color.GREEN,Color.YELLOW,Color.BLACK};
        Shader shader=new LinearGradient(0,0,getMeasuredWidth(),getMeasuredHeight(),colors,null, Shader.TileMode.CLAMP);

        Rect rect=new Rect(0,0,getMeasuredWidth(),getMeasuredHeight());
        Paint paint=new Paint();
        paint.setShader(shader);
        canvas.drawRect(rect,paint);
    }


    enum DIR{
        UP_DIR,DOWN_DIR
    }
    DIR mDIR;
    float downY=0;

    boolean dir;
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mVelocityTracker.addMovement(event);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:

                downY=event.getY();

                break;
            case MotionEvent.ACTION_MOVE:

                if (event.getY()-downY>0) {
                    dir=true;
                }else {
                    dir=false;
                }

                break;
            case MotionEvent.ACTION_UP:
                Log.i(TAG, "onTouchEvent: UP ");
                mVelocityTracker.computeCurrentVelocity(1000,1000);
                mScroller.startScroll(getScrollX(),getScrollY(),0,dir?500:-500,500);
                invalidate();
                break;
            default:
                break;
        }
        return true;
    }

    @Override
    public void computeScroll() {
        if (!mScroller.isFinished()) {
//        计算
            mScroller.computeScrollOffset();
//        移动
            Log.i(TAG, "computeScroll: curX :" + mScroller.getCurrX() + "  curY : " + mScroller.getCurrY());
            scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
//        重绘
            invalidate();
        }
        super.computeScroll();
    }
}
