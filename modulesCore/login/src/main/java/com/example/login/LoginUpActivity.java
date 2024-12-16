package com.example.login;

import static android.app.ProgressDialog.show;

import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
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
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import org.w3c.dom.Text;

@Route(path = "/login/LoginUpActivity")
public class LoginUpActivity extends AppCompatActivity {
    TextView textView_loginUp;
    TextView textView_signIn;
    View view_loginUp;
    View view_signIn;
    LoginUpPresenter loginUpPresenter;
    //登录界面的控件
    Button button_loginUp;
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
        button_signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUpPresenter.signIn(editText_userName_in.getText().toString(), editText_password_in.getText().toString(), editText_emailNumber_in.getText().toString());
            }
        });
        //获取登陆界面的控件以及控件的点击事件的逻辑
        button_loginUp = findViewById(R.id.button_loginUp1);
        editText_emailNumber_up = findViewById(R.id.editText_emailNumber_up);
        editText_password_up = findViewById(R.id.editText_password_up);
        button_forgetPassword_up = findViewById(R.id.button_forgetPassword_up);
        checkBox_keepSignUp_up = findViewById(R.id.checkBox_keepSignUp_up);
        button_loginUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUpPresenter.loginUpByPassword(editText_emailNumber_up.getText().toString(), editText_password_up.getText().toString());
            }
        });
        button_forgetPassword_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUpPresenter.forgetPassword(editText_emailNumber_up.getText().toString());
            }
        });
        //选择登录还是注册
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
    }
    public void goToThematicSection() {
        ARouter.getInstance().build("/thematicsection/MainActivity1").navigation();
    }

    public void shouTips(String tips) {
        Toast.makeText(this, tips, Toast.LENGTH_SHORT).show();
    }
}