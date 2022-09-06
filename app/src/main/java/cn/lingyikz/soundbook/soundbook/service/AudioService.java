package cn.lingyikz.soundbook.soundbook.service;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import java.util.HashMap;
import java.util.Map;

import cn.lingyikz.soundbook.soundbook.api.RequestService;
import cn.lingyikz.soundbook.soundbook.home.activity.PlayAudioActivity;
import cn.lingyikz.soundbook.soundbook.modle.XmlyNextPaly;
import cn.lingyikz.soundbook.soundbook.utils.Constans;
import cn.lingyikz.soundbook.soundbook.utils.DataBaseHelper;
import cn.lingyikz.soundbook.soundbook.utils.MediaPlayer;
import cn.lingyikz.soundbook.soundbook.utils.SharedPreferences;
import cn.lingyikz.soundbook.soundbook.utils.SuperMediaPlayer;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class AudioService extends Service  {


    private DataBaseHelper dataBaseHelper ;
    private Bundle bundle ;
    private long count = 0 ;
    private SuperMediaPlayer superMediaPlayer ;
    private IntentFilter intentFilter;
    private PlayAudioActivity.PlaystateReceiver myBroadcastReceiver;
    public AudioService() {

    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.i("TAG","MyBinder");

        // TODO: Return the communication channel to the service.
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
//        Log.i("TAG","onCreate");
        superMediaPlayer = SuperMediaPlayer.getInstance();
        intentFilter = new IntentFilter();
        intentFilter.addAction(Constans.CHANGE_PLAY_IMG);
        myBroadcastReceiver = new PlayAudioActivity.PlaystateReceiver();
        registerReceiver(myBroadcastReceiver, intentFilter);
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

//        Log.i("TAG","onStartCommand");
        if(intent.getAction().equals(Constans.SET_BLOCK)){
            int index = intent.getIntExtra("index",-1);
            bundle = intent.getExtras();
//            Log.i("TAG","service"+index);
            hadler.removeCallbacksAndMessages(null);
            switch (index){
                case 0 :
                    count = 30 * 60 * 1000;
                    hadler.sendEmptyMessage(Constans.UPDATE_BLOCK);
                    break;
                case 1 :
                    count = 60 * 60 * 1000;
                    hadler.sendEmptyMessage(Constans.UPDATE_BLOCK);
                    break;
                case 2 :
                    count = 90 * 60 * 1000;
                    hadler.sendEmptyMessage(Constans.UPDATE_BLOCK);
                    break;
                case 3 :
                    count = 120 * 60 * 1000;
                    hadler.sendEmptyMessage(Constans.UPDATE_BLOCK);
                    break;
                case 4 :
                    count = 150 * 60 * 1000;
                    hadler.sendEmptyMessage(Constans.UPDATE_BLOCK);
                    break;
                default:
                    break;

            }
        }
//        if(intent.getAction().equals(Constans.BIND_SERVICE)){
//            Log.i("TAG","BIND_SERVICE");
//            bundle = intent.getExtras();
//            if(player.isPlay()){
//                Bundle oldAudioInfo = SharedPreferences.getOldAudioInfo(this);
//                if(oldAudioInfo.getString("src" ).equals(bundle.getString("src" ))){
//
//                }else {
//                    player.onStop();
//                    //保存上一条信息 todo
//                    addHistory2(oldAudioInfo);
//                    player.reset();
//                    if(bundle.getLong("audioDuration") > 0){
//                        player.onRead(bundle.getString("src"),bundle.getLong("audioDuration"));
//                    }else{
//                        player.onRead(bundle.getString("src"));
//                    }
//                    SharedPreferences.saveOldAudioInfo(this,bundle);
//                }
//            }else {
//                player.onStop();
//                player.reset();
//                if(bundle.getLong("audioDuration") > 0){
//                    player.onRead(bundle.getString("src"),bundle.getLong("audioDuration"));
//                }else{
//                    player.onRead(bundle.getString("src"));
//                }
//                SharedPreferences.saveOldAudioInfo(this,bundle);
//            }
//        }
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("TAG","onDestroy");
        SuperMediaPlayer.getInstance().stop();
        SuperMediaPlayer.getInstance().reset();
        SuperMediaPlayer.getInstance().release();
        if(myBroadcastReceiver != null){
            unregisterReceiver(myBroadcastReceiver);
        }

    }

    @SuppressLint("HandlerLeak")
    private android.os.Handler hadler = new Handler(Looper.myLooper()) {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Constans.UPDATE_BLOCK:
                    updateBlock();
                    break;
            }
        }


    };
    private void updateBlock() {
//        Log.i("TAG","updateBlock:"+count);
        if(count <= 0){
            hadler.removeCallbacksAndMessages(null);
            count = 0 ;
            if(superMediaPlayer.isPlaying()){
                superMediaPlayer.pause();
//                Log.i("TAG","updateBlock:"+superMediaPlayer.getCurrentPosition());
                bundle.putLong("audioDuration",superMediaPlayer.getCurrentPosition());
                dataBaseHelper = DataBaseHelper.getInstance(getApplication());
                dataBaseHelper.addPlayHistory(bundle);
                dataBaseHelper.close();
                SharedPreferences.saveOldAudioInfo(this,bundle);
                Bundle spBundle = new Bundle();
                spBundle.putString("lable","");
                spBundle.putInt("index",-1);
                SharedPreferences.saveBolckClose(getApplicationContext(),spBundle);
                //发广播给palyaudioactivity切换UI
                Intent intent = new Intent(Constans.CHANGE_PLAY_IMG);
                sendBroadcast(intent); // 发送广播
            }
//            Log.i("TAG","updateBlock结束");
        }else {
            count = count - 1000;
            hadler.sendEmptyMessageDelayed(Constans.UPDATE_BLOCK,1000);
        }

    }
}