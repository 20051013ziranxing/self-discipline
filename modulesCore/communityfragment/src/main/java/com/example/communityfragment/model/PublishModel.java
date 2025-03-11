package com.example.communityfragment.model;

import android.util.Log;

import com.example.communityfragment.contract.IPublishContract;
import com.example.communityfragment.presenter.PublishPresenter;
import com.example.eventbus.UserBaseMessageEventBus;
import com.example.localdatabase.UserMessageHelper;
import com.example.localdatabase.bean.UserBaseMessage;

import org.apache.commons.io.FileUtils;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
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


    public PublishModel(PublishPresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void publish(String content,String userId, String imagePath, IPublishContract.PublishCallback callback) {

        // 通过表单上传
        MultipartBody.Builder requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM);

        // 上传文件及类型
        requestBody.addFormDataPart("user_id", userId);
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
}
