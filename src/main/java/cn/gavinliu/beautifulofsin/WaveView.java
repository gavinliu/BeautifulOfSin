package cn.gavinliu.beautifulofsin;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.DrawFilter;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;

public class WaveView extends View {

    private static final int X_SPEED = 20; // px
    private int mXOffset = 0;

    private int mViewWidth, mViewHeight;

    private Paint mPaint;
    private DrawFilter mDrawFilter;

    private float[] mPointY;
    private float[] mDynamicPointY;

    public WaveView(Context context) {
        super(context);
    }

    public WaveView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WaveView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(0xFF009688);

        mDrawFilter = new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        long startTime = System.currentTimeMillis();

        canvas.setDrawFilter(mDrawFilter);

        runWave();

        for (int i = 0; i < mViewWidth; i++) {
            canvas.drawLine(i, mViewWidth - mDynamicPointY[i] - 400, i, mViewHeight, mPaint);
        }

        mXOffset += X_SPEED;

        if (mXOffset > mViewWidth) {
            mXOffset = 0;
        }

        long endTime = System.currentTimeMillis();

        int delay = 0;

        if ((endTime - startTime) < 30) {
            delay = (int) (30 - (endTime - startTime));
        }

        postInvalidateDelayed(delay);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        mViewWidth = w;
        mViewHeight = h;

        mPointY = new float[w];
        mDynamicPointY = new float[w];

        // y = a * sin(2π) + b
        for (int i = 0; i < w; i++) {
            // i/w, 把sin(2π)拉升到整个View宽度
            mPointY[i] = (float) (30 * Math.sin(2 * Math.PI * i / w));
        }
    }

    private void runWave() {
        int yInterval = mPointY.length - mXOffset;
        System.arraycopy(mPointY, 0, mDynamicPointY, mXOffset, yInterval);
        System.arraycopy(mPointY, yInterval, mDynamicPointY, 0, mXOffset);
    }

}
