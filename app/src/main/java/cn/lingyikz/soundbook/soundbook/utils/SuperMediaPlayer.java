package cn.lingyikz.soundbook.soundbook.utils;

import android.media.MediaPlayer;

public class SuperMediaPlayer extends MediaPlayer {

    private volatile static SuperMediaPlayer superMediaPlayer ;

    public static int error = 0 ;

    public static SuperMediaPlayer getInstance() {

        if(superMediaPlayer == null){
            synchronized (SuperMediaPlayer.class){
                if(superMediaPlayer == null){
                    superMediaPlayer = new SuperMediaPlayer();
                }
            }
        }
        return superMediaPlayer;
    }
}
