package cn.lingyikz.soundbook.soundbook.home.activity;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import com.liys.onclickme.LOnClickMe;
import com.liys.onclickme_annotations.AClick;
import com.wyt.searchbox.SearchFragment;
import com.wyt.searchbox.custom.IOnSearchClickListener;

import java.util.ArrayList;
import java.util.List;

import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import cn.lingyikz.soundbook.soundbook.R;
import cn.lingyikz.soundbook.soundbook.api.RequestService;
import cn.lingyikz.soundbook.soundbook.base.BaseObsever;
import cn.lingyikz.soundbook.soundbook.databinding.ActivitySearchlistBinding;
import cn.lingyikz.soundbook.soundbook.home.adapter.HomeAdapter;
import cn.lingyikz.soundbook.soundbook.main.BaseFragmentActivity;
import cn.lingyikz.soundbook.soundbook.modle.Album;
import cn.lingyikz.soundbook.soundbook.utils.Constans;
import cn.lingyikz.soundbook.soundbook.utils.IntentAction;
import cn.lingyikz.soundbook.soundbook.utils.SharedPreferences;
import cn.lingyikz.soundbook.soundbook.utils.SuperMediaPlayer;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class SearchActivity extends BaseFragmentActivity implements HomeAdapter.ItemOperaCallBack {

    private ActivitySearchlistBinding binding ;
    private HomeAdapter homeAdapter = null;
    private List<Album.DataDTO.ListDTO> mList = new ArrayList<>() ;
    private int nextPage = 1;
    private SearchFragment searchFragment ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected void setData() {

    }

    @Override
    protected void setView() {

    }

    @Override
    protected View setLayout() {
        binding = ActivitySearchlistBinding.inflate(getLayoutInflater());
        LOnClickMe.init(this,binding.getRoot());
        Bundle bundle = getIntent().getExtras();
        String keyword = bundle.getString("keyword");
        this.initView(keyword);
        this.searchList(keyword);
        return binding.getRoot();
    }

    public void initView(String keyword){
        searchFragment = SearchFragment.newInstance();
        searchFragment.setOnSearchClickListener(iOnSearchClickListener);
        binding.titleBar.title.setText(keyword);
        binding.titleBar.toSearch.setVisibility(View.VISIBLE);
        binding.titleBar.goBacK.setVisibility(View.VISIBLE);
        binding.titleBar.goSearch.setVisibility(View.GONE);
        binding.spinKit.setVisibility(View.VISIBLE);


    }
    private IOnSearchClickListener iOnSearchClickListener = new IOnSearchClickListener() {
        @Override
        public void OnSearchClick(String keyword) {
            searchList(keyword);
        }
    };
    public void searchList(String keyword){

        Observable<Album> observable  = RequestService.getInstance().getApi().getPostInfo(nextPage,Constans.PAGE_SIZE,keyword);
        observable.subscribeOn(Schedulers.io()) // 在子线程中进行Http访问
                .observeOn(AndroidSchedulers.mainThread()) // UI线程处理返回接口
                .subscribe(new BaseObsever<Album>() { // 订阅

                    @Override
                    public void onNext(Album postInfo) {
                        if(postInfo.getCode() == 200 && postInfo.getData().getList().size() > 0){
//                            Log.i("http返回：", postInfo.toString() + "");
                            if(mList == null) {
                                mList = new ArrayList<>();
                            }
                            mList.clear();
                            List<Album.DataDTO.ListDTO> newList = postInfo.getData().getList();
                            nextPage = postInfo.getData().getNextPage();
                            mList.addAll(newList);
                            if(homeAdapter == null){
                                homeAdapter = new HomeAdapter(mList,SearchActivity.this,0,SearchActivity.this);
                                binding.recyclerView.setLayoutManager(new LinearLayoutManager(SearchActivity.this));
                                binding.recyclerView.setAdapter(homeAdapter);
                                DividerItemDecoration divider = new DividerItemDecoration(SearchActivity.this,DividerItemDecoration.VERTICAL);
                                binding.recyclerView.addItemDecoration(divider);
                            }
                            homeAdapter.notifyDataSetChanged();
                        }else{
                            Toast.makeText(SearchActivity.this, Constans.EMPTY_TOAST, Toast.LENGTH_SHORT).show();
                        }
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                binding.recyclerView.setVisibility(View.VISIBLE);
                                binding.spinKit.setVisibility(View.GONE);
                            }
                        },700);

                    }
                });
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
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onResume() {
        super.onResume();
        handler.sendEmptyMessage(Constans.UPDATE_AUDIO);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null ;
    }

    @AClick({R.id.go_bacK, R.id.go_play,R.id.titleSpinKit,R.id.to_search})
    public void click(View view) {
        switch (view.getId()) {
            case R.id.go_bacK:
            case R.id.titleSpinKit:
                finish();
                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                break;
            case R.id.go_play:
                Bundle bundle = SharedPreferences.getOldAudioInfo(this);
                if(bundle.getString("src") == null){
                    Toast.makeText(this, Constans.NO_OLD_AUDIOINFO, Toast.LENGTH_SHORT).show();
                }else {
                    IntentAction.setValueActivity(this,PlayAudioActivity.class,bundle);
                    overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                }
                break;
            case R.id.to_search:
                searchFragment.showFragment(getSupportFragmentManager(),SearchFragment.TAG);
                break;
        }
    }

    @Override
    public void deleteItem(int albumId) {

    }
}