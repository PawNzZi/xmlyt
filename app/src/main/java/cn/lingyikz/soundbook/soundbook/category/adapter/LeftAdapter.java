package cn.lingyikz.soundbook.soundbook.category.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;
import cn.lingyikz.soundbook.soundbook.R;
import cn.lingyikz.soundbook.soundbook.databinding.ItemLeftBinding;
import cn.lingyikz.soundbook.soundbook.modle.Category;
import cn.lingyikz.soundbook.soundbook.utils.SharedPreferences;

public class LeftAdapter extends RecyclerView.Adapter<LeftAdapter.ViewHolder>{
    private int currentPosition ;
    private Context mContext ;
    private List<Category.DataDTO> mList ;
    private AdapterOnClick adapterOnClick ;
    public LeftAdapter(Context mContext,List<Category.DataDTO> mList,AdapterOnClick adapterOnClick){
        this.mContext = mContext ;
        this.mList = mList ;
        this.adapterOnClick = adapterOnClick;

    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemLeftBinding itemLeftBinding = ItemLeftBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        return new ViewHolder(itemLeftBinding);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder( LeftAdapter.ViewHolder holder, int position) {
//        Log.i("TAG",currentPosition+"");
        this.currentPosition = SharedPreferences.getCategoryIndex(mContext);
        holder.itemLeftBinding.tvTitle.setText(mList.get(position).getName());
        if(position == currentPosition){
            holder.itemLeftBinding.tvTitle.setTextColor(mContext.getResources().getColor(R.color.colorPrimary,null));
            holder.itemLeftBinding.tvBottomline.setVisibility(View.VISIBLE);
        }
        else{
            holder.itemLeftBinding.tvTitle.setTextColor(mContext.getResources().getColor(R.color.colorTitle,null));
            holder.itemLeftBinding.tvBottomline.setVisibility(View.GONE);
        }
        holder.itemLeftBinding.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.saveCategoryIndex(mContext,position);
                holder.itemLeftBinding.tvTitle.setTextColor(mContext.getResources().getColor(R.color.colorPrimary,null));
                holder.itemLeftBinding.tvBottomline.setVisibility(View.VISIBLE);

                notifyDataSetChanged();
                adapterOnClick.clickItem(mList.get(position).getId());
            }
        });
        //holder.itemLeftBinding.tvTitle.setBackgroundColor(mCheckedPosition == position ? 0xffeeeeee : 0xffffffff);//选中灰色，不选择白色
    }


    @Override
    public int getItemCount() {
        return mList.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder{
        ItemLeftBinding itemLeftBinding;
        public ViewHolder(ItemLeftBinding itemLeftBinding) {
            super(itemLeftBinding.getRoot());
            this.itemLeftBinding = itemLeftBinding ;
        }
    }

    public interface AdapterOnClick{
       void  clickItem(int categortId);
    }
}
