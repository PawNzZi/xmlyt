package cn.lingyikz.soundbook.soundbook.utils;


import android.util.Log;

import java.io.IOException;


public class MediaPlayer {
    private volatile static MediaPlayer mediaPlayer ;
    private static android.media.MediaPlayer player ;
    public static int error = 0 ;


    private MediaPlayer(){
        if(player == null){
            player = new android.media.MediaPlayer();

        }
    }

    public static MediaPlayer getInstance() {

        if(mediaPlayer == null){
            synchronized (MediaPlayer.class){
                if(mediaPlayer == null){
                    mediaPlayer = new MediaPlayer();
                }
            }
        }
        return mediaPlayer;
    }

    public void onRead(String src){
//        Log.i("TAG",src);
        try {

            player.setDataSource(src);
            player.prepareAsync();

            player.setOnPreparedListener(new android.media.MediaPlayer.OnPreparedListener() {

                @Override
                public void onPrepared(android.media.MediaPlayer mediaPlayer) {
                    player.start();
                }
            });
            player.setOnErrorListener(new android.media.MediaPlayer.OnErrorListener() {
                @Override
                public boolean onError(android.media.MediaPlayer mediaPlayer, int i, int i1) {
                    Log.i("TAG","cuowu");
                    error = 1;
                    return false;
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public void onRead(String src,long currentPosition){
//        Log.i("TAG",src);
        try {

            player.setDataSource(src);
            player.prepare();
            player.seekTo((int) currentPosition);
            player.setOnSeekCompleteListener(new android.media.MediaPlayer.OnSeekCompleteListener() {
                @Override
                public void onSeekComplete(android.media.MediaPlayer mediaPlayer) {
//                    Log.i("TAG","onSeekComplete");
                    mediaPlayer.start();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void onPlay(){
        player.start();
    }
    public void onPause(){
        player.pause();
    }

    public void onStop(){
        player.stop();

    }
    public android.media.MediaPlayer getPlayer(){
        return player;
    }
    public boolean isPlay(){
        return player.isPlaying();
    }
    public void reset(){
        player.reset();
    }
    public void freeMediaPlayer(){
        player.release();
    }
    public long getCurrentPosition(){
//        Log.i("TAG","getCurrentPosition:"+player.getCurrentPosition());
        return player.getCurrentPosition();
    }
    public void setCurrentPosition(int currentPosition){
        player.seekTo(currentPosition);
    }

    public int getDuration(){
        return player.getDuration();
    }

}
