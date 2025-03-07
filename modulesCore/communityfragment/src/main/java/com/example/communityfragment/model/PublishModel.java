package com.example.communityfragment.model;

import android.util.Log;

import com.example.communityfragment.contract.IPublishContract;
import com.example.communityfragment.presenter.PublishPresenter;

import org.apache.commons.io.FileUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class PublishModel implements IPublishContract.Model {
    public static final String TAG = "PublishFunctionTAG";

    private PublishPresenter presenter;
    private final static String BASE_URL = "http://101.200.121.142:9999";

    private final static String POST_URL = BASE_URL + "/community/post";

    private OkHttpClient client = new OkHttpClient();
    private File imageFile;


    public PublishModel(PublishPresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void publish(String content, String imagePath, IPublishContract.PublishCallback callback) {
        // 通过表单上传
        MultipartBody.Builder requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM);

        // 上传文件及类型
        requestBody.addFormDataPart("user_id", "3");
        requestBody.addFormDataPart("content", content);
        File imageFile;
        if (imagePath != null) {
            imageFile = new File(imagePath);
            RequestBody fileBody = RequestBody.create(MediaType.parse("image/*"), imageFile);
            requestBody.addFormDataPart("file", imageFile.getName(), fileBody);
        } else {
            requestBody.addFormDataPart("file", "");
        }

        Request request = new Request.Builder()
                .url(POST_URL)
                .post(requestBody.build())
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "提交帖子失败: " + e.getMessage());
                callback.onFailure();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseBody = response.body().string();
                Log.d(TAG, "onResponse: " + responseBody);
                if (response.isSuccessful()) {
                    try {
                        JSONObject json = new JSONObject(responseBody);
                        int postId = json.getJSONObject("data").getInt("id");
                        callback.onSuccess(postId);
                        Log.d(TAG, "发布成功: " + postId);
                    } catch (JSONException e) {
                        Log.e(TAG, "解析帖子ID失败: " + e.getMessage());
                        callback.onFailure();
                    }
                } else {
                    Log.e(TAG, "提交帖子响应异常: " + response.code());
                    callback.onFailure();
                }
            }
        });
    }

//    @Override
//    public void publish(String content, String imagePath, IPublishContract.PublishCallback callback) {
//        File file = new File(imagePath);
//        String base64Image;
//        try {
//            // 读取文件内容并转换为 Base64 字符串
//            byte[] fileBytes = java.nio.file.Files.readAllBytes(file.toPath());
//            base64Image = java.util.Base64.getEncoder().encodeToString(fileBytes);
//        } catch (IOException e) {
//            Log.e(TAG, "读取图片失败: " + e.getMessage());
//            callback.onFailure();
//            return;
//        }
//
//        JSONObject jsonObject = new JSONObject();
//        try {
//            jsonObject.put("user_id", "1");
//            jsonObject.put("content", content);
//            jsonObject.put("image_url", base64Image);
//        } catch (JSONException e) {
//            Log.e(TAG, "构造 JSON 失败: " + e.getMessage());
//            callback.onFailure();
//            return;
//        }
//
//        // 将 JSON 对象转换为字符串
//        String jsonStr = jsonObject.toString();
//
//        // 使用 raw 方式创建请求体，MediaType 设置为 text/plain
//        RequestBody requestBody = RequestBody.create(MediaType.parse("text/plain"), jsonStr);
//
//        Request request = new Request.Builder()
//                .url(POST_URL)
//                .post(requestBody)
//                .build();
//
//        client.newCall(request).enqueue(new Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//                Log.e(TAG, "提交帖子失败: " + e.getMessage());
//                callback.onFailure();
//            }
//
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//                if (response.isSuccessful()) {
//                    try {
//                        JSONObject json = new JSONObject(response.body().string());
//                        int postId = json.getJSONObject("data").getInt("id");
//                        callback.onSuccess(postId);
//                    } catch (JSONException e) {
//                        Log.e(TAG, "解析帖子ID失败: " + e.getMessage());
//                        callback.onFailure();
//                    }
//                } else {
//                    Log.e(TAG, "提交帖子响应异常: " + response.code());
//                    callback.onFailure();
//                }
//            }
//        });
//    }

}
