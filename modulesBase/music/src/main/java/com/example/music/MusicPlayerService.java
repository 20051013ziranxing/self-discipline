package com.example.music;

import android.media.MediaPlayer;
import android.util.Log;

public class MusicPlayerService implements IMusicPlayerService {
    private MediaPlayer mediaPlayer;

    @Override
    public void play(String musicUrl) {
        if (mediaPlayer == null) {
            /*mediaPlayer = MediaPlayer.create(context, Uri.parse(musicUrl));*/
        }
        if (!mediaPlayer.isPlaying()) {
            mediaPlayer.start();
            Log.d("MusicPlayerService", "Playing music: " + musicUrl);
        }
    }

    @Override
    public void pause() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            Log.d("MusicPlayerService", "Music paused");
        }
    }

    @Override
    public void stop() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
            Log.d("MusicPlayerService", "Music stopped");
        }
    }

    @Override
    public boolean isPlaying() {
        return mediaPlayer != null && mediaPlayer.isPlaying();
    }
}
