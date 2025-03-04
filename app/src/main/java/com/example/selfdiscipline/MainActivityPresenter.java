package com.example.selfdiscipline;

import com.alibaba.android.arouter.launcher.ARouter;

public class MainActivityPresenter {
    MainActivityModule mainActivityModule;
    MainActivity mainActivity;

    public MainActivityPresenter(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
        this.mainActivityModule = new MainActivityModule(mainActivity);
    }
    public void come() {
        int i = mainActivityModule.queryAllUser();
        if (i == 1) {
            ARouter.getInstance().build("/thematicsection/MainActivity1").navigation();
        } else {
            ARouter.getInstance().build("/login/LoginUpActivity").navigation();
        }
    }
}
