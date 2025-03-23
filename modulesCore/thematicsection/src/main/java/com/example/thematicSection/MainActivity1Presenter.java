package com.example.thematicSection;

import android.util.Log;

import com.example.localdatabase.bean.UserTables;
import com.example.networkrequests.NetworkClient;

public class MainActivity1Presenter {
    MainActivity1 mainActivity1;
    MainActivity1Module mainActivity1Module;

    public MainActivity1Presenter(MainActivity1 mainActivity1) {
        this.mainActivity1 = mainActivity1;
        this.mainActivity1Module = new MainActivity1Module(mainActivity1, new NetworkClient());
    }

    public void addListing(String userId, String listingColor, String listingName) {
        mainActivity1Module.addListing(userId, listingColor, listingName, new MainActivity1Module.MainActivity1ModuleListen() {
            @Override
            public void onSucceed() {

            }

            @Override
            public void onFailed() {

            }

            @Override
            public void onSucceed(UserTables userTablesByUserId) {
                mainActivity1.upDataRecyclerViewListing(userTablesByUserId.getUserListings());
            }
        });

    }
}
