package com.example.myfragment;

import android.app.Activity;

import com.example.localdatabase.UserMessageHelper;
import com.example.networkrequests.NetworkClient;

public class MyFragmentModule {
    UserMessageHelper userMessageHelper;
    NetworkClient networkClient;
    public MyFragmentModule(Activity activity, NetworkClient networkClient) {
        this.userMessageHelper = new UserMessageHelper(activity, "AllUsersMessage", null, 1);
        this.networkClient = networkClient;
    }
    public String getUserName() {
        return userMessageHelper.queryAllUser().getUserName();
    }
    public String getUserPicture() {
        return userMessageHelper.queryAllUser().getUserPictureURL();
    }
}
