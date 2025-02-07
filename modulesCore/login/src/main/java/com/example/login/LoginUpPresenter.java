package com.example.login;

import android.util.Log;

import com.alibaba.android.arouter.launcher.ARouter;
import com.example.localdatabase.UserMessageHelper;
import com.example.sendEmail.sendEmailMessage;

public class LoginUpPresenter {
    public static final String TAG = "TestTT_LoginUpPresenter";
    LoginUpActivity loginUpActivity;
    UserMessageModel userMessageModel;

    public LoginUpPresenter(LoginUpActivity activity) {
        this.loginUpActivity = activity;
        userMessageModel = new UserMessageModel(activity);
    }
    //进行注册操作，注册操作需要传入用户名，密码，邮件地址，还要进行验证码的验证，此处还没有进行添加
    public void signIn(String userName, String userPassword, String userEmail) {
        boolean insert = true;
        if (insert && userMessageModel.queryUser(userEmail) ==  null) {
            insert = userMessageModel.insert(userName, userPassword, userEmail);
        }
        if (insert) {
            Log.d(TAG, "添加成功");
            loginUpActivity.goToThematicSection();
        } else {
            Log.d(TAG, "添加失败");
        }
        userMessageModel.queryAllUser();
    }
    //根据密码进行登录
    public void loginUpByPassword(String userEmail, String userPassword) {
        String s = userMessageModel.queryUser(userEmail);
        if (s == null) {
            loginUpActivity.shouTips("该账号并没注册");
        } else if (userPassword.equals(s)) {
            loginUpActivity.goToThematicSection();
        } else {
            loginUpActivity.shouTips("密码错误，请检查之后重新进行输入");
        }
    }
    //忘记密码根据Email的验证码对密码进行更改
    public void forgetPassword(String emailNumber) {
        String s = userMessageModel.queryUser(emailNumber);
        if(s == null) {
            loginUpActivity.shouTips("该账号还未进行注册");
        } else {
            ARouter.getInstance().build("/findPassword/MainActivityFindPassword")
                    .withString("emailNumber", emailNumber).navigation();
        }
    }
    //注册时候对用户的信息进行查询，检查是否注册过了
    public boolean checkIsRegistered(String emailNumber) {
        String s = userMessageModel.queryUser(emailNumber);
        Log.d(TAG, s + "s");
        return s == null;
    }
    //检查输入是否规范
    public boolean checkIsSpecification(String userName, String userPassword, String userEmail) {
        if (userName.isEmpty() || userPassword.isEmpty() || userEmail.isEmpty() ||
                userName.matches(".*\\s+.*") || userPassword.matches(".*\\s+.*") || userEmail.matches(".*\\s+.*")) {
            loginUpActivity.shouTips("注册信息的输入不规范");
            return false;
        }
        return true;
    }
}
