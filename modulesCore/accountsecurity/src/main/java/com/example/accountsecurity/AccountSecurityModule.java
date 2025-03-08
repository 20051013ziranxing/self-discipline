package com.example.accountsecurity;

import android.app.Activity;

import com.example.localdatabase.UserMessageHelper;
import com.example.networkrequests.NetworkClient;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class AccountSecurityModule {
    UserMessageHelper userMessageHelper;
    private NetworkClient networkClient;
    public AccountSecurityModule(Activity activity, NetworkClient networkClient) {
        this.userMessageHelper = new UserMessageHelper(activity, "AllUsersMessage", null, 1);
        this.networkClient = networkClient;
    }
    public void signOut() {
        userMessageHelper.updateUniqueUserToken(null);
    }
    public void modifyTheUserSAvatar(String id, File file) {
        String url = "http://101.200.121.142:9999/profile";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("id", id);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 创建请求体
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("json", jsonObject.toString())
                .addFormDataPart("file", file.getName(), RequestBody.create(file, MediaType.parse("image/jpeg"))) // 上传文件
                .build();
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
