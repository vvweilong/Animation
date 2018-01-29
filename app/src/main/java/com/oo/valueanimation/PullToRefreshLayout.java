package com.oo.valueanimation;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.view.GestureDetectorCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.animation.DecelerateInterpolator;
import android.widget.LinearLayout;
import android.widget.Scroller;

/**
 * Created by zhuxiaolong on 2018/1/25.
 */

public class PullToRefreshLayout extends LinearLayout {
    private final String TAG = getClass().getSimpleName();

    public PullToRefreshLayout(Context context) {
        this(context, null);
    }

    public PullToRefreshLayout(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PullToRefreshLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
//        手势监听
        gestureDetectorCompat = new GestureDetectorCompat(context, gestureListener);
//        滚动辅助类 + 减速插值器
        scroller = new Scroller(context, new DecelerateInterpolator());
    }

    boolean isTop = true;
    boolean isBottom = false;

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        Log.i(TAG, "onInterceptTouchEvent: ");
//        判断 内部的可滚动 view 是否已经到头了
//        如果已经到头 那么拦截事件
        if (isBottom || isTop) {
            Log.i(TAG, "onInterceptTouchEvent: return true");
            return true;
        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.i(TAG, "onTouchEvent: ");
        return gestureDetectorCompat.onTouchEvent(event);
    }

    private Scroller scroller;

    private GestureDetectorCompat gestureDetectorCompat;
    private GestureDetector.SimpleOnGestureListener gestureListener = new GestureDetector.SimpleOnGestureListener() {
        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            Log.i(TAG, "onScroll: ");
            return true;
        }
    };

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (scroller.computeScrollOffset()){
            scrollTo(scroller.getCurrX(),scroller.getCurrY());
            postInvalidate();
        }
    }
}
