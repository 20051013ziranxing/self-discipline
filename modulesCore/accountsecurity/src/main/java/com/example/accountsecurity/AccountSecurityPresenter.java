package com.example.accountsecurity;

import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import androidx.loader.content.CursorLoader;

import com.example.accountsecurity.bean.ModifyNameAndIcon;
import com.example.networkrequests.NetworkClient;
import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;

public class AccountSecurityPresenter {
    private static String TAG = "TestTT_AccountSecurityPresenter";
    AccountSecurityModule accountSecurityModule;
    AccountSecurityActivity accountSecurityActivity;
    Iconsave iconsave;

    public AccountSecurityPresenter(AccountSecurityActivity accountSecurityActivity, String emailNumber) {
        NetworkClient networkClient = new NetworkClient();
        this.accountSecurityActivity = accountSecurityActivity;
        this.accountSecurityModule = new AccountSecurityModule(accountSecurityActivity, networkClient);
    }

    public void saveMessageNameAndIcon(String id, String username, File imageUri) {
        /*Log.d(TAG, "id:" + id + username + imageUri.toString());*/
        accountSecurityModule.modifyTheUserSAvatar(id, username, imageUri, new AccountSecurityModule.ModelCallback() {
            @Override
            public Boolean onSuccess(String response) {
                Log.d(TAG, response);
                accountSecurityActivity.sendToast("修改成功");
                ModifyNameAndIcon modifyNameAndIcon = new Gson().fromJson(response, ModifyNameAndIcon.class);
                accountSecurityModule.modifyTheBasicInformationOfALocalUser(modifyNameAndIcon.getUsername(), modifyNameAndIcon.getAvatar_url());
                accountSecurityActivity.sendEventBus(accountSecurityModule.getBaseMessage());
                accountSecurityActivity.finish();
                return null;
            }

            @Override
            public Boolean onFailure(IOException e) {
                accountSecurityActivity.sendToast("失败");
                return null;
            }
        });
    }

    //进行初始化
    public void initData() {
        /*//根据邮箱地址获取图片并将其设置到图片的位置
        iconsave = new Iconsave("content://media/external_primary/images/media/1000031793");
        accountSecurityActivity.displayImage(iconsave.getMyString());
        //文字设置为获取到的信息
        accountSecurityActivity.editText.setText("hhh");*/
    }

    public void changeIcon(String icon) {
        iconsave.setMyString(icon);
        Log.d(TAG, iconsave.getMyString() + "changeIcon");
    }

    public void signOut() {
        accountSecurityModule.signOut();
    }

    //获取绝对路径
    /*private String getRealPathFromURI(Uri contentUri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        CursorLoader loader = new CursorLoader(accountSecurityActivity, contentUri, proj, null, null, null);
        Cursor cursor = loader.loadInBackground();
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        Log.d(TAG, "id:" + contentUri.toString());
        return cursor.getString(column_index);
    }*/
}
