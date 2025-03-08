package com.example.todofragment;

import com.example.networkrequests.NetworkClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.RequestBody;

public class ToDoFragmentModule {
    NetworkClient networkClient;

    public ToDoFragmentModule(NetworkClient networkClient) {
        this.networkClient = networkClient;
    }
    public void getToDoThings(String user_id, String updated_at, final ModelCallback callback) {
        String url = "http://101.200.121.142:9999/get-todo";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("user_id", user_id);
            jsonObject.put("updated_at", updated_at);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        String json = jsonObject.toString();
        PublicNetworkRequestMethod(url, json, callback);
    }
    public void PublicNetworkRequestMethod(String url,String Json, ToDoFragmentModule.ModelCallback callback) {
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
