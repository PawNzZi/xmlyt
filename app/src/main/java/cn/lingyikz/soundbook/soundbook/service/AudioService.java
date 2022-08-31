package cn.lingyikz.soundbook.soundbook.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import java.util.HashMap;
import java.util.Map;
import cn.lingyikz.soundbook.soundbook.api.RequestService;
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
        SuperMediaPlayer.getInstance();
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Log.i("TAG","onStartCommand");
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
    }
}