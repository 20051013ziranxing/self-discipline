package com.example.myfragment;

import com.example.networkrequests.NetworkClient;

public class MyFragmentPresenter {
    MyFragmentModule myFragmentModule;
    MyFragment_1 myFragment1;

    public MyFragmentPresenter(MyFragmentModule myFragmentModule, MyFragment_1 myFragment1) {
        this.myFragmentModule = myFragmentModule;
        this.myFragment1 = myFragment1;
    }

    public String getUserName() {
        return myFragmentModule.getUserName();
    }
    public String getUserPicture() {
        return myFragmentModule.getUserPicture();
    }
}
