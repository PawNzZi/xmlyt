package cn.lingyikz.soundbook.soundbook.home.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import cn.lingyikz.soundbook.soundbook.databinding.ItemAudiolistBinding;
import cn.lingyikz.soundbook.soundbook.modle.AlbumDetail;



public class AudioListAdapter extends RecyclerView.Adapter<AudioListAdapter.ViewHolder>{

    private List<AlbumDetail.DataDTO.ListDTO> list ;
    private Context context ;
    private AudioListen audioListen ;
    private SimpleDateFormat dateFormat ;
    @SuppressLint("SimpleDateFormat")
    public AudioListAdapter(List<AlbumDetail.DataDTO.ListDTO> list, Context context, AudioListen audioListen ){
        this.list = list ;
        this.context = context ;
        this.audioListen = audioListen;
        dateFormat = new SimpleDateFormat("yyyy-MM-dd");

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
        holder.binding.listIndex.setText(position+1+"");
        holder.binding.listName.setText(list.get(position).getName());

        holder.binding.listDate.setText(dateFormat.format(new Date(list.get(position).getCreated())));
        holder.binding.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Map<String,Object> map = new HashMap<>();
                map.put("albumId",list.get(position).getAlbumId());
                map.put("episodes",list.get(position).getEpisodes());
                map.put("audioCreated",list.get(position).getCreated());
                map.put("audioDes",list.get(position).getDescription());
                map.put("audioDuration",0);
                map.put("title",list.get(position).getName());
                map.put("src",list.get(position).getUrl());
                map.put("audioId",list.get(position).getId());
//                map.put("currentPosition",0L);
                audioListen.onAudioPlay(map);
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
        void onAudioPlay(Map<String,Object> map);
    }
}
