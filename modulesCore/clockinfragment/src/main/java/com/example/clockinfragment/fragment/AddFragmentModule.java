package com.example.clockinfragment.fragment;

import com.example.clockinfragment.ClockInFragmentModule;
import com.example.networkrequests.NetworkClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.RequestBody;

public class AddFragmentModule {
    NetworkClient networkClient;

    public AddFragmentModule(NetworkClient networkClient) {
        this.networkClient = networkClient;
    }
    public void createAPunchInTask(String user_id, String status, String title, String target_checkin_count, final ClockInFragmentModule.ModelCallback callback) {
        String url = "http://101.200.121.142:9999/profile";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("user_id",user_id);
            jsonObject.put("status", status);
            jsonObject.put("title", title);
            jsonObject.put("target_checkin_count", target_checkin_count);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        String json = jsonObject.toString();
        PublicNetworkRequestMethod(url, json, callback);
    }

    //公共提取出来的post方法
    public void PublicNetworkRequestMethod(String url,String Json, ClockInFragmentModule.ModelCallback callback) {
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
    public interface ModelCallback {
        Boolean onSuccess(String response);
        Boolean onFailure(IOException e);
    }
}
