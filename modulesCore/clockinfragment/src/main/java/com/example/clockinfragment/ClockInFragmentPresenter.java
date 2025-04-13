package com.example.clockinfragment;

import android.util.Log;
import android.view.View;

import com.example.clockinfragment.bean.CheckInBean;
import com.example.clockinfragment.bean.CheckInBean1;
import com.example.clockinfragment.fragment.ModifyFragmentModule;
import com.example.clockinfragment.singleton.DateSingleton;
import com.example.networkrequests.NetworkClient;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
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
                Log.d(TAG, "responseToGson：" + response);
                CheckInBean1 checkInBean = new Gson().fromJson(response, CheckInBean1.class);
                if (checkInBean != null) {
                    List<CheckInBean1.CheckinData> checkinDataList = checkInBean.getData().getCheckins();
                    if (checkinDataList != null) {
                        for (CheckInBean1.CheckinData checkinData : checkinDataList) {
                            Log.d(TAG, date);
                            Log.d(TAG, String.valueOf(checkinData.getDecodedCheckinCount()));
                        }
                        if (checkinDataList != null) {
                            //进行排序操作
                            clockInFragment1.updateThePunchList(SortThePunches(checkinDataList, date), date);
                        }
                    } else {
                        clockInFragment1.updateThePunchList(null, date);
                    }
                }else {
                    clockInFragment1.updateThePunchList(null, date);
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

    public List<CheckInBean1.CheckinData> SortThePunches(List<CheckInBean1.CheckinData> checkinDataList, String data) {
        Collections.sort(checkinDataList, new Comparator<CheckInBean1.CheckinData>() {
            @Override
            public int compare(CheckInBean1.CheckinData o1, CheckInBean1.CheckinData o2) {
                int targetValue1 = o1.getCheckin().getTarget_checkin_count();
                Integer decodedValue1 = o1.getDecodedCheckinCount().get(data);
                int targetValue2 = o2.getCheckin().getTarget_checkin_count();
                Integer decodedValue2 = o2.getDecodedCheckinCount().get(data);
                boolean isEqual1 = targetValue1 == decodedValue1;
                boolean isEqual2 = targetValue2 == decodedValue2;
                if (isEqual1 && isEqual2) {
                    return 0;
                }
                if (isEqual1) {
                    return 1;
                }
                if (isEqual2) {
                    return -1;
                }
                return 0;
            }
        });
        return checkinDataList;
    }
}
