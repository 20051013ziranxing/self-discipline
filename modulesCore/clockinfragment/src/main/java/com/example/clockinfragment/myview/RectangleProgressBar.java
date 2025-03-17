package com.example.clockinfragment.myview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

public class RectangleProgressBar extends View {
    private static final String TAG = "TestTT_RectangleProgressBar";
    private int progressBarColor = 0xFF00FF00; // 进度条颜色（绿色）

    public void setProgressBarColor(int progressBarColor) {
        this.progressBarColor = progressBarColor;
        Log.d(TAG, "我更换了颜色");
        init();
    }

    private int backgroundColor = Color.parseColor("#00ffffff");
    private float progress = 0f;

    private Paint progressPaint;
    private Paint backgroundPaint;

    public RectangleProgressBar(Context context) {
        super(context);
        init();
    }

    public RectangleProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public RectangleProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        progressPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        progressPaint.setColor(progressBarColor);

        backgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        backgroundPaint.setColor(backgroundColor);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawRect(0, 0, getWidth(), getHeight(), backgroundPaint);
        float progressWidth = getWidth() * progress;
        canvas.drawRect(0, 0, progressWidth, getHeight(), progressPaint);
    }
    public void setProgress(float progress) {
        if (progress < 0f) {
            progress = 0f;
        } else if (progress > 1f) {
            progress = 1f;
        }
        this.progress = progress;
        invalidate();
    }
    public float getProgress() {
        return progress;
    }
}
