package com.example.pomodorotechnique;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.pomodorotechnique.MusicService;

public class MusicControlReceiver extends BroadcastReceiver {
    MusicService musicService;
    private static final String TAG = "TestTT_MusicControlReceiver";
    @Override
    public void onReceive(Context context, Intent intent) {
        musicService = new MusicService();
        String action = intent.getAction();
        if ("PLAY".equals(action)) {
            MusicService.playMusic();
        } else if ("PAUSE".equals(action)) {
            MusicService.pauseMusic();
        } else if ("STOP".equals(action)) {
            MusicService.stopMusic();
        } else if ("CHANGE_MUSIC".equals(action)) {
            Log.d(TAG, "接收到音乐切换了");
            String musicPath = intent.getStringExtra("MUSIC_PATH");
            Log.d(TAG, "接收到的音乐为" + musicPath);
            if (musicPath != null) {
                Log.d(TAG,"执行到该调用切换音乐的方法了");
                musicService.changeMusic(musicPath);
            }
        }
    }
}
