package com.example.todofragment;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;

import com.example.networkrequests.NetworkClient;
import com.example.todofragment.adapter.RecyclerViewToDoAdapter;
import com.example.todofragment.bean.GetToDoThings;
import com.example.todofragment.bean.GetToDothingMessage;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ToDoFragmentPresenter {
    private static final String TAG = "TestTT_ToDoFragmentPresenter";
    ToDoFragmentModule toDoFragmentModule;
    ToDoFragment toDoFragment;

    public ToDoFragmentPresenter(ToDoFragment toDoFragment) {
        NetworkClient networkClient = new NetworkClient();
        this.toDoFragment = toDoFragment;
        this.toDoFragmentModule = new ToDoFragmentModule(networkClient);
    }
    public void getToDoThings(String user_id, String updated_at) {
        Log.d(TAG, user_id + "user_id" + updated_at + "updated_at");
        toDoFragmentModule.getToDoThings(user_id, updated_at, new ToDoFragmentModule.ModelCallback() {
            @Override
            public Boolean onSuccess(String response) {
                Log.d(TAG, response + "ppp");
                Handler mainHandler = new Handler(Looper.getMainLooper());
                mainHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        Log.d(TAG, "我要开始更新了" + response);
                        GetToDoThings getToDoThings = null;
                        List<GetToDothingMessage> itemList = null;
                        if (response != null) {
                            Gson gson = new Gson();
                            Type listType = new TypeToken<List<GetToDothingMessage>>() {}.getType();
                            itemList = gson.fromJson(response, listType);
                            /*getToDoThings = new Gson().fromJson(response, GetToDoThings.class);*/
                        }
                        /*List<GetToDoThings.GetToDothingMessage> toDoThings = null;
                        if (getToDoThings != null) {
                            toDoThings = getToDoThings.getData();
                        }
                        List<GetToDoThings.GetToDothingMessage> toDothingMessages = filteringByTime(toDoThings, updated_at);*/
                        if (itemList != null) {
                            Collections.sort(itemList, new ToDoThingComparator());
                            toDoFragment.remindersChange(itemList);
                            toDoFragment.constraintLayout_not.setVisibility(View.GONE);
                        } else {
                            toDoFragment.constraintLayout_not.setVisibility(View.VISIBLE);
                            toDoFragment.remindersChange(itemList);
                        }
                    }
                });
                return null;
            }
            @Override
            public Boolean onFailure(IOException e) {
                toDoFragment.sendToast("获取数据失败");
                return null;
            }
        });
    }

    public void modifyTheTaskCompletionStatus(String id, boolean check) {
        String status = "pending";
        if (check) {
            status = "completed";
        }
        toDoFragmentModule.modifyTheTaskCompletionStatus(id, status, new ToDoFragmentModule.ModelCallback() {
            @Override
            public Boolean onSuccess(String response) {
                getToDoThings(toDoFragment.userBaseMessageEventBus.getUserId(), toDoFragment.textView_data.getText().toString());
                return null;
            }

            @Override
            public Boolean onFailure(IOException e) {
                toDoFragment.sendToast("网络连接失败...修改完成状态失败");
                getToDoThings(toDoFragment.userBaseMessageEventBus.getUserId(), toDoFragment.textView_data.getText().toString());
                return null;
            }
        });
    }

    public void markWhetherTheAgencyIsCompleteOrNot(String id, Boolean isFinish) {
        toDoFragmentModule.markWhetherTheTaskIsCompletedOrNot(id, isFinish, new ToDoFragmentModule.ModelCallback() {
            @Override
            public Boolean onSuccess(String response) {
                Log.d(TAG, "标记成功" + response);
                getToDoThings(toDoFragment.userBaseMessageEventBus.getUserId(), toDoFragment.textView_data.getText().toString());
                return null;
            }

            @Override
            public Boolean onFailure(IOException e) {
                toDoFragment.sendToast("标记失败");
                return null;
            }
        });
    }
    public void modifyTheAgencyInformation(String id, String title, String description, String status, String updated_at) {
        toDoFragmentModule.modifyTheAgencyInformation(id, title, description, status, updated_at, new ToDoFragmentModule.ModelCallback() {
            @Override
            public Boolean onSuccess(String response) {
                toDoFragment.sendToast("修改成功");
                return null;
            }

            @Override
            public Boolean onFailure(IOException e) {
                toDoFragment.sendToast("修改失败");
                return null;
            }
        });
    }

    public void deleteToDoThing(String id, String user_id, String updata_at) {
        toDoFragmentModule.deleteToDoThing(id, new ToDoFragmentModule.ModelCallback() {
            @Override
            public Boolean onSuccess(String response) {
                toDoFragment.sendToast("删除成功");
                getToDoThings(user_id, updata_at);
                return null;
            }

            @Override
            public Boolean onFailure(IOException e) {
                toDoFragment.sendToast("删除失败");
                return null;
            }
        });
    }

    //对获取到的代办事件进行筛选
    public List<GetToDoThings.GetToDothingMessage> filteringByTime(List<GetToDoThings.GetToDothingMessage> toDoThings, String updated_at) {
        if (toDoThings != null) {
            List<GetToDoThings.GetToDothingMessage> toDothingMessages = new ArrayList<>();
            Log.d(TAG, "获取到的数据：" + toDoThings.size());
            for (int i = 0;i < toDoThings.size(); i++) {
                Log.d(TAG, toDoThings.get(i).getUpdated_at().toString() + "   ooo    " + updated_at);
                if (toDoThings.get(i).getUpdated_at().equals(updated_at)) {
                    toDothingMessages.add(toDoThings.get(i));
                }
            }
            Log.d(TAG, "帅选到的数据：" + toDothingMessages.size());
            return toDothingMessages;
        }
        return null;
    }
}
