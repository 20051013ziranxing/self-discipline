package com.example.todofragment;

import com.example.networkrequests.NetworkClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.RequestBody;

public class MyBottomSheetDialogFragmentModule {
    private NetworkClient networkClient;

    public MyBottomSheetDialogFragmentModule(NetworkClient networkClient) {
        this.networkClient = networkClient;
    }

    public void modifyTheAgencyInformation(String id, String title, String description, String status, String updated_at, final ToDoFragmentModule.ModelCallback callback) {
        String url = "http://101.200.121.142:9999/reset-todo";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("id", id);
            jsonObject.put("title",title);
            jsonObject.put("description",description);
            jsonObject.put("status",status);
            jsonObject.put("updated_at", updated_at);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        String json = jsonObject.toString();
        publicNetworkRequestMethodPost(url, json, callback);
    }

    public void addToDoThing(String title, String description, String status, String user_id, String updated_at, final ModelCallback callback) {
        String url = "http://101.200.121.142:9999/create-todo";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("title", title);
            jsonObject.put("description", description);
            jsonObject.put("status", status);
            jsonObject.put("user_id", user_id);
            jsonObject.put("updated_at", updated_at);
        } catch (Exception e) {
            e.printStackTrace();
        }
        String json = jsonObject.toString();
        PublicNetworkRequestMethod(url, json, callback);
    }

    public void publicNetworkRequestMethodPost(String url, String json, ToDoFragmentModule.ModelCallback callback) {
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody requestBody = RequestBody.create(
                JSON, json);
        networkClient.put(url, requestBody, new NetworkClient.NetworkCallback() {
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
