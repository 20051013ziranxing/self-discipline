package com.example.accountsecurity;

import android.app.Activity;

import com.example.localdatabase.UserMessageHelper;

public class AccountSecurityModule {
    UserMessageHelper userMessageHelper;
    public AccountSecurityModule(Activity activity) {
        this.userMessageHelper = new UserMessageHelper(activity, "AllUsersMessage", null, 1);
    }
    public void revise() {
        userMessageHelper.updateIsActiveToZero();
    }
}
