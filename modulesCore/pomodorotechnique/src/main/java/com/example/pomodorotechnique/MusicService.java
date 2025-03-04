package com.example.pomodorotechnique;

import android.app.Service;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.util.Log;

public class MusicService extends Service {
    private final static String TAG = "TestTT_MusicService";
    private static MediaPlayer mediaPlayer;

    @Override
    public void onCreate() {
        super.onCreate();
        mediaPlayer = MediaPlayer.create(this, R.raw.music1);
        mediaPlayer.setLooping(true);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null && "CHANGE_MUSIC".equals(intent.getAction())) {
            Log.d(TAG, "对应上了");
            int resourceId = intent.getIntExtra("MUSIC_RESOURCE_ID", -1);
            Log.d(TAG, String.valueOf(resourceId));
            if (resourceId != -1) {
                changeMusic(String.valueOf(resourceId));
            }
        } else if ("START_MUSIC".equals(intent.getAction())) {
            mediaPlayer.start();
        } else {

        }
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public static void playMusic() {
        Log.d(TAG, "进入了");
        if (mediaPlayer != null && !mediaPlayer.isPlaying()) {
            Log.d(TAG, "开始了");
            mediaPlayer.start();
        }
    }

    public static void pauseMusic() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
        }
    }

    public static void stopMusic() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
            mediaPlayer.reset();
        }
    }

    public void changeMusic(String filePath) {
        Log.d(TAG, "执行到更改音乐了");
        if (mediaPlayer != null) {
            mediaPlayer.reset();
            try {
                Log.d(TAG, "哈哈哈更改了");
                /*mediaPlayer.setDataSource(this, Uri.withAppendedPath(MediaStore.Audio.Media.INTERNAL_CONTENT_URI, String.valueOf(filePath)));*/
                AssetFileDescriptor afd = getResources().openRawResourceFd(Integer.parseInt(filePath));
                if (afd != null) {
                    mediaPlayer.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
                    afd.close();
                }
                mediaPlayer.prepare();
                mediaPlayer.start();
            } catch (Exception e) {
                Log.d(TAG, e.toString());
                e.printStackTrace();
            }
        }
    }
}