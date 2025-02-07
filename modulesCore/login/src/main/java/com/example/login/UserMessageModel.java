package com.example.login;

import android.app.Activity;

import com.example.localdatabase.UserMessageHelper;

public class UserMessageModel {
    UserMessageHelper userMessageHelper;

    public UserMessageModel(Activity activity) {
        this.userMessageHelper = new UserMessageHelper(activity, "AllUsersMessage", null, 1);
    }

    public boolean insert(String userName, String userPassword, String userEmail) {
        boolean insert = userMessageHelper.insert(userName, userPassword, userEmail);
        return insert;
    }
    public String queryUser(String userEmail) {
        String s = userMessageHelper.queryUser(userEmail);
        return s;
    }

    public void queryAllUser() {
        userMessageHelper.queryAllUser();
    }
}
