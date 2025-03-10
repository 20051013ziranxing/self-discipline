package com.example.myfragment;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.android.arouter.launcher.ARouter;
import com.bumptech.glide.Glide;
import com.example.eventbus.UserBaseMessageEventBus;
import com.example.myfragment.adapter.AddFounctionAdapter;
import com.example.myfragment.adapter.FunctionAdapter;
import com.example.myfragment.bean.Function;
import com.example.myfragment.bean.NewFunction;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MyFragment_1#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyFragment_1 extends Fragment {
    UserBaseMessageEventBus userBaseMessageEventBus;
    CircleImageView imageView_headPicture;
    TextView textView_UserName;
    MyFragmentPresenter presenter;
    ConstraintLayout constraintLayout;
    RecyclerView recyclerView;
    RecyclerView recyclerView_function;
    List<NewFunction> newFunctionList;
    List<Function> functionList;
    public static final String TAG = "TestTT_MyFragment_1";
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    public MyFragment_1() {

    }
    public MyFragment_1(String emailNumber) {

    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MyFragment_1.
     */
    // TODO: Rename and change types and number of parameters
    public static MyFragment_1 newInstance(String param1, String param2) {
        MyFragment_1 fragment = new MyFragment_1();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
            Log.d(TAG, mParam1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_1, container, false);
        EventBus.getDefault().register(this);
        imageView_headPicture = view.findViewById(R.id.imageView_headPicture);
        Log.d(TAG, userBaseMessageEventBus.getUserPictureURL());
        Glide.with(this)
                .load("https://mmbiz.qpic.cn/mmbiz_jpg/50flWREUFnHqHqia20eqULiczW6UPOolbIucpDClrcnOc50C5zqRq9dfY7uzzTNNS46VUicibdMrkibgvXwzcRR4jWg/640?wx_fmt=jpeg&from=appmsg&tp=wxpic&wxfrom=5&wx_lazy=1&wx_co=1")
                .into(imageView_headPicture);
        textView_UserName = view.findViewById(R.id.textView_UserName);
        textView_UserName.setText(userBaseMessageEventBus.getUserName());
        //进行数据的初始化
        initData();
        constraintLayout = view.findViewById(R.id.constraint);
        constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ARouter.getInstance().build("/accountsecurity/AccountSecurityActivity").navigation();
            }
        });
        //新增功能的展示
        recyclerView = view.findViewById(R.id.recyclerView_AddFunction);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        AddFounctionAdapter addFounctionAdapter = new AddFounctionAdapter(newFunctionList);
        recyclerView.setAdapter(addFounctionAdapter);
        //我的界面的原始所需功能
        recyclerView_function = view.findViewById(R.id.recyclerView_Function);
        LinearLayoutManager linearLayoutManager_function = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView_function.setLayoutManager(linearLayoutManager_function);
        FunctionAdapter functionAdapter = new FunctionAdapter(functionList);
        recyclerView_function.setAdapter(functionAdapter);
        return view;
    }
    public void initData() {
        newFunctionList = new ArrayList<>();
        NewFunction newFunction = new NewFunction(R.drawable.pomodoro, "番茄钟");
        newFunctionList.add(newFunction);

        functionList = new ArrayList<>();
        Function function = new Function(R.drawable.account_security, "账号与安全");
        functionList.add(function);
        Function function5 = new Function(R.drawable.personalized_service, "个性化服务");
        functionList.add(function5);
        Function function2 = new Function(R.drawable.getting_started, "入门指南");
        functionList.add(function2);
        Function function3 = new Function(R.drawable.feedback, " 问题反馈");
        functionList.add(function3);
        Function function1 = new Function(R.drawable.user_agreement, "用户协议");
        functionList.add(function1);
        Function function4 = new Function(R.drawable.privacy_policy, "隐私政策");
        functionList.add(function4);
    }

    public void setPresenter(MyFragmentPresenter presenter) {
        this.presenter = presenter;
    }

    /*@Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
    }*/

    @Subscribe(threadMode = ThreadMode.POSTING, sticky = true)
    public void onMoonStickyEvent(UserBaseMessageEventBus userBaseMessageEventBus) {
        this.userBaseMessageEventBus = userBaseMessageEventBus;
    }
}