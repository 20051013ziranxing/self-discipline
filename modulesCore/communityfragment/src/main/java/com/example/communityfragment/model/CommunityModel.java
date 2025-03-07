package com.example.communityfragment.model;

import android.util.Log;

import com.example.communityfragment.bean.Post;
import com.example.communityfragment.contract.ICommunityContract;
import com.example.communityfragment.presenter.CommunityPresenter;
import com.google.gson.JsonObject;

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

    private OkHttpClient client = new OkHttpClient();

    public CommunityModel(CommunityPresenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void getData() {

        Request request = new Request.Builder()
                .get()
                .url(ALLLIST_URL)
                .build();

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
                                post.setContent(jsonObject.getString("content"));
                                post.setImageUrl(jsonObject.getString("image_url"));
                                post.setCreatedTime(jsonObject.getString("created_at"));
                                post.setLikeConunt(jsonObject.getString("likes_count"));
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

    public void deletePost(int postId) {

//        JSONObject object = new JSONObject();
//        object.put("user_id", "2");
//        object.put("post_id", postId);
//        String json = object.toString();
//        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), json);
//        Request request = new Request.Builder()
//                .url(POST_URL)
//                .delete(requestBody)
//                .build();
//
//        client.newCall(request).enqueue(new Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//                Log.d(TAG, "onFailure: " + e.getMessage());
//            }
//
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//                if (response.isSuccessful()) {
//                }
//            }
//        });
    }

    public void unlikePost(int postId) {

        JSONObject object = new JSONObject();
        try {
            object.put("user_id", "2");
            object.put("post_id", postId);
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
                Log.d(TAG, "onFailure: " + e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    JSONObject jsonObject = new JSONObject(response.body().string());
                    if (jsonObject.get("message") == "Post liked successfully") {
                        Log.d(TAG, "删除成功: " + response.body().string());

                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    public void likePost(int postId) {
        JSONObject object = new JSONObject();
        try {
            object.put("user_id", "3");
            object.put("post_id", postId);
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
                if (response.isSuccessful()) {
                    try {
                        JSONObject jsonObject = new JSONObject(response.body().string());
                        if (jsonObject.get("message") == "Post liked successfully") {
                            Log.d(TAG, "点赞成功: " + response.body().string());
                            // 发送点赞成功的广播

                        }
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });
    }


//    @Override
//    public void getData() {
//        String userId = "3";
//        String url = LIST_URL + "?user_id=" + userId;
//
//        Request request = new Request.Builder()
//                .url(url)
//                .get()
//                .build();
//
//        client.newCall(request).enqueue(new Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//                Log.d(TAG, "onFailure: " + e.getMessage());
//            }
//
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//                String responseData = response.body().string();
////                Log.d(TAG, "onResponse: " + responseData);
//                if (response.isSuccessful()) {
//                    try {
//                        Log.d(TAG, "帖子 : " + responseData);
//                        JSONObject json = new JSONObject(responseData);
//                        if (json.isNull("data")) {
//                            Log.d(TAG, "帖子为空");
//                            mPresenter.onDataReceived(new ArrayList<>());
//                        } else {
//                            JSONArray jsonArray = json.getJSONArray("data");
//                            List<Post> postList = new ArrayList<>();
//                            for (int i = 0; i < jsonArray.length(); i++) {
//                                JSONObject jsonObject = jsonArray.getJSONObject(i);
//                                Post post = new Post();
//                                post.setId(jsonObject.getInt("id"));
//                                post.setUserid(jsonObject.getString("user_id"));
//                                post.setContent(jsonObject.getString("content"));
//                                post.setImageUrl(jsonObject.getString("image_url"));
//                                post.setCreatedTime(jsonObject.getString("created_at"));
//                                post.setLikeConunt(jsonObject.getString("likes_count"));
//                                postList.add(post);
//                                Log.d(TAG, "帖子 : " + post.toString());
//                                mPresenter.onDataReceived(postList);
//                            }
//                        }
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                } else {
//                    Log.e(TAG, "onResponse: " + response.code());
//                }
//            }
//        });
//    }

}
