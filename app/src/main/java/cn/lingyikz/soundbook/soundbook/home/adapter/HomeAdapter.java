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
import cn.lingyikz.soundbook.soundbook.utils.DataBaseHelper;
import cn.lingyikz.soundbook.soundbook.utils.IntentAction;
import cn.lingyikz.soundbook.soundbook.utils.SharedPreferences;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.ViewHolder> {
    private List<Album.DataDTO.ListDTO> list ;
    private Context context ;
    private ItemOperaCallBack itemOperaCallBack;
    private int Tag  ;
    private DataBaseHelper dataBaseHelper ;
    public HomeAdapter(List<Album.DataDTO.ListDTO> list,Context context,int Tag,ItemOperaCallBack itemOperaCallBack){
        this.list = list ;
        this.context = context ;
        this.Tag = Tag ;
        this.itemOperaCallBack = itemOperaCallBack;
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
        if(Tag == 1){
            holder.itemHomeBinding.collectionIcon.setVisibility(View.VISIBLE);
            holder.itemHomeBinding.collectionIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                      itemOperaCallBack.deleteItem(list.get(position).getId());
//                    dataBaseHelper = DataBaseHelper.getInstance(context);
//                    dataBaseHelper.cancleCollection(list.get(position).getId(), SharedPreferences.getUUID(context));
//                    dataBaseHelper.close();

                }
            });
        }

        holder.itemHomeBinding.itemBookName.setText(list.get(position).getName());
//        Log.i("tag",list.get(position).getBookName());
        holder.itemHomeBinding.itemBookDescription.setText(list.get(position).getDescription());
        Glide.with(holder.itemHomeBinding.getRoot()).load(list.get(position).getThumb()).into(holder.itemHomeBinding.itemThumb);
        holder.itemHomeBinding.contentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Log.i("TAG",""+position);
                Bundle bundle = new Bundle();
//                bundle.putString("audio_id",list.get(position).getId());
                bundle.putSerializable("bookObject",list.get(position));
                IntentAction.setValueContext(context, AudioDetailActivity.class,bundle);
//                verridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
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

    public interface ItemOperaCallBack{
        void deleteItem(int albumId);
    }
}
