package com.example.pomodorotechnique.myView;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.CountDownTimer;
import android.util.AttributeSet;
import android.view.View;

public class CircleProgressBar extends View {
    private Paint paint;
    private int width;
    private int height;
    private int progress = 0;
    private CountDownTimer countDownTimer;

    public CircleProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(20); // 设置圆环的宽度
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        width = w;
        height = h;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float radius = Math.min(width, height) / 2 - paint.getStrokeWidth() / 2;
        float sweepAngle = 360 * progress / 100;

        // 绘制背景圆环
        paint.setColor(Color.LTGRAY);
        canvas.drawCircle(width / 2, height / 2, radius, paint);

        // 绘制进度圆环
        paint.setColor(Color.WHITE);
        canvas.drawArc(new RectF(width / 2 - radius, height / 2 - radius,
                width / 2 + radius, height / 2 + radius), -90, sweepAngle, false, paint);
    }

    public void setProgress(int progress) {
        this.progress = progress;
        invalidate();
    }

    public void startCountDown(long millisInFuture, long interval) {
        countDownTimer = new CountDownTimer(millisInFuture, interval) {
            @Override
            public void onTick(long millisUntilFinished) {
                int progress = (int) ((millisInFuture - millisUntilFinished) * 100 / millisInFuture);
                setProgress(progress);
            }

            @Override
            public void onFinish() {
                setProgress(0);
            }
        }.start();
    }

    public void cancelCountDown() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }
}