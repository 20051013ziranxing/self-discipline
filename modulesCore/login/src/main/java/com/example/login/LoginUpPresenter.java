package com.example.login;

import android.util.Log;

import com.alibaba.android.arouter.launcher.ARouter;
import com.example.sendEmail.sendEmailMessage;

public class LoginUpPresenter {
    public static final String TAG = "TestTT_LoginUpPresenter";
    LoginUpActivity loginUpActivity;
    UserMessageHelper userMessageHelper;

    public LoginUpPresenter(LoginUpActivity activity) {
        this.loginUpActivity = activity;
        userMessageHelper = new UserMessageHelper(activity, "AllUsersMessage", null, 1);
    }
    //注册操作需要传入用户名，密码，邮件地址
    public void signIn(String userName, String userPassword, String userEmail) {
        boolean insert = true;
        Log.d(TAG, String.valueOf(userEmail == null));
        if (userName == null || userPassword == null || userEmail == null ||
                userName.matches(".*\\s+.*") || userPassword.matches(".*\\s+.*") || userEmail.matches(".*\\s+.*")) {
            insert = false;
            loginUpActivity.shouTips("注册信息的输入不规范");
        }
        if (insert && userMessageHelper.queryUser(userEmail) ==  null) {
            insert = userMessageHelper.insert(userName, userPassword, userEmail);
        }
        if (insert) {
            Log.d(TAG, "添加成功");
        } else {
            Log.d(TAG, "添加失败");
        }
        userMessageHelper.queryAllUser();
    }
    public void loginUpByPassword(String userEmail, String userPassword) {
        String s = userMessageHelper.queryUser(userEmail);
        if (s == null) {
            loginUpActivity.shouTips("该账号并没注册");
        } else if (userPassword.equals(s)) {
            loginUpActivity.goToThematicSection();
        } else {
            loginUpActivity.shouTips("密码错误，请检查之后重新进行输入");
        }
    }

    public void forgetPassword(String emailNumber) {
        String s = userMessageHelper.queryUser(emailNumber);
        if(s == null) {
            loginUpActivity.shouTips("该账号还未进行注册");
        } else {
            ARouter.getInstance().build("/findPassword/MainActivityFindPassword")
                    .withString("emailNumber", emailNumber).navigation();
        }
    }
}
