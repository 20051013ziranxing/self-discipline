package com.example.clockinfragment.myview;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.clockinfragment.R;

public class CustomDialog extends Dialog {
    private Button cancelButton;
    private Button confirmButton;
    TextView countTextView;
    private Button addButton;
    private Button reductionButton;
    private OnDialogConfirmedListener listener;

    public CustomDialog(Context context) {
        super(context);
    }
    public interface OnDialogConfirmedListener {
        void onDialogConfirmed(String inputText);
    }

    public void setOnDialogConfirmedListener(OnDialogConfirmedListener listener) {
        this.listener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_custom);

        cancelButton = findViewById(R.id.dialog_cancel);
        confirmButton = findViewById(R.id.dialog_confirm);
        reductionButton = findViewById(R.id.button_dialog_reduction);
        addButton = findViewById(R.id.button_dialog_add);
        countTextView = findViewById(R.id.textView_dialog_count);
        reductionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s = countTextView.getText().toString();
                int count = Integer.parseInt(s);
                if(count > 1) {
                    count--;
                }
                countTextView.setText(String.valueOf(count));
            }
        });
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s = countTextView.getText().toString();
                int count = Integer.parseInt(s);
                count++;
                countTextView.setText(String.valueOf(count));
            }
        });
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onDialogConfirmed(countTextView.getText().toString());
                }
                dismiss();
            }
        });
    }
}