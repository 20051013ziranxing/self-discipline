package com.example.pomodorotechnique.myView;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

import com.example.pomodorotechnique.R;

public class SwitchImageView extends View {
    private Bitmap offBitmap;
    private Bitmap onBitmap;
    private boolean isOn = false;

    public SwitchImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        offBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.start_icon);
        onBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.paused_icon);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (isOn) {
            canvas.drawBitmap(onBitmap, 0, 0, null);
        } else {
            canvas.drawBitmap(offBitmap, 0, 0, null);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            isOn = !isOn;
            invalidate();
        }
        return true;
    }
}
