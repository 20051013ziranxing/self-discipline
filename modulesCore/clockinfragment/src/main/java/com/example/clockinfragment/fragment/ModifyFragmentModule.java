package com.example.clockinfragment.fragment;

import android.util.Log;

import com.example.networkrequests.NetworkClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.RequestBody;

public class ModifyFragmentModule extends AddFragmentModule{
    private static final String TAG = "TestTT_ModifyFragmentModule";
    NetworkClient networkClient;
    public ModifyFragmentModule(NetworkClient networkClient) {
        super(networkClient);
        this.networkClient = networkClient;
    }

    //删除打卡任务
    public void deleteAClockInTask(String checkin_id, final ModelCallback callback) {
        String url = "delete-todo/" + checkin_id;
        networkClient.deleteTask(url, new NetworkClient.NetworkCallback() {
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

    public void delete(String checkin_id, final ModelCallback callback) {
        Log.d(TAG, checkin_id);
        String url = "checkin/delete";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("checkin_id", checkin_id);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        String json = jsonObject.toString();
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody requestBody = RequestBody.create(
                JSON, json);
        networkClient.deleteTaskRe(url, requestBody, new NetworkClient.NetworkCallback() {
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

    //修改打卡信息
    public void modifyTheClockInInformation(int id, String title, String start_date, String end_date,
                                            int icon, int target_checkin_count, String motivation_message,
                                            final ModifyFragmentModule.ModelCallback callback) {
        String url = "checkin/update";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("id", id);
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
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody requestBody = RequestBody.create(
                JSON, json);
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

    //根据checkin_id获取打卡信息
    public void getAttendanceRecordsBasedOnYourID(String checkin_id, final ModifyFragmentModule.ModelCallback callback) {
        String url = "get-checkin";
        HttpUrl.Builder httpUrlBuilder = HttpUrl.parse("http://116.62.29.172:9999/get-checkin").newBuilder();
        httpUrlBuilder.addQueryParameter("checkin_id", checkin_id);
        httpUrlBuilder.addQueryParameter("checkin_id", checkin_id);
        publicGetNetworkRequestMethod("http://116.62.29.172:9999/get-todo", httpUrlBuilder, callback);
    }


    public void publicGetNetworkRequestMethod(String url,HttpUrl.Builder httpUrlBuilder, ModifyFragmentModule.ModelCallback callback) {
        new NetworkClient().get(url, httpUrlBuilder, new NetworkClient.NetworkCallback() {
            @Override
            public void onSuccess(String responseData) {
                Log.d(TAG, "Response: " + responseData);
                callback.onSuccess(responseData);
            }

            @Override
            public void onFailure(IOException e) {
                Log.e(TAG, "请求失败：hhhhh", e);
                callback.onFailure(e);
            }
        });
    }

    public interface ModelCallback {
        Boolean onSuccess(String response);
        Boolean onFailure(IOException e);
    }
}
