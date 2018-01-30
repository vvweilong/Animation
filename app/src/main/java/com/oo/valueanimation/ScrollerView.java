package com.oo.valueanimation;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.LinearLayout;
import android.widget.OverScroller;
import android.widget.Scroller;

/**
 * Created by zhuxiaolong on 2018/1/29.
 */

public class ScrollerView extends LinearLayout {
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
        setOrientation(VERTICAL);
        init(context);
    }


    private VelocityTracker mVelocityTracker;
    private Scroller mScroller;
    private OverScroller mOverScroller;
    private float mTouchSlop;


    private void init(Context context) {
        final ViewConfiguration configuration = ViewConfiguration.get(context);
        mVelocityTracker = VelocityTracker.obtain();
        mScroller = new Scroller(context);
        mMaximumVelocity = configuration.getScaledMaximumFlingVelocity();
        mTouchSlop = configuration.getScaledTouchSlop();
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        childPosition();
    }

    private void childPosition() {
        int childCount = getChildCount();
        if (childCount > 0) {
            for (int i = 0; i < childCount; i++) {
                View child = getChildAt(i);
            }
        }
    }

    private int getTargetChild(float currentY) {
        int childCount = getChildCount();
        int targetIndex = 0;
        if (childCount > 0) {
            for (int i = 0; i < childCount; i++) {
                View child = getChildAt(i);
                int middle = child.getTop() + child.getBottom();
                if (currentY > middle) {
                    targetIndex = i;
                }
            }
        }
        return targetIndex;
    }


    float downY = 0;

    boolean dir;
    float currentY;


    private void smoothScrollBy(int x, int y, int offset) {
        mScroller.startScroll(x, y, x, y + offset);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mVelocityTracker.addMovement(event);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:

                downY = event.getY();
                currentY = downY;
                break;
            case MotionEvent.ACTION_MOVE:
                float diffY = event.getY() - currentY;
                Log.i(TAG, "onTouchEvent: move diffY=" + diffY + "  mTouchSlop=" + mTouchSlop);

                scrollBy(0, (int) (-(diffY)));
                currentY = event.getY();
                break;
            case MotionEvent.ACTION_UP:
                Log.i(TAG, "onTouchEvent: UP ");
//                int targetChild = getTargetChild(event.getY());
//                float currentY = event.getY();
//                float targetScrollY = getChildAt(targetChild).getScrollY();
//                mVelocityTracker.computeCurrentVelocity(1000, 1000);
//                mScroller.startScroll(getScrollX(), getScrollY(), 0, (int) (dir ? (targetScrollY - currentY) : (currentY - targetScrollY)), 500);
//                invalidate();
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
            postInvalidateOnAnimation();
        }
        super.computeScroll();
    }
}
