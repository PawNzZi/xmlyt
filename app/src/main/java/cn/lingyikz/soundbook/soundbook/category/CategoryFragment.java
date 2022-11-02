package cn.lingyikz.soundbook.soundbook.category;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.liys.onclickme.LOnClickMe;
import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import cn.lingyikz.soundbook.soundbook.api.RequestService;
import cn.lingyikz.soundbook.soundbook.base.BaseObsever;
import cn.lingyikz.soundbook.soundbook.category.adapter.BottomAdapter;
import cn.lingyikz.soundbook.soundbook.category.adapter.LeftAdapter;
import cn.lingyikz.soundbook.soundbook.databinding.FragmentCategoryBinding;
import cn.lingyikz.soundbook.soundbook.main.BaseFragment;
import cn.lingyikz.soundbook.soundbook.modle.AlbumCategoryBean;
import cn.lingyikz.soundbook.soundbook.modle.Category;
import cn.lingyikz.soundbook.soundbook.utils.Constans;
import cn.lingyikz.soundbook.soundbook.utils.EndlessRecyclerOnScrollListener;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class CategoryFragment extends BaseFragment implements LeftAdapter.AdapterOnClick {

    private FragmentCategoryBinding binding ;

    private LeftAdapter leftAdapter ;

    private BottomAdapter bottomAdapter ;

    private List<Category.DataDTO> mList = new ArrayList<>();

    private List<AlbumCategoryBean.DataDTO.ListDTO> albumList = new ArrayList<>();

    private int nextPage = 1;

    private int size = 10 ;

    private boolean hasNextPage = false;

    public int mCategoryId = 0 ;

    public static CategoryFragment newInstance() {
        return new CategoryFragment();
    }

    @Override
    protected View setLayout(LayoutInflater inflater, ViewGroup container) {
        binding = FragmentCategoryBinding.inflate(LayoutInflater.from(getActivity()),container,false);
        LOnClickMe.init(this,binding.getRoot());
        return binding.getRoot();
    }

    @Override
    protected void setView() {
        binding.titleBar.title.setText("分类");
        binding.titleBar.setBlock.setVisibility(View.GONE);
        binding.titleBar.setting.setVisibility(View.GONE);
        binding.titleBar.toSearch.setVisibility(View.GONE);
        binding.titleBar.goPlay.setVisibility(View.GONE);
        binding.titleBar.goBacK.setVisibility(View.GONE);

        binding.recyclerViewBottom.addOnScrollListener(onScrollListener);
    }

    @Override
    protected void setData() {
        this.initData(binding.getRoot());
    }

    private void initData(View view) {
        Observable<Category> observable  = RequestService.getInstance().getApi().getCategory();
        observable.subscribeOn(Schedulers.io()) // 在子线程中进行Http访问
                .observeOn(AndroidSchedulers.mainThread()) // UI线程处理返回接口
                .subscribe(new BaseObsever<Category>() { // 订阅
                    @Override
                    public void onNext(Category category) {
//                        Log.i("http返回：", postInfo.toString() + "");
                        if(category.getCode() == 200 && category.getData().size()>0){
                            if(mList == null){
                                mList = new ArrayList<>();
                            }
                            mList.clear();
                            List<Category.DataDTO> newList = category.getData();
                            mList.addAll(newList) ;

                            if(leftAdapter == null){
                                leftAdapter = new LeftAdapter(getContext(),mList, CategoryFragment.this);
                                binding.recyclerViewLeft.setLayoutManager(new LinearLayoutManager(getContext()));
                                binding.recyclerViewLeft.setAdapter(leftAdapter);
                                LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
                                layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
                                binding.recyclerViewLeft.setLayoutManager(layoutManager);
//                                DividerItemDecoration divider = new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL);
//                                binding.recyclerViewLeft.addItemDecoration(divider);
                            }
                            leftAdapter.notifyDataSetChanged();
                            mCategoryId = category.getData().get(0).getId() ;
                            getAlbumCategory(mCategoryId,1,size,true);
                        }else{
                            Toast.makeText(getContext(), Constans.EMPTY_TOAST, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        observable.unsubscribeOn(Schedulers.io());
    }

//    @AClick({R.id.copyQQGruop})
//    public void click(View view) {
//        switch (view.getId()) {
//            case R.id.copyQQGruop:
////                Toast.makeText(getActivity(), "复制成功", Toast.LENGTH_SHORT).show();
//                break;
//        }
//    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null ;
    }

    @Override
    public void clickItem(int categortId) {
        //加载当前分类的音频
        this.mCategoryId = categortId;
        getAlbumCategory(categortId,1,size,true);
    }

    private  RecyclerView.OnScrollListener  onScrollListener = new EndlessRecyclerOnScrollListener() {
        @Override
        public void onLoadMore() {
            if(hasNextPage){
                getAlbumCategory(mCategoryId,nextPage,size,false);
            }else{
                //不加载
                Toast.makeText(getContext(), Constans.NO_LOADING, Toast.LENGTH_SHORT).show();
            }
        }
    };

    private void getAlbumCategory(int categoryId,int page,int size,boolean isChangeCategory){
        Observable<AlbumCategoryBean> observable  = RequestService.getInstance().getApi().getAlbumCategory(categoryId,page,size);
        observable.subscribeOn(Schedulers.io()) // 在子线程中进行Http访问
                .observeOn(AndroidSchedulers.mainThread()) // UI线程处理返回接口
                .subscribe(new BaseObsever<AlbumCategoryBean>() { // 订阅
                    @Override
                    public void onNext(AlbumCategoryBean category) {
                        Log.i("http返回：", category.toString() + "");
                        if(category.getCode() == 200 && category.getData().getList().size() > 0 ){

                                if(albumList == null){
                                    albumList = new ArrayList<>();
                                }
                                if(isChangeCategory){
                                    //选择新的分类
                                    albumList.clear();
                                }

                                List<AlbumCategoryBean.DataDTO.ListDTO> newList = category.getData().getList();
                                albumList.addAll(newList) ;

                                if(bottomAdapter == null){
                                    bottomAdapter = new BottomAdapter(albumList,getContext());
                                    binding.recyclerViewBottom.setLayoutManager(new LinearLayoutManager(getContext()));
                                    binding.recyclerViewBottom.setAdapter(bottomAdapter);
                                    LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
                                    layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                                    binding.recyclerViewBottom.setLayoutManager(layoutManager);
                                    DividerItemDecoration divider = new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL);
                                    binding.recyclerViewBottom.addItemDecoration(divider);
                                }
                                bottomAdapter.notifyDataSetChanged();

                            nextPage = category.getData().getNextPage();
                            hasNextPage = category.getData().getHasNextPage();
                        }else{
                            Toast.makeText(getContext(), Constans.EMPTY_TOAST, Toast.LENGTH_SHORT).show();
                            hasNextPage = false;
                        }
                    }
                });
        observable.unsubscribeOn(Schedulers.io());
    }
}
