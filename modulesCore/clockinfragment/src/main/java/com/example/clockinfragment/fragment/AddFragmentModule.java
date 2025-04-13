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
    public void createAPunchInTask(String user_id, String title, String start_date, String end_date,int target_checkin_count, int icon,
                                   String motivation_message, final AddFragmentModule.ModelCallback callback) {
        String url = "checkin";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("user_id",user_id);
            jsonObject.put("title", title);
            jsonObject.put("start_date", start_date);
            jsonObject.put("end_date", end_date);
            jsonObject.put("icon", icon);
            jsonObject.put("target_checkin_count", target_checkin_count);
            jsonObject.put("motivation_message", motivation_message);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        String json = jsonObject.toString();
        PublicNetworkRequestMethod(url, json, callback);
    }

    //公共提取出来的post方法
    public void PublicNetworkRequestMethod(String url,String Json, AddFragmentModule.ModelCallback callback) {
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
