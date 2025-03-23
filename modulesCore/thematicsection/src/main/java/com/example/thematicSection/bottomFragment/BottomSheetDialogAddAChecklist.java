package com.example.thematicSection.bottomFragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eventbus.UserBaseMessageEventBus;
import com.example.thematicSection.R;
import com.example.thematicSection.adapter.RecyclerViewAdapterColor;
import com.example.thematicSection.bean.ColorChoose;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

public class BottomSheetDialogAddAChecklist extends BottomSheetDialogFragment {
    BottomSheetDialogAddAChecklistPresenter bottomSheetDialogAddAChecklistPresenter;
    UserBaseMessageEventBus userBaseMessageEventBus;
    TextView button_cancel;
    TextView button_confirm;
    EditText editText_theNameOfTheManifest;
    RecyclerView recyclerView_colorChose;
    List<ColorChoose> colorChooses;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottom_add_list, container, false);
        bottomSheetDialogAddAChecklistPresenter = new BottomSheetDialogAddAChecklistPresenter(this);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        recyclerView_colorChose = view.findViewById(R.id.recyclerView_colorChose);
        initData();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView_colorChose.setLayoutManager(linearLayoutManager);
        RecyclerViewAdapterColor recyclerViewAdapterColor = new RecyclerViewAdapterColor(colorChooses, new RecyclerViewAdapterColor.RecyclerViewAdapterColorListen() {
            @Override
            public void choose(String color) {

            }
        });
        recyclerView_colorChose.setAdapter(recyclerViewAdapterColor);
        editText_theNameOfTheManifest = view.findViewById(R.id.editText_theNameOfTheManifest);
        button_cancel = view.findViewById(R.id.button_cancel);
        button_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        button_confirm = view.findViewById(R.id.button_confirm);
        button_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editText_theNameOfTheManifest.getText().toString().isEmpty()) {
                    sendToast("请输入标签名称");
                } else {
                    //进行添加逻辑
                    /*bottomSheetDialogAddAChecklistPresenter.addListing();*/
                }
            }
        });
        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    @Subscribe(threadMode = ThreadMode.POSTING, sticky = true)
    public void onMoonStickyEvent(UserBaseMessageEventBus userBaseMessageEventBus) {
        this.userBaseMessageEventBus = userBaseMessageEventBus;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (EventBus.getDefault().isRegistered(this)) {

        }
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    public void initData() {
        colorChooses = new ArrayList<>();
        ColorChoose colorChoose0 = new ColorChoose(true, "#d8caaf");
        ColorChoose colorChoose1 = new ColorChoose(false, "#96a48d");
        ColorChoose colorChoose2 = new ColorChoose(false, "#c1b3ad");
        ColorChoose colorChoose3 = new ColorChoose(false, "#b2b7c0");
        ColorChoose colorChoose4 = new ColorChoose(false, "#d9cdcb");
        ColorChoose colorChoose5 = new ColorChoose(false, "#d4afa8");
        ColorChoose colorChoose6 = new ColorChoose(false, "#d7ccc8");
        ColorChoose colorChoose7 = new ColorChoose(false, "#a8bcc3");
        ColorChoose colorChoose8 = new ColorChoose(false, "#f6cec1");
        ColorChoose colorChoose9 = new ColorChoose(false, "#b5c1cf");
        ColorChoose colorChoose10 = new ColorChoose(false, "#cfc1ca");
        ColorChoose colorChoose11 = new ColorChoose(false, "#b8bca6");
        ColorChoose colorChoose12 = new ColorChoose(false, "#c0c2bb");
        colorChooses.add(colorChoose0);
        colorChooses.add(colorChoose1);
        colorChooses.add(colorChoose2);
        colorChooses.add(colorChoose3);
        colorChooses.add(colorChoose4);
        colorChooses.add(colorChoose5);
        colorChooses.add(colorChoose6);
        colorChooses.add(colorChoose7);
        colorChooses.add(colorChoose8);
        colorChooses.add(colorChoose9);
        colorChooses.add(colorChoose10);
        colorChooses.add(colorChoose11);
        colorChooses.add(colorChoose12);
    }

    public void sendToast(String message) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
