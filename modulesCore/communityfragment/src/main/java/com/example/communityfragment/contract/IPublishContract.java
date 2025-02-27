package com.example.communityfragment.contract;

public interface IPublishContract {
    interface View {
        void publishSuccess(int id);

        void publishFailure();
    }

    interface Presenter {
        void publish(String content, String imgURL);
    }

    interface Model {
        void publish(String content, String imgURL,PublishCallback callback);
    }

    interface PublishCallback {
        void onSuccess(int id);

        void onFailure();
    }

}
