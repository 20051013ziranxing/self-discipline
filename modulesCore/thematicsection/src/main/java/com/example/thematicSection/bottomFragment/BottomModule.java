package com.example.thematicSection.bottomFragment;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.example.localdatabase.ListOfLabelsHelper;
import com.example.localdatabase.bean.UserTables;
import com.example.networkrequests.NetworkClient;

import java.util.List;

public class BottomModule {
    private static final String TAG = "TestTT_BottomModule";
    ListOfLabelsHelper listOfLabelsHelper;
    public BottomModule(Context context) {
        listOfLabelsHelper = new ListOfLabelsHelper(context, "hh", null, 1);
    }

    public interface BottomModuleListen {
        void onSucceed();
        void onFailed();
    }

    public List<UserTables.UserListing> addListing(String userId, String listingColor, String listingName, BottomModuleListen bottomModuleListen) {
        UserTables userTablesByUserId = listOfLabelsHelper.findUserTablesByUserId(userId);
        if (userTablesByUserId.addUserListing(new UserTables.UserListing(listingColor, listingName))) {
            listOfLabelsHelper.updateUserListings(userId, userTablesByUserId.getUserListings(), new ListOfLabelsHelper.CallbackLocal() {
                @Override
                public void onSucceed() {
                    Log.d(TAG, "添加成功");
                    bottomModuleListen.onSucceed();
                }

                @Override
                public void onFailed() {
                    Log.d(TAG, "添加失败");
                    bottomModuleListen.onFailed();
                }
            });
        }
        return null;
    }
}
