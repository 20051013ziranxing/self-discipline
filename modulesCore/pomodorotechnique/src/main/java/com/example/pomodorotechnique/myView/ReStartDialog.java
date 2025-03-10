package com.example.pomodorotechnique.myView;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.pomodorotechnique.R;

public class ReStartDialog extends Dialog {
    public TextView textView_discardTheCurrentTimer;
    public TextView textView_finishTheTimingAheadOfSchedule;
    public TextView textView_cancel;
    private ReStartDialogListener listener;
    public ReStartDialog(@NonNull Context context) {
        super(context);
    }

    public interface ReStartDialogListener {
        void textViewDiscardTheCurrentTimer();
        void textViewFinishTheTimingAheadOfSchedule();
    }
    public void setOnDialogReStartDialogListener(ReStartDialogListener listener) {
        this.listener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.skip_the_countdown);
        textView_discardTheCurrentTimer = findViewById(R.id.textView_discardTheCurrentTimer);
        textView_discardTheCurrentTimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.textViewDiscardTheCurrentTimer();
                dismiss();
            }
        });
        textView_finishTheTimingAheadOfSchedule = findViewById(R.id.textView_finishTheTimingAheadOfSchedule);
        textView_finishTheTimingAheadOfSchedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.textViewFinishTheTimingAheadOfSchedule();
                dismiss();
            }
        });
        textView_cancel = findViewById(R.id.textView_cancel);
        textView_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }
}
