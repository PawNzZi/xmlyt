package cn.lingyikz.soundbook.soundbook.home.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.liys.onclickme.LOnClickMe;
import com.liys.onclickme_annotations.AClick;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import cn.lingyikz.soundbook.soundbook.R;
import cn.lingyikz.soundbook.soundbook.api.RequestService;
import cn.lingyikz.soundbook.soundbook.databinding.ActivityAudiodetailBinding;
import cn.lingyikz.soundbook.soundbook.home.adapter.AudioListAdapter;
import cn.lingyikz.soundbook.soundbook.home.adapter.HomeAdapter;
import cn.lingyikz.soundbook.soundbook.modle.Album;
import cn.lingyikz.soundbook.soundbook.modle.AlbumDetail;
import cn.lingyikz.soundbook.soundbook.pojo.ItemAudioDetail;
import cn.lingyikz.soundbook.soundbook.pojo.ItemHome;
import cn.lingyikz.soundbook.soundbook.service.AudioService;
import cn.lingyikz.soundbook.soundbook.utils.DataBaseHelper;
import cn.lingyikz.soundbook.soundbook.utils.SharedPreferences;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class AudioDetailActivity extends Activity implements AudioListAdapter.AudioListen {

    private ActivityAudiodetailBinding activityAudiodetailBinding;
    private AudioListAdapter adapter ;
    private List<AlbumDetail.DataDTO.ListDTO> mList ;
    private static final int SIZE = 10;
    private int nextPage = 1;
    private int totalPage = 0 ;
    private boolean hasNextPage = false;
    private Album.DataDTO.ListDTO albumDetail ;
    private DataBaseHelper dataBaseHelper;
    private boolean isCollection = false ;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityAudiodetailBinding = ActivityAudiodetailBinding.inflate(getLayoutInflater());
        setContentView(activityAudiodetailBinding.getRoot());
        LOnClickMe.init(this,activityAudiodetailBinding.getRoot());
        this.initData();
    }


    public void initData(){

        Bundle bundle = getIntent().getExtras();
        albumDetail = (Album.DataDTO.ListDTO) bundle.getSerializable("bookObject");
//        database = new DataBaseHelper(this);
        dataBaseHelper = DataBaseHelper.getInstance(this);
        dataBaseHelper.getReadLink();
        int count = dataBaseHelper.queryCollectionCount(albumDetail.getId(),SharedPreferences.getUUID(this));
        dataBaseHelper.close();
        if(count == 0 ){
//            Log.i("TAG","结果为空");

        }else if(count > 0){

            Glide.with(activityAudiodetailBinding.getRoot()).load(R.mipmap.like).into(activityAudiodetailBinding.collection);
        }

        //设置图片圆角
        Glide.with(activityAudiodetailBinding.getRoot()).applyDefaultRequestOptions(RequestOptions.bitmapTransform(new CircleCrop())).load(albumDetail.getThumb()).into(activityAudiodetailBinding.bookThumb);
        activityAudiodetailBinding.bookName.setText(albumDetail.getName());
        activityAudiodetailBinding.bookAuthor.setText(albumDetail.getDescription());

        //调接口查询列表
        this.queryList(albumDetail.getId());
    }
    @SuppressLint("SetTextI18n")
    public void queryList(int id){
        Observable<AlbumDetail> observable  = RequestService.getInstance().getApi().getAlbumDetail(nextPage,SIZE,id);
        observable.subscribeOn(Schedulers.io()) // 在子线程中进行Http访问
                .observeOn(AndroidSchedulers.mainThread()) // UI线程处理返回接口
                .subscribe(new Observer<AlbumDetail>() { // 订阅

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(AlbumDetail reslut) {
                        Log.i("http返回：", reslut.toString() + "");
                        mList = reslut.getData().getList();
                        hasNextPage = reslut.getData().getHasNextPage();
                        nextPage = reslut.getData().getNextPage();

                        adapter = new AudioListAdapter(mList,AudioDetailActivity.this,AudioDetailActivity.this);
                        activityAudiodetailBinding.recyclerView.setLayoutManager(new LinearLayoutManager(AudioDetailActivity.this));
                        activityAudiodetailBinding.recyclerView.setAdapter(adapter);
                        DividerItemDecoration divider = new DividerItemDecoration(AudioDetailActivity.this,DividerItemDecoration.VERTICAL);
                        activityAudiodetailBinding.recyclerView.addItemDecoration(divider);
                        activityAudiodetailBinding.listLen.setText("共"+ reslut.getData().getTotal() +"集");

                    }
                });


    }


    @AClick({R.id.backIcon,R.id.collection})
    public void click(View view) {
        switch (view.getId()) {
            case R.id.backIcon:
                finish();
                break;
            case R.id.collection:
                //收藏
                Log.i("TAG","collection");
                String sql = "";
                dataBaseHelper = DataBaseHelper.getInstance(this);
                dataBaseHelper.getWriteLink();
                int count = dataBaseHelper.queryCollectionCount(albumDetail.getId(),SharedPreferences.getUUID(this));
                if( count > 0 ){
                    //取消
                    dataBaseHelper.cancleCollection(albumDetail.getId(),SharedPreferences.getUUID(this));
                    Glide.with(activityAudiodetailBinding.getRoot()).load(R.mipmap.unlike).into(activityAudiodetailBinding.collection);
                }else{
                    //关注
                    dataBaseHelper.addCollection(albumDetail,SharedPreferences.getUUID(this));
                    Glide.with(activityAudiodetailBinding.getRoot()).load(R.mipmap.like).into(activityAudiodetailBinding.collection);
                }
                dataBaseHelper.close();
                break;
        }
    }

    @Override
    public void onAudioPlay(Map<String,Object> map) {
        Intent intent = new Intent(this, AudioService.class);
        Map<String,Object> oldAudioInfo =  SharedPreferences.getOldAudioInfo(this);

        if(oldAudioInfo.get("src") != null){
            if(oldAudioInfo.get("src").toString().equals(map.get("src").toString())){
                //暂停
//                Log.i("TAG","暂停");
                intent.putExtra("continue",true);
                map.put("currentPosition",oldAudioInfo.get("currentPosition"));
            }else{
                //stop并重新播放
//                Log.i("TAG","stop并重新播放");
                intent.putExtra("continue",false);
                map.put("currentPosition",0L);
            }
        }else {
            intent.putExtra("continue",false);
            map.put("currentPosition",0L);
        }

        intent.putExtra("path",map.get("src").toString());
        SharedPreferences.saveOldAudioInfo(this,map);
        startService(intent);
        dataBaseHelper = DataBaseHelper.getInstance(this);
        dataBaseHelper.addPlayHistory(map);


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        activityAudiodetailBinding = null ;
    }
}
