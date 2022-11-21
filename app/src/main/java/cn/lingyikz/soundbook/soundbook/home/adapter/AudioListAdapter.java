package cn.lingyikz.soundbook.soundbook.home.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;

import android.util.Log;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.SeekBar;

import com.hjq.permissions.OnPermissionCallback;
import com.hjq.permissions.Permission;
import com.hjq.permissions.XXPermissions;
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
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import cn.lingyikz.soundbook.soundbook.databinding.ItemAudiolistBinding;
import cn.lingyikz.soundbook.soundbook.modle.v2.AlbumSound;
import cn.lingyikz.soundbook.soundbook.utils.Constans;
import cn.lingyikz.soundbook.soundbook.utils.DownLoadUtil;


public class AudioListAdapter extends RecyclerView.Adapter<AudioListAdapter.ViewHolder>{

    private List<AlbumSound.DataDTO.RowsDTO> list ;
    private Context context ;
    private AudioListen audioListen ;
    private int nextPage = 0 ;

    @SuppressLint("SimpleDateFormat")
    public AudioListAdapter(List<AlbumSound.DataDTO.RowsDTO> list, Context context, AudioListen audioListen ){
        this.list = list ;
        this.context = context ;
        this.audioListen = audioListen;
    }
    @NonNull

    @Override
    public AudioListAdapter.ViewHolder onCreateViewHolder(@NonNull  ViewGroup parent, int viewType) {

        ItemAudiolistBinding binding = ItemAudiolistBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false) ;

        return new ViewHolder(binding);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull  AudioListAdapter.ViewHolder holder, int position) {
//        LOnClickMe.init(this,holder.binding.getRoot());
//        Log.i("TAG","onBindViewHolder" + this.nextPage);
        holder.binding.donwloadAudio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                XXPermissions.with(context)
                        // 申请权限
                        .permission(Permission.WRITE_EXTERNAL_STORAGE)
                        .permission(Permission.READ_EXTERNAL_STORAGE)
                        .request(new OnPermissionCallback() {
                            @Override
                            public void onGranted(List<String> permissions, boolean all) {
                                if (!all) {
                                    return;
                                }
                                holder.binding.donwloadAudio.setVisibility(View.GONE);
                                holder.binding.progressBar.setVisibility(View.VISIBLE);
//                                String targetPath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "Download" + File.separator + list.get(position).getName() + ".mp3" ;
//                                audioListen.downLoad(list.get(position).getUrl(),targetPath,holder.binding.progressBar);
                                DownLoadUtil downLoadUtil = new DownLoadUtil(list.get(position).getName(),list.get(position).getUrl());
                                DownloadTask task =  downLoadUtil.createDownloadTask();
                                task.enqueue(new MyDownloadListener4WithSpeed(holder.binding.progressBar));

                            }
                            @Override
                            public void onDenied(List<String> permissions, boolean never) {
                                if (never) {

                                    // 如果是被永久拒绝就跳转到应用权限系统设置页面
                                    XXPermissions.startPermissionActivity(context, permissions);
                                } else {

                                }
                            }
                        });

//                Toast.makeText(context, "暂未开放", Toast.LENGTH_SHORT).show();

            }
        });
        holder.binding.listIndex.setText(nextPage * 50 + position + 1 +"");
        holder.binding.listName.setText(list.get(position).getName());
        holder.binding.listDate.setText(list.get(position).getCreateTime().substring(0,19));
//            holder.binding.listDate.setText(tip + dateFormat.format(new Date(Long.parseLong(list.get(position).getCreateTime()))));
        holder.binding.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Map<String,Object> map = new HashMap<>();
                Bundle bundle = new Bundle();
                bundle.putLong("albumId",list.get(position).getAlbumId());
                bundle.putInt("episodes",list.get(position).getEpisodes());
                bundle.putString("audioCreated", String.valueOf(System.currentTimeMillis()));
                bundle.putString("audioDes", (String) list.get(position).getDescription());
//                bundle.putString("audioDuration","0");
                bundle.putString("title",list.get(position).getName());
                bundle.putString("src",list.get(position).getUrl());
                bundle.putLong("audioId",list.get(position).getId());
                bundle.putInt("playModel", Constans.PLAY_MODLE_LIST);

                audioListen.onAudioPlay(bundle);
            }
        });
    }
    public void notifyDataSetChange(int nextPage){
        this.nextPage = nextPage ;
        notifyDataSetChanged();
    }
    @Override
    public int getItemCount() {
        return list.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{

        ItemAudiolistBinding binding  ;
        public ViewHolder(ItemAudiolistBinding binding) {

            super(binding.getRoot());
            this.binding = binding ;
        }

    }

    public interface AudioListen{
        void onAudioPlay(Bundle bundle);
//        void onDeleteItem(Long albumId);
        void downLoad(String url , String targetPath, ProgressBar progressBar);
    }

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
        public void infoReady(DownloadTask task, BreakpointInfo info, boolean fromBreakpoint, Listener4SpeedAssistExtend.Listener4SpeedModel model) {
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
        public void blockEnd(DownloadTask task, int blockIndex, BlockInfo info, SpeedCalculator blockSpeed) {
            Log.i(TAG,"blockEnd");
        }

        @Override
        public void taskEnd(DownloadTask task, EndCause cause, Exception realCause, SpeedCalculator taskSpeed) {
            Log.i(TAG,"taskEnd");
            progressBar.setVisibility(View.GONE);
        }
    }
}
