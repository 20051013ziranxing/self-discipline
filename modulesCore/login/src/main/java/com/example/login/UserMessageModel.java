package com.example.login;

import android.app.Activity;

import com.example.localdatabase.UserMessageHelper;
import com.example.networkrequests.NetworkClient;

import org.json.JSONObject;

import java.io.IOException;

public class UserMessageModel {
    private final static String TAG = "TestTT_UserMessageModel";
    private NetworkClient networkClient;
    UserMessageHelper userMessageHelper;

    public UserMessageModel(Activity activity, NetworkClient networkClient) {
        this.userMessageHelper = new UserMessageHelper(activity, "AllUsersMessage", null, 1);
        this.networkClient = networkClient;
    }

    public boolean insert(String userName, String userPictureURL, String userEmail, String userToken, int userId) {
        boolean insert = userMessageHelper.insert(userName, userPictureURL, userEmail, userToken, userId);
        return insert;
    }
    public String queryUser(String userEmail) {
        String s = userMessageHelper.queryUser(userEmail);
        return s;
    }

    //发送验证码
    public void sendEmailVerificationCode(String email, final ModelCallback callback) {
        String url = "http://101.200.121.142:9999/register-email";
        String jsonBody = "{\"email\":\"" + email + "\"}";
        PublicNetworkRequestMethod(url, jsonBody, callback);
    }
    //验证码的验证
    public void VerificationOfTheVerificationCode(String emailNumber, String code, final ModelCallback callback) {
        String url = "http://101.200.121.142:9999/VerifyCode-email";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("email", emailNumber);
            jsonObject.put("code", code);
        } catch (Exception e) {
            e.printStackTrace();
        }
        String jsonString = jsonObject.toString();
        PublicNetworkRequestMethod(url, jsonString, callback);
    }
    public void LogInWithYourPassword(String email, String password, final ModelCallback callback) {
        String url = "http://101.200.121.142:9999/login";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("email", email);
            jsonObject.put("password", password);
        } catch (Exception e) {
            e.printStackTrace();
        }
        String jsonString = jsonObject.toString();
        PublicNetworkRequestMethod(url, jsonString, callback);
    }

    //进行注册操作
    public void Registration(String userName, String userPassword, String userEmail, String userCode, final ModelCallback callback) {
        String url = "http://101.200.121.142:9999/register";
        String jsonBody = "{\"username\":\"" + userName + "\",\"password\":\"" + userPassword + "\",\"email\":\"" + userEmail + "\",\"code\":\"" + userCode + "\"}";
        PublicNetworkRequestMethod(url, jsonBody, callback);
    }

    //将调用接口提出来
    public void PublicNetworkRequestMethod(String url,String Json, ModelCallback callback) {
        networkClient.get(url, Json, new NetworkClient.NetworkCallback() {
            @Override
            public void onSuccess(String response) {
                callback.onSuccess(response);
            }

            @Override
            public void onFailure(IOException e) {
                callback.onFailure(e);
            }
        });
    }

    public interface ModelCallback {
        Boolean onSuccess(String response);
        Boolean onFailure(IOException e);
    }
}
