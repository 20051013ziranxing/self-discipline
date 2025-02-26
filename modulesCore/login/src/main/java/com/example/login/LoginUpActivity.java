package com.example.login;

import static android.app.ProgressDialog.show;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.example.activitymanager.ActivityManager;
import com.google.android.material.textfield.TextInputLayout;

@Route(path = "/login/LoginUpActivity")
public class LoginUpActivity extends AppCompatActivity {
    //邮箱与密码的格式检验
    private static final String EMAIL_REGEX = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
    private static final String PASSWORD_PATTERN   = "^(?=.*[a-zA-Z])(?=.*\\d)[a-zA-Z\\d]{6}$";
    private static final String TAG = "TestTT_LoginUpActivity";
    TextView textView_loginUp;
    TextView textView_signIn;
    View view_loginUp;
    View view_signIn;
    LoginUpPresenter loginUpPresenter;
    //登录界面的控件
    Button button_loginUp;
    TextInputLayout textInputLayout_emailNumber_up;
    EditText editText_emailNumber_up;
    EditText editText_password_up;
    Button button_forgetPassword_up;
    CheckBox checkBox_keepSignUp_up;
    //注册界面的控件
    Button button_signIn;
    EditText editText_userName_in;
    EditText editText_password_in;
    EditText editText_emailNumber_in;
    CheckBox checkBox_keepSignUp_in;
    EditText editText_emailNumberCaptcha_in;
    private Button btnGetCode;
    private TextView tvCountdown;
    private CountDownTimer countDownTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login_up);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        //进行活动的添加
        ActivityManager.getInstance().addActivity(this);
        textView_loginUp = findViewById(R.id.button_loginUp);
        textView_signIn = findViewById(R.id.button_registered);
        view_signIn = findViewById(R.id.include_sign_in);
        view_loginUp = findViewById(R.id.include_login_up);
        loginUpPresenter = new LoginUpPresenter(this);
        //获取注册界面的控件以及控件的点击事件的逻辑
        button_signIn = findViewById(R.id.button_signIn1);
        editText_userName_in = findViewById(R.id.editText_userName_in);
        editText_password_in = findViewById(R.id.editText_password_in);
        editText_emailNumber_in = findViewById(R.id.editText_emailNumber_in);
        checkBox_keepSignUp_in = findViewById(R.id.checkBox_keepSignUp_in);
        editText_emailNumberCaptcha_in = findViewById(R.id.editText_emailNumberCaptcha_in);
        //按下注册按钮
        button_signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Boolean email = CheckEmailRight(editText_emailNumber_in.getText().toString());
                Boolean password = CheckPasswordRight(editText_password_in.getText().toString());
                if (!email) {
                    SendToast("邮箱输入格式错误");
                } else if (!password) {
                    SendToast("密码输入格式错误");
                } else {
                    loginUpPresenter.signIn(editText_userName_in.getText().toString(), editText_password_in.getText().toString(),
                            editText_emailNumber_in.getText().toString(), editText_emailNumberCaptcha_in.getText().toString(),
                            checkBox_keepSignUp_in.isChecked());
                }
            }
        });

        //获取登陆界面的控件以及控件的点击事件的逻辑
        button_loginUp = findViewById(R.id.button_loginUp1);
        textInputLayout_emailNumber_up = findViewById(R.id.textInputLayout_emailNumber_up);
        editText_emailNumber_up = findViewById(R.id.editText_emailNumber_up);
        editText_password_up = findViewById(R.id.editText_password_up);
        button_forgetPassword_up = findViewById(R.id.button_forgetPassword_up);
        checkBox_keepSignUp_up = findViewById(R.id.checkBox_keepSignUp_up);
        button_loginUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Boolean Email = CheckEmailRight(editText_emailNumber_up.getText().toString());
                Boolean password = CheckPasswordRight(editText_password_up.getText().toString());
                if (!Email) {
                    SendToast("邮箱输入格式错误");
                } else if (!password){
                    SendToast("密码输入格式错误");
                    loginUpPresenter.loginUpByPassword(editText_emailNumber_up.getText().toString(), editText_password_up.getText().toString());
                } else {
                    loginUpPresenter.loginUpByPassword(editText_emailNumber_up.getText().toString(), editText_password_up.getText().toString());
                }
                Log.d(TAG, editText_emailNumber_up.getText().toString() + editText_password_up.getText().toString());
            }
        });
        button_forgetPassword_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUpPresenter.forgetPassword(editText_emailNumber_up.getText().toString());
            }
        });

        //选择登录还是注册带来的页面的变化
        textView_loginUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textView_loginUp.setTextSize(TypedValue.COMPLEX_UNIT_SP, 22);
                textView_signIn.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
                view_loginUp.setVisibility(View.VISIBLE);
                view_signIn.setVisibility(View.GONE);
            }
        });
        textView_signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textView_loginUp.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
                textView_signIn.setTextSize(TypedValue.COMPLEX_UNIT_SP, 22);
                view_loginUp.setVisibility(View.GONE);
                view_signIn.setVisibility(View.VISIBLE);
            }
        });

        btnGetCode = findViewById(R.id.btn_get_code);
        tvCountdown = findViewById(R.id.tv_countdown);
        //注册的时候发送验证码
        btnGetCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Boolean emailNumber = CheckEmailRight(editText_emailNumber_in.getText().toString());
                if (emailNumber) {
                    loginUpPresenter.SendAnEmailVerificationCodeRegistered(editText_emailNumber_in.getText().toString());
                    startCountdown();
                } else {
                    SendToast("邮箱格式错误");
                }
            }
        });
    }
    //开始获取验证码的倒计时
    private void startCountdown() {
        btnGetCode.setVisibility(View.GONE);
        tvCountdown.setVisibility(View.VISIBLE);
        countDownTimer = new CountDownTimer(60000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                tvCountdown.setText(String.format("%d秒", millisUntilFinished / 1000));
            }

            @Override
            public void onFinish() {
                btnGetCode.setVisibility(View.VISIBLE);
                tvCountdown.setVisibility(View.GONE);
                tvCountdown.setText("60秒");
            }
        }.start();
    }

    //到达首页面
    public void goToThematicSection() {
        ARouter.getInstance().build("/thematicsection/MainActivity1").navigation();
    }

    //进行短暂提示的语言输入
    public void shouTips(String tips) {
        Toast.makeText(this, tips, Toast.LENGTH_SHORT).show();
    }
    //进行注册的时候弹出对话框，使用户进行信息的确认
    public void shouDialog() {
        LayoutInflater inflater = LayoutInflater.from(LoginUpActivity.this);
        View layout = inflater.inflate(R.layout.custon_dialog, null);
        TextView textView = layout.findViewById(R.id.dialog_emailNumber);
        textView.setText("邮箱账号：" + editText_emailNumber_in.getText().toString());
        TextView textView1 = layout.findViewById(R.id.dialog_password);
        textView1.setText("密码：" + editText_password_in.getText().toString());
        AlertDialog.Builder dialog = new AlertDialog.Builder(LoginUpActivity.this);
        dialog.setView(layout);
        dialog.setNegativeButton("重新输入", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        dialog.setPositiveButton("注册并登录", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.d("TAG", editText_emailNumber_in.getText().toString());
            }
        });
        dialog.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }

    public void SendToast(final String message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(LoginUpActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public Boolean CheckEmailRight(String s) {
        return s != null && s.matches(EMAIL_REGEX);
    }

    public Boolean CheckPasswordRight(String s) {
        return s != null && s.matches(PASSWORD_PATTERN);
    }
}