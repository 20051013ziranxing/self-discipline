package com.example.thematicSection;

import android.app.Activity;

import com.example.localdatabase.ListOfLabelsHelper;
import com.example.localdatabase.bean.UserTables;
import com.example.networkrequests.NetworkClient;

import java.util.List;

public class MainActivity1Module {
    public interface MainActivity1ModuleListen {
        void onSucceed();
        void onFailed();

        void onSucceed(UserTables userTablesByUserId);
    }
    ListOfLabelsHelper listOfLabelsHelper;
    NetworkClient networkClient;

    public MainActivity1Module(Activity activity, NetworkClient networkClient) {
        this.networkClient = networkClient;
        listOfLabelsHelper = new ListOfLabelsHelper(activity, "hh", null, 1);
    }

    public List<UserTables.UserListing> addListing(String userId, String listingColor, String listingName, MainActivity1ModuleListen mainActivity1ModuleListen) {
        UserTables userTablesByUserId = listOfLabelsHelper.findUserTablesByUserId(userId);
        if (userTablesByUserId.addUserListing(new UserTables.UserListing(listingColor, listingName))) {
            boolean b = listOfLabelsHelper.updateUserListings(userId, userTablesByUserId.getUserListings(), new ListOfLabelsHelper.CallbackLocal() {
                @Override
                public void onSucceed() {
                    mainActivity1ModuleListen.onSucceed(listOfLabelsHelper.findUserTablesByUserId(userId));
                }

                @Override
                public void onFailed() {
                    mainActivity1ModuleListen.onFailed();
                }
            });
        }
        return null;
    }
}
