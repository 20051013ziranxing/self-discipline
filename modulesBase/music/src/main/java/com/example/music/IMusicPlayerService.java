package com.example.music;

public interface IMusicPlayerService {
    void play(String musicUrl);  // 播放音乐
    void pause();               // 暂停播放
    void stop();                // 停止播放
    boolean isPlaying();        // 检查是否正在播放
}
