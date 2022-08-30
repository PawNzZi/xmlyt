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
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class AudioService extends Service  implements android.media.MediaPlayer.OnCompletionListener {

    private MediaPlayer player ;
    private DataBaseHelper dataBaseHelper ;
    private Bundle bundle ;
    private AudioService.MyBinder myBinder = new MyBinder();

    public AudioService() {

    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return myBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
//        Log.i("TAG","onCreate");
        player = MediaPlayer.getInstance();
        player.getPlayer().setOnCompletionListener(this);

    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {


        Log.i("TAG","onStartCommand");
//        bundle = intent.getExtras();
//        if(player.isPlay()){
//            if(bundle.getBoolean("continuePlay")){
//                 if(bundle.getInt("playModel") == Constans.PLAY_MODLE_INNER){
//                     player.onPause();
//                     addHistory1();
//                     SharedPreferences.saveOldAudioInfo(this,bundle);
//                 }
//
//            }else {
//                player.onStop();
//                //保存上一条信息 todo
//                addHistory2(SharedPreferences.getOldAudioInfo(this));
//                player.reset();
////                player.onRead(bundle.getString("src"));
//                if(bundle.getLong("audioDuration") > 0){
//                    player.onRead(bundle.getString("src"),bundle.getLong("audioDuration"));
//                }else{
//                    player.onRead(bundle.getString("src"));
//                }
//                SharedPreferences.saveOldAudioInfo(this,bundle);
//            }
//        }else{
////            if(bundle.getBoolean("continuePlay")){
////                Log.i("TAG","onPlay");
////                player.onPlay();
//                player.onStop();
//                player.reset();
//                if(bundle.getLong("audioDuration") > 0){
//                    player.onRead(bundle.getString("src"),bundle.getLong("audioDuration"));
//                }else{
//                    player.onRead(bundle.getString("src"));
//                }
//                SharedPreferences.saveOldAudioInfo(this,bundle);
//        }


//        player.onPlay();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("TAG","onDestroy");
        player.onPause();
//        saveCurrentPosition();
        closrService();
        player.onStop();
        player.reset();
        player.freeMediaPlayer();
    }

    @Override
    public void onCompletion(android.media.MediaPlayer mediaPlayer) {


        Log.i("TAG","播放完毕");
//        Log.i("TAG","albumId:"+bundle.getInt("albumId"));
//        Log.i("TAG","播放完毕");
        Observable<XmlyNextPaly> observable  = RequestService.getInstance().getApi().getNextPlay(bundle.getInt("albumId"),bundle.getInt("episodes") + 1);
        observable.subscribeOn(Schedulers.io()) // 在子线程中进行Http访问
                .observeOn(AndroidSchedulers.mainThread()) // UI线程处理返回接口
                .subscribe(new Observer<XmlyNextPaly>() { // 订阅

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(XmlyNextPaly xmlyNextPaly) {
                        if(xmlyNextPaly.getCode() == 200 && xmlyNextPaly.getData().size() > 0 && MediaPlayer.error == 0) {
                            Log.i("TAG", xmlyNextPaly.toString() + "");
                            XmlyNextPaly.DataDTO dataDTO = xmlyNextPaly.getData().get(0);
                            Bundle reslutBundle = new Bundle();
                            reslutBundle.putInt("albumId",dataDTO.getAlbumId());
                            reslutBundle.putInt("episodes",dataDTO.getEpisodes());
                            reslutBundle.putString("title",dataDTO.getName());
                            reslutBundle.putString("audioDes","");
                            reslutBundle.putLong("audioDuration",0);
                            reslutBundle.putLong("audioCreated",dataDTO.getCreated());
                            reslutBundle.putString("src",dataDTO.getUrl());
                            reslutBundle.putInt("audioId",dataDTO.getId());
//                            reslutBundle.putInt("totalCount",bundle.getInt("totalCount"));
                            player.onStop();
                            player.reset();
                            player.onRead(dataDTO.getUrl());
                            SharedPreferences.saveOldAudioInfo(getApplication(),bundle);
                            addHistory1();
                            bundle = reslutBundle;
                        }
//                        else {
//                            Toast.makeText(AudioService.this, "播放结束", Toast.LENGTH_SHORT).show();
//                        }

                    }
                });
    }


    public class MyBinder extends Binder {
        //判断是否处于播放状态
        public boolean isPlaying(){
            return player.isPlay();
        }

        public void onStop(){
            player.onStop();
        }
        public void onReset(){
            player.reset();
        }
        public void onRead(String src){
            player.onRead(src);
        }
        public void onRead(String src,long currentPostion){
            player.onRead(src,currentPostion);
        }
        public void onPause(){
            player.onPause();
        }
        public void onStart(){
            player.onPlay();
        }
        //返回歌曲的长度，单位为毫秒
        public int getDuration(){
            return player.getDuration();
        }

        //返回歌曲目前的进度，单位为毫秒
        public long getCurrenPostion(){
            return player.getCurrentPosition();
        }

        //设置歌曲播放的进度，单位为毫秒
        public void seekTo(int mesc){
            player.setCurrentPosition(mesc);
        }
    }


    public void addHistory1(){

        dataBaseHelper = DataBaseHelper.getInstance(this);
        dataBaseHelper.addPlayHistory(bundle);
        dataBaseHelper.close();
    }
    public void addHistory2(Bundle bundle){

        dataBaseHelper = DataBaseHelper.getInstance(this);
        dataBaseHelper.addPlayHistory(bundle);
        dataBaseHelper.close();
    }
    public void closrService(){
        if(player.isPlay()){
            if(bundle.getBoolean("continuePlay")){
                addHistory1();
                SharedPreferences.saveOldAudioInfo(this,bundle);
            }else {
                addHistory2(SharedPreferences.getOldAudioInfo(this));
                SharedPreferences.saveOldAudioInfo(this,bundle);
            }
        }else{
            SharedPreferences.saveOldAudioInfo(this,bundle);
        }
    }

}