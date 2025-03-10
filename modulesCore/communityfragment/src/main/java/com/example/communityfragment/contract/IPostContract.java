package com.example.communityfragment.contract;

import com.example.communityfragment.bean.Comment;

import java.util.List;

public interface IPostContract {
    interface View {
        void onCommentsSuccess(List<Comment> comments);
        void onCommentsFailure();

        void onPublishCommentSuccess();
        void onPublishCommentFailure();
    }

    interface Presenter {
        void comment(int postId, String comment);

        void onCommentsSuccess(List<Comment> comments);
        void onCommentsFailure();

        void onPublishCommentSuccess();
        void onPublishCommentFailure();
    }

    interface Model {
        void getComments(int postId);
        void comment(int postId, String comment);
    }


}
