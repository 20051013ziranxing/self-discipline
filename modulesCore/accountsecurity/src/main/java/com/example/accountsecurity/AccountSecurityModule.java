package com.example.accountsecurity;

import static android.provider.ContactsContract.CommonDataKinds.Website.URL;

import android.app.Activity;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import androidx.loader.content.CursorLoader;

import com.example.localdatabase.UserMessageHelper;
import com.example.localdatabase.bean.UserBaseMessage;
import com.example.networkrequests.NetworkClient;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;

public class AccountSecurityModule {
    UserMessageHelper userMessageHelper;
    private NetworkClient networkClient;
    public AccountSecurityModule(Activity activity, NetworkClient networkClient) {
        this.userMessageHelper = new UserMessageHelper(activity, "AllUsersMessage", null, 1);
        this.networkClient = networkClient;
    }
    public void signOut() {
        userMessageHelper.updateUniqueUserToken(null);
    }
    public void modifyTheUserSAvatar(String id, String username, File file, final ModelCallback callback) {
        String url = "profile";
        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        builder.addFormDataPart("id", id);
        builder.addFormDataPart("username", username);
        if (file != null) {
            builder.addFormDataPart("file", file.getName(), RequestBody.create(MediaType.parse("image/jpeg"), file));
        }
        RequestBody requestBody = builder.build();
        publicPutNetworkRequestMethod(url, requestBody, callback);
    }
    //将调用接口提出来
    public void PublicNetworkRequestMethod(String url,String Json, ModelCallback callback) {
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody requestBody = RequestBody.create(
                JSON, Json);
        networkClient.post(url, requestBody, new NetworkClient.NetworkCallback() {
            @Override
            public void onSuccess(String response) {
                callback.onSuccess(response);
            }

            @Override
            public void onFailure(IOException e) {
                callback.onFailure(e);
            }
        });
    }

    public void modifyTheBasicInformationOfALocalUser(String newName, String newIcon) {
        userMessageHelper.updateUniqueUserNameAndIcon(newName, newIcon);
    }

    public UserBaseMessage getBaseMessage() {
        return userMessageHelper.queryAllUser();
    }

    public void publicPutNetworkRequestMethod(String url,RequestBody requestBody, ModelCallback callback) {
        networkClient.put(url, requestBody, new NetworkClient.NetworkCallback() {
            @Override
            public void onSuccess(String response) {
                callback.onSuccess(response);
            }

            @Override
            public void onFailure(IOException e) {
                callback.onFailure(e);
            }
        });
    }
    public interface ModelCallback {
        Boolean onSuccess(String response);
        Boolean onFailure(IOException e);
    }


}
