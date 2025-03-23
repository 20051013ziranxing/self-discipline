package com.example.login;

import android.util.Log;

import com.alibaba.android.arouter.launcher.ARouter;
import com.example.eventbus.UserBaseMessageEventBus;
import com.example.localdatabase.bean.UserTables;
import com.example.login.bean.UserBaseMessage;
import com.example.networkrequests.NetworkClient;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;

public class LoginUpPresenter {
    public static final String TAG = "TestTT_LoginUpPresenter";
    LoginUpActivity loginUpActivity;
    LoginUpModel loginUpModel;

    public LoginUpPresenter(LoginUpActivity activity) {
        NetworkClient networkClient = new NetworkClient();
        this.loginUpActivity = activity;
        loginUpModel = new LoginUpModel(activity, networkClient);
    }
    //进行注册操作，注册操作需要传入用户名，密码，邮件地址，还要进行验证码的验证，此处还没有进行添加
    public void signIn(String userName, String userPassword, String userEmail, String userCode, Boolean isCheck) {
        loginUpModel.Registration(userName, userPassword, userEmail, userCode, new LoginUpModel.ModelCallback() {
            @Override
            public Boolean onSuccess(String response) {
                Log.d(TAG, "注册：" + response);
                UserBaseMessage userBaseMessage = new Gson().fromJson(response, UserBaseMessage.class);
                String userToken;
                if (isCheck) {
                    userToken = userBaseMessage.getMessage().getAccess_token();
                } else {
                    userToken = null;
                }
                String userPictureURL = userBaseMessage.getMessage().getAvatar_url();
                String userId = userBaseMessage.getMessage().getUser_id();
                loginUpModel.insert(userName, userPictureURL, userEmail, userToken, userId);
                UserBaseMessageEventBus userBaseMessage1 = new UserBaseMessageEventBus(userName, userPictureURL, userEmail, userToken, userId);
                EventBus.getDefault().postSticky(userBaseMessage1);
                loginUpModel.insert(new UserTables(userName, userId, null, null));
                loginUpActivity.goToThematicSection();
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
    public void loginUpByPassword(String userEmail, String userPassword, Boolean ischeck) {
        Log.d(TAG, userEmail + "hh" + userPassword);
        loginUpModel.LogInWithYourPassword(userEmail, userPassword, new LoginUpModel.ModelCallback() {
            @Override
            public Boolean onSuccess(String response) {
                Log.d(TAG, "登录成功，收到的返回数据为：" + response);
                UserBaseMessage userBaseMessage = new Gson().fromJson(response, UserBaseMessage.class);
                String userName = userBaseMessage.getMessage().getUsername();
                String userPictureURL = userBaseMessage.getMessage().getAvatar_url();
                String userToken = userBaseMessage.getMessage().getAccess_token();
                String userId = userBaseMessage.getMessage().getUser_id();
                Log.d(TAG, userPictureURL + userToken + userId + userName);
                if (ischeck == true) {
                    loginUpModel.insert(userName, userPictureURL, userEmail,userToken, userId);
                } else {
                    loginUpModel.insert(userName, userPictureURL, userEmail, null, userId);
                }
                UserBaseMessageEventBus userBaseMessage1 = new UserBaseMessageEventBus(userName, userPictureURL, userEmail, userToken, userId);
                EventBus.getDefault().postSticky(userBaseMessage1);
                loginUpModel.insert(new UserTables(userName, userId, null, null));
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
        String s = loginUpModel.queryUser(emailNumber);
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
        loginUpModel.sendEmailVerificationCode(emailNumber, new LoginUpModel.ModelCallback() {
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
        loginUpModel.VerificationOfTheVerificationCode(emailNumber, code, new LoginUpModel.ModelCallback() {
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
