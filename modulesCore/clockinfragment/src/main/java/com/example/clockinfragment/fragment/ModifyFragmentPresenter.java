package com.example.clockinfragment.fragment;

import android.util.Log;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.clockinfragment.bean.CheckInById;
import com.example.networkrequests.NetworkClient;
import com.google.gson.Gson;

import java.io.IOException;

public class ModifyFragmentPresenter {
    private static final String TAG = "TestTT_ModifyFragmentPresenter";
    ModifyFragmentModule modifyFragmentModule;
    ModifyFragment modifyFragment;
    CheckInById checkInById;

    public ModifyFragmentPresenter(ModifyFragment modifyFragment) {
        NetworkClient networkClient = new NetworkClient();
        this.modifyFragment = modifyFragment;
        this.modifyFragmentModule = new ModifyFragmentModule(networkClient);
    }

    //根据checkin_id获取具体信息展示在界面上
    public void getAttendanceRecordsBasedOnYourID(String checkin_id) {
        modifyFragmentModule.getAttendanceRecordsBasedOnYourID(checkin_id, new ModifyFragmentModule.ModelCallback() {
            @Override
            public Boolean onSuccess(String response) {
                checkInById = new Gson().fromJson(response, CheckInById.class);
                modifyFragment.getTheDataForInitialization(checkInById);
                return null;
            }

            @Override
            public Boolean onFailure(IOException e) {
                return null;
            }
        });
    }

    public void deleteAClockInTask(String checkin_id) {
        modifyFragmentModule.deleteAClockInTask(checkin_id, new ModifyFragmentModule.ModelCallback() {
            @Override
            public Boolean onSuccess(String response) {

                return null;
            }

            @Override
            public Boolean onFailure(IOException e) {
                return null;
            }
        });
    }

    public void modifyTheClockInInformation(int id, String title, String start_date, String end_date,
                                            int icon, int target_checkin_count, String motivation_message) {
        Log.d(TAG, id+ title+ start_date+ end_date+ icon+ target_checkin_count+
                motivation_message + "  kk");
        modifyFragmentModule.modifyTheClockInInformation(id, title, start_date, end_date, icon, target_checkin_count,
                motivation_message, new ModifyFragmentModule.ModelCallback() {
            @Override
            public Boolean onSuccess(String response) {
                modifyFragment.sendToast("修改成功");
                modifyFragment.addFragmentListen.makeAnUpdate();
                removeAddFragment();
                return null;
            }

            @Override
            public Boolean onFailure(IOException e) {
                modifyFragment.sendToast("修改失败");
                return null;
            }
        });
    }



    public void removeAddFragment () {
        FragmentManager fragmentManager = modifyFragment.getParentFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.remove(modifyFragment).commit();
    }
}
