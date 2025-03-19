package com.example.communityfragment.presenter;

import com.example.communityfragment.bean.Post;
import com.example.communityfragment.contract.ICommunityContract;
import com.example.communityfragment.model.CommunityModel;

import java.util.List;

public class CommunityPresenter implements ICommunityContract.Presenter {

    private ICommunityContract.View mView;
    private ICommunityContract.Model mModel;

    public CommunityPresenter(ICommunityContract.View view) {
        mView = view;
        mModel = new CommunityModel(this);
    }

    @Override
    public void getData(String userId,int type) {
        mModel.getData(userId,type);
    }

    @Override
    public void checkLikeStatus(int postId, String userId) {
        mModel.checkLikeStatus(postId,userId);
    }

    public void updatePostLikeStatus(int postId, boolean isLiked) {
        mView.updatePostLikeStatus(postId, isLiked);
    }

    @Override
    public void onDataReceived(List<Post> postList) {
        mView.onDataReceived(postList);
    }

    @Override
    public void deletePostSuccess(int  postId) {
        mView.deletePostSuccess(postId);
    }

    public void deletePost(int postId) {
        mModel.deletePost(postId);
    }

    public void unlikePost(int postId, String userId) {
        mModel.unlikePost(postId,userId);
    }

    public void likePost(int postId, String userId) {
        mModel.likePost(postId, userId);
    }

}
