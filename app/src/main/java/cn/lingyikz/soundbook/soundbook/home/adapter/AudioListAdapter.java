package cn.lingyikz.soundbook.soundbook.home.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.lang.reflect.TypeVariable;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import cn.lingyikz.soundbook.soundbook.R;
import cn.lingyikz.soundbook.soundbook.databinding.ItemAudiolistBinding;
import cn.lingyikz.soundbook.soundbook.modle.AlbumDetail;



public class AudioListAdapter extends RecyclerView.Adapter<AudioListAdapter.ViewHolder>{

    private List<AlbumDetail.DataDTO.ListDTO> list ;
    private Context context ;
    private AudioListen audioListen ;
    private SimpleDateFormat dateFormat ;
    private int Tag ;
    @SuppressLint("SimpleDateFormat")
    public AudioListAdapter(List<AlbumDetail.DataDTO.ListDTO> list, Context context, AudioListen audioListen ,int Tag){
        this.list = list ;
        this.context = context ;
        this.audioListen = audioListen;
        this.Tag = Tag ;
        if(Tag == 0){
            dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        }else {
            dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        }


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
//        Log.i("TAG","onBindViewHolder");
        String tip = "";
        if(Tag == 1){
            tip = "上一次播放 ";
            holder.binding.listDate.setTextSize(TypedValue.COMPLEX_UNIT_SP,11);
            holder.binding.donwloadAudio.setImageDrawable(context.getResources().getDrawable(R.mipmap.delete, context.getTheme()));
        }
        holder.binding.donwloadAudio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Tag == 1){
                    audioListen.onDeleteItem(list.get(position).getAlbumId());
                }else{
                    Toast.makeText(context, "暂未开放", Toast.LENGTH_SHORT).show();
                }
            }
        });
        holder.binding.listIndex.setText(position+1+"");
        holder.binding.listName.setText(list.get(position).getName());
//        Log.i("TAG","onBindViewHolder"+list.get(position).getName());
        holder.binding.listDate.setText(tip + dateFormat.format(new Date(list.get(position).getCreated())));
        holder.binding.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Map<String,Object> map = new HashMap<>();
                Bundle bundle = new Bundle();
                bundle.putInt("albumId",list.get(position).getAlbumId());
                bundle.putInt("episodes",list.get(position).getEpisodes());
                bundle.putLong("audioCreated",System.currentTimeMillis());
                bundle.putString("audioDes", (String) list.get(position).getDescription());
//                bundle.putString("audioDuration","0");
                bundle.putString("title",list.get(position).getName());
                bundle.putString("src",list.get(position).getUrl());
                bundle.putInt("audioId",list.get(position).getId());
                audioListen.onAudioPlay(bundle);
            }
        });

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
        void onDeleteItem(int albumId);
    }
}
