package com.example.communityfragment.presenter;

import android.util.Log;

import com.example.communityfragment.contract.IPublishContract;
import com.example.communityfragment.model.PublishModel;
import com.example.communityfragment.view.PublishActivity;

public class PublishPresenter implements IPublishContract.Presenter {
    public static final String TAG = "PublishFunctionTAG";

    private PublishModel mModel;
    private PublishActivity mView;

    public PublishPresenter(PublishActivity view) {
        this.mView = view;
        mModel = new PublishModel(this);
    }

    @Override
    public void publish(String content, String userId, String imagePath) {
        Log.d(TAG, "选中的图片 完整路径: " + imagePath);
        mModel.publish(content, userId, imagePath, new IPublishContract.PublishCallback() {
            @Override
            public void onSuccess(int id) {
                mView.publishSuccess(id);
            }

            @Override
            public void onFailure() {
                mView.publishFailure();
            }
        });
    }


}
