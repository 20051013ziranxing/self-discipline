package com.example.communityfragment.presenter;

import com.example.communityfragment.bean.Post;
import com.example.communityfragment.view.CommunityFragemnt;
import com.example.communityfragment.contract.ICommunityContract;
import com.example.communityfragment.model.CommunityModel;

import java.util.List;

public class CommunityPresenter implements ICommunityContract.Presenter {

    private CommunityFragemnt mView;
    private CommunityModel mModel;

    public CommunityPresenter(CommunityFragemnt view) {
        mView = view;
        mModel = new CommunityModel(this);
    }

    @Override
    public void getData() {
        mModel.getData();
    }

    @Override
    public void onDataReceived(List<Post> postList) {
        mView.onDataReceived(postList);
    }

    public void deletePost(int postId) {
        mModel.deletePost(postId);
    }

    public void unlikePost(int postId) {
        mModel.unlikePost(postId);
    }

    public void likePost(int postId) {
        mModel.likePost(postId);
    }
}
