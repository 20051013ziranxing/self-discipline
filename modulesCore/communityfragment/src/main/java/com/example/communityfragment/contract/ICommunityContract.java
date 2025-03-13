package com.example.communityfragment.contract;

import com.example.communityfragment.bean.Post;

import java.util.List;

public interface ICommunityContract {
    public interface View {

        void updatePostLikeStatus(int postId, boolean isLiked);

        void onDataReceived(List<Post> postList);

        void deletePostSuccess(int postId);
    }

    public interface Presenter {
        void getData(String userId,int type);
        void checkLikeStatus(int postId, String userId);

        void onDataReceived(List<Post> postList);

        void deletePostSuccess(int postId);
    }

    public interface Model {
        void getData(String userId,int type);

        void checkLikeStatus(int postId, String userId);

        void unlikePost(int postId, String userId);

        void likePost(int postId, String userId);

        void deletePost(int postId);

    }

    public interface MyCallback {
        void onSucces();

        void onFail();
    }
}
