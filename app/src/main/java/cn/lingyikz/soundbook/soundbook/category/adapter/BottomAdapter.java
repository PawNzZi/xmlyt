package cn.lingyikz.soundbook.soundbook.category.adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.bumptech.glide.Glide;
import java.util.List;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import cn.lingyikz.soundbook.soundbook.databinding.FootviewBinding;
import cn.lingyikz.soundbook.soundbook.databinding.ItemHomeBinding;
import cn.lingyikz.soundbook.soundbook.home.activity.AudioDetailActivity;
import cn.lingyikz.soundbook.soundbook.modle.Album;
import cn.lingyikz.soundbook.soundbook.modle.AlbumCategoryBean;
import cn.lingyikz.soundbook.soundbook.utils.IntentAction;

public class BottomAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private List<AlbumCategoryBean.DataDTO.ListDTO> list ;
    private Context context ;
    // 普通布局
    private final int TYPE_ITEM = 1;
    // 脚布局
    private final int TYPE_FOOTER = 2;
    // 当前加载状态，默认为加载完成
    private int loadState = 2;
    // 正在加载
    public final int LOADING = 1;
    // 加载完成
    public final int LOADING_COMPLETE = 2;
    // 加载到底
    public final int LOADING_END = 3;


    public BottomAdapter(List<AlbumCategoryBean.DataDTO.ListDTO> list, Context context){
        this.list = list ;
        this.context = context ;
    }

    @Override
    public int getItemViewType(int position) {
        // 最后一个item设置为FooterView
        if (position + 1 == getItemCount()) {
            return TYPE_FOOTER;
        } else {
            return TYPE_ITEM;
        }

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if(viewType == TYPE_ITEM){
            ItemHomeBinding itemHomeBinding = ItemHomeBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
            return new BottomAdapter.ViewHolder(itemHomeBinding);
        }else if(viewType == TYPE_FOOTER){
            FootviewBinding footviewBinding = FootviewBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
            return new BottomAdapter.FootViewHolder(footviewBinding);
        }
        return null ;
    }

    @Override
    public void onBindViewHolder(@NonNull  RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof FootViewHolder){
         FootViewHolder footViewHolder = (FootViewHolder) holder;
         switch (loadState){
             case LOADING://正在加载
                 footViewHolder.footviewBinding.loadingLinearLayout.setVisibility(View.VISIBLE);
                 footViewHolder.footviewBinding.noDataLinearLayout.setVisibility(View.GONE);
                 break;
             case LOADING_COMPLETE://加载完成
                 footViewHolder.footviewBinding.loadingLinearLayout.setVisibility(View.GONE);
                 footViewHolder.footviewBinding.noDataLinearLayout.setVisibility(View.GONE);
                 break;

             case LOADING_END://加载到底
                 footViewHolder.footviewBinding.loadingLinearLayout.setVisibility(View.GONE);
                 footViewHolder.footviewBinding.noDataLinearLayout.setVisibility(View.VISIBLE);
                 break;

             default:
                 break;
         }
        }else if(holder instanceof ViewHolder){
            ViewHolder viewHolder = (ViewHolder) holder;
            viewHolder.itemHomeBinding.itemBookName.setText(list.get(position).getAlbum().getName());
//        Log.i("tag",list.get(position).getBookName());
            viewHolder.itemHomeBinding.itemBookDescription.setText(list.get(position).getAlbum().getDescription());
            Glide.with(viewHolder.itemHomeBinding.getRoot()).load(list.get(position).getAlbum().getThumb()).into(viewHolder.itemHomeBinding.itemThumb);
            viewHolder.itemHomeBinding.contentLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                Log.i("TAG",""+position);
                    Bundle bundle = new Bundle();

                    Album.DataDTO.ListDTO albumDetail = new Album.DataDTO.ListDTO();
                    albumDetail.setThumb(list.get(position).getAlbum().getThumb());
                    albumDetail.setDescription(list.get(position).getAlbum().getDescription());
                    albumDetail.setName(list.get(position).getAlbum().getName());
                    albumDetail.setId(list.get(position).getAlbumId());
                    albumDetail.setCategories(list.get(position).getAlbum().getCategories());
                    albumDetail.setAuthor(list.get(position).getAlbum().getAuthor());

                    bundle.putSerializable("bookObject", albumDetail);
                    IntentAction.setValueContext(context, AudioDetailActivity.class,bundle);
//                verridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return list.size() + 1;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ItemHomeBinding itemHomeBinding ;
        public ViewHolder(ItemHomeBinding itemHomeBinding) {
            super(itemHomeBinding.getRoot());
            this.itemHomeBinding = itemHomeBinding ;
        }
    }

    public class FootViewHolder extends  RecyclerView.ViewHolder{

        FootviewBinding footviewBinding ;
        public FootViewHolder(FootviewBinding footviewBinding ) {
            super(footviewBinding.getRoot());
            this.footviewBinding = footviewBinding ;
        }
    }

    /**
     * 设置上拉加载状态
     *
     * @param loadState 0.正在加载 1.加载完成 2.加载到底
     */
    public void setLoadState(int loadState) {
        this.loadState = loadState;
        notifyDataSetChanged();
    }
}
