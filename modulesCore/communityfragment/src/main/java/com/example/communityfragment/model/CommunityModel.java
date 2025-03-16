package com.example.communityfragment.model;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.communityfragment.bean.Post;
import com.example.communityfragment.contract.ICommunityContract;
import com.example.communityfragment.presenter.CommunityPresenter;
import com.example.eventbus.UserBaseMessageEventBus;
import com.google.gson.JsonObject;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class CommunityModel implements ICommunityContract.Model {
    public static final String TAG = "CommunityModelTAG";
    private CommunityPresenter mPresenter;

    private final static String BASE_URL = "http://101.200.121.142:9999";

    private final static String ALLLIST_URL = BASE_URL + "/community/all-list";
    private final static String LIST_URL = BASE_URL + "/community/list";
    private final static String LIKE_URL = BASE_URL + "/community/like";
    private final static String UNLIKE_URL = BASE_URL + "/community/unlike";
    private final static String CHECKLIKESTATUS_URL = BASE_URL + "/check-like-status";
    private final static String DELETE_URL = BASE_URL + "/community/del-post";

    private OkHttpClient client = new OkHttpClient();

    public CommunityModel(CommunityPresenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void getData(String userId, int type) {
        Request request = null;
        if (type == 1) {
            request = new Request.Builder()
                    .url(ALLLIST_URL)
                    .get()
                    .build();
        } else if (type == 2) {
            String MYLIST_URL = LIST_URL + "?user_id=" + userId;
            request = new Request.Builder()
                    .url(MYLIST_URL)
                    .get()
                    .build();
        }

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d(TAG, "onFailure: " + e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseData = response.body().string();
//                Log.d(TAG, "onResponse: " + responseData);
                if (response.isSuccessful()) {
                    try {
                        Log.d(TAG, "帖子 : " + responseData);
                        JSONObject json = new JSONObject(responseData);
                        if (json.isNull("data")) {
                            Log.d(TAG, "帖子为空");
                            mPresenter.onDataReceived(new ArrayList<>());
                        } else {
                            JSONArray jsonArray = json.getJSONArray("data");
                            List<Post> postList = new ArrayList<>();
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                Post post = new Post();
                                post.setId(jsonObject.getInt("id"));
                                post.setUserid(jsonObject.getString("user_id"));
                                post.setUserName(jsonObject.getString("user_name"));
                                post.setUserAvatar(jsonObject.getString("avatar_url"));
                                post.setContent(jsonObject.getString("content"));
                                post.setImageUrl(jsonObject.getString("image_url"));
                                post.setCreatedTime(jsonObject.getString("created_at"));
                                post.setLikeConunt(jsonObject.getString("likes_count"));
                                post.setCommentCount(jsonObject.getString("comment_count"));

                                postList.add(post);
                                Log.d(TAG, "帖子 : " + post.toString());
                            }
                            mPresenter.onDataReceived(postList);
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
    public void checkLikeStatus(int postId, String userId) {
        JSONObject object = new JSONObject();
        try {
            object.put("user_id", userId);
            object.put("post_id", String.valueOf(postId));
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        String json = object.toString();
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), json);
        Request request = new Request.Builder()
                .url(CHECKLIKESTATUS_URL)
                .post(requestBody)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.d(TAG, "onFailure: " + e.getMessage());
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                String responseData = response.body().string();
                Log.d(TAG, "onResponse: " + responseData);
                if (response.isSuccessful()) {
                    try {
                        JSONObject jsonObject = new JSONObject(responseData);
                        if (jsonObject.getInt("data") == 0) {
                            mPresenter.updatePostLikeStatus(postId, false);
                        } else {
                            mPresenter.updatePostLikeStatus(postId, true);
                        }
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    Log.e(TAG, "onResponse: " + response.code());
                }
            }
        });
    }

    @Override
    public void deletePost(int postId) {
        String MYDELETE_URL = DELETE_URL + "?id=" +postId;
        Request request = new Request.Builder()
                .url(MYDELETE_URL)
                .delete()
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    mPresenter.deletePostSuccess(postId);
                }
            }
        });
    }


    public void likePost(int postId, String userId) {
        JSONObject object = new JSONObject();
        try {
            object.put("user_id", userId);
            object.put("post_id", String.valueOf(postId));
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        String json = object.toString();
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), json);
        Request request = new Request.Builder()
                .url(LIKE_URL)
                .post(requestBody)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d(TAG, "点赞onFailure: " + e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseData = response.body().string();
                if (response.isSuccessful()) {
                    try {
                        JSONObject jsonObject = new JSONObject(responseData);
                        if (jsonObject.get("message").equals("Post liked successfully")) {
                            Log.d(TAG, "点赞成功:");
                        }
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    Log.e(TAG, "点赞失败" + response.code());
                }
            }
        });
    }

    public void unlikePost(int postId, String userId) {
        JSONObject object = new JSONObject();
        try {
            object.put("user_id", userId);
            object.put("post_id", String.valueOf(postId));
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        String json = object.toString();
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), json);
        Request request = new Request.Builder()
                .url(UNLIKE_URL)
                .delete(requestBody)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d(TAG, "onFailure: " + e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseData = response.body().string();
                if (response.isSuccessful()) {
                    try {
                        JSONObject jsonObject = new JSONObject(responseData);
                        if (jsonObject.get("message").equals("Post unliked successfully")) {
                            Log.d(TAG, "取消点赞成功");
                        }
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    Log.e(TAG, "取消点赞失败" + response.code());
                }
            }
        });
    }


}
