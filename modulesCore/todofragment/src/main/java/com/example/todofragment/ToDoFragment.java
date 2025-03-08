package com.example.todofragment;

import android.app.ActionBar;
import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.eventbus.UserBaseMessageEventBus;
import com.example.todofragment.adapter.RecyclerViewToDoAdapter;
import com.example.todofragment.bean.GetToDoThings;
import com.example.todofragment.bean.ToDoThing;
import com.example.todofragment.fragment.AddToDoFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.necer.calendar.NCalendar;
import com.necer.enumeration.CalendarState;
import com.necer.enumeration.CheckModel;
import com.necer.enumeration.DateChangeBehavior;
import com.necer.listener.OnCalendarChangedListener;
import com.necer.listener.OnCalendarStateChangedListener;
import com.necer.painter.InnerPainter;
import com.necer.utils.hutool.ChineseDate;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ToDoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ToDoFragment extends Fragment {
    UserBaseMessageEventBus userBaseMessageEventBus;
    ToDoFragmentPresenter toDoFragmentPresenter;
    TextView textView_data;
    ImageButton imageButton;
    List<GetToDoThings.GetToDothingMessage> toDoThings;
    Toolbar toolbar;
    TextView textView;
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
        EventBus.getDefault().register(this);
        toDoThings = new ArrayList<>();
        initData();
        recyclerView_ToDoFragment_show = view.findViewById(R.id.recyclerView_ToDoFragment_show);
        toolbar = view.findViewById(R.id.toolbar);
        textView = view.findViewById(R.id.data);
        NCalendar miui10Calendar = view.findViewById(R.id.miui10Calendar);
        floatingActionButton_backDay = view.findViewById(R.id.floatingButton_backNowDay);
        floatingActionButton_add = view.findViewById(R.id.floatingButton_add);
        floatingActionButton_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyBottomSheetDialogFragment bottomSheetDialogFragment = new MyBottomSheetDialogFragment();
                bottomSheetDialogFragment.show(getChildFragmentManager(), "MyBottomSheetDialogFragment");
            }
        });
        miui10Calendar.setOnCalendarChangedListener(new OnCalendarChangedListener() {
            @Override
            public void onCalendarChange(int year, int month, LocalDate localDate, DateChangeBehavior dateChangeBehavior) {
                if (localDate != null) {
                    ChineseDate chineseDate = new ChineseDate(localDate);
                    String s = localDate.toString();
                    String s2 = LocalDate.now().toString();
                    textView.setText(localDate.toString());
                    if (! s.equals(s2)){
                        Log.d("nvjbifgj", localDate.toString() + "pppppp" + LocalDate.now().toString());
                        floatingActionButton_backDay.setVisibility(View.VISIBLE);
                    } else {
                        floatingActionButton_backDay.setVisibility(View.GONE);
                    }
                }
            }
        });
        floatingActionButton_backDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                miui10Calendar.toToday();
            }
        });
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView_ToDoFragment_show.setLayoutManager(linearLayoutManager);
        RecyclerViewToDoAdapter recyclerViewToDoAdapter = new RecyclerViewToDoAdapter(toDoThings);
        recyclerView_ToDoFragment_show.setAdapter(recyclerViewToDoAdapter);
        textView_data = view.findViewById(R.id.data);
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        textView_data.setText(year + "-" + month + "-" + day);
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
        toDoFragmentPresenter.getToDoThings(userBaseMessageEventBus.getUserId(), "2025-03-08");
        /*toDoThings = new ArrayList<>();
        ToDoThing toDoThing = new ToDoThing("喝水", "一级", "不计时", true);
        toDoThings.add(toDoThing);
        ToDoThing toDoThing1 = new ToDoThing("喝水", "三级", "倒计时", false);
        toDoThings.add(toDoThing1);
        ToDoThing toDoThing2 = new ToDoThing("喝水", "一级", "不计时", false);
        toDoThings.add(toDoThing2);*/
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
}