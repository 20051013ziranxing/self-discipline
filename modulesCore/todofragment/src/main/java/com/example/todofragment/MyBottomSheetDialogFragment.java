package com.example.todofragment;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.eventbus.UserBaseMessageEventBus;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.w3c.dom.Text;

import java.util.Calendar;

public class MyBottomSheetDialogFragment extends BottomSheetDialogFragment {
    private static final String TAG = "TestTT_MyBottomSheetDialogFragment";
    RadioGroup RadioGroup_111;
    RadioGroup RadioGroup_222;
    UserBaseMessageEventBus userBaseMessageEventBus;
    MyBottomSheetDialogFragmentPresenter myBottomSheetDialogFragmentPresenter;
    EditText editText_AddToDoFragment_toDoThingName;
    //用来显式你所选的时间
    TextView textView_AddToDoFragment_toDoThingTime;
    //用来选择时间的按钮
    ImageButton imageButton_AddToDoFragment_toDoThingTimeChoice;
    ConstraintLayout constraintLayout_gradle_4;
    ConstraintLayout constraintLayout_gradle_3;
    ConstraintLayout constraintLayout_gradle_2;
    ConstraintLayout constraintLayout_gradle_1;
    RadioButton radioButton_gradle_4;
    RadioButton radioButton_gradle_3;
    RadioButton radioButton_gradle_2;
    RadioButton radioButton_gradle_1;
    RadioButton radioButton_AddToDoFragment_toDoThingGradle;
    RadioButton radioButton_AddToDoFragment_toDoThingPomdoro;
    //番茄钟的按钮
    RadioButton radioButton_pomodoro_countdown_1_1;
    RadioButton radioButton_pomodoro_PositiveTiming_1_2;
    RadioButton radioButton_pomodoro_NoTimers_1_3;
    RadioButton radioButton_pomodoro_25_minutes_2_1;
    RadioButton radioButton_pomodoro_35_minutes_2_2;
    RadioButton radioButton_pomodoro_free_minutes_2_3;
    ImageButton imageButton_AddToDoFragment_save;

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_to_do, container, false);
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        String formattedMonth = String.format("%02d", month);
        String formattedDay = String.format("%02d", day);
        RadioGroup_111 = view.findViewById(R.id.RadioGroup_111);
        RadioGroup_222 = view.findViewById(R.id.RadioGroup_222);
        myBottomSheetDialogFragmentPresenter = new MyBottomSheetDialogFragmentPresenter(this);
        editText_AddToDoFragment_toDoThingName = view.findViewById(R.id.editText_AddToDoFragment_toDoThingName);
        textView_AddToDoFragment_toDoThingTime = view.findViewById(R.id.textView_AddToDoFragment_toDoThingTime);
        textView_AddToDoFragment_toDoThingTime.setText(year + "-" + formattedMonth + "-" + formattedDay);
        //根据选择的时间进行展示
        imageButton_AddToDoFragment_toDoThingTimeChoice = view.findViewById(R.id.imageButton_AddToDoFragment_toDoThingTimeChoice);
        imageButton_AddToDoFragment_toDoThingTimeChoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        String formattedMonth = String.format("%02d", month);
                        String formattedDay = String.format("%02d", dayOfMonth);
                        textView_AddToDoFragment_toDoThingTime.setText(year + "-" + formattedMonth + "-" + formattedDay);
                    }
                }, year, month, day);
                datePickerDialog.show();
            }
        });
        radioButton_AddToDoFragment_toDoThingGradle = view.findViewById(R.id.imageButton_AddToDoFragment_toDoThingGradle);
        radioButton_AddToDoFragment_toDoThingPomdoro = view.findViewById(R.id.imageButton_AddToDoFragment_toDoThingPomdoro);
        radioButton_AddToDoFragment_toDoThingGradle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                view.findViewById(R.id.imageButton_AddToDoFragment_layoutChange_gradle).setVisibility(View.VISIBLE);
                view.findViewById(R.id.imageButton_AddToDoFragment_layoutChange_time).setVisibility(View.GONE);

            }
        });
        radioButton_AddToDoFragment_toDoThingPomdoro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                view.findViewById(R.id.imageButton_AddToDoFragment_layoutChange_time).setVisibility(View.VISIBLE);
                view.findViewById(R.id.imageButton_AddToDoFragment_layoutChange_gradle).setVisibility(View.GONE);
            }
        });
        constraintLayout_gradle_4 = view.findViewById(R.id.constraintLayout_gradle_4);
        constraintLayout_gradle_3 = view.findViewById(R.id.constraintLayout_gradle_3);
        constraintLayout_gradle_2 = view.findViewById(R.id.constraintLayout_gradle_2);
        constraintLayout_gradle_1 = view.findViewById(R.id.constraintLayout_gradle_1);
        radioButton_gradle_4 = view.findViewById(R.id.radioButton_gradle4);
        radioButton_gradle_3 = view.findViewById(R.id.radioButton_gradle3);
        radioButton_gradle_2 = view.findViewById(R.id.radioButton_gradle2);
        radioButton_gradle_1 = view.findViewById(R.id.radioButton_gradle1);
        constraintLayout_gradle_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClick_4();
            }
        });
        radioButton_gradle_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClick_4();
            }
        });
        constraintLayout_gradle_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClick_3();
            }
        });
        radioButton_gradle_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClick_3();
            }
        });
        constraintLayout_gradle_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClick_2();
            }
        });
        radioButton_gradle_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClick_2();
            }
        });
        constraintLayout_gradle_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClick_1();
            }
        });
        radioButton_gradle_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClick_1();
            }
        });

        //番茄钟页面
        radioButton_pomodoro_countdown_1_1 = view.findViewById(R.id.pomodoro_countdown_1_1);
        radioButton_pomodoro_PositiveTiming_1_2 = view.findViewById(R.id.pomodoro_PositiveTiming_1_2);
        radioButton_pomodoro_NoTimers_1_3 = view.findViewById(R.id.pomodoro_NoTimers_1_3);
        radioButton_pomodoro_25_minutes_2_1 = view.findViewById(R.id.pomodoro_25_minutes_2_1);
        radioButton_pomodoro_35_minutes_2_2 = view.findViewById(R.id.pomodoro_35_minutes_2_2);
        radioButton_pomodoro_free_minutes_2_3 = view.findViewById(R.id.pomodoro_free_minutes_2_3);
        RadioGroup radioGroup_111 = view.findViewById(R.id.RadioGroup_111);
        RadioGroup radioGroup_222 = view.findViewById(R.id.RadioGroup_222);
        radioGroup_111.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.pomodoro_countdown_1_1) {
                    radioButton_pomodoro_25_minutes_2_1.setEnabled(true);
                    radioButton_pomodoro_35_minutes_2_2.setEnabled(true);
                    radioButton_pomodoro_free_minutes_2_3.setEnabled(true);
                } else {
                    radioButton_pomodoro_25_minutes_2_1.setEnabled(false);
                    radioButton_pomodoro_35_minutes_2_2.setEnabled(false);
                    radioButton_pomodoro_free_minutes_2_3.setEnabled(false);
                }
            }
        });
        radioGroup_222.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.pomodoro_free_minutes_2_3) {
                    showCustomDialog();
                }
            }
        });
        //进行添加操作
        imageButton_AddToDoFragment_save = view.findViewById(R.id.imageButton_AddToDoFragment_save);
        imageButton_AddToDoFragment_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!editText_AddToDoFragment_toDoThingName.getText().toString().isEmpty()) {
                    String userId = userBaseMessageEventBus.getUserId();
                    Log.d(TAG, editText_AddToDoFragment_toDoThingName.getText().toString()+
                            getGrade() + "," + getPomodoroInformation()+
                            "pending"+userId+ textView_AddToDoFragment_toDoThingTime.getText().toString());
                    myBottomSheetDialogFragmentPresenter.addToDoThing(editText_AddToDoFragment_toDoThingName.getText().toString(),
                            getGrade() + "," + getPomodoroInformation(),
                            "pending",userId, textView_AddToDoFragment_toDoThingTime.getText().toString());

                    dismiss();
                } else {
                    Toast.makeText(getContext(), "事情的名称不能为空", Toast.LENGTH_SHORT).show();
                }
            }
        });
        return view;
    }

    public void showCustomDialog() {
        Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.dialog_custom_countdown);
        Button dialog_custom_countdown_button = dialog.findViewById(R.id.dialog_custom_countdown_button);
        EditText dialog_custom_countdown_textView = dialog.findViewById(R.id.dialog_custom_countdown_textView);
        dialog_custom_countdown_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str = dialog_custom_countdown_textView.getText().toString();
                if (str != null && str.matches("\\d+") && Integer.parseInt(str) <= 180 && Integer.parseInt(str) >= 10) {
                    radioButton_pomodoro_free_minutes_2_3.setText(str + "分钟");
                    dialog.dismiss();
                } else {
                    Toast.makeText(getContext(), "输入的只能是数字，且小于180分钟，大于等于10分钟", Toast.LENGTH_SHORT).show();
                }
            }
        });
        dialog.show();
    }

    public void onClick_4() {
        radioButton_gradle_4.setChecked(true);
        radioButton_gradle_3.setChecked(false);
        radioButton_gradle_2.setChecked(false);
        radioButton_gradle_1.setChecked(false);
    }
    public void onClick_3() {
        radioButton_gradle_3.setChecked(true);
        radioButton_gradle_4.setChecked(false);
        radioButton_gradle_2.setChecked(false);
        radioButton_gradle_1.setChecked(false);
    }
    public void onClick_2() {
        radioButton_gradle_2.setChecked(true);
        radioButton_gradle_3.setChecked(false);
        radioButton_gradle_4.setChecked(false);
        radioButton_gradle_1.setChecked(false);
    }
    public void onClick_1() {
        radioButton_gradle_1.setChecked(true);
        radioButton_gradle_3.setChecked(false);
        radioButton_gradle_2.setChecked(false);
        radioButton_gradle_4.setChecked(false);
    }

    public void sendToast(String message) {
        Log.d(TAG, message);
        if (getActivity() != null) {
            getActivity().runOnUiThread(() -> {
                Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
            });
        }
    }
    @Subscribe(threadMode = ThreadMode.POSTING, sticky = true)
    public void onMoonStickyEvent(UserBaseMessageEventBus userBaseMessageEventBus) {
        this.userBaseMessageEventBus = userBaseMessageEventBus;
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }
    public String getGrade() {
        if (radioButton_gradle_4.isChecked()) {
            return "4级";
        } else if (radioButton_gradle_3.isChecked()) {
            return "3级";
        } else if (radioButton_gradle_2.isChecked()) {
            return "2级";
        } else if (radioButton_gradle_1.isChecked()) {
            return "1级";
        } else {
            return "0级";
        }
    }
    public String getPomodoroInformation() {
        int checkedId = RadioGroup_111.getCheckedRadioButtonId();
        if (checkedId == R.id.pomodoro_countdown_1_1) {
            int time = RadioGroup_222.getCheckedRadioButtonId();
            if (time == R.id.pomodoro_25_minutes_2_1) {
                return "倒计时,25";
            } else if (time == R.id.pomodoro_35_minutes_2_2) {
                return "倒计时,35";
            } else {
                if (radioButton_pomodoro_free_minutes_2_3.getText().toString().equals("自定义")) {
                    return "倒计时,-1";
                } else {
                    StringBuilder number = new StringBuilder();
                    String input = radioButton_pomodoro_free_minutes_2_3.getText().toString();
                    for (char c : input.toCharArray()) {
                        if (Character.isDigit(c)) {
                            number.append(c);
                        }
                    }
                    Log.d(TAG, "倒计时," + number.toString());
                    return "倒计时," + number.toString();
                }
            }
        } else if (checkedId == R.id.pomodoro_PositiveTiming_1_2) {
            return "正向计时, -1";
        } else if (checkedId == R.id.pomodoro_NoTimers_1_3) {
            return "不计时";
        } else {
            return "不计时";
        }
    }
}