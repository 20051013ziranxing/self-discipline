package com.example.communityfragment.model;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.communityfragment.bean.Comment;
import com.example.communityfragment.contract.IPostContract;
import com.example.communityfragment.presenter.PostPresenter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class PostModel implements IPostContract.Model {
    public static final String TAG = "PostModelTAG";

    private PostPresenter mPresenter;
    private Context mContext;
    private final static String BASE_URL = "http://101.200.121.142:9999";

    private final static String COMMENT_URL = BASE_URL + "/comment";
    private final static String COMMENTS_URL = BASE_URL + "/comments";

    private OkHttpClient client = new OkHttpClient();

    public PostModel(Context mContext, PostPresenter mPresenter) {
        this.mContext = mContext;
        this.mPresenter = mPresenter;

    }

    @Override
    public void getComments(int postId) {
        String GETCOMMENTS_URL = COMMENTS_URL + "?post_id" + postId;

        Request request = new Request.Builder()
                .url(GETCOMMENTS_URL)
                .get()
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d(TAG, "onFailure: " + e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseData = response.body().string();
                Log.d(TAG, "onResponse: " + responseData);
                if (response.isSuccessful()) {
                    try {
                        Log.d(TAG, "帖子 : " + responseData);
                        JSONObject json = new JSONObject(responseData);
                        if (json.isNull("data")) {
                            Log.d(TAG, "无评论");
                            mPresenter.onCommentsSuccess(new ArrayList<>());
                        } else {
                            JSONArray jsonArray = json.getJSONArray("data");
                            List<Comment> comments = new ArrayList<>();
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                Comment comment = new Comment();
                                comment.setUserid(jsonObject.getString("user_id"));
                                comment.setContent(jsonObject.getString("content"));
                                comments.add(comment);
                                Log.d(TAG, "帖子 : " + comment.toString());
                            }
                            mPresenter.onCommentsFailure();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    Log.e(TAG, "onResponse: " + response.code());
                }
            }
        });
    }

    @Override
    public void comment(int postId, String comment) {
        MultipartBody.Builder requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM);

        requestBody.addFormDataPart("user_id", "2");
        requestBody.addFormDataPart("post_id", String.valueOf(postId));
        requestBody.addFormDataPart("content", comment);

        Request request = new Request.Builder()
                .url(COMMENT_URL)
                .post(requestBody.build())
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.d(TAG, "onFailure: " + e.getMessage());
                mPresenter.onPublishCommentSuccess();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                String responseData = response.body().string();
                if (response.isSuccessful()) {
                    Log.d(TAG, "onResponse: " + responseData);
                    Comment comment = new Comment();
                    try {
//                        JSONObject json = new JSONObject(responseData);
//
//                        JSONObject jsonObject = json.getJSONObject("data");
//                        comment.setId(String.valueOf(jsonObject.getInt("id")));
//                        comment.setUserid(jsonObject.getString("user_id"));
//                        comment.setUserName(jsonObject.getString("user_name"));
//                        comment.setUserAavatar(jsonObject.getString("user_avatar"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                    mPresenter.onPublishCommentSuccess();
                } else {
                    Log.d(TAG, "onResponse: " + responseData);
                    mPresenter.onPublishCommentFailure();
                }
            }
        });
    }
}
