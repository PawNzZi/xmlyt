package cn.lingyikz.soundbook.soundbook.user.fragment;

import android.content.Intent;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.util.ArrayList;
import java.util.List;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import androidx.recyclerview.widget.RecyclerView;

import com.kongzue.dialogx.dialogs.PopTip;

import cn.hutool.core.util.ObjectUtil;
import cn.lingyikz.soundbook.soundbook.api.RequestService;
import cn.lingyikz.soundbook.soundbook.base.BaseObsever;
import cn.lingyikz.soundbook.soundbook.databinding.FragmentPalyhistoryBinding;
import cn.lingyikz.soundbook.soundbook.home.activity.PlayAudioActivity;
import cn.lingyikz.soundbook.soundbook.home.adapter.AudioListAdapter;
import cn.lingyikz.soundbook.soundbook.main.BaseFragment;
import cn.lingyikz.soundbook.soundbook.modle.AlbumDetail;
import cn.lingyikz.soundbook.soundbook.modle.v2.AlbumSound;
import cn.lingyikz.soundbook.soundbook.modle.v2.BaseModel;
import cn.lingyikz.soundbook.soundbook.modle.v2.CollectionHistory;
import cn.lingyikz.soundbook.soundbook.modle.v2.PlayHistories;
import cn.lingyikz.soundbook.soundbook.service.AudioService;
import cn.lingyikz.soundbook.soundbook.user.adapter.CollectionAdapter;
import cn.lingyikz.soundbook.soundbook.user.adapter.PlayHistoryAdapter;
import cn.lingyikz.soundbook.soundbook.utils.Constans;
import cn.lingyikz.soundbook.soundbook.utils.DataBaseHelper;
import cn.lingyikz.soundbook.soundbook.utils.EndlessRecyclerOnScrollListener;
import cn.lingyikz.soundbook.soundbook.utils.IntentAction;
import cn.lingyikz.soundbook.soundbook.utils.SharedPreferences;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class PlayHistoryFragment extends BaseFragment implements PlayHistoryAdapter.ItemOperaCallBack {
    private FragmentPalyhistoryBinding binding ;
    private PlayHistoryAdapter adapter = null;
    private List<PlayHistories.DataDTO.RowsDTO> mList = new ArrayList<>();

    private int nextPage = 1;
    private boolean hasNextPage = false;


    public static PlayHistoryFragment newInstance() {
        return new PlayHistoryFragment();
    }

    @Override
    protected View setLayout(LayoutInflater inflater, ViewGroup container) {
        binding = FragmentPalyhistoryBinding.inflate(LayoutInflater.from(getContext()),container,false);
        return binding.getRoot();
    }


    @Override
    protected void setView() {
        binding.swipeRecyclerView.addOnScrollListener(onScrollListener);
    }

    @Override
    protected void setData() {

    }

    private void initData() {
        if(ObjectUtil.isNotNull(Constans.user)){
            playHistoryList();
        }

    }
    @Override
    public void onStart() {
        super.onStart();
//        Log.i("TAG","onStart");
//        Log.i("TAG","PlayHistoryFragment:onStart");
    }

    @Override
    public void onStop() {
        super.onStop();
//        Log.i("TAG","onStop");
//        Log.i("TAG","PlayHistoryFragment:onStop");
    }

    @Override
    public void onResume() {
        super.onResume();
//        Log.i("TAG","onResume");
        mList.clear();
        initData();
//        Log.i("TAG","PlayHistoryFragment:onResume");

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
//        Log.i("TAG","onDestroy");
        binding = null ;
    }

    private void playHistoryList(){
        Observable<PlayHistories> observable = RequestService.getInstance().getApi().playHistoriesList(Constans.user.getId(),nextPage,Constans.PAGE_SIZE);
        observable.subscribeOn(Schedulers.io()) // 在子线程中进行Http访问
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObsever<PlayHistories>() {
                    @Override
                    public void onNext(PlayHistories baseModel) {
                        if(baseModel.getCode() == 200 && baseModel.getData().getRows().size() > 0){
//                            Log.i("TAG",baseModel.toString());
                            if(ObjectUtil.isNull(mList)){
                                mList = new ArrayList<>();
                            }
                            List<PlayHistories.DataDTO.RowsDTO> newList = baseModel.getData().getRows();
                            mList.addAll(newList);
                            if(ObjectUtil.isNull(adapter)){
                                binding.swipeRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                                adapter = new PlayHistoryAdapter(mList,getContext(),PlayHistoryFragment.this);
                                binding.swipeRecyclerView.setAdapter(adapter);
                            }

                            nextPage = baseModel.getData().getNextPage();
                            hasNextPage = baseModel.getData().getHasNextPage();
                        }else {
                            // PopTip.show("修改失败："+baseModel.getMessage()).showLong();

                        }
                        adapter.notifyDataSetChanged();

                    }
                });
        observable.unsubscribeOn(Schedulers.io());
    }

    private  RecyclerView.OnScrollListener  onScrollListener = new EndlessRecyclerOnScrollListener() {
        @Override
        public void onLoadMore() {
            if(hasNextPage){
                adapter.setLoadState(adapter.LOADING);
                playHistoryList();
            }else{
                //不加载
                adapter.setLoadState(adapter.LOADING_END);
                //Toast.makeText(getContext(), Constans.NO_LOADING, Toast.LENGTH_SHORT).show();
            }
        }
    };
    private void deletePlayHistory(Long playHistoryId){
        Observable<BaseModel> observable  = RequestService.getInstance().getApi().deleteHistroy(playHistoryId,Constans.user.getId());
        observable.subscribeOn(Schedulers.io()) // 在子线程中进行Http访问
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObsever<BaseModel>() {
                    @Override
                    public void onNext(BaseModel baseModel) {
                        if(baseModel.getCode() == 200){
                            mList.clear();
                            initData();
                        }else {
                            PopTip.show("删除失败："+baseModel.getMessage()).showLong();
                        }

                    }
                });
        observable.unsubscribeOn(Schedulers.io());
    }
    @Override
    public void deleteItem(Long playHistoryId) {
        deletePlayHistory(playHistoryId);
    }
}
