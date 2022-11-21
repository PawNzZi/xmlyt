package cn.lingyikz.soundbook.soundbook.utils;

import android.content.Context;
import android.util.Log;
import android.widget.ProgressBar;

import com.liulishuo.okdownload.DownloadTask;
import com.liulishuo.okdownload.SpeedCalculator;
import com.liulishuo.okdownload.core.Util;
import com.liulishuo.okdownload.core.breakpoint.BlockInfo;
import com.liulishuo.okdownload.core.breakpoint.BreakpointInfo;
import com.liulishuo.okdownload.core.cause.EndCause;
import com.liulishuo.okdownload.core.listener.DownloadListener4WithSpeed;
import com.liulishuo.okdownload.core.listener.assist.Listener4SpeedAssistExtend;

import java.util.List;
import java.util.Map;



public class MyDownloadListener4WithSpeed extends DownloadListener4WithSpeed {

    private long totalLength;
    private String readableTotalLength;
    private ProgressBar progressBar;//谨防内存泄漏
    private Context context;//谨防内存泄漏

    private String TAG = "download";

    public MyDownloadListener4WithSpeed(ProgressBar progressBar){
        this.progressBar = progressBar;
        this.context = progressBar.getContext();
    }
    @Override
    public void taskStart( DownloadTask task) {
        Log.i(TAG,"taskStart");
    }

    @Override
    public void connectStart( DownloadTask task, int blockIndex,  Map<String, List<String>> requestHeaderFields) {
        Log.i(TAG,"connectStart");
    }

    @Override
    public void connectEnd( DownloadTask task, int blockIndex, int responseCode,  Map<String, List<String>> responseHeaderFields) {
        Log.i(TAG,"connectEnd");
    }

    @Override
    public void infoReady( DownloadTask task,  BreakpointInfo info, boolean fromBreakpoint,  Listener4SpeedAssistExtend.Listener4SpeedModel model) {
        Log.i(TAG,"infoReady");
        totalLength = info.getTotalLength();
        readableTotalLength = Util.humanReadableBytes(totalLength, true);
        progressBar.setMax((int) totalLength);
        Log.i(TAG, "【2、infoReady】当前进度" + (float) info.getTotalOffset() / totalLength * 100 + "%" + "，" + info.toString());
    }

    @Override
    public void progressBlock( DownloadTask task, int blockIndex, long currentBlockOffset,  SpeedCalculator blockSpeed) {
        Log.i(TAG,"progressBlock");
    }

    @Override
    public void progress( DownloadTask task, long currentOffset, SpeedCalculator taskSpeed) {
        Log.i(TAG,"progress");
        String readableOffset = Util.humanReadableBytes(currentOffset, true);
        String progressStatus = readableOffset + "/" + readableTotalLength;
        String speed = taskSpeed.speed();
        float percent = (float) currentOffset / totalLength * 100;
        Log.i("bqt", "【6、progress】" + currentOffset + "[" + progressStatus + "]，速度：" + speed + "，进度：" + percent + "%");
        progressBar.setProgress((int) currentOffset);
    }

    @Override
    public void blockEnd( DownloadTask task, int blockIndex, BlockInfo info, SpeedCalculator blockSpeed) {
        Log.i(TAG,"blockEnd");
    }

    @Override
    public void taskEnd(DownloadTask task, EndCause cause, Exception realCause, SpeedCalculator taskSpeed) {
        Log.i(TAG,"taskEnd");
    }
}
