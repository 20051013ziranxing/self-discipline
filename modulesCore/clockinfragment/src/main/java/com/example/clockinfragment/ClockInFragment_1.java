package com.example.clockinfragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.example.clockinfragment.adapter.ClockInRecyclerAdapter;
import com.example.clockinfragment.bean.CheckInBean;
import com.example.clockinfragment.bean.CheckInBean1;
import com.example.clockinfragment.bean.TestBead;
import com.example.clockinfragment.fragment.AddFragment;
import com.example.clockinfragment.fragment.ModifyFragment;
import com.example.clockinfragment.singleton.DateSingleton;
import com.example.eventbus.UserBaseMessageEventBus;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.necer.calendar.NCalendar;
import com.necer.enumeration.DateChangeBehavior;
import com.necer.listener.OnCalendarChangedListener;
import com.necer.utils.hutool.ChineseDate;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ClockInFragment_1#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ClockInFragment_1 extends Fragment{
    UserBaseMessageEventBus userBaseMessageEventBus;
    private static final String TAG = "TestTT_ClockInFragment_1";
    RecyclerView recyclerView;
    List<TestBead> testBeads;
    ClockInFragmentPresenter clockInFragmentPresenter;
    FloatingActionButton floatingActionButton_floatingButton_add_clockIn;
    NCalendar nCalendar;
    FloatingActionButton floatingButton_backNowDay;
    ClockInRecyclerAdapter addFounctionAdapter;
    ItemTouchHelper itemTouchHelper;


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
        clockInFragmentPresenter = new ClockInFragmentPresenter(this);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
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
                    DateSingleton.getInstance().setDate(s);
                    if (! s.equals(s2)){
                        Log.d("nvjbifgj", localDate.toString() + "pppppp" + LocalDate.now().toString());
                        floatingButton_backNowDay.setVisibility(View.VISIBLE);
                    } else {
                        floatingButton_backNowDay.setVisibility(View.GONE);
                    }
                }
                Log.d(TAG, "要重新获取数据了");
                clockInFragmentPresenter.getAllThePunchesForAGivenDay(userBaseMessageEventBus.getUserId(), localDate.toString());
            }
        });
        floatingActionButton_floatingButton_add_clockIn = view.findViewById(R.id.floatingButton_add_clockIn);
        recyclerView = view.findViewById(R.id.clockIn_recycler_View);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        addFounctionAdapter = new ClockInRecyclerAdapter(null,
                DateSingleton.getInstance().getDate(), new ClockInRecyclerAdapter.OnItemClickListener() {
            @Override
            public void modifyTheBinding(String id, String start_time) {
                Calendar calendar = Calendar.getInstance();
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                String formattedDate = dateFormat.format(calendar.getTime());
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                Date date1;
                Date date2;
                try {
                    date1 = formatter.parse(DateSingleton.getInstance().getDate());
                    date2 = formatter.parse(formattedDate);
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
                //执行相关逻辑，并
                /*clockInFragmentPresenter.deleteAClockInTask(id);*/
                if (date1.equals(date2)) {
                    Log.d(TAG, formattedDate);
                    clockInFragmentPresenter.modifyTheClockInInformationToEndEarly(Integer.parseInt(id),start_time.substring(0, 10), formattedDate);
                } else {
                    clockInFragmentPresenter.deleteAClockInTask(id);
                }
                itemTouchHelper.attachToRecyclerView(recyclerView);
            }

            @Override
            public void modifyTheAttendanceInformationByID(int id) {
                FragmentManager fragmentManager = getChildFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.add(R.id.fragment_addPunchShards, new ModifyFragment(id, new AddFragment.AddFragmentListen() {
                    @Override
                    public void makeAnUpdate() {
                        recaptureTheDataAndUpdateIt();
                    }
                })).commit();
            }

            @Override
            public void modifyTheClockInInformation(String title, String start_date, String end_date, int icon,
                                                    int target_checkin_count, String motivation_message) {
                FragmentManager fragmentManager = getChildFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.add(R.id.fragment_addPunchShards, new ModifyFragment(title, start_date,
                        end_date, icon, target_checkin_count, motivation_message)).commit();
            }

            @Override
            public void onItemClick(int i, String data) {
                clockInFragmentPresenter.add1ToTheNumberOfCheckInsCompleted(i, data);
            }
        });
        FrameLayout childFragmentContainer = view.findViewById(R.id.fragment_addPunchShards);
        recyclerView.setAdapter(addFounctionAdapter);
        clockInFragmentPresenter.getAllThePunchesForAGivenDay(userBaseMessageEventBus.getUserId(), DateSingleton.getInstance().getDate());
        floatingActionButton_floatingButton_add_clockIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "点击了应该添加碎片了");
                FragmentManager fragmentManager = getChildFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                AddFragment addFragment = new AddFragment(new AddFragment.AddFragmentListen() {
                    @Override
                    public void makeAnUpdate() {
                        recaptureTheDataAndUpdateIt();
                    }
                });
                fragmentTransaction.add(R.id.fragment_addPunchShards, addFragment).commit();
            }
        });
        SwipeToActionCallbackClock.SwipeToActionCallbackListener swipeToActionCallbackListener = new SwipeToActionCallbackClock.SwipeToActionCallbackListener() {
            @Override
            public void unbind() {
                itemTouchHelper.attachToRecyclerView(null);
            }

            @Override
            public void harkBackTo() {

            }
        };
        itemTouchHelper = new ItemTouchHelper(new SwipeToActionCallbackClock(swipeToActionCallbackListener));
        itemTouchHelper.attachToRecyclerView(recyclerView);
        return view;
    }
    public void initData() {

        int k = R.drawable.morning;
        Log.d(TAG, String.valueOf(k));
        testBeads = new ArrayList<>();
        TestBead testBead = new TestBead("喝水", "8", "8", k, 3);
        testBeads.add(testBead);
        /*TestBead testBead1 = new TestBead("早起", "6", "3", R.drawable.morning, 3);
        testBeads.add(testBead1);*/
        TestBead testBead2 = new TestBead("早睡", "6", "3", R.drawable.night, 3);
        testBeads.add(testBead2);
        TestBead testBead3 = new TestBead("吃水果", "5", "3", R.drawable.firut, 3);
        testBeads.add(testBead3);
        TestBead testBead4 = new TestBead("锻炼", "5", "3", R.drawable.exercise, 3);
        testBeads.add(testBead4);
        TestBead testBead5 = new TestBead("背单词", "3", "3", R.drawable.word, 3);
        testBeads.add(testBead5);
        testBeads.sort((o1, o2) -> Boolean.compare(o1.getFinish(), o2.getFinish()));
    }
    public void setClockInFragmentPresenter(ClockInFragmentPresenter clockInFragmentPresenter) {
        this.clockInFragmentPresenter = clockInFragmentPresenter;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    @Subscribe(threadMode = ThreadMode.POSTING, sticky = true)
    public void onMoonStickyEvent(UserBaseMessageEventBus userBaseMessageEventBus) {
        this.userBaseMessageEventBus = userBaseMessageEventBus;
    }

    public void updateThePunchList(List<CheckInBean1.CheckinData> checkinDataList, String data) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                addFounctionAdapter.setCheckinBeanList(checkinDataList);
                addFounctionAdapter.setData(data);
                Log.d(TAG, "我要开始更新页面了");
                /*for (CheckInBean1.CheckinData checkinData : checkinDataList) {
                    Log.d(TAG, data + checkinData.getCheckin().getTitle() + checkinData.getCheckin().getId());
                }*/
                addFounctionAdapter.notifyDataSetChanged();
            }
        });
    }

    public void recaptureTheDataAndUpdateIt() {
        clockInFragmentPresenter.getAllThePunchesForAGivenDay(userBaseMessageEventBus.getUserId(), DateSingleton.getInstance().getDate());
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