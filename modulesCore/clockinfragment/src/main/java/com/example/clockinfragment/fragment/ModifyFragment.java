package com.example.clockinfragment.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.clockinfragment.R;
import com.example.clockinfragment.StringFinder;
import com.example.clockinfragment.bean.CheckInById;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ModifyFragment extends AddFragment{
    AddFragmentListen addFragmentListen;
    ModifyFragmentPresenter modifyFragmentPresenter;
    private static final String TAG = "TestTT_ModifyFragment";
    String title;
    String start_date;
    String end_date;
    int icon;
    int target_checkin_count;
    String motivation_message;

    public ModifyFragment(String title, String start_date, String end_date, int icon, int target_checkin_count, String motivation_message) {
        this.title = title;
        this.start_date = start_date;
        this.end_date = end_date;
        this.icon = icon;
        this.target_checkin_count = target_checkin_count;
        this.motivation_message = motivation_message;
    }
    int checkInId;

    public ModifyFragment(int checkInId, AddFragmentListen modifyFragmentListen) {
        this.checkInId = checkInId;
        this.addFragmentListen = modifyFragmentListen;
    }

    EditText editText_addFragment_HabitName;

    TextView textView_addHabit_121;
    ImageView imageView_addFragment_HabitIconChoice;
    TextView textViewStart;
    TextView selectionOfTheEndDate;
    TextView textViewSumCount;
    EditText editText_EncouragementWords;
    Button button_habitsForSavingSettings;
    ImageButton imageButton_deleteClock;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        modifyFragmentPresenter = new ModifyFragmentPresenter(this);
        textView_addHabit_121 = view.findViewById(R.id.textView_addHabit_121);
        textView_addHabit_121.setText("修改习惯");
        editText_addFragment_HabitName = view.findViewById(R.id.editText_addFragment_HabitName);
        imageView_addFragment_HabitIconChoice = view.findViewById(R.id.imageView_addFragment_HabitIconChoice);
        textViewStart = view.findViewById(R.id.textViewStartTime);
        selectionOfTheEndDate = view.findViewById(R.id.textViewEndTime);
        textViewSumCount = view.findViewById(R.id.textViewSumCount);
        editText_EncouragementWords = view.findViewById(R.id.editText_EncouragementWords);
        button_habitsForSavingSettings = view.findViewById(R.id.button_habitsForSavingSettings);
        modifyFragmentPresenter.getAttendanceRecordsBasedOnYourID(String.valueOf(checkInId));
        imageButton_deleteClock = view.findViewById(R.id.imageButton_deleteClock);
        button_habitsForSavingSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                Date date1;
                Date date2;
                try {
                    date1 = formatter.parse(textView_StartTime.getText().toString());
                    date2 = formatter.parse(textView_EndTime.getText().toString());
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
                if (editText_addFragment_HabitName.getText().toString().isEmpty()) {
                    sendToast("习惯名不可为空");
                } else if (editText_EncouragementWords.getText().toString().isEmpty()) {
                    sendToast("设置一个鼓励语吧！");
                } else if (!date2.after(date1)) {
                    sendToast("请检查时间的设置");
                } else {
                    modifyFragmentPresenter.modifyTheClockInInformation(checkInId, editText_addFragment_HabitName.getText().toString(),
                            textView_StartTime.getText().toString(), textView_EndTime.getText().toString(),
                            ImageViewSrcSingleton.getInstance().getImageResId(),
                            Integer.parseInt(textView_SumCount.getText().toString()), editText_EncouragementWords.getText().toString());
                    Log.d(TAG, checkInId+ editText_addFragment_HabitName.getText().toString()+
                            textView_StartTime.getText().toString()+ textView_EndTime.getText().toString()+
                            ImageViewSrcSingleton.getInstance().getImageResId()+
                            Integer.parseInt(textView_SumCount.getText().toString())+ editText_EncouragementWords.getText().toString());
                }
            }
        });
        return view;
    }

    public void getTheDataForInitialization(CheckInById checkInById) {
        Log.d(TAG, String.valueOf(checkInById.getData().getCheckin().getIcon()));
        editText_addFragment_HabitName.setText(checkInById.getData().getCheckin().getTitle());
        imageView_addFragment_HabitIconChoice.setImageResource(checkInById.getData().getCheckin().getIcon());
        imageView_addFragment_HabitIconChoice.setBackgroundColor(Color.parseColor(StringFinder.getStringFromInt(checkInById.getData().getCheckin().getIcon())));
        textViewStart.setText(checkInById.getData().getCheckin().getStart_date().substring(0, 10));
        selectionOfTheEndDate.setText(checkInById.getData().getCheckin().getEnd_date().substring(0, 10));
        Log.d(TAG, String.valueOf(checkInById.getData().getCheckin().getTarget_checkin_count()));
        textViewSumCount.setText(String.valueOf(checkInById.getData().getCheckin().getTarget_checkin_count()));
        editText_EncouragementWords.setText(checkInById.getData().getCheckin().getMotivation_message());
        imageButton_deleteClock.setVisibility(View.VISIBLE);
        imageButton_deleteClock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                modifyFragmentPresenter.deleteAClockInTask(String.valueOf(checkInById));
                Log.d(TAG, "删除了");
            }
        });
    }
}
