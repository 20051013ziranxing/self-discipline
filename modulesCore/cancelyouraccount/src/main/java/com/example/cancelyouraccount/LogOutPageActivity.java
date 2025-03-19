package com.example.cancelyouraccount;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.alibaba.android.arouter.facade.annotation.Route;

import org.greenrobot.eventbus.EventBus;

@Route(path = "/cancelyouraccount/LogOutPageActivity")
public class LogOutPageActivity extends AppCompatActivity {
    LogOutPageActivityPresenter logOutPageActivityPresenter;
    EditText editText_logoff_emailNumber;
    EditText editText_logoff_password;
    Button button_reallyWriteOff;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_log_out_page);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        logOutPageActivityPresenter = new LogOutPageActivityPresenter(this);
        editText_logoff_emailNumber = findViewById(R.id.editText_logoff_emailNumber);
        editText_logoff_emailNumber.setText(logOutPageActivityPresenter.getUserName());
        editText_logoff_password = findViewById(R.id.editText_logoff_password);
        button_reallyWriteOff = findViewById(R.id.button_reallyWriteOff);
        //监听文字变化设置按钮点击性
        editText_logoff_password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!editText_logoff_password.getText().toString().isEmpty()) {
                    button_reallyWriteOff.setEnabled(true);
                } else {
                    button_reallyWriteOff.setEnabled(false);
                }
            }
        });
        button_reallyWriteOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logOutPageActivityPresenter.performALogoutOperation(editText_logoff_emailNumber.getText().toString(),
                        editText_logoff_password.getText().toString());
            }
        });
    }
    public void sendToast(String message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(LogOutPageActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }
}