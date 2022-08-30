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
import cn.lingyikz.soundbook.soundbook.modle.AlbumCount;
import cn.lingyikz.soundbook.soundbook.modle.XmlyNextPaly;
import cn.lingyikz.soundbook.soundbook.service.AudioService;
import cn.lingyikz.soundbook.soundbook.utils.Constans;
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
//        if(bundle.getInt("playModel") != Constans.PLAY_MODLE_ICON){
//
//        }
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
        conn = new MyConnection();
        bindService(intent, conn, BIND_AUTO_CREATE);
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
        if(myBinder!=null){
            handler.sendEmptyMessage(UPDATE_PROGRESS);
            if(myBinder.isPlaying()){
                binding.startPlay.setImageDrawable(getResources().getDrawable(R.mipmap.activity_start, null));
            }else {
                binding.startPlay.setImageDrawable(getResources().getDrawable(R.mipmap.activity_pause, null));
            }
        }
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
        if (b){
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
                SharedPreferences.saveOldAudioInfo(this,bundle);
                finish();
                break;
            case R.id.startPlay:
                if(myBinder.isPlaying()){
                    myBinder.onPause();
                    bundle.putLong("",myBinder.getCurrenPostion());
                    dataBaseHelper.addPlayHistory(bundle);
                    dataBaseHelper.close();
                    SharedPreferences.saveOldAudioInfo(this,bundle);
                }else {
                    myBinder.onStart();
                }
                startService();
                break;
            default:
                break;
        }
    }
    public void playAudio(){

        Bundle historyBundle = dataBaseHelper.queryPlayHistory(bundle.getInt("albumId"),bundle.getInt("audioId"));
        dataBaseHelper.close();
        Bundle oldAudioInfo = SharedPreferences.getOldAudioInfo(this);

        if(myBinder.isPlaying()){
                if(oldAudioInfo.getString("src" ).equals(bundle.getString("src" ))){

                }else{
                    myBinder.onPause();
                    oldAudioInfo.putLong("audioDuration",myBinder.getCurrenPostion());
                    myBinder.onStop();
                    dataBaseHelper.addPlayHistory(oldAudioInfo);
                    dataBaseHelper.close();

                    myBinder.onReset();
                    if(historyBundle.getLong("audioDuration") > 0){
                        myBinder.onRead(bundle.getString("src"),historyBundle.getLong("audioDuration"));
                    }else{
                        myBinder.onRead(bundle.getString("src"));
                    }
                    SharedPreferences.saveOldAudioInfo(this,bundle);
                }
        }else{
            //没有播放，拿到刚刚bundle中的数据进行播放
            myBinder.onStop();
            myBinder.onReset();
            if(historyBundle.getLong("audioDuration") > 0){
                myBinder.onRead(bundle.getString("src"),historyBundle.getLong("audioDuration"));
            }else{
                myBinder.onRead(bundle.getString("src"));
            }
            SharedPreferences.saveOldAudioInfo(this,bundle);
        }
    }

    private class MyConnection  implements ServiceConnection {

        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            myBinder = (AudioService.MyBinder) iBinder;
            binding.seekbar.setMax(myBinder.getDuration());
            //设置进度条的进度
            binding.seekbar.setProgress((int) myBinder.getCurrenPostion());
            if(bundle.getInt("playModel") != Constans.PLAY_MODLE_ICON){
                playAudio();
            }
            myBinder.setBundle(bundle);

        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {

        }
    }
}
