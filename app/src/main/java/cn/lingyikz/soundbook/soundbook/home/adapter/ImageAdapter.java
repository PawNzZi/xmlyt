package cn.lingyikz.soundbook.soundbook.home.adapter;

import android.view.ViewGroup;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.youth.banner.adapter.BannerAdapter;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;
import cn.lingyikz.soundbook.soundbook.modle.Banner;

public class ImageAdapter extends BannerAdapter<Banner.DataDTO,ImageAdapter.BannerViewHolder> {


    public ImageAdapter(List<Banner.DataDTO> datas) {
        super(datas);
    }

    @Override
    public BannerViewHolder onCreateHolder(ViewGroup parent, int viewType) {
        ImageView imageView = new ImageView(parent.getContext());
        imageView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        return new BannerViewHolder(imageView);
    }

    @Override
    public void onBindView(BannerViewHolder holder, Banner.DataDTO data, int position, int size) {
        Glide.with(holder.imageView).load(data.getValue())
                .into(holder.imageView);
    }

    public class BannerViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView ;
        public BannerViewHolder(ImageView imageView) {
            super(imageView);
            this.imageView = imageView ;
        }
    }
}
