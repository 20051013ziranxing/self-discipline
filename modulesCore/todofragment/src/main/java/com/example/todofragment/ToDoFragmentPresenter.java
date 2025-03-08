package com.example.todofragment;

import com.example.networkrequests.NetworkClient;
import com.example.todofragment.bean.GetToDoThings;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.Collections;

public class ToDoFragmentPresenter {
    ToDoFragmentModule toDoFragmentModule;
    ToDoFragment toDoFragment;

    public ToDoFragmentPresenter(ToDoFragment toDoFragment) {
        NetworkClient networkClient = new NetworkClient();
        this.toDoFragment = toDoFragment;
        this.toDoFragmentModule = new ToDoFragmentModule(networkClient);
    }
    public void getToDoThings(String user_id, String updated_at) {
        toDoFragmentModule.getToDoThings(user_id, updated_at, new ToDoFragmentModule.ModelCallback() {
            @Override
            public Boolean onSuccess(String response) {
                GetToDoThings getToDoThings = new Gson().fromJson(response, GetToDoThings.class);
                toDoFragment.toDoThings = getToDoThings.getData();
                Collections.sort(toDoFragment.toDoThings, new ToDoThingComparator());
                return null;
            }

            @Override
            public Boolean onFailure(IOException e) {
                toDoFragment.sendToast("获取数据失败");
                return null;
            }
        });
    }
}
