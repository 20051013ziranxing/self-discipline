package com.example.pomodorotechnique;

import android.content.Intent;
import android.os.CountDownTimer;
import android.view.View;

import java.util.Random;

public class PomodoroTimerActivityPresenter {
    PomodoroTimerActivity pomodoroTimerActivity;
    PomodoroTimerActivityModule pomodoroTimerActivityModule;

    public PomodoroTimerActivityPresenter(PomodoroTimerActivity pomodoroTimerActivity) {
        this.pomodoroTimerActivity = pomodoroTimerActivity;
        this.pomodoroTimerActivityModule = new PomodoroTimerActivityModule();
    }
    public void changeRandomWordsOfEncouragement() {
        String[] sentences = {
                "梦想就像星辰，即使你永远无法触及，但它依然能引导你前行",
                "你的梦想值得全力以赴，哪怕路途遥远",
                "每一步都算数，每一个努力都不会白费",
                "不要因为眼前的困难而停下脚步，因为坚持下去，就会看到曙光",
                "成功不是终点，努力才是永恒的主题",
                "每天反复做的事情造就了我们，然后你会发现，优秀不是一种行为，而是一种习惯"
        };
        Random random = new Random();
        int index = random.nextInt(sentences.length);
        String randomSentence = sentences[index];
        pomodoroTimerActivity.textView_PomodoroGreetings.setText(randomSentence);
    }

    public void startTimer(int min) {
        pomodoroTimerActivity.progressCircle.startCountDown(min * 60000L, 1000);
        new CountDownTimer(min * 60000L, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                pomodoroTimerActivity.RadioGroup_TimingSelection.setVisibility(View.GONE);
                int seconds = (int) (millisUntilFinished / 1000) % 60;
                int minutes = (int) ((millisUntilFinished / 1000) / 60) % 60;
                pomodoroTimerActivity.timeTextView.setText(String.format("%02d:%02d", minutes, seconds));
            }

            @Override
            public void onFinish() {
                pomodoroTimerActivity.RadioGroup_TimingSelection.setVisibility(View.VISIBLE);
                pomodoroTimerActivity.timeTextView.setText("00:00");
                pomodoroTimerActivity.progressCircle.cancelCountDown();
                if (pomodoroTimerActivity.textView_StatusInformation_focus_or_rest.getText().toString().equals("休息中")) {
                    pomodoroTimerActivity.finish();
                }
            }
        }.start();
    }

    private void startPositiveTimer() {
        pomodoroTimerActivity.elapsedTime = 0;
        pomodoroTimerActivity.countDownTimer = new CountDownTimer(Long.MAX_VALUE, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                pomodoroTimerActivity.RadioGroup_TimingSelection.setVisibility(View.GONE);
                pomodoroTimerActivity.elapsedTime += 1000;
                pomodoroTimerActivity.timeTextView.setText(formatTime(pomodoroTimerActivity.elapsedTime));
            }

            @Override
            public void onFinish() {
                pomodoroTimerActivity.RadioGroup_TimingSelection.setVisibility(View.VISIBLE);
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
}
