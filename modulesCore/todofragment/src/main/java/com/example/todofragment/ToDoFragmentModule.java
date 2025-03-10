package com.example.todofragment;

import android.util.Log;

import com.example.networkrequests.NetworkClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.RequestBody;

public class ToDoFragmentModule {
    private static final String TAG = "TestTT_ToDoFragmentModule";
    NetworkClient networkClient;

    public ToDoFragmentModule(NetworkClient networkClient) {
        this.networkClient = networkClient;
    }

    public void deleteToDoThing(String Id, final ModelCallback callback) {
        String url = "http://101.200.121.142:9999/delete-todo/" + Id;
        networkClient.deleteTask(url, Id, new NetworkClient.NetworkCallback() {
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
    public void getToDoThings(String user_id, String updated_at, final ModelCallback callback) {
        HttpUrl.Builder httpUrlBuilder = HttpUrl.parse("http://101.200.121.142:9999/get-todo").newBuilder();
        httpUrlBuilder.addQueryParameter("user_id", user_id);
        httpUrlBuilder.addQueryParameter("updated_at", updated_at);
        publicGetNetworkRequestMethod("http://101.200.121.142:9999/get-todo", httpUrlBuilder, callback);
    }

    public void markWhetherTheTaskIsCompletedOrNot(String id, Boolean isFinish, final ModelCallback callback) {
        String url = "http://101.200.121.142:9999/complete";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("id", id);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        String json = jsonObject.toString();
        PublicNetworkRequestMethodPut(url, json, callback);
    }

    /*public void markTheTaskAsComplete(String Id, Boolean isFinish, final ModelCallback callback) {
        HttpUrl.Builder httpUrlBuilder = HttpUrl.parse("http://101.200.121.142:9999/complete").newBuilder();
        httpUrlBuilder.addQueryParameter("id", Id);
        publicGetNetworkRequestMethod("http://101.200.121.142:9999/complete", httpUrlBuilder, callback);
    }*/
    public void PublicNetworkRequestMethodPut(String url,String Json, ToDoFragmentModule.ModelCallback callback) {
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody requestBody = RequestBody.create(
                JSON, Json);
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

    public void publicGetNetworkRequestMethod(String url,HttpUrl.Builder httpUrlBuilder, ToDoFragmentModule.ModelCallback callback) {
        new NetworkClient().get(url, httpUrlBuilder, new NetworkClient.NetworkCallback() {
            @Override
            public void onSuccess(String responseData) {
                Log.d(TAG, "Response: " + responseData);
                callback.onSuccess(responseData);
            }

            @Override
            public void onFailure(IOException e) {
                Log.e(TAG, "请求失败", e);
                callback.onFailure(e);
            }
        });
    }

    public interface ModelCallback {
        Boolean onSuccess(String response);
        Boolean onFailure(IOException e);
    }
}
