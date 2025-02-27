package com.example.clockinfragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;

import com.example.clockinfragment.adapter.ClockInRecyclerAdapter;
import com.example.clockinfragment.bean.TestBead;
import com.example.clockinfragment.fragment.AddFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.necer.calendar.NCalendar;
import com.necer.enumeration.DateChangeBehavior;
import com.necer.listener.OnCalendarChangedListener;
import com.necer.utils.hutool.ChineseDate;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ClockInFragment_1#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ClockInFragment_1 extends Fragment implements ClockInRecyclerAdapter.OnItemClickListener{
    private static final String TAG = "TestTT_ClockInFragment_1";
    RecyclerView recyclerView;
    List<TestBead> testBeads;
    ClockInFragmentPresenter clockInFragmentPresenter;
    FloatingActionButton floatingActionButton_floatingButton_add_clockIn;
    NCalendar nCalendar;
    FloatingActionButton floatingButton_backNowDay;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ClockInFragment_1() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ClockInFragment_1.
     */
    // TODO: Rename and change types and number of parameters
    public static ClockInFragment_1 newInstance(String param1, String param2) {
        ClockInFragment_1 fragment = new ClockInFragment_1();
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
        View view = inflater.inflate(R.layout.fragment_clock_in_1, container, false);
        initData();
        nCalendar = view.findViewById(R.id.weekCalendar);
        floatingButton_backNowDay = view.findViewById(R.id.floatingButton_backNowDay);
        floatingButton_backNowDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nCalendar.toToday();
            }
        });
        nCalendar.setOnCalendarChangedListener(new OnCalendarChangedListener() {
            @Override
            public void onCalendarChange(int i, int i1, LocalDate localDate, DateChangeBehavior dateChangeBehavior) {
                if (localDate != null) {
                    ChineseDate chineseDate = new ChineseDate(localDate);
                    String s = localDate.toString();
                    String s2 = LocalDate.now().toString();
                    if (! s.equals(s2)){
                        Log.d("nvjbifgj", localDate.toString() + "pppppp" + LocalDate.now().toString());
                        floatingButton_backNowDay.setVisibility(View.VISIBLE);
                    } else {
                        floatingButton_backNowDay.setVisibility(View.GONE);
                    }
                }
            }
        });
        floatingActionButton_floatingButton_add_clockIn = view.findViewById(R.id.floatingButton_add_clockIn);
        recyclerView = view.findViewById(R.id.clockIn_recycler_View);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        ClockInRecyclerAdapter addFounctionAdapter = new ClockInRecyclerAdapter(testBeads, (ClockInRecyclerAdapter.OnItemClickListener) this);
        FrameLayout childFragmentContainer = view.findViewById(R.id.fragment_addPunchShards);
        recyclerView.setAdapter(addFounctionAdapter);
        floatingActionButton_floatingButton_add_clockIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "点击了应该添加碎片了");
                FragmentManager fragmentManager = getChildFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                AddFragment addFragment = new AddFragment();
                fragmentTransaction.add(R.id.fragment_addPunchShards, addFragment).commit();
            }
        });
        return view;
    }
    public void initData() {
        testBeads = new ArrayList<>();
        TestBead testBead = new TestBead("喝水", "8", "8", R.drawable.walter, 3);
        testBeads.add(testBead);
        TestBead testBead1 = new TestBead("早起", "6", "3", R.drawable.morning, 3);
        testBeads.add(testBead1);
        TestBead testBead2 = new TestBead("早睡", "6", "3", R.drawable.night, 3);
        testBeads.add(testBead2);
        TestBead testBead3 = new TestBead("吃水果", "5", "3", R.drawable.firut, 3);
        testBeads.add(testBead3);
        TestBead testBead4 = new TestBead("锻炼", "5", "3", R.drawable.exercise, 3);
        testBeads.add(testBead4);
        TestBead testBead5 = new TestBead("背单词", "2", "3", R.drawable.word, 3);
        testBeads.add(testBead5);
    }
    public void setClockInFragmentPresenter(ClockInFragmentPresenter clockInFragmentPresenter) {
        this.clockInFragmentPresenter = clockInFragmentPresenter;
    }
    @Override
    public void onItemClick() {


    }
}