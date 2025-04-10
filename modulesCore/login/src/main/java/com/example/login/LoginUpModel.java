package com.example.login;

import android.app.Activity;
import android.util.Log;

import com.example.localdatabase.ListOfLabelsHelper;
import com.example.localdatabase.UserMessageHelper;
import com.example.localdatabase.bean.UserTables;
import com.example.networkrequests.NetworkClient;

import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.RequestBody;

public class LoginUpModel {
    private final static String TAG = "TestTT_LoginUpModel";
    private NetworkClient networkClient;
    UserMessageHelper userMessageHelper;
    ListOfLabelsHelper listOfLabelsHelper;

    public LoginUpModel(Activity activity, NetworkClient networkClient) {
        this.userMessageHelper = new UserMessageHelper(activity, "AllUsersMessage", null, 1);
        this.listOfLabelsHelper = new ListOfLabelsHelper(activity, "hhh", null, 1);
        this.networkClient = networkClient;
    }

    public boolean insert(UserTables userTables) {
        boolean ret = false;
        Log.d(TAG, "我进行添加方法hhhhh" + userTables.getUserId());
        if (listOfLabelsHelper.findUserTablesByUserId(userTables.getUserId()) == null) {
            Log.d(TAG, "为bull，进行添加" + ret);
            ret = listOfLabelsHelper.insert(userTables);
            Log.d(TAG, "为bull，进行添加" + ret);
        }
        List<UserTables> allUserLabels = listOfLabelsHelper.getAllUserLabels();
        Log.d(TAG, String.valueOf(allUserLabels.size()));
        for (UserTables allUserLabel : allUserLabels) {
            Log.d(TAG, allUserLabel.getUserId() + allUserLabel.getUserName());
        }
        return ret;
    }

    public boolean insert(String userName, String userPictureURL, String userEmail, String userToken, String userId) {
        boolean insert = userMessageHelper.insert(userName, userPictureURL, userEmail, userToken, userId);
        return insert;
    }
    public String queryUser(String userEmail) {
        String s = userMessageHelper.queryUser(userEmail);
        return s;
    }

    //发送验证码
    public void sendEmailVerificationCode(String email, final ModelCallback callback) {
        String url = "http://116.62.29.172:9999/register-email";
        String jsonBody = "{\"email\":\"" + email + "\"}";
        PublicNetworkRequestMethod(url, jsonBody, callback);
    }
    //验证码的验证
    public void VerificationOfTheVerificationCode(String emailNumber, String code, final ModelCallback callback) {
        String url = "http://116.62.29.172:9999/VerifyCode-email";
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
        String url = "http://116.62.29.172:9999/login";
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
        String url = "http://116.62.29.172:9999/register";
        String jsonBody = "{\"username\":\"" + userName + "\",\"password\":\"" + userPassword + "\",\"email\":\"" + userEmail + "\",\"code\":\"" + userCode + "\"}";
        PublicNetworkRequestMethod(url, jsonBody, callback);
    }

    //将调用接口提出来
    public void PublicNetworkRequestMethod(String url,String Json, ModelCallback callback) {
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody requestBody = RequestBody.create(
                JSON, Json);
        networkClient.post(url, requestBody, new NetworkClient.NetworkCallback() {
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
