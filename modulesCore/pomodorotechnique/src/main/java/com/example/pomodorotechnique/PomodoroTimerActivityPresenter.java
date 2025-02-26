package com.example.pomodorotechnique;

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
}
