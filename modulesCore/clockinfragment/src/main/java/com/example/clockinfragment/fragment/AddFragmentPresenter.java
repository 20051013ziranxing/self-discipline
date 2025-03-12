package com.example.clockinfragment.fragment;

import android.widget.Toast;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.clockinfragment.ClockInFragmentModule;
import com.example.networkrequests.NetworkClient;

import java.io.IOException;
import java.util.Random;

public class AddFragmentPresenter {
    AddFragmentModule addFragmentModule;
    AddFragment addFragment;

    public AddFragmentPresenter(AddFragment addFragment) {
        NetworkClient networkClient = new NetworkClient();
        this.addFragment = addFragment;
        this.addFragmentModule = new AddFragmentModule(networkClient);
    }
    public void changeRandomWordsOfEncouragement() {
        String[] sentences = {
                "梦想就像星辰，即使你永远无法触及，但它依然能引导你前行",
                "你的梦想值得全力以赴，哪怕路途遥远",
                "每一步都算数，每一个努力都不会白费",
                "不要因为眼前的困难而停下脚步，因为坚持下去，就会看到曙光",
                "成功不是终点，努力才是永恒的主题"
        };
        Random random = new Random();
        int index = random.nextInt(sentences.length);
        String randomSentence = sentences[index];
        addFragment.editText_EncouragementWords.setText(randomSentence);
    }

    public void createAPunchInTask(String user_id, String status, String title, String target_checkin_count, String start_time, String end_time) {
        addFragmentModule.createAPunchInTask(user_id, status, title, target_checkin_count, new ClockInFragmentModule.ModelCallback() {
            @Override
            public Boolean onSuccess(String response) {
                addFragment.sendToast("习惯添加成功");
                removeAddFragment();
                return null;
            }

            @Override
            public Boolean onFailure(IOException e) {
                addFragment.sendToast("习惯添加失败");
                return null;
            }
        });
    }

    public void habitsForSavingSettings() {
        if (!addFragment.editText_addFragment_HabitName.getText().toString().isEmpty() &&
                addFragment.editText_EncouragementWords.getText().toString().isEmpty()) {
            //进行保存操作
            removeAddFragment();
        } else {
            addFragment.SendToast("习惯名不可为空");
        }
    }

    public void removeAddFragment () {
        FragmentManager fragmentManager = addFragment.getParentFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.remove(addFragment).commit();
    }
}
