package com.example.pomodorotechnique.myView;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.pomodorotechnique.R;

public class ReStartDialog extends Dialog {
    TextView textView_discardTheCurrentTimer;
    TextView textView_finishTheTimingAheadOfSchedule;
    TextView textView_cancel;
    public ReStartDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.skip_the_countdown);
        textView_discardTheCurrentTimer = findViewById(R.id.textView_discardTheCurrentTimer);
        textView_finishTheTimingAheadOfSchedule = findViewById(R.id.textView_finishTheTimingAheadOfSchedule);
        textView_cancel = findViewById(R.id.textView_cancel);
        textView_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }
}
