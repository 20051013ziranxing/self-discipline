package com.example.pomodorotechnique;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.example.pomodorotechnique.MusicService;
import com.example.pomodorotechnique.myView.CircleProgressBar;

@Route(path = "/PomodoroTimerActivity/PomodoroTimerActivity")
public class PomodoroTimerActivity extends AppCompatActivity implements SelectTheSongBottomSheetDialogFragment.OnSongSelectedListener{
    private CountDownTimer countDownTimer;
    private long elapsedTime = 0;
    PomodoroTimerActivityPresenter pomodoroTimerActivityPresenter;
    TextView textView_PomodoroGreetings;
    //进行ImageView的切换
    private int[] imageResources = {R.drawable.start_icon, R.drawable.paused_icon};
    private int currentImageIndex = 0;
    private final static String TAG = "TestTT_PomodoroTimerActivity";
    //时间的显示（倒计时与正向计时）
    private TextView timeTextView;
    //计时
    private CircleProgressBar progressCircle;
    //专注的时候的开关
    ImageView imageButton_ASwitchThatControlsTheTiming;
    //专注的时候背景音乐的选择按钮
    ImageButton imageView_TheChoiceOfMusicWhenFocused;
    //跳过当前计时
    ImageButton imageView_SkipTheCurrentTimer;
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
        pomodoroTimerActivityPresenter = new PomodoroTimerActivityPresenter(this);
        imageButton_ASwitchThatControlsTheTiming = findViewById(R.id.imageButton_ASwitchThatControlsTheTiming);
        imageView_TheChoiceOfMusicWhenFocused = findViewById(R.id.imageView_TheChoiceOfMusicWhenFocused);
        imageView_SkipTheCurrentTimer = findViewById(R.id.imageView_SkipTheCurrentTimer);
        imageButton_ASwitchThatControlsTheTiming.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentImageIndex = (currentImageIndex + 1) % imageResources.length;
                Log.d(TAG, String.valueOf(currentImageIndex));
                imageButton_ASwitchThatControlsTheTiming.setImageResource(imageResources[currentImageIndex]);
                Log.d(TAG, "进入了");
                if (currentImageIndex == 1) {
                    Intent intent = new Intent("PLAY");
                    intent.setPackage(getPackageName());
                    sendBroadcast(intent);
                } else {
                    Intent intent = new Intent("PAUSE");
                    intent.setPackage(getPackageName());
                    sendBroadcast(intent);
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
        /*startTimer(25);*/
        startPositiveTimer();
        textView_PomodoroGreetings = findViewById(R.id.textView_PomodoroGreetings);
        pomodoroTimerActivityPresenter.changeRandomWordsOfEncouragement();
    }
    //开始倒计时
    private void startTimer(int min) {
        progressCircle.startCountDown(min * 60000L, 1000); // 5分钟倒计时
        new CountDownTimer(min * 60000L, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                int seconds = (int) (millisUntilFinished / 1000) % 60;
                int minutes = (int) ((millisUntilFinished / 1000) / 60) % 60;
                timeTextView.setText(String.format("%02d:%02d", minutes, seconds));
            }

            @Override
            public void onFinish() {
                timeTextView.setText("00:00");
                progressCircle.cancelCountDown();
            }
        }.start();
    }
    //开始正向计时
    private void startPositiveTimer() {
        elapsedTime = 0;
        countDownTimer = new CountDownTimer(Long.MAX_VALUE, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                elapsedTime += 1000;
                timeTextView.setText(formatTime(elapsedTime));
            }

            @Override
            public void onFinish() {
            }
        }.start();
    }

    private String formatTime(long millis) {
        int seconds = (int) (millis / 1000);
        int hours = seconds / 3600;
        int minutes = (seconds % 3600) / 60;
        int secs = seconds % 60;

        return String.format("%02d:%02d:%02d", hours, minutes, secs);
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
        Intent changeMusicIntent = new Intent(PomodoroTimerActivity.this, MusicService.class);
        changeMusicIntent.setAction("CHANGE_MUSIC");
        int s = Integer.parseInt(songName);
        changeMusicIntent.putExtra("MUSIC_RESOURCE_ID", s);
        startService(changeMusicIntent);
    }
}