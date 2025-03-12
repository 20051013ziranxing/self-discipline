package com.example.todofragment;

import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.eventbus.UserBaseMessageEventBus;
import com.example.todofragment.adapter.RecyclerViewToDoAdapter;
import com.example.todofragment.bean.GetToDoThings;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.necer.calendar.NCalendar;
import com.necer.enumeration.DateChangeBehavior;
import com.necer.listener.OnCalendarChangedListener;
import com.necer.utils.hutool.ChineseDate;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ToDoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ToDoFragment extends Fragment implements MyBottomSheetDialogFragment.OnFragmentInteractionListener {
    private static final String TAG = "TestTT_ToDoFragment";
    private static final String DATA = "2025_03_08";
    UserBaseMessageEventBus userBaseMessageEventBus;
    ToDoFragmentPresenter toDoFragmentPresenter;
    TextView textView_data;
    ImageButton imageButton;
    List<GetToDoThings.GetToDothingMessage> toDoThings;
    Toolbar toolbar;
    NCalendar miui10Calendar;
    /*TextView textView_data;*/
    RecyclerViewToDoAdapter recyclerViewToDoAdapter;
    RecyclerView recyclerView_ToDoFragment_show;
    FloatingActionButton floatingActionButton_backDay;
    FloatingActionButton floatingActionButton_add;
    DrawerLayout drawerLayout;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    public ToDoFragment() {

    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ToDoFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ToDoFragment newInstance(String param1, String param2) {
        ToDoFragment fragment = new ToDoFragment();
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
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_to_do, container, false);
        toDoFragmentPresenter = new ToDoFragmentPresenter(this);
        /*Log.d(TAG, "注册过？：" + EventBus.getDefault().isRegistered(this));
        EventBus.getDefault().register(this);*/
        Log.d(TAG, "注册过？：" + EventBus.getDefault().isRegistered(this));
        if (!EventBus.getDefault().isRegistered(this)) {
            Log.d(TAG, "ToDoFragment执行onStart方法，并进行了注册");
            EventBus.getDefault().register(this);
        }
        toDoThings = new ArrayList<>();
        textView_data = view.findViewById(R.id.data_time);
        Calendar calendar = Calendar.getInstance();
        Date currentDate = calendar.getTime(); // 获取当前日期
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String formattedDate = dateFormat.format(currentDate);
        textView_data.setText(formattedDate);
        textView_data.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (userBaseMessageEventBus != null)
                    toDoFragmentPresenter.getToDoThings(userBaseMessageEventBus.getUserId(), textView_data.getText().toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        miui10Calendar = view.findViewById(R.id.miui10Calendar);
        miui10Calendar.setOnCalendarChangedListener(new OnCalendarChangedListener() {
            @Override
            public void onCalendarChange(int year, int month, LocalDate localDate, DateChangeBehavior dateChangeBehavior) {
                if (localDate != null) {
                    ChineseDate chineseDate = new ChineseDate(localDate);
                    String s = localDate.toString();
                    String s2 = LocalDate.now().toString();
                    textView_data.setText(localDate.toString());
                    if (!s.equals(s2)) {
                        Log.d("nvjbifgj", localDate.toString() + "pppppp" + LocalDate.now().toString());
                        floatingActionButton_backDay.setVisibility(View.VISIBLE);
                    } else {
                        floatingActionButton_backDay.setVisibility(View.GONE);
                    }
                }
            }
        });
        recyclerView_ToDoFragment_show = view.findViewById(R.id.recyclerView_ToDoFragment_show);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView_ToDoFragment_show.setLayoutManager(linearLayoutManager);
        recyclerViewToDoAdapter = new RecyclerViewToDoAdapter(toDoThings, new RecyclerViewToDoAdapter.RecyclerViewToDoAdapterListener() {
            @Override
            public void markComplete(String id, boolean checked) {
                toDoFragmentPresenter.markWhetherTheAgencyIsCompleteOrNot(id, checked);
            }
        });
        recyclerView_ToDoFragment_show.setAdapter(recyclerViewToDoAdapter);
        initData();

        toolbar = view.findViewById(R.id.toolbar);
        floatingActionButton_backDay = view.findViewById(R.id.floatingButton_backNowDay);
        floatingActionButton_add = view.findViewById(R.id.floatingButton_add);
        floatingActionButton_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyBottomSheetDialogFragment bottomSheetDialogFragment = new MyBottomSheetDialogFragment(new MyBottomSheetDialogFragment.OnFragmentInteractionListener() {
                    @Override
                    public void onMethodCalled() {
                        if (userBaseMessageEventBus != null)
                            toDoFragmentPresenter.getToDoThings(userBaseMessageEventBus.getUserId(), textView_data.getText().toString());
                        /*getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Log.d(TAG, "开始进行更新");
                                recyclerViewToDoAdapter.setToDoThings(toDoThings);
                                recyclerViewToDoAdapter.notifyDataSetChanged();
                            }
                        });*/
                    }
                });
                bottomSheetDialogFragment.show(getChildFragmentManager(), "MyBottomSheetDialogFragment");
            }
        });
        floatingActionButton_backDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                miui10Calendar.toToday();
            }
        });
        /*textView_data = view.findViewById(R.id.data_time);*/
        drawerLayout = view.findViewById(R.id.DrawableLayout);
        imageButton = view.findViewById(R.id.showNestedScrollView);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });
        return view;
    }

    public void initData() {
        toDoThings = new ArrayList<>();
        if (userBaseMessageEventBus != null)
            toDoFragmentPresenter.getToDoThings(userBaseMessageEventBus.getUserId(), textView_data.getText().toString());
    }

    @Subscribe(threadMode = ThreadMode.POSTING, sticky = true)
    public void onMoonStickyEvent(UserBaseMessageEventBus userBaseMessageEventBus) {
        this.userBaseMessageEventBus = userBaseMessageEventBus;
    }

    public void sendToast(String message) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    //接口回调使其执行刷新操作
    @Override
    public void onMethodCalled() {
        Log.d(TAG, "开始执行回调了");
        if (userBaseMessageEventBus != null) {
            toDoFragmentPresenter.getToDoThings(userBaseMessageEventBus.getUserId(), textView_data.getText().toString());
        }
    }

    public void remindersChange(List<GetToDoThings.GetToDothingMessage> toDoThings) {
        Log.d(TAG, "我应该开始执行更新了");
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "开始进行更新");
                recyclerViewToDoAdapter.setToDoThings(toDoThings);
                recyclerViewToDoAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "注册过？hh：" + EventBus.getDefault().isRegistered(this));
        if (!EventBus.getDefault().isRegistered(this)) {
            Log.d(TAG, "ToDoFragment执行onStart方法，并进行了注册hh");
            EventBus.getDefault().register(this);
        }
    }


    @Override
    public void onStop() {
        super.onStop();
        if (EventBus.getDefault().isRegistered(this)) {
            Log.d(TAG, "ToDoFragment执行onStop方法，并进行了取消注册hh");
            EventBus.getDefault().unregister(this);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (EventBus.getDefault().isRegistered(this)) {
            Log.d(TAG, "ToDoFragment执行onStop方法，并进行了取消注册");
            EventBus.getDefault().unregister(this);
        }
    }
}