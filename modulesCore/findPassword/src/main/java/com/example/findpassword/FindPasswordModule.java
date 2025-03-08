package com.example.findpassword;

import com.example.networkrequests.NetworkClient;

import org.json.JSONObject;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.RequestBody;

public class FindPasswordModule {
    private final static String TAG = "TestTT_FindPasswordModule";
    private NetworkClient networkClient;

    public FindPasswordModule(NetworkClient networkClient) {
        this.networkClient = networkClient;
    }

    //发送验证码
    public void sendEmailVerificationCode(String email, final ModelCallback callback) {
        String url = "http://101.200.121.142:9999/reset-email";
        String jsonBody = "{\"email\":\"" + email + "\"}";
        PublicNetworkRequestMethod(url, jsonBody, callback);
    }
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
    public void ResetOfPassword(String password, final ModelCallback callback) {
        String url = "http://101.200.121.142:9999/reset-password";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("password", password);
        } catch (Exception e) {
            e.printStackTrace();
        }
        String jsonString = jsonObject.toString();
        PublicNetworkRequestMethod(url, jsonString, callback);
    }
    public interface ModelCallback {
        Boolean onSuccess(String response);
        Boolean onFailure(IOException e);
    }
    //将调用接口提出来
    public void PublicNetworkRequestMethod(String url,String Json, ModelCallback callback) {
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody requestBody = RequestBody.create(
                JSON, Json);
        networkClient.get(url, requestBody, new NetworkClient.NetworkCallback() {
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
}
