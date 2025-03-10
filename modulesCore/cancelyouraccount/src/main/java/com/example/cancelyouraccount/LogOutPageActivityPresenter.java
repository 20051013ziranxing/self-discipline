package com.example.cancelyouraccount;

import com.example.networkrequests.NetworkClient;

import java.io.IOException;

public class LogOutPageActivityPresenter {
    LogOutPageActivity logOutPageActivity;
    LogOutPageActivityModule logOutPageActivityModule;

    public LogOutPageActivityPresenter(LogOutPageActivity logOutPageActivity) {
        NetworkClient networkClient = new NetworkClient();
        this.logOutPageActivity = logOutPageActivity;
        logOutPageActivityModule = new LogOutPageActivityModule(logOutPageActivity, networkClient);
    }
    public String getUserName() {
        return logOutPageActivityModule.getUserName();
    }
    public void performALogoutOperation(String userName, String password) {
        logOutPageActivityModule.logOutOfTheOperation(userName, password, new LogOutPageActivityModule.ModelCallback() {
            @Override
            public Boolean onSuccess(String response) {
                logOutPageActivityModule.deleteUserLocalMessage();
                logOutPageActivity.sendToast("注销成功");
                return null;
            }

            @Override
            public Boolean onFailure(IOException e) {
                logOutPageActivity.sendToast("注销失败");
                return null;
            }
        });
    }
}
