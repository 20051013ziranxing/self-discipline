package com.example.communityfragment.contract;

import com.example.communityfragment.bean.Post;

import java.util.List;

public interface ICommunityContract {
    public interface View {

    }

    public interface Presenter {
        void getData();
        void checkLikeStatus(int postId, String userId);

        void onDataReceived(List<Post> postList);
    }

    public interface Model {
        void getData();

        void checkLikeStatus(int postId, String userId);
    }

    public interface MyCallback {
        void onSucces();

        void onFail();
    }
}
