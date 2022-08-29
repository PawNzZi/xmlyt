package cn.lingyikz.soundbook.soundbook.home.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.SeekBar;
import android.widget.Toast;

import com.liys.onclickme.LOnClickMe;
import com.liys.onclickme_annotations.AClick;

import androidx.annotation.Nullable;

import cn.lingyikz.soundbook.soundbook.R;
import cn.lingyikz.soundbook.soundbook.api.RequestService;
import cn.lingyikz.soundbook.soundbook.databinding.ActivityPalyaduioBinding;
import cn.lingyikz.soundbook.soundbook.modle.XmlyNextPaly;
import cn.lingyikz.soundbook.soundbook.service.AudioService;
import cn.lingyikz.soundbook.soundbook.utils.DataBaseHelper;
import cn.lingyikz.soundbook.soundbook.utils.MediaPlayer;
import cn.lingyikz.soundbook.soundbook.utils.SharedPreferences;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class PlayAudioActivity extends Activity implements SeekBar.OnSeekBarChangeListener {

    private ActivityPalyaduioBinding binding ;
    private DataBaseHelper dataBaseHelper;
    private MyConnection conn ;
    private AudioService.MyBinder myBinder ;
    private static final int UPDATE_PROGRESS = 0;
    private Bundle bundle ;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPalyaduioBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        LOnClickMe.init(this,binding.getRoot());
        initData();
    }

    /**
     * 初始化数据
     */
    private void initData() {
        binding.titleBar.goSearch.setVisibility(View.GONE);
        binding.titleBar.goPlay.setVisibility(View.GONE);
        binding.titleBar.goBacK.setVisibility(View.VISIBLE);
        bundle = getIntent().getExtras();
        binding.titleBar.title.setText(bundle.getString("title"));

        binding.seekbar.setOnSeekBarChangeListener(this);

        dataBaseHelper = DataBaseHelper.getInstance(this);
        startService();

    }

    /**
     * 启动服务播放
     */
    private void startService(){

        Intent intent = new Intent(this, AudioService.class);
        long audioDuration = dataBaseHelper.queryPlayHistory(bundle.getInt("albumId"),bundle.getInt("audioId"));
        dataBaseHelper.close();
        Log.i("TAG","episodes:"+bundle.getInt("episodes"));
        Bundle oldAudioInfo =  SharedPreferences.getOldAudioInfo(this);
        if(oldAudioInfo.getString("src" ) != null){
            if(oldAudioInfo.getString("src" ).equals(bundle.getString("src" ))){
//                Log.i("TAG","暂停");
                bundle.putBoolean("continuePlay",true);
            }else{
                //stop并重新播放
//                Log.i("TAG","stop并重新播放");
                bundle.putBoolean("continuePlay",false);
            }
        }else {
            bundle.putBoolean("continuePlay",false);
        }
        bundle.putLong("audioDuration", audioDuration);
        intent.putExtras(bundle);
        conn = new MyConnection();
        startService(intent);
//        bindService(intent, conn, BIND_ADJUST_WITH_ACTIVITY);

        new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    bindService(intent, conn, BIND_AUTO_CREATE);
                }
            },1000);

    }
    public void getPlaySource(int episodes){


        Observable<XmlyNextPaly> observable  = RequestService.getInstance().getApi().getNextPlay(bundle.getInt("albumId"),episodes);
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
                            reslutBundle.putInt("totalCount",bundle.getInt("totalCount"));
//                           dataDTO player.onStop();
//                            player.reset();
//                            player.onRead(dataDTO.getUrl());
                            SharedPreferences.saveOldAudioInfo(getApplication(),bundle);
                            bundle = reslutBundle;
                            startService();
                        }
//                        else {
//                            Toast.makeText(AudioService.this, "播放结束", Toast.LENGTH_SHORT).show();
//                        }

                    }
                });
    }
    //使用handler定时更新进度条
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler(Looper.myLooper()) {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case UPDATE_PROGRESS:
                    updateProgress();
                    break;
            }
        }
    };
    //更新进度条
    private void updateProgress() {
        int currenPostion = (int) myBinder.getCurrenPostion();
//        Log.i("TAG","currenPostion:"+currenPostion);
        binding.seekbar.setProgress(currenPostion);
        //使用Handler每500毫秒更新一次进度条
        handler.sendEmptyMessageDelayed(UPDATE_PROGRESS, 300);
    }
    @Override
    protected void onResume() {
        super.onResume();
        //进入到界面后开始更新进度条
        Log.i("TAG","onResume");

    }
    @Override
    protected void onStop() {
        super.onStop();
        //停止更新进度条的进度
        handler.removeCallbacksAndMessages(null);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(conn);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
//        Log.i("TAG","onProgressChanged");
//        Log.i("TAG","i:"+i);
        if (b){
//            Log.i("TAG","i:"+i);
            myBinder.seekTo(i);
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    @AClick({R.id.go_bacK, R.id.prePlay, R.id.startPlay, R.id.nextPlay})
    public void click(View view) {
        switch (view.getId()) {
            case R.id.go_bacK:
                finish();
                break;
            case R.id.prePlay:
                int episodes = bundle.getInt("episodes") - 1;
                if(episodes > 0){
                    getPlaySource(episodes);
//                    startService();
                }else {
                    Toast.makeText(this, "已经是第一集了", Toast.LENGTH_SHORT).show();
                }

                break;
            case R.id.startPlay:
                startService();
                break;
            case R.id.nextPlay:
                int totalCount = bundle.getInt("totalCount");
                int episodes1 = bundle.getInt("episodes") + 1;
                if(totalCount != 0 && episodes1 <= totalCount){
                    getPlaySource(episodes1);

                }else {
                    Toast.makeText(this, "已经是最后一集了", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private class MyConnection  implements ServiceConnection {

        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            myBinder = (AudioService.MyBinder) iBinder;
//            Log.i("TAG","getDuration:"+myBinder.getDuration());
            binding.seekbar.setMax(myBinder.getDuration());
            //设置进度条的进度
            binding.seekbar.setProgress((int) myBinder.getCurrenPostion());
            handler.sendEmptyMessage(UPDATE_PROGRESS);
            if(myBinder.isPlaying()){
                binding.startPlay.setImageDrawable(getResources().getDrawable(R.mipmap.activity_start, null));
            }else {
                binding.startPlay.setImageDrawable(getResources().getDrawable(R.mipmap.activity_pause, null));
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {

        }
    }
}
