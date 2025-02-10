package com.example.findpassword;

public class FindPasswordPresenter {
    FindPasswordActivity findPasswordActivity;
    FindPasswordModule findPasswordModule;

    public FindPasswordPresenter(FindPasswordActivity findPasswordActivity) {
        this.findPasswordActivity = findPasswordActivity;
        this.findPasswordModule = new FindPasswordModule();
    }
}
