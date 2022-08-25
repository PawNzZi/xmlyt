package cn.lingyikz.soundbook.soundbook.home.activity;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.liys.onclickme.LOnClickMe;
import com.liys.onclickme_annotations.AClick;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import cn.lingyikz.soundbook.soundbook.R;

import cn.lingyikz.soundbook.soundbook.api.RequestService;
import cn.lingyikz.soundbook.soundbook.databinding.ActivitySearchlistBinding;
import cn.lingyikz.soundbook.soundbook.home.adapter.HomeAdapter;
import cn.lingyikz.soundbook.soundbook.modle.Album;
import cn.lingyikz.soundbook.soundbook.service.AudioService;
import cn.lingyikz.soundbook.soundbook.utils.IntentAction;
import cn.lingyikz.soundbook.soundbook.utils.MediaPlayer;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class SearchActivity extends Activity {

    private ActivitySearchlistBinding binding ;
    private HomeAdapter homeAdapter ;
    private List<Album.DataDTO.ListDTO> mList ;
    private static final int SIZE = 10;
    private int nextPage = 1;
    private int totalPage = 0 ;
    private boolean hasNextPage = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySearchlistBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        LOnClickMe.init(this,binding.getRoot());
        Bundle bundle = getIntent().getExtras();
        String keyword = bundle.getString("keyword");
        this.initView(keyword);
        this.searchList(keyword);
    }
    public void initView(String keyword){
        binding.titleBar.title.setText(keyword);
        binding.titleBar.goBacK.setVisibility(View.VISIBLE);
        binding.titleBar.goSearch.setVisibility(View.GONE);



    }
    public void searchList(String keyword){

        Observable<Album> observable  = RequestService.getInstance().getApi().getPostInfo(nextPage,SIZE,keyword);
        observable.subscribeOn(Schedulers.io()) // 在子线程中进行Http访问
                .observeOn(AndroidSchedulers.mainThread()) // UI线程处理返回接口
                .subscribe(new Observer<Album>() { // 订阅

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Album postInfo) {
                        Log.i("http返回：", postInfo.toString() + "");
                        hasNextPage = postInfo.getData().getHasNextPage();
                        nextPage = postInfo.getData().getNextPage();

                        mList = new ArrayList<>();
                        mList = postInfo.getData().getList();
                        homeAdapter = new HomeAdapter(mList,SearchActivity.this);
                        binding.recyclerView.setLayoutManager(new LinearLayoutManager(SearchActivity.this));
                        binding.recyclerView.setAdapter(homeAdapter);
                        DividerItemDecoration divider = new DividerItemDecoration(SearchActivity.this,DividerItemDecoration.VERTICAL);
                        binding.recyclerView.addItemDecoration(divider);

                    }
                });
    }
    @Override
    protected void onStart() {
        super.onStart();
        if(MediaPlayer.getInstance().isPlay()){
            binding.titleBar.goPlay.setImageDrawable(getResources().getDrawable(R.mipmap.title_play, getResources().newTheme()));
        }else {
            binding.titleBar.goPlay.setImageDrawable(getResources().getDrawable(R.mipmap.title_pause, getResources().newTheme()));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null ;
    }

    @AClick({R.id.go_bacK, R.id.go_play})
    public void click(View view) {
        switch (view.getId()) {
            case R.id.go_bacK:
                finish();
                break;
            case R.id.go_play:
                IntentAction.startService(SearchActivity.this, AudioService.class, (ImageView) view);
                break;
        }
    }
}