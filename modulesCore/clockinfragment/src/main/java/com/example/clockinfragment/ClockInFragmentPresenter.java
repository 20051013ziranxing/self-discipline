package com.example.clockinfragment;

import android.util.Log;

import com.example.clockinfragment.bean.CheckInBean;
import com.example.clockinfragment.fragment.ModifyFragmentModule;
import com.example.clockinfragment.singleton.DateSingleton;
import com.example.networkrequests.NetworkClient;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.List;

public class ClockInFragmentPresenter {
    private static final String TAG = "TestTT_ClockInFragmentPresenter";
    ClockInFragment_1 clockInFragment1;
    ClockInFragmentModule clockInFragmentModule;

    public ClockInFragmentPresenter(ClockInFragment_1 clockInFragment1) {
        NetworkClient networkClient = new NetworkClient();
        this.clockInFragment1 = clockInFragment1;
        this.clockInFragmentModule = new ClockInFragmentModule(networkClient);
    }
    public void add1ToTheNumberOfCheckInsCompleted(int checkin_id, String date) {
        clockInFragmentModule.add1ToTheNumberOfCheckInsCompleted(checkin_id, date, new ClockInFragmentModule.ModelCallback() {
            @Override
            public Boolean onSuccess(String response) {
                Log.d(TAG , "打卡成功");
                getAllThePunchesForAGivenDay(clockInFragment1.userBaseMessageEventBus.getUserId(), DateSingleton.getInstance().getDate());
                return null;
            }

            @Override
            public Boolean onFailure(IOException e) {
                clockInFragment1.sendToast("打卡失败");
                return null;
            }
        });
    }

    public void modifyTheClockInInformationToEndEarly(int id,String  start_date,String end_date) {
        Log.d(TAG, start_date + end_date);
        clockInFragmentModule.modifyTheClockInInformationToEndEarly(id,start_date ,end_date, new ClockInFragmentModule.ModelCallback() {
            @Override
            public Boolean onSuccess(String response) {

                Log.d(TAG, "提前结束成功");
                return null;
            }

            @Override
            public Boolean onFailure(IOException e) {
                Log.d(TAG, "提前结束失败");
                return null;
            }
        });
    }

    public void deleteAClockInTask(String checkin_id) {
        clockInFragmentModule.deleteAClockInTask(checkin_id, new ModifyFragmentModule.ModelCallback() {
            @Override
            public Boolean onSuccess(String response) {
                Log.d(TAG, "删除成功");
                getAllThePunchesForAGivenDay(clockInFragment1.userBaseMessageEventBus.getUserId(), DateSingleton.getInstance().getDate());
                return null;
            }

            @Override
            public Boolean onFailure(IOException e) {
                Log.d(TAG, "删除失败");
                clockInFragment1.sendToast("删除失败");
                return null;
            }
        });
    }

    public void getAllThePunchesForAGivenDay(String user_id, String date) {
        clockInFragmentModule.getAllThePunchesForAGivenDay(user_id, date, new ClockInFragmentModule.ModelCallback() {
            @Override
            public Boolean onSuccess(String response) {
                CheckInBean checkInBean = new Gson().fromJson(response, CheckInBean.class);
                if (checkInBean != null) {
                    List<CheckInBean.CheckinData> checkinDataList = checkInBean.getData();
                    /*for (CheckInBean.CheckinData checkinData : checkinDataList) {
                        Log.d(TAG, date);
                        Log.d(TAG, String.valueOf(checkinData.getDecodedCheckinCount().get(date)));
                    }*/
                    if (checkinDataList != null) {
                        clockInFragment1.updateThePunchList(checkinDataList);
                    }
                }
                return null;
            }

            @Override
            public Boolean onFailure(IOException e) {
                return null;
            }
        });
    }

    public void createAPunchInTask(String user_id, String status, String title, String target_checkin_count) {
        clockInFragmentModule.createAPunchInTask(user_id, status, title, target_checkin_count, new ClockInFragmentModule.ModelCallback() {
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
}
