package com.example.thematicSection.bottomFragment;

import com.example.localdatabase.bean.UserTables;
import com.example.thematicSection.MainActivity1Module;

public class BottomSheetDialogAddAChecklistPresenter {

    BottomSheetDialogAddAChecklist bottomSheetDialogAddAChecklist;
    BottomModule bottomModule;

    public BottomSheetDialogAddAChecklistPresenter(BottomSheetDialogAddAChecklist bottomSheetDialogAddAChecklist) {
        this.bottomSheetDialogAddAChecklist = bottomSheetDialogAddAChecklist;
        bottomModule = new BottomModule(bottomSheetDialogAddAChecklist.getContext());
    }

    public void addListing(String userId, String listingColor, String listingName) {
        bottomModule.addListing(userId, listingColor, listingName, new BottomModule.BottomModuleListen() {
            @Override
            public void onSucceed() {
                bottomSheetDialogAddAChecklist.sendToast("添加成功");
                bottomSheetDialogAddAChecklist.dismiss();
            }

            @Override
            public void onFailed() {
                bottomSheetDialogAddAChecklist.sendToast("添加失败");
            }
        });

    }
}
