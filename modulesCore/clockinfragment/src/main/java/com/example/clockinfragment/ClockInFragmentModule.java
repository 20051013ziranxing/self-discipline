package com.example.clockinfragment;

import android.util.Log;

import com.example.clockinfragment.fragment.ModifyFragmentModule;
import com.example.networkrequests.NetworkClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.RequestBody;

public class ClockInFragmentModule {
    private static final String TAG = "TestTT_ClockInFragmentModule";
    NetworkClient networkClient;

    public ClockInFragmentModule(NetworkClient networkClient) {
        this.networkClient = networkClient;
    }

    //提前结束打卡
    public void modifyTheClockInInformationToEndEarly(int id, String start_date,String end_date, final ModelCallback callback) {
        String url = "http://116.62.29.172:9999/checkin/update";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("id", id);
            jsonObject.put("start_date", start_date);
            jsonObject.put("end_date", end_date);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        String json = jsonObject.toString();
        PublicNetworkRequestMethod(url, json, callback);
    }
    //标记打卡一次
    public void createAPunchInTask(String user_id, String status, String title, String target_checkin_count, final ModelCallback callback) {
        String url = "http://116.62.29.172:9999/profile";
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

    //打卡次数完成加1
    public void add1ToTheNumberOfCheckInsCompleted(int checkin_id, String date, final ModelCallback callback) {
        String url = "http://116.62.29.172:9999/IncrementCheckinCount";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("checkin_id", checkin_id);
            jsonObject.put("date", date);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        String json = jsonObject.toString();
        PublicNetworkRequestMethod(url, json, callback);
    }

    //删除打卡任务
    public void deleteAClockInTask(String checkin_id, final ModifyFragmentModule.ModelCallback callback) {
        String url = "http://116.62.29.172:9999/delete-todo/" + checkin_id;
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

    //获取当天的所有打卡记录
    public void getAllThePunchesForAGivenDay(String user_id, String date, final ModelCallback callback) {
        String url = "http://116.62.29.172:9999/get-checkinsByUserAndDate";
        HttpUrl.Builder httpUrlBuilder = HttpUrl.parse("http://116.62.29.172:9999/get-checkinsByUserAndDate").newBuilder();
        httpUrlBuilder.addQueryParameter("user_id", user_id);
        httpUrlBuilder.addQueryParameter("date", date);
        publicGetNetworkRequestMethod(url, httpUrlBuilder, callback);
    }

    //公共提取出来的post方法
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

    public void publicGetNetworkRequestMethod(String url,HttpUrl.Builder httpUrlBuilder, ClockInFragmentModule.ModelCallback callback) {
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
