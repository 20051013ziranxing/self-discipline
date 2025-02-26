package com.example.clockinfragment.myview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class LineView extends View {
    private Paint paint;
    private float lineLength = 0;

    public LineView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        paint = new Paint();
        paint.setColor(0xFF000000);
        paint.setStrokeWidth(4);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // 绘制线条，从中心向两边延伸
        float startX = (getWidth() - lineLength) / 2;
        float startY = getHeight() / 2;
        float stopX = startX + lineLength;
        float stopY = startY;
        canvas.drawLine(startX, startY, stopX, stopY, paint);
    }

    public void setLineLength(float length) {
        this.lineLength = length;
        invalidate();
    }
}
