package cn.lingyikz.soundbook.soundbook.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import java.io.IOException;
import java.util.Map;

import cn.lingyikz.soundbook.soundbook.home.activity.AudioDetailActivity;
import cn.lingyikz.soundbook.soundbook.utils.MediaPlayer;
import cn.lingyikz.soundbook.soundbook.utils.SharedPreferences;

public class AudioService extends Service implements MediaPlayer.AudioStateLinstener {

    private MediaPlayer player ;


    public AudioService() {

    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return new AudioService.MyBinder();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i("TAG","onCreate");
        player = MediaPlayer.getInstance();

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("TAG","onStartCommand");

        if(player.isPlay()){
            if(intent.getBooleanExtra("continue",false)){
                player.onPause();
                saveCurrentPosition();

            }else {
                player.onStop();
                player.reset();
                player.onRead(intent.getStringExtra("path"));
            }
        }else{
            if(intent.getBooleanExtra("continue",false)){
                Log.i("TAG","onPlay");
//                player.onPlay();
                player.onStop();
                player.reset();
                if(getCurrentPosition() != 0){
                    player.onRead(intent.getStringExtra("path"),getCurrentPosition());
                }else{
                    player.onRead(intent.getStringExtra("path"));
                }

            }else {
                player.onStop();
                player.reset();
                player.onRead(intent.getStringExtra("path"));
            }
        }

//        player.onPlay();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("TAG","onDestroy");
        player.onPause();
        saveCurrentPosition();
        player.onStop();
        player.reset();
        player.freeMediaPlayer();

    }

    @Override
    public void onStopLinstener() {

    }

    @Override
    public void onPauserLinstener() {

    }

    @Override
    public void onCompleteLinstener() {
        //播放结束，自动播放下一集
    }

    public class MyBinder extends Binder {

    }

    public void saveCurrentPosition(){
        Map<String,Object> map = SharedPreferences.getOldAudioInfo(getApplicationContext());
        map.put("currentPosition",player.getCurrentPosition());
        SharedPreferences.saveOldAudioInfo(getApplicationContext(),map);
    }
    public long getCurrentPosition(){
        long currentPosition = 0 ;
        Map<String,Object> map = SharedPreferences.getOldAudioInfo(getApplicationContext());
        if(map.get("currentPosition") != null){
            currentPosition = (long) map.get("currentPosition");
        }

        return currentPosition;
    }


}