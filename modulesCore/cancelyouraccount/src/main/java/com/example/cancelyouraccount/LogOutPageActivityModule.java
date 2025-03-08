package com.example.cancelyouraccount;

import android.app.Activity;
import android.util.Log;

import com.example.localdatabase.UserMessageHelper;
import com.example.networkrequests.NetworkClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class LogOutPageActivityModule {
    public static final String TAG = "TestTT_LogOutPageActivityModule";
    UserMessageHelper userMessageHelper;
    private NetworkClient networkClient;
    public LogOutPageActivityModule(Activity activity, NetworkClient networkClient) {
        this.userMessageHelper = new UserMessageHelper(activity, "AllUsersMessage", null, 1);
        this.networkClient = networkClient;
    }

    public void logOutOfTheOperation(String username,String password, final ModelCallback modelCallback) {
        String url = "http://101.200.121.142:9999/deactivate";
        JSONObject jsonObject = new JSONObject();
        try {
            Log.d(TAG, "username"+ username + "password"+ password);
            jsonObject.put("username", username);
            jsonObject.put("password", password);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        String jsonString = jsonObject.toString();
        PublicNetworkRequestMethod(url, jsonString, modelCallback);
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

    public String getUserName() {
        return userMessageHelper.queryAllUser().getUserName();
    }
    public void deleteUserLocalMessage() {
        userMessageHelper.clearUsersTable();
    }
}
