package com.example.findpassword;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.example.activitymanager.ActivityManager;

@Route(path = "/findPassword/MainActivityFindPassword")
public class FindPasswordActivity extends AppCompatActivity {
    private static final String EMAIL_REGEX = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
    private static final String PASSWORD_PATTERN = "^(?=.*[a-zA-Z])(?=.*\\d)[a-zA-Z\\d]{8}$";
    private static final String TAG = "TestTT_FindPasswordActivity";
    @Autowired(name = "emailNumber")
    public String emailNumber;
    Toolbar toolbar;
    EditText textView;
    EditText editText_captcha;
    EditText editText_newPassword;
    Button button_setNewPassword;
    FindPasswordPresenter findPasswordPresenter;
    //用来获取验证码
    Button btn_get_code;
    TextView tvCountdown;
    CountDownTimer countDownTimer;;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main_find_password);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        findPasswordPresenter = new FindPasswordPresenter(this);
        editText_captcha = findViewById(R.id.editText_captcha);
        editText_newPassword = findViewById(R.id.editText_newPassword);
        ActivityManager.getInstance().addActivity(this);
        button_setNewPassword = findViewById(R.id.button_setNewPassword);
        textView = findViewById(R.id.textView_EmailNumber);
        ARouter.getInstance().inject(this);
        textView.setText(emailNumber);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        button_setNewPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Boolean password = CheckPasswordRight(editText_newPassword.getText().toString());
                if (password) {
                    findPasswordPresenter.VerificationOfTheVerificationCode(textView.getText().toString(), editText_captcha.getText().toString(),
                            editText_newPassword.getText().toString());
                } else {
                    SendToast("密码格式错误");
                }
                Log.d(TAG, emailNumber + " 111 " + editText_captcha.getText().toString() +
                        " 111 " + editText_newPassword.getText().toString());
            }
        });
        btn_get_code = findViewById(R.id.btn_get_code);
        tvCountdown = findViewById(R.id.tv_countdown);
        btn_get_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Boolean emailRight = CheckEmailRight(textView.getText().toString());
                if (emailRight) {
                    findPasswordPresenter.SendAVerificationCodeToResetYourPassword(textView.getText().toString());
                    startCountdown();
                } else {
                    SendToast("邮箱格式错误");
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityManager.getInstance().removeActivity(this);
    }

    private void startCountdown() {
        btn_get_code.setVisibility(View.GONE);
        tvCountdown.setVisibility(View.VISIBLE);
        countDownTimer = new CountDownTimer(60000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                tvCountdown.setText(String.format("%d秒", millisUntilFinished / 1000));
            }

            @Override
            public void onFinish() {
                btn_get_code.setVisibility(View.VISIBLE);
                tvCountdown.setVisibility(View.GONE);
                tvCountdown.setText("60秒");
            }
        }.start();
    }
    public void SendToast(String s1) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(FindPasswordActivity.this, s1, Toast.LENGTH_SHORT).show();
            }
        });
    }
    public Boolean CheckEmailRight(String s) {
        return true;
//        return s != null && s.matches(EMAIL_REGEX);
    }
    public Boolean CheckPasswordRight(String s) {
        return s != null && s.matches(PASSWORD_PATTERN);
    }

}