package com.example.selfdiscipline;

import com.alibaba.android.arouter.launcher.ARouter;
import com.example.eventbus.UserBaseMessageEventBus;

import org.greenrobot.eventbus.EventBus;

public class MainActivityPresenter {
    MainActivityModule mainActivityModule;
    MainActivity mainActivity;

    public MainActivityPresenter(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
        this.mainActivityModule = new MainActivityModule(mainActivity);
    }
    public void come() {
        String i;
        if (mainActivityModule.queryAllUser() != null) {
            i = mainActivityModule.queryAllUser().getUserToken();
        } else {
            i = null;
        }
        if (i != null) {
            UserBaseMessageEventBus userBaseMessage = new UserBaseMessageEventBus(mainActivityModule.queryAllUser().getUserName(),
                    mainActivityModule.queryAllUser().getUserPictureURL(), mainActivityModule.queryAllUser().getUserEmail(),
                    mainActivityModule.queryAllUser().getUserToken(), mainActivityModule.queryAllUser().getUserId());
            EventBus.getDefault().postSticky(userBaseMessage);
            ARouter.getInstance().build("/thematicsection/MainActivity1").navigation();
        } else {
            ARouter.getInstance().build("/login/LoginUpActivity").navigation();
        }
    }
}
