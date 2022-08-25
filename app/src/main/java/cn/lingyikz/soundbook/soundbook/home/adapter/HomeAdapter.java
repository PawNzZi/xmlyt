package cn.lingyikz.soundbook.soundbook.home.adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.bumptech.glide.Glide;
import java.util.List;
import androidx.recyclerview.widget.RecyclerView;
import cn.lingyikz.soundbook.soundbook.databinding.ItemHomeBinding;
import cn.lingyikz.soundbook.soundbook.home.activity.AudioDetailActivity;
import cn.lingyikz.soundbook.soundbook.modle.Album;
import cn.lingyikz.soundbook.soundbook.utils.IntentAction;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.ViewHolder> {
    private List<Album.DataDTO.ListDTO> list ;
    private Context context ;
    public HomeAdapter(List<Album.DataDTO.ListDTO> list,Context context){
        this.list = list ;
        this.context = context ;
//        Log.i("tag",list.get(0).getBookName());
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        Log.i("TAG","onCreateViewHolder");
        ItemHomeBinding itemHomeBinding = ItemHomeBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        return new ViewHolder(itemHomeBinding);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
//        Log.i("TAG","onBindViewHolder");
        holder.itemHomeBinding.itemBookName.setText(list.get(position).getName());
//        Log.i("tag",list.get(position).getBookName());
        holder.itemHomeBinding.itemBookDescription.setText(list.get(position).getDescription());
        Glide.with(holder.itemHomeBinding.getRoot()).load(list.get(position).getThumb()).into(holder.itemHomeBinding.itemThumb);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Log.i("TAG",""+position);
                Bundle bundle = new Bundle();
//                bundle.putString("audio_id",list.get(position).getId());
                bundle.putSerializable("bookObject",list.get(position));
                IntentAction.setValueContext(context, AudioDetailActivity.class,bundle);
            }
        });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
//        Log.i("TAG",""+list.size());
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        ItemHomeBinding itemHomeBinding ;
        public ViewHolder(ItemHomeBinding itemHomeBinding) {

            super(itemHomeBinding.getRoot());
            this.itemHomeBinding = itemHomeBinding ;
        }

    }
}
