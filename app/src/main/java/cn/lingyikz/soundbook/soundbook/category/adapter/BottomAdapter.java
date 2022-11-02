package cn.lingyikz.soundbook.soundbook.category.adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;

import java.io.Serializable;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import cn.lingyikz.soundbook.soundbook.databinding.ItemHomeBinding;
import cn.lingyikz.soundbook.soundbook.home.activity.AudioDetailActivity;
import cn.lingyikz.soundbook.soundbook.modle.Album;
import cn.lingyikz.soundbook.soundbook.modle.AlbumCategoryBean;
import cn.lingyikz.soundbook.soundbook.utils.IntentAction;

public class BottomAdapter extends RecyclerView.Adapter<BottomAdapter.ViewHolder>{

    private List<AlbumCategoryBean.DataDTO.ListDTO> list ;
    private Context context ;

    public BottomAdapter(List<AlbumCategoryBean.DataDTO.ListDTO> list, Context context){
        this.list = list ;
        this.context = context ;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemHomeBinding itemHomeBinding = ItemHomeBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        return new BottomAdapter.ViewHolder(itemHomeBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull  BottomAdapter.ViewHolder holder, int position) {

        holder.itemHomeBinding.itemBookName.setText(list.get(position).getAlbum().getName());
//        Log.i("tag",list.get(position).getBookName());
        holder.itemHomeBinding.itemBookDescription.setText(list.get(position).getAlbum().getDescription());
        Glide.with(holder.itemHomeBinding.getRoot()).load(list.get(position).getAlbum().getThumb()).into(holder.itemHomeBinding.itemThumb);
        holder.itemHomeBinding.contentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Log.i("TAG",""+position);
                Bundle bundle = new Bundle();

                Album.DataDTO.ListDTO albumDetail = new Album.DataDTO.ListDTO();
                albumDetail.setThumb(list.get(position).getAlbum().getThumb());
                albumDetail.setDescription(list.get(position).getAlbum().getDescription());
                albumDetail.setName(list.get(position).getAlbum().getName());
                albumDetail.setId(list.get(position).getId());
                albumDetail.setCategories(list.get(position).getAlbum().getCategories());
                albumDetail.setAuthor(list.get(position).getAlbum().getAuthor());

                bundle.putSerializable("bookObject", albumDetail);
                IntentAction.setValueContext(context, AudioDetailActivity.class,bundle);
//                verridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ItemHomeBinding itemHomeBinding ;
        public ViewHolder(ItemHomeBinding itemHomeBinding) {
            super(itemHomeBinding.getRoot());
            this.itemHomeBinding = itemHomeBinding ;
        }
    }

}
