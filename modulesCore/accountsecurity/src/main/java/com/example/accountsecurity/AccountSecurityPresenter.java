package com.example.accountsecurity;

import android.util.Log;

public class AccountSecurityPresenter {
    private static String TAG = "TestTT_AccountSecurityPresenter";
    AccountSecurityModule accountSecurityModule;
    AccountSecurityActivity accountSecurityActivity;
    Iconsave iconsave;

    public AccountSecurityPresenter(AccountSecurityActivity accountSecurityActivity, String emailNumber) {
        this.accountSecurityActivity = accountSecurityActivity;
        this.accountSecurityModule = new AccountSecurityModule();
    }

    public void saveMessage() {
        //此时获取到了用户名
        String userName = accountSecurityActivity.editText.getText().toString();
        String userIcon = iconsave.getMyString();
        Log.d(TAG, userName + "UserIcon" + userIcon);
    }
    //进行初始化
    public void initData() {
        //根据邮箱地址获取图片并将其设置到图片的位置
        iconsave = new Iconsave("content://media/external_primary/images/media/1000031793");
        accountSecurityActivity.displayImage(iconsave.getMyString());
        //文字设置为获取到的信息
        accountSecurityActivity.editText.setText("hhh");
    }

    public void changeIcon(String icon) {
        iconsave.setMyString(icon);
        Log.d(TAG, iconsave.getMyString() + "changeIcon");
    }
}
