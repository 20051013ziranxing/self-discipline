package com.example.selfdiscipline;

import android.app.Activity;

import com.example.localdatabase.UserMessageHelper;
import com.example.localdatabase.bean.UserBaseMessage;
import com.example.networkrequests.NetworkClient;

public class MainActivityModule {
    UserMessageHelper userMessageHelper;
    public MainActivityModule(Activity activity) {
        this.userMessageHelper = new UserMessageHelper(activity, "AllUsersMessage", null, 1);
    }
    public UserBaseMessage queryAllUser() {
        return userMessageHelper.queryAllUser();
    }
}
