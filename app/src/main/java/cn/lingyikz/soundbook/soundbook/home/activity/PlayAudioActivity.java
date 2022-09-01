package cn.lingyikz.soundbook.soundbook.home.activity;

import android.annotation.SuppressLint;
import android.app.Activity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.SeekBar;
import android.widget.Toast;

import com.liys.onclickme.LOnClickMe;
import com.liys.onclickme_annotations.AClick;

import java.io.IOException;

import androidx.annotation.Nullable;

import cn.lingyikz.soundbook.soundbook.R;
import cn.lingyikz.soundbook.soundbook.api.RequestService;
import cn.lingyikz.soundbook.soundbook.databinding.ActivityPalyaduioBinding;
import cn.lingyikz.soundbook.soundbook.modle.XmlyNextPaly;

import cn.lingyikz.soundbook.soundbook.utils.DataBaseHelper;
import cn.lingyikz.soundbook.soundbook.utils.SharedPreferences;
import cn.lingyikz.soundbook.soundbook.utils.SuperMediaPlayer;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class PlayAudioActivity extends Activity implements SeekBar.OnSeekBarChangeListener {

    private ActivityPalyaduioBinding binding ;
    private DataBaseHelper dataBaseHelper;
    private static final int UPDATE_PROGRESS = 0;
    private static final int CHANGE_SECONDE = 15 ;
    private Bundle bundle ;
    private SuperMediaPlayer superMediaPlayer = SuperMediaPlayer.getInstance();



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
        binding.spinKit.setVisibility(View.VISIBLE);
        binding.titleBar.goSearch.setVisibility(View.GONE);
        binding.titleBar.goPlay.setVisibility(View.GONE);
        binding.titleBar.titleSpinKit.setVisibility(View.GONE);
        binding.titleBar.goBacK.setVisibility(View.VISIBLE);
        bundle = getIntent().getExtras();
//        Log.i("TAG",bundle+"");
        binding.titleBar.title.setText(bundle.getString("title"));
        binding.seekbar.setOnSeekBarChangeListener(this);
        dataBaseHelper = DataBaseHelper.getInstance(this);
        superMediaPlayer.setOnPreparedListener(onPreparedListener);
        superMediaPlayer.setOnSeekCompleteListener(onSeekCompleteListener);
        superMediaPlayer.setOnCompletionListener(onCompletionListener);
        superMediaPlayer.setOnErrorListener(onErrorListener);

        Bundle historyBundle = dataBaseHelper.queryPlayHistory(bundle.getInt("albumId"),bundle.getInt("audioId"));
        dataBaseHelper.close();

        if(historyBundle.getString("audioDuration") == null){
//            Log.i("TAG","audioDuration == null");
            bundle.putString("audioDuration","0");
        }else {
//            Log.i("TAG","audioDuration != null");
//            Log.i("TAG","audioDuration != null"+historyBundle.getString("audioDuration"));
            bundle.putString("audioDuration",historyBundle.getString("audioDuration"));
        }
        playAudio();
    }

    /**
     * 启动服务播放
     */
    private void playAudio(){
        SuperMediaPlayer.error = 0 ;
        if(superMediaPlayer.isPlaying()){

            Bundle oldAudioInfo = SharedPreferences.getOldAudioInfo(this);
            Log.i("TAG",oldAudioInfo.getString("title" ));
            Log.i("TAG",bundle.getString("title" ));
            if(oldAudioInfo.getString("src" ).equals(bundle.getString("src" ))){
                binding.startPlay.setImageDrawable(getResources().getDrawable(R.mipmap.activity_start, null));
                binding.spinKit.setVisibility(View.GONE);
            }else {
                superMediaPlayer.stop();
                oldAudioInfo.putLong("audioDuration",superMediaPlayer.getCurrentPosition());
                dataBaseHelper.addPlayHistory(oldAudioInfo);
                dataBaseHelper.close();
                superMediaPlayer.reset();
                if(Long.parseLong(bundle.getString("audioDuration")) > 0){
                    onSeekToRead(bundle.getString("src"),Long.parseLong(bundle.getString("audioDuration")));
                }else {
                    onRead(bundle.getString("src"));
                }
                SharedPreferences.saveOldAudioInfo(this,bundle);
            }
        }else{
            superMediaPlayer.stop();
            superMediaPlayer.reset();
            if(Long.parseLong(bundle.getString("audioDuration")) > 0){
                onSeekToRead(bundle.getString("src"),Long.parseLong(bundle.getString("audioDuration")));
            }else {
                onRead(bundle.getString("src"));
            }
            SharedPreferences.saveOldAudioInfo(this,bundle);
        }


    }
    private void onRead(String url){
        try {
            superMediaPlayer.setDataSource(url);
            superMediaPlayer.prepareAsync();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void onSeekToRead(String url,long duration){

        try {
            superMediaPlayer.setDataSource(url);
            superMediaPlayer.prepare();
            superMediaPlayer.seekTo((int) duration);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private final SuperMediaPlayer.OnPreparedListener onPreparedListener = new MediaPlayer.OnPreparedListener() {
        @Override
        public void onPrepared(MediaPlayer mediaPlayer) {
            if(binding != null){
                binding.startPlay.setImageDrawable(getResources().getDrawable(R.mipmap.activity_start, null));
                binding.spinKit.setVisibility(View.GONE);
            }

            mediaPlayer.start();
        }
    };
    private final SuperMediaPlayer.OnSeekCompleteListener onSeekCompleteListener = new MediaPlayer.OnSeekCompleteListener() {
        @Override
        public void onSeekComplete(MediaPlayer mediaPlayer) {
            if(binding != null){
                binding.startPlay.setImageDrawable(getResources().getDrawable(R.mipmap.activity_start, null));
                binding.spinKit.setVisibility(View.GONE);
            }

            mediaPlayer.start();
        }
    };
    private final SuperMediaPlayer.OnErrorListener onErrorListener = new MediaPlayer.OnErrorListener() {
        @Override
        public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
            mediaPlayer.stop();
            mediaPlayer.reset();
            SuperMediaPlayer.error = 1;
            Toast.makeText(PlayAudioActivity.this, "加载失败！", Toast.LENGTH_SHORT).show();
            return false;
        }
    };
    private final SuperMediaPlayer.OnCompletionListener onCompletionListener = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mediaPlayer) {
            if(binding != null){
                binding.startPlay.setImageDrawable(getResources().getDrawable(R.mipmap.activity_pause, null));
            }

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
                            Log.i("TAG", xmlyNextPaly.toString() + "");
                            if(xmlyNextPaly.getCode() == 200 && xmlyNextPaly.getData().size() > 0 && SuperMediaPlayer.error == 0) {
//                                Log.i("TAG", xmlyNextPaly.toString() + "");
                                XmlyNextPaly.DataDTO dataDTO = xmlyNextPaly.getData().get(0);
                                if(binding != null){
                                    binding.titleBar.title.setText(dataDTO.getName());
                                }
                                Bundle reslutBundle = new Bundle();
                                reslutBundle.putInt("albumId",dataDTO.getAlbumId());
                                reslutBundle.putInt("episodes",dataDTO.getEpisodes());
                                reslutBundle.putString("title",dataDTO.getName());
                                reslutBundle.putString("audioDes","");
                                reslutBundle.putLong("audioDuration",0);
                                reslutBundle.putString("audioCreated", String.valueOf(dataDTO.getCreated()));
                                reslutBundle.putString("src",dataDTO.getUrl());
                                reslutBundle.putInt("audioId",dataDTO.getId());
                                superMediaPlayer.stop();
                                superMediaPlayer.reset();
                                onRead(dataDTO.getUrl());
//                                SharedPreferences.saveOldAudioInfo(PlayAudioActivity.this,bundle);
                                dataBaseHelper.addPlayHistory(bundle);
                                dataBaseHelper.close();
                                bundle = reslutBundle;
                                SharedPreferences.saveOldAudioInfo(PlayAudioActivity.this,reslutBundle);

                            }else if(xmlyNextPaly.getCode() == 200 && xmlyNextPaly.getData().size() == 0){
                                superMediaPlayer.stop();
                                bundle.putLong("audioDuration",0);
                                bundle.putString("audioCreated", String.valueOf(System.currentTimeMillis()));
                                superMediaPlayer.reset();
                                dataBaseHelper.addPlayHistory(bundle);
                                dataBaseHelper.close();
                            }
                        }
                    });
        }
    };

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
        if(superMediaPlayer.isPlaying()){
            binding.seekbar.setMax(superMediaPlayer.getDuration());
//            Log.i("TAG","getDuration:"+superMediaPlayer.getDuration());
            //设置进度条的进度
            binding.seekbar.setProgress((int) superMediaPlayer.getCurrentPosition());
        }
//        Log.i("TAG","currenPostion:"+superMediaPlayer.getCurrentPosition());
//        binding.seekbar.setProgress(superMediaPlayer.getCurrentPosition());
        //使用Handler每500毫秒更新一次进度条
        handler.sendEmptyMessageDelayed(UPDATE_PROGRESS, 500);
    }
    @Override
    protected void onResume() {
        super.onResume();
        //进入到界面后开始更新进度条
//        Log.i("TAG","onResume");
        handler.sendEmptyMessage(UPDATE_PROGRESS);

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
        binding = null ;
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
        if (b){
            superMediaPlayer.seekTo(i);
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    @AClick({R.id.go_bacK,  R.id.startPlay,R.id.kuaituiClick,R.id.kuaijinClick})
    public void click(View view) {
        switch (view.getId()) {
            case R.id.go_bacK:
                finish();
                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                break;
            case R.id.startPlay:
                if(superMediaPlayer.isPlaying()){
                    superMediaPlayer.pause();
                    bundle.putLong("audioDuration",superMediaPlayer.getCurrentPosition());
                    dataBaseHelper.addPlayHistory(bundle);
                    dataBaseHelper.close();
                    SharedPreferences.saveOldAudioInfo(this,bundle);
                    binding.startPlay.setImageDrawable(getResources().getDrawable(R.mipmap.activity_pause, null));
                }else {
                    binding.startPlay.setImageDrawable(getResources().getDrawable(R.mipmap.activity_start, null));
                    superMediaPlayer.start();
                }
                break;
            case R.id.kuaituiClick:
                Log.i("TAG",superMediaPlayer.getCurrentPosition()+"");
                superMediaPlayer.seekTo(superMediaPlayer.getCurrentPosition() - CHANGE_SECONDE * 1000);
                break;
            case R.id.kuaijinClick:
                superMediaPlayer.seekTo(superMediaPlayer.getCurrentPosition() + CHANGE_SECONDE * 1000);
                break;
            default:
                break;
        }
    }


}
