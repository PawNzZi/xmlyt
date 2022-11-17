package cn.lingyikz.soundbook.soundbook.home;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.liys.onclickme.LOnClickMe;
import com.liys.onclickme_annotations.AClick;
import com.wyt.searchbox.SearchFragment;
import com.wyt.searchbox.custom.IOnSearchClickListener;
import com.youth.banner.indicator.CircleIndicator;
import com.youth.banner.util.BannerLifecycleObserver;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import cn.lingyikz.soundbook.soundbook.R;
import cn.lingyikz.soundbook.soundbook.api.RequestService;
import cn.lingyikz.soundbook.soundbook.base.BaseObsever;
import cn.lingyikz.soundbook.soundbook.databinding.FragmentHomeBinding;
import cn.lingyikz.soundbook.soundbook.home.activity.PlayAudioActivity;
import cn.lingyikz.soundbook.soundbook.home.activity.SearchActivity;
import cn.lingyikz.soundbook.soundbook.home.adapter.HomeAdapter;
import cn.lingyikz.soundbook.soundbook.home.adapter.ImageAdapter;
import cn.lingyikz.soundbook.soundbook.main.BaseFragment;
import cn.lingyikz.soundbook.soundbook.modle.v2.Album;
import cn.lingyikz.soundbook.soundbook.modle.Banner;
import cn.lingyikz.soundbook.soundbook.modle.v2.HomeBanner;
import cn.lingyikz.soundbook.soundbook.utils.Constans;
import cn.lingyikz.soundbook.soundbook.utils.IntentAction;
import cn.lingyikz.soundbook.soundbook.utils.SharedPreferences;
import cn.lingyikz.soundbook.soundbook.utils.SuperMediaPlayer;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class HomeFragment extends BaseFragment implements HomeAdapter.ItemOperaCallBack {

    private FragmentHomeBinding binding;
    private HomeAdapter homeAdapter = null;
    private ImageAdapter imageAdapter = null ;
    private SearchFragment searchFragment ;
    private List<Album.DataDTO.RowsDTO> mList  = new ArrayList<>();
    private List<HomeBanner.DataDTO> bannerList = new ArrayList<>();


    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    @Override
    protected View setLayout(LayoutInflater inflater, ViewGroup container) {
        binding = FragmentHomeBinding.inflate(inflater,container,false);
        LOnClickMe.init(this,binding.getRoot());
        return binding.getRoot();
    }

    @Override
    protected void setView() {
        binding.titleBar.title.setText("首页");
        binding.titleBar.goBacK.setVisibility(View.GONE);
        binding.titleBar.goSearch.setVisibility(View.VISIBLE);
    }

    @Override
    protected void setData() {
        searchFragment = SearchFragment.newInstance();
        searchFragment.setOnSearchClickListener(iOnSearchClickListener);
        this.initData(binding.getRoot());
    }
    private IOnSearchClickListener iOnSearchClickListener = new IOnSearchClickListener() {
        @Override
        public void OnSearchClick(String keyword) {
            Bundle bundle = new Bundle();
            bundle.putString("keyword",keyword);
            IntentAction.setValueActivity(getActivity(), SearchActivity.class,bundle);
            getActivity().overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        }
    };

    public void initData(View view){

        Observable<Album> observable  = RequestService.getInstance().getApi().getPostInfo(1,Constans.INDEX_PAGE_SIZE,"");
        observable.subscribeOn(Schedulers.io()) // 在子线程中进行Http访问
                .observeOn(AndroidSchedulers.mainThread()) // UI线程处理返回接口
                .subscribe(new BaseObsever<Album>() { // 订阅

                    @Override
                    public void onNext(Album postInfo) {
//                        Log.i("http返回：", postInfo.toString() + "");
                        if(postInfo.getCode() == 200 && postInfo.getData().getRows().size() > 0){
                            if(mList == null){
                                mList = new ArrayList<>();
                            }
                            mList.clear();
                            List<Album.DataDTO.RowsDTO> newList = postInfo.getData().getRows();
                            mList.addAll(newList) ;

                            if(homeAdapter == null){
                                homeAdapter = new HomeAdapter(mList,getContext(),0,HomeFragment.this);
                                binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                                binding.recyclerView.setAdapter(homeAdapter);
                                //DividerItemDecoration divider = new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL);
                                //binding.recyclerView.addItemDecoration(divider);
                            }
                            homeAdapter.notifyDataSetChanged();
                        }else{
                            Toast.makeText(getContext(), Constans.EMPTY_TOAST, Toast.LENGTH_SHORT).show();
                        }
                        binding.recyclerView.setVisibility(View.VISIBLE);
                        binding.spinKit1.setVisibility(View.GONE);

                    }
                });
        observable.unsubscribeOn(Schedulers.io());

        Observable<HomeBanner> bannerObservable  = RequestService.getInstance().getApi().getHomeBanner(Constans.HOME_BANNER);
        bannerObservable.subscribeOn(Schedulers.io()) // 在子线程中进行Http访问
                .observeOn(AndroidSchedulers.mainThread()) // UI线程处理返回接口
                .subscribe(new BaseObsever<HomeBanner>() { // 订阅

                    @Override
                    public void onNext(HomeBanner postInfo) {
//                        Log.i("http返回：", postInfo.toString() + "");
                        if(postInfo.getCode() == 200 && postInfo.getData().size() > 0){
                            if(bannerList == null){
                                bannerList = new ArrayList<>();
                            }
                            bannerList.clear();
                            List<HomeBanner.DataDTO> newList = postInfo.getData();
                            bannerList.addAll(newList) ;

                            if(imageAdapter == null){
                                imageAdapter = new ImageAdapter(bannerList);
                                binding.banner.setAdapter(imageAdapter)
                                        .addBannerLifecycleObserver(getActivity())
                                        .setIndicator(new CircleIndicator(getContext()));
                            }

                            imageAdapter.notifyDataSetChanged();
                        }

                    }
                });
        bannerObservable.unsubscribeOn(Schedulers.io());
    }
    @Override
    public void onStart() {
        super.onStart();
//        Log.i("Tag","onStart");
//        Log.i("Tag","onStart:"+ MediaPlayer.getInstance().isPlay());


    }
    //使用handler定时更新进度条
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler(Looper.myLooper()) {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Constans.UPDATE_AUDIO:
                    updateAudioImg();
                    break;
            }
        }
    };

    private void updateAudioImg() {
        if(binding != null){
            if(SuperMediaPlayer.getInstance().isPlaying()){
                binding.titleBar.goPlay.setVisibility(View.GONE);
                binding.titleBar.titleSpinKit.setVisibility(View.VISIBLE);
            }else {
                binding.titleBar.goPlay.setVisibility(View.VISIBLE);
                binding.titleBar.titleSpinKit.setVisibility(View.GONE);
            }
            handler.sendEmptyMessageDelayed(Constans.UPDATE_AUDIO,500);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
//        Log.i("Tag","onStop");
    }

    @Override
    public void onResume() {
        super.onResume();
//        Log.i("Tag","onResume");
        handler.sendEmptyMessage(Constans.UPDATE_AUDIO);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    @AClick({R.id.go_search, R.id.go_play,R.id.titleSpinKit})
    public void click(View view) {
        switch (view.getId()) {
            case R.id.go_search:
//                Log.i("TAG","onclick");
                searchFragment.showFragment(getActivity().getSupportFragmentManager(),SearchFragment.TAG);
                break;
            case R.id.go_play:
            case R.id.titleSpinKit:
                //                IntentAction.startService(getActivity(), AudioService.class, (ImageView) view, DataBaseHelper.getInstance(getActivity()));
//                Bundle bundle = DataBaseHelper.getInstance(getActivity()).queryPlayHistoryRecent();
                toNextActivity();
                break;
        }
    }

    private void toNextActivity(){
        Bundle bundle = SharedPreferences.getOldAudioInfo(getActivity());
//                Log.i("TAG",bundle.getString("src"));
        if(bundle.getString("src") == null){
            Toast.makeText(getActivity(), Constans.NO_OLD_AUDIOINFO, Toast.LENGTH_SHORT).show();
        }else {
            IntentAction.setValueActivity(getActivity(),PlayAudioActivity.class,bundle);
            getActivity().overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        }
    }

    @Override
    public void deleteItem(Long albumId) {

    }
}
