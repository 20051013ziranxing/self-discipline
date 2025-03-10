package com.example.pomodorotechnique;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.example.pomodorotechnique.MusicService;
import com.example.pomodorotechnique.myView.CircleProgressBar;
import com.example.pomodorotechnique.myView.ReStartDialog;

@Route(path = "/PomodoroTimerActivity/PomodoroTimerActivity")
public class PomodoroTimerActivity extends AppCompatActivity implements
        SelectTheSongBottomSheetDialogFragment.OnSongSelectedListener,
        ReStartDialog.ReStartDialogListener{
    @Autowired(name = "pomodoro")
    public String pomodoro_requirements;
    String[] messagePomodoro;
    RadioButton radioButton_pomodoro_free_minutes_2_3;
    private static final int COUNTDOWN = 0;
    private static final int FORWARD_TIMING = 1;
    /*private static final int TIMING = 1;*/
    private static final int REST = 2;
    public int state;
    public CountDownTimer countDownTimer;
    public long elapsedTime = 0;
    PomodoroTimerActivityPresenter pomodoroTimerActivityPresenter;
    TextView textView_PomodoroGreetings;
    //进行ImageView的切换
    private int[] imageResources = {R.drawable.start_icon, R.drawable.paused_icon};
    private int currentImageIndex = 0;
    private final static String TAG = "TestTT_PomodoroTimerActivity";
    //时间的显示（倒计时与正向计时）
    public TextView timeTextView;
    RadioGroup RadioGroup_TimingSelection;
    //计时
    public CircleProgressBar progressCircle;
    //专注的时候的开关
    ImageView imageButton_ASwitchThatControlsTheTiming;
    //专注的时候背景音乐的选择按钮
    ImageButton imageView_TheChoiceOfMusicWhenFocused;
    //跳过当前计时
    ImageButton imageView_SkipTheCurrentTimer;
    RadioButton radioButton_countdown_11;
    RadioButton radioButton_forwardTiming_11;
    TextView textView_StatusInformation_focus_or_rest;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_pomodoro_timer);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        state = 3;
        ARouter.getInstance().inject(this);
        Log.d(TAG, "pomodoro_requirements" + pomodoro_requirements);
        /*pomodoro_requirements = "倒计时,25";*/
        /*pomodoro_requirements = "正向计时,-1";*/
        /*pomodoro_requirements = "倒计时,-1";*/
        messagePomodoro = pomodoro_requirements.split(",");
        for (String s : messagePomodoro) {
            Log.d(TAG, s);
        }
        pomodoroTimerActivityPresenter = new PomodoroTimerActivityPresenter(this);
        imageButton_ASwitchThatControlsTheTiming = findViewById(R.id.imageButton_ASwitchThatControlsTheTiming);
        imageView_TheChoiceOfMusicWhenFocused = findViewById(R.id.imageView_TheChoiceOfMusicWhenFocused);
        imageView_SkipTheCurrentTimer = findViewById(R.id.imageView_SkipTheCurrentTimer);
        RadioGroup_TimingSelection = findViewById(R.id.RadioGroup_TimingSelection);
        radioButton_countdown_11 = findViewById(R.id.radioButton_countdown_11);
        textView_StatusInformation_focus_or_rest = findViewById(R.id.textView_StatusInformation_focus_or_rest);
        radioButton_forwardTiming_11 = findViewById(R.id.radioButton_forwardTiming_11);
        if (messagePomodoro[0].equals("倒计时")) {
            radioButton_countdown_11.setChecked(true);
        } else {
            radioButton_forwardTiming_11.setChecked(true);
        }
        RadioGroup_TimingSelection.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.radioButton_countdown_11) {
                    state = COUNTDOWN;
                } else if (checkedId == R.id.radioButton_forwardTiming_11) {
                    state = FORWARD_TIMING;
                }
            }
        });
        imageView_SkipTheCurrentTimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (state == COUNTDOWN) {
                    ReStartDialog reStartDialog = new ReStartDialog(PomodoroTimerActivity.this);
                    reStartDialog.setOnDialogReStartDialogListener((ReStartDialog.ReStartDialogListener) PomodoroTimerActivity.this);
                    reStartDialog.show();
                    reStartDialog.textView_finishTheTimingAheadOfSchedule.setText("提前完成计时");
                } else if (state == FORWARD_TIMING) {
                    ReStartDialog reStartDialog = new ReStartDialog(PomodoroTimerActivity.this);
                    reStartDialog.setOnDialogReStartDialogListener((ReStartDialog.ReStartDialogListener) PomodoroTimerActivity.this);
                    reStartDialog.show();
                    reStartDialog.textView_finishTheTimingAheadOfSchedule.setText("结束本次正向计时");
                    countDownTimer.cancel();
                }  else if (state == REST) {
                    ReStartDialog reStartDialog = new ReStartDialog(PomodoroTimerActivity.this);
                    reStartDialog.setOnDialogReStartDialogListener((ReStartDialog.ReStartDialogListener) PomodoroTimerActivity.this);
                    reStartDialog.show();
                    reStartDialog.textView_finishTheTimingAheadOfSchedule.setVisibility(View.GONE);
                    reStartDialog.textView_discardTheCurrentTimer.setText("跳过当前休息计时");
                } else {
                    ;
                }
            }
        });
        imageButton_ASwitchThatControlsTheTiming.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentImageIndex = (currentImageIndex + 1) % imageResources.length;
                Log.d(TAG, String.valueOf(currentImageIndex));
                Log.d(TAG, "进入了");
                if (currentImageIndex == 1) {
                    if (messagePomodoro[0].equals("倒计时") && Integer.parseInt(messagePomodoro[1]) >= 10) {
                        pomodoroTimerActivityPresenter.startTimer(Integer.parseInt(messagePomodoro[1]));
                        state = COUNTDOWN;
                        imageButton_ASwitchThatControlsTheTiming.setImageResource(imageResources[1]);
                    } else if (messagePomodoro[0].equals("正向计时") && Integer.parseInt(messagePomodoro[1]) == -1) {
                        pomodoroTimerActivityPresenter.startPositiveTimer();
                        state = FORWARD_TIMING;
                        imageButton_ASwitchThatControlsTheTiming.setImageResource(imageResources[1]);
                    } else if (messagePomodoro[0].equals("倒计时") && Integer.parseInt(messagePomodoro[1]) == -1) {
                        showCustomDialog();
                    } else {
                        radioButton_forwardTiming_11.setChecked(true);
                    }
                   /* Intent intent = new Intent("PLAY");
                    intent.setPackage(getPackageName());
                    sendBroadcast(intent);*/
                } else {

                }
            }
        });
        imageView_TheChoiceOfMusicWhenFocused.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectTheSongBottomSheetDialogFragment selectTheSongBottomSheetDialogFragment = new SelectTheSongBottomSheetDialogFragment();
                selectTheSongBottomSheetDialogFragment.show(getSupportFragmentManager(), "SelectTheSongBottomSheetDialogFragment");
            }
        });
        timeTextView = findViewById(R.id.timeTextView_TheTimeOfTheTimer);
        progressCircle = findViewById(R.id.progressCircle);
        textView_PomodoroGreetings = findViewById(R.id.textView_PomodoroGreetings);
        pomodoroTimerActivityPresenter.changeRandomWordsOfEncouragement();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        progressCircle.cancelCountDown();
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }

    @Override
    public void onSongSelected(String songName) {
        if (state == COUNTDOWN || state == FORWARD_TIMING) {
            Intent changeMusicIntent = new Intent(PomodoroTimerActivity.this, MusicService.class);
            changeMusicIntent.setAction("CHANGE_MUSIC");
            int s = Integer.parseInt(songName);
            changeMusicIntent.putExtra("MUSIC_RESOURCE_ID", s);
            startService(changeMusicIntent);
        } else {

        }
    }

    public void stopMusic() {
        Intent intent = new Intent("PAUSE");
        intent.setPackage(getPackageName());
        sendBroadcast(intent);
    }

    @Override
    public void textViewDiscardTheCurrentTimer() {
        finish();
    }

    @Override
    public void textViewFinishTheTimingAheadOfSchedule() {
        pomodoroTimerActivityPresenter.startTimer(5);
        textView_StatusInformation_focus_or_rest.setText("休息中");
        state = REST;
    }

    public void showCustomDialog() {
        Dialog dialog = new Dialog(PomodoroTimerActivity.this);
        dialog.setContentView(R.layout.dialog_custom_countdown);
        Button dialog_custom_countdown_button = dialog.findViewById(R.id.dialog_custom_countdown_button);
        EditText dialog_custom_countdown_textView = dialog.findViewById(R.id.dialog_custom_countdown_textView);
        dialog_custom_countdown_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str = dialog_custom_countdown_textView.getText().toString();
                if (str != null && str.matches("\\d+") && Integer.parseInt(str) <= 180 && Integer.parseInt(str) >= 10) {
                    messagePomodoro[1] = str;
                    timeTextView.setText(str+":00");
                    dialog.dismiss();
                } else {
                    Toast.makeText(PomodoroTimerActivity.this, "输入的只能是数字，且小于180分钟，大于10分钟", Toast.LENGTH_SHORT).show();
                }
            }
        });
        dialog.show();
    }
}