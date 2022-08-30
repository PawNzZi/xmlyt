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
        Log.i("TAG",bundle+"");
        binding.titleBar.title.setText(bundle.getString("title"));
        binding.seekbar.setOnSeekBarChangeListener(this);
        dataBaseHelper = DataBaseHelper.getInstance(this);
        Intent intent = new Intent(this, AudioService.class);
        conn = new MyConnection();
        intent.setAction(Constans.BIND_SERVICE);
        Bundle historyBundle = dataBaseHelper.queryPlayHistory(bundle.getInt("albumId"),bundle.getInt("audioId"));
        dataBaseHelper.close();
        if(historyBundle.getString("audioDuration") == null){
            Log.i("TAG","audioDuration == null");
            bundle.putLong("audioDuration",0);
        }else {
            Log.i("TAG","audioDuration != null");
            Log.i("TAG","audioDuration != null"+historyBundle.getString("audioDuration"));
            bundle.putLong("audioDuration",Long.parseLong(historyBundle.getString("audioDuration")));
        }
        intent.putExtras(bundle);
        startService(intent);
        bindService(intent, conn, BIND_AUTO_CREATE);

    }

    /**
     * 启动服务播放
     */
    private void startService(){


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
        handler.sendEmptyMessageDelayed(UPDATE_PROGRESS, 500);
    }
    @Override
    protected void onResume() {
        super.onResume();
        //进入到界面后开始更新进度条
//        Log.i("TAG","onResume");

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

    private class MyConnection  implements ServiceConnection {

        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            myBinder = (AudioService.MyBinder) iBinder;
            Log.i("TAG","onServiceConnected:"+myBinder.isPlaying());
            binding.seekbar.setMax(myBinder.getDuration());
            Log.i("TAG","getDuration:"+myBinder.getDuration());
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
