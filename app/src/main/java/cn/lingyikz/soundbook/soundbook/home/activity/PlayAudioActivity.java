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

import com.liys.onclickme.LOnClickMe;

import androidx.annotation.Nullable;
import cn.lingyikz.soundbook.soundbook.databinding.ActivityPalyaduioBinding;
import cn.lingyikz.soundbook.soundbook.service.AudioService;
import cn.lingyikz.soundbook.soundbook.utils.DataBaseHelper;
import cn.lingyikz.soundbook.soundbook.utils.SharedPreferences;

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
    private void startService(){
        Intent intent = new Intent(this, AudioService.class);
        long audioDuration = dataBaseHelper.queryPlayHistory(bundle.getInt("albumId"),bundle.getInt("audioId"));
        dataBaseHelper.close();

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
        },2000);
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

    private class MyConnection  implements ServiceConnection {

        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            myBinder = (AudioService.MyBinder) iBinder;
//            Log.i("TAG","getDuration:"+myBinder.getDuration());
            binding.seekbar.setMax(myBinder.getDuration());
            //设置进度条的进度
            binding.seekbar.setProgress((int) myBinder.getCurrenPostion());
            handler.sendEmptyMessage(UPDATE_PROGRESS);

        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {

        }
    }
}
