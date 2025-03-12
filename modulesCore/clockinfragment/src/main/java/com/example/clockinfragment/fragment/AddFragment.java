package com.example.clockinfragment.fragment;

import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.clockinfragment.ClockInFragment_1;
import com.example.clockinfragment.R;
import com.example.clockinfragment.adapter.AddHabitRecyclerViewAdapter;
import com.example.clockinfragment.adapter.ClockInRecyclerAdapter;
import com.example.clockinfragment.bean.Icon;
import com.example.clockinfragment.myview.CustomDialog;
import com.example.eventbus.UserBaseMessageEventBus;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddFragment extends Fragment implements CustomDialog.OnDialogConfirmedListener, AddHabitRecyclerViewAdapter.OnItemClickListener{
    private static final String TAG = "TestTT_AddFragment";
    UserBaseMessageEventBus userBaseMessageEventBus;
    List<Icon> iconList;
    EditText editText_addFragment_HabitName;
    Button button_habitsForSavingSettings;
    Button button_addFragment_Cancel;
    AddFragmentPresenter addFragmentPresenter;
    RecyclerView recyclerView;
    EditText editText_EncouragementWords;
    ImageButton imageButton_imageView_addFragment_HabitIconChoice;
    TextView textView_StartTime;
    TextView textView_EndTime;
    TextView textView_SumCount;
    ConstraintLayout constraintLayout_Start;
    ConstraintLayout constraintLayout_End;
    ConstraintLayout constraintLayout_Count;
    ImageButton imageButton_randomWordsOfEncouragement;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AddFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AddFragment newInstance(String param1, String param2) {
        AddFragment fragment = new AddFragment();
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

        initData();
        EventBus.getDefault().register(this);
        View view = inflater.inflate(R.layout.fragment_add, container, false);
        addFragmentPresenter = new AddFragmentPresenter(this);
        editText_addFragment_HabitName = view.findViewById(R.id.editText_addFragment_HabitName);
        button_addFragment_Cancel = view.findViewById(R.id.button_addFragment_Cancel);
        button_addFragment_Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addFragmentPresenter.removeAddFragment();
            }
        });
        button_habitsForSavingSettings = view.findViewById(R.id.button_habitsForSavingSettings);
        button_habitsForSavingSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addFragmentPresenter.createAPunchInTask(userBaseMessageEventBus.getUserId(), "successful",
                        editText_addFragment_HabitName.getText().toString(), textView_SumCount.getText().toString(),
                        textView_StartTime.getText().toString(), textView_EndTime.getText().toString());
                Log.d(TAG, userBaseMessageEventBus.getUserId()+ "successful"+
                        editText_addFragment_HabitName.getText().toString()+textView_SumCount.getText().toString()+
                        textView_StartTime.getText().toString()+textView_EndTime.getText().toString());
            }
        });
        editText_EncouragementWords = view.findViewById(R.id.editText_EncouragementWords);
        imageButton_imageView_addFragment_HabitIconChoice = view.findViewById(R.id.imageView_addFragment_HabitIconChoice);
        imageButton_randomWordsOfEncouragement = view.findViewById(R.id.imageButton_randomWordsOfEncouragement);
        recyclerView = view.findViewById(R.id.recyclerView_addFragment_HabitIconAll);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        AddHabitRecyclerViewAdapter addHabitRecyclerViewAdapter = new AddHabitRecyclerViewAdapter(iconList, this);
        recyclerView.setAdapter(addHabitRecyclerViewAdapter);
        constraintLayout_Start = view.findViewById(R.id.selectionOfTheStartDate);
        constraintLayout_End = view.findViewById(R.id.selectionOfTheEndDate);
        constraintLayout_Count = view.findViewById(R.id.selectionOfTheSumCount);
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        textView_StartTime = view.findViewById(R.id.textViewStartTime);
        textView_StartTime.setText(year + "-" + month + "-" + day);
        textView_EndTime = view.findViewById(R.id.textViewEndTime);
        textView_SumCount = view.findViewById(R.id.textViewSumCount);
        //对打卡任务开始时期的选择
        constraintLayout_Start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        textView_StartTime.setText(year + "-" + month + "-" + dayOfMonth);
                    }
                }, year, month, day);
                datePickerDialog.show();
            }
        });
        //对打卡任务结束时期的选择
        constraintLayout_End.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        textView_EndTime.setText(year + "-" + month + "-" + dayOfMonth);
                    }
                }, year, month, day);
                datePickerDialog.show();
            }
        });
        //对于每天打卡次数设置
        constraintLayout_Count.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomDialog dialog = new CustomDialog(getContext());
                dialog.setOnDialogConfirmedListener((CustomDialog.OnDialogConfirmedListener) AddFragment.this);
                dialog.show();
            }
        });
        imageButton_randomWordsOfEncouragement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addFragmentPresenter.changeRandomWordsOfEncouragement();
            }
        });
        return view;
    }
    public void initData() {
        iconList = new ArrayList<>();
        Icon icon = new Icon(R.drawable.walter, "#ADD8E6");
        Icon icon1 = new Icon(R.drawable.morning, "#FFFBEA");
        Icon icon2 = new Icon(R.drawable.night, "#D8BFD8");
        Icon icon3 = new Icon(R.drawable.firut, "#D8BFD8");
        Icon icon4 = new Icon(R.drawable.exercise, "#ADD8E6");
        Icon icon5 = new Icon(R.drawable.word, "#D2D2E6");
        iconList.add(icon);
        iconList.add(icon1);
        iconList.add(icon2);
        iconList.add(icon3);
        iconList.add(icon4);
        iconList.add(icon5);
    }
    @Override
    public void onDialogConfirmed(String inputText) {
        textView_SumCount.setText(inputText + "次＞");
    }

    private void changeFragmentLayout(int color, int icon) {
        View view = getView();
        if (view != null) {
            imageButton_imageView_addFragment_HabitIconChoice.setImageResource(icon);
            imageButton_imageView_addFragment_HabitIconChoice.setBackgroundColor(color);
        }
    }

    @Override
    public void onItemClick(int color, int iconIn) {
        changeFragmentLayout(color, iconIn);
    }
    public void SendToast(final String message) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void sendToast(String message) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.POSTING, sticky = true)
    public void onMoonStickyEvent(UserBaseMessageEventBus userBaseMessageEventBus) {
        this.userBaseMessageEventBus = userBaseMessageEventBus;
    }
}