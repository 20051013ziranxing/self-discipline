package com.example.login;

import android.util.Log;

import com.alibaba.android.arouter.launcher.ARouter;
import com.example.networkrequests.NetworkClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class LoginUpPresenter {
    public static final String TAG = "TestTT_LoginUpPresenter";
    LoginUpActivity loginUpActivity;
    UserMessageModel userMessageModel;

    public LoginUpPresenter(LoginUpActivity activity) {
        NetworkClient networkClient = new NetworkClient();
        this.loginUpActivity = activity;
        userMessageModel = new UserMessageModel(activity, networkClient);
    }
    //进行注册操作，注册操作需要传入用户名，密码，邮件地址，还要进行验证码的验证，此处还没有进行添加
    public void signIn(String userName, String userPassword, String userEmail, String userCode, Boolean isCheck) {
        userMessageModel.Registration(userName, userPassword, userEmail, userCode, new UserMessageModel.ModelCallback() {
            @Override
            public Boolean onSuccess(String response) {
                loginUpActivity.goToThematicSection();
                userMessageModel.insert(userName, userPassword, userEmail, isCheck);
                return null;
            }

            @Override
            public Boolean onFailure(IOException e) {
                loginUpActivity.SendToast("注册失败");
                return null;
            }
        });
    }
    //根据密码进行登录
    public void loginUpByPassword(String userEmail, String userPassword) {
        Log.d(TAG, userEmail + "hh" + userPassword);
        userMessageModel.LogInWithYourPassword(userEmail, userPassword, new UserMessageModel.ModelCallback() {
            @Override
            public Boolean onSuccess(String response) {
                Log.d(TAG, "登录成功，收到的返回数据为：" + response);
                loginUpActivity.goToThematicSection();
                return null;
            }

            @Override
            public Boolean onFailure(IOException e) {
                Log.d(TAG, e.toString());
                loginUpActivity.SendToast("登陆失败");
                return null;
            }
        });
    }
    //忘记密码根据Email的验证码对密码进行更改
    public void forgetPassword(String emailNumber) {
        ARouter.getInstance().build("/findPassword/MainActivityFindPassword")
                .withString("emailNumber", emailNumber).navigation();
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
    public void SendAnEmailVerificationCodeRegistered(String emailNumber) {
        userMessageModel.sendEmailVerificationCode(emailNumber, new UserMessageModel.ModelCallback() {
            @Override
            public Boolean onSuccess(String response) {
                loginUpActivity.SendToast("发送验证码成功");
                return true;
            }

            @Override
            public Boolean onFailure(IOException e) {
                return false;
            }
        });
    }
    //进行验证码的验证以及注册
    public void VerificationOfTheVerificationCode(String emailNumber, String code, String userName, String userPassword) {
        userMessageModel.VerificationOfTheVerificationCode(emailNumber, code, new UserMessageModel.ModelCallback() {
            @Override
            public Boolean onSuccess(String response) {
                Log.d(TAG, "验证成功" + emailNumber + code);
                return true;
            }

            @Override
            public Boolean onFailure(IOException e) {
                loginUpActivity.SendToast("验证码输入错误");
                return false;
            }
        });
    }
}
