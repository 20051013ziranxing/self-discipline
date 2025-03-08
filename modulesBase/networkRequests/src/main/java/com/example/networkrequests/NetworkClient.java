package com.example.networkrequests;

import android.util.Log;

import androidx.annotation.NonNull;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class NetworkClient {
    private static final String TAG = "TestTT_NetworkClient";
    private final OkHttpClient client = new OkHttpClient();

    public void get(String url, String Json, final NetworkCallback callback) {
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody requestBody = RequestBody.create(
                JSON, Json);
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                callback.onFailure(e);
                if (e instanceof SocketTimeoutException) {
                    Log.e(TAG, "请求超时: " + e.getMessage());
                } else if (e instanceof UnknownHostException) {
                    Log.e(TAG, "无法解析主机名: " + e.getMessage());
                } else {
                    Log.e(TAG, "其他网络错误: " + e.getMessage());
                }
                Log.e(TAG, "请求失败: " + e.getMessage());
                Log.e(TAG, "请求信息: " + call.request().url());
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful() && response.body() != null) {
                    String responseData = response.body().string();
                    callback.onSuccess(responseData);
                    Log.d(TAG, "成功");
                } else {
                    String errorResponse = response.body() != null ? response.body().string() : "No response body";
                    Log.d(TAG, "请求失败: " + response.code() + " - " + response.message() + "\n" + errorResponse);
                    callback.onFailure(new IOException("Unexpected code " + response));
                }
            }
        });
    }

    public interface NetworkCallback {
        void onSuccess(String response);
        void onFailure(IOException e);
    }
}
