package com.example.communityfragment.model;

import android.util.Log;

import com.example.communityfragment.bean.Post;
import com.example.communityfragment.contract.ICommunityContract;
import com.example.communityfragment.presenter.CommunityPresenter;

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

    private final static String LIST_URL = BASE_URL + "/community/list";
    private final static String POST_URL = BASE_URL + "/community/post";
    private final static String LIKE_URL = BASE_URL + "/community/like";
    private final static String UNLIKE_URL = BASE_URL + "/community/unlike";

    private OkHttpClient client = new OkHttpClient();

    public CommunityModel(CommunityPresenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void getData() {
        Log.d(TAG, "getData: ");

        Request request = new Request.Builder()
                .url(LIST_URL)
                .get()
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d(TAG, "onFailure: " + e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    Log.d(TAG, "onResponse: 1");
                    try {
                        String responseData = response.body().string();
                        JSONObject json = new JSONObject(responseData);
                        JSONArray jsonArray = json.getJSONArray("data");
                        List<Post> postList = new ArrayList<>();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            Post post = new Post();
                            post.setId(jsonObject.getInt("id"));
                            post.setUserid(jsonObject.getString("userId"));
                            post.setContent(jsonObject.getString("content"));
                            post.setCreatedTime(jsonObject.getString("createdTime"));
                            post.setImageUrl(jsonObject.getString("imageUrl"));
                            post.setLikeConunt(jsonObject.getString("likeCount"));
                            postList.add(post);
                            mPresenter.onDataReceived(postList);
                        }
                    } catch (JSONException e) {
                        Log.d(TAG, "onResponse: " + e.getMessage());
                    }
                } else {
                    Log.d(TAG, "onResponse: 2");
                }
            }
        });
    }

    public void deletePost(int postId) {

    }

    public void unlikePost(int postId) {
    }

    public void likePost(int postId) {
    }
}
