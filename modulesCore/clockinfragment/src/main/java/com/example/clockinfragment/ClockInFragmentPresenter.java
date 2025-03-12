package com.example.clockinfragment;

import com.example.networkrequests.NetworkClient;

import java.io.IOException;

public class ClockInFragmentPresenter {
    private static final String TAG = "TestTT_ClockInFragmentPresenter";
    ClockInFragment_1 clockInFragment1;
    ClockInFragmentModule clockInFragmentModule;

    public ClockInFragmentPresenter(ClockInFragment_1 clockInFragment1, ClockInFragmentModule clockInFragmentModule) {
        NetworkClient networkClient = new NetworkClient();
        this.clockInFragment1 = clockInFragment1;
        this.clockInFragmentModule = new ClockInFragmentModule(networkClient);
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
