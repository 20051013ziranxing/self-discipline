package com.example.communityfragment.contract;

import com.example.communityfragment.bean.Post;

import java.util.List;

public interface ICommunityContract {
    public interface View {

    }
    public interface Presenter {
        void getData();

        void onDataReceived(List<Post> postList);
    }
    public interface Model {
        void getData();
    }
}
