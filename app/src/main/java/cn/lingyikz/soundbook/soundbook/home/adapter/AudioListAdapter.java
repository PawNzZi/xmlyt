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
import cn.lingyikz.soundbook.soundbook.modle.v2.AlbumSound;
import cn.lingyikz.soundbook.soundbook.utils.Constans;


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
        String tip = "";
        holder.binding.donwloadAudio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast.makeText(context, "暂未开放", Toast.LENGTH_SHORT).show();

            }
        });
        holder.binding.listIndex.setText(nextPage * 50 + position + 1 +"");
        holder.binding.listName.setText(list.get(position).getName());
        holder.binding.listDate.setText(list.get(position).getCreateTime());
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
        void onDeleteItem(Long albumId);
    }
}
