package cn.lingyikz.soundbook.soundbook.service;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Binder;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import java.util.HashMap;
import java.util.Map;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import cn.lingyikz.soundbook.soundbook.R;
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
    private Notification.Builder builder ;
    private NotificationManager manager ;
    private static final String CHANNEL_ID = "AudioService";
    public AudioService() {

    }

    @Override
    public IBinder onBind(Intent intent) {
//        Log.i("TAG","MyBinder");
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
        startForeground(1,getNotificationBuilder().build());
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
        return START_STICKY;
    }

    @SuppressLint("WrongConstant")
    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("TAG","onDestroy");
        stopForeground(1);
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
        bundle = null ;

    }
    private Notification.Builder getNotificationBuilder(){

        if(builder == null){
            builder = new Notification.Builder(this,CHANNEL_ID);
            builder.setContentTitle("后台播放服务");//标题
            builder.setContentText("运行中...");//内容
            builder.setWhen(System.currentTimeMillis());
            builder.setSmallIcon(R.mipmap.logo_round);//小图标一定需要设置,否则会报错(如果不设置它启动服务前台化不会报错,但是你会发现这个通知不会启动),如果是普通通知,不设置必然报错
            builder.setLargeIcon(BitmapFactory.decodeResource(getResources(),R.mipmap.logo_round));
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            builder.setChannelId(CHANNEL_ID);
            NotificationChannel Channel = new NotificationChannel(CHANNEL_ID,"后台播放服务",NotificationManager.IMPORTANCE_LOW);
            Channel.enableLights(true);//设置提示灯
            Channel.setLightColor(Color.RED);//设置提示灯颜色
            Channel.setShowBadge(true);//显示logo
            Channel.setDescription("后台播放服务");//设置描述
            Channel.setSound(null, null);
            Channel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC); //设置锁屏可见 VISIBILITY_PUBLIC=可见
            getNotificationManager().createNotificationChannel(Channel);
        }
        return builder;
    }

    private NotificationManager getNotificationManager(){
        if(manager == null){
            manager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        }
        return manager;
    }
}