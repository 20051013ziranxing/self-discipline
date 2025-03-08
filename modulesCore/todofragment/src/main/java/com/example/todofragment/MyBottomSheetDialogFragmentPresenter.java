package com.example.todofragment;

import com.example.networkrequests.NetworkClient;

import java.io.IOException;

public class MyBottomSheetDialogFragmentPresenter {
    MyBottomSheetDialogFragment myBottomSheetDialogFragment;
    MyBottomSheetDialogFragmentModule myBottomSheetDialogFragmentModule;

    public MyBottomSheetDialogFragmentPresenter(MyBottomSheetDialogFragment myBottomSheetDialogFragment) {
        NetworkClient networkClient = new NetworkClient();
        this.myBottomSheetDialogFragment = myBottomSheetDialogFragment;
        this.myBottomSheetDialogFragmentModule = new MyBottomSheetDialogFragmentModule(networkClient);
    }
    public void addToDoThing(String title, String description, String status, String user_id, String updated_at) {
        myBottomSheetDialogFragmentModule.addToDoThing(title, description, status, user_id, updated_at, new MyBottomSheetDialogFragmentModule.ModelCallback() {
            @Override
            public Boolean onSuccess(String response) {
                myBottomSheetDialogFragment.sendToast("添加成功");
                return null;
            }

            @Override
            public Boolean onFailure(IOException e) {
                myBottomSheetDialogFragment.sendToast("添加失败");
                return null;
            }
        });
    }
}
