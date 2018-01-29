package com.oo.valueanimation;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * Created by zhuxiaolong on 2018/1/25.
 */

public class ColumView extends View {

    private final String TAG = getClass().getSimpleName();

    public ColumView(Context context) {
        this(context, null);
    }

    public ColumView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ColumView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        // Try for a width based on our minimum
        int minw = getPaddingLeft() + getPaddingRight() + getSuggestedMinimumWidth();
        int w = resolveSizeAndState(minw, widthMeasureSpec, 1);
        // Whatever the width ends up being, ask for a height that would let the pie
        // get as big as it can
        int minh = MeasureSpec.getSize(w) + getPaddingBottom() + getPaddingTop();
        int h = resolveSizeAndState(MeasureSpec.getSize(w), heightMeasureSpec, 0);
        setMeasuredDimension(w, h);
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        float l = getPaddingLeft();
        float t = getPaddingTop();
        float r = w - getPaddingRight();
        float b = h - getPaddingBottom();
        //记录 绘制范围  考虑轮廓线的宽
        drawRect = new RectF(l, t, r, b);
        Log.i(TAG, "onSizeChanged: drawRect : " + drawRect.toString());
    }


    private void init() {
        mainPaint = new Paint();
        mainPaint.setColor(Color.RED);
        grownAnimator = ValueAnimator.ofFloat(0f, 0.5f, 0.75f, 1f);
        grownAnimator.setDuration(1000);
        grownAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
//                获取最新数值
                columPersent = (float) animation.getAnimatedValue();
                Log.i(TAG, "onAnimationUpdate: "+columPersent);
//                通知重新绘制
                invalidate();
            }
        });
    }

    private RectF drawRect;
    /**
     * 画笔
     */
    private Paint mainPaint;


    /**
     * 增长动画
     */
    private ValueAnimator grownAnimator;

    /**
     * 绘制柱状图的百分比
     */
    private float columPersent = 1;

    /**
     * totale value
     */
    private int value = 100;

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
//        绘制 柱状图
        float h = drawRect.height() * (1f - columPersent) * (value * 0.01f);
        canvas.drawRect(drawRect.left, h, drawRect.right, drawRect.bottom, mainPaint);
    }

    public void setValue(int value) {
        this.value = value;
        columPersent=0;
        grownAnimator.start();
    }


}
