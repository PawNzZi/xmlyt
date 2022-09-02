package cn.lingyikz.soundbook.soundbook.home.activity;

import android.annotation.SuppressLint;
import android.app.Activity;

import android.content.Intent;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.liys.onclickme.LOnClickMe;
import com.liys.onclickme_annotations.AClick;

import org.angmarch.views.NiceSpinner;
import org.angmarch.views.OnSpinnerItemSelectedListener;

import java.util.ArrayList;
import java.util.List;


import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import cn.lingyikz.soundbook.soundbook.R;
import cn.lingyikz.soundbook.soundbook.api.RequestService;
import cn.lingyikz.soundbook.soundbook.databinding.ActivityAudiodetailBinding;
import cn.lingyikz.soundbook.soundbook.home.adapter.AudioListAdapter;
import cn.lingyikz.soundbook.soundbook.modle.Album;
import cn.lingyikz.soundbook.soundbook.modle.AlbumDetail;
import cn.lingyikz.soundbook.soundbook.utils.Constans;
import cn.lingyikz.soundbook.soundbook.utils.DataBaseHelper;
import cn.lingyikz.soundbook.soundbook.utils.IntentAction;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class AudioDetailActivity extends Activity implements AudioListAdapter.AudioListen ,OnSpinnerItemSelectedListener{

    private ActivityAudiodetailBinding activityAudiodetailBinding;
    private AudioListAdapter adapter = null;
    private List<AlbumDetail.DataDTO.ListDTO> mList  = new ArrayList<>();

    private int nextPage = 1;
    private int basePostion = 0 ;
    private Album.DataDTO.ListDTO albumDetail ;
    private DataBaseHelper dataBaseHelper;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        overridePendingTransition(R.anim.translate_in,R.anim.translate_in);
        super.onCreate(savedInstanceState);
        activityAudiodetailBinding = ActivityAudiodetailBinding.inflate(getLayoutInflater());
        setContentView(activityAudiodetailBinding.getRoot());
        LOnClickMe.init(this,activityAudiodetailBinding.getRoot());
        this.initData();
    }


    public void initData(){

        Bundle bundle = getIntent().getExtras();
        albumDetail = (Album.DataDTO.ListDTO) bundle.getSerializable("bookObject");
        dataBaseHelper = DataBaseHelper.getInstance(this);
        int count = dataBaseHelper.queryCollectionCount(albumDetail.getId());
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
        activityAudiodetailBinding.recyclerView.setVisibility(View.GONE);
//        activityAudiodetailBinding.spinKit.setVisibility(View.VISIBLE);
        //调接口查询列表
        this.queryList(albumDetail.getId(),true);

    }
    @SuppressLint("SetTextI18n")
    public void queryList(int id,boolean isSpinner){
        activityAudiodetailBinding.recyclerView.setVisibility(View.GONE);
        activityAudiodetailBinding.spinKit.setVisibility(View.VISIBLE);
        basePostion = nextPage ;
        Observable<AlbumDetail> observable  = RequestService.getInstance().getApi().getAlbumDetail(nextPage, Constans.PAGE_SIZE,id);
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
                        if(reslut.getCode() == 200 && reslut.getData().getList().size() > 0 ) {
//                            Log.i("TAG：", reslut.toString() + "");
//                        mList = null ;
                            if(mList == null){
                                mList = new ArrayList<>();
                            }
                            mList.clear();
                            List<AlbumDetail.DataDTO.ListDTO> newList = reslut.getData().getList();
                            mList.addAll(newList);
//                        Log.i("TAG",mList+"");
//                        Log.i("TAG：", mList.get(0).getName() + "");
                            nextPage = reslut.getData().getNextPage();
                            if(adapter == null){
//                                Log.i("TAG","adapter == null");
                                adapter = new AudioListAdapter(mList,AudioDetailActivity.this,AudioDetailActivity.this,0);
                                activityAudiodetailBinding.recyclerView.setLayoutManager(new LinearLayoutManager(AudioDetailActivity.this));
                                activityAudiodetailBinding.recyclerView.setAdapter(adapter);
                                DividerItemDecoration divider = new DividerItemDecoration(AudioDetailActivity.this,DividerItemDecoration.VERTICAL);
                                activityAudiodetailBinding.recyclerView.addItemDecoration(divider);
                                activityAudiodetailBinding.listLen.setText("共"+ reslut.getData().getTotal() +"集");
                            }else {
//                                Log.i("TAG","adapter != null" + nextPage);
                                adapter.notifyDataSetChange(basePostion - 1);      //数据没有刷新，是因为mList对象被指向新的地址，

                            }

                            if(isSpinner){
                                int pages = reslut.getData().getPages();
//                                totalCount  = reslut.getData().getTotal();
                                List<String> spinnerItemList = new ArrayList<>();
                                if(pages == 1){
                                    for (int i = 0; i < pages + 1; i++) {
                                        String str = "" ;
                                        str = "第1"+"—"+ reslut.getData().getTotal() +"集";
//                                       str = "第" + (i*SIZE+1) +"—"+(reslut.getData().getTotal() - (i*SIZE)) +  +"集";
//                                        Log.i("TAG","STR:"+str);
                                        spinnerItemList.add(str);
                                    }
                                }else if(pages > 1){
                                    for (int i = 0; i < pages; i++) {
                                        String str = "" ;
                                        if(i == pages - 1){
                                            str = "第" + (i*Constans.PAGE_SIZE+1) +"—"+ reslut.getData().getTotal() +"集";
//                                        str = "第" + (i*SIZE+1) +"—"+(reslut.getData().getTotal() - (i*SIZE)) +  +"集";
//                                            Log.i("TAG","STR:"+str);
                                        }else {
                                            str = "第" + (i*Constans.PAGE_SIZE+1) +"—"+(i+1)*Constans.PAGE_SIZE+"集";
//                                            Log.i("TAG","STR1:"+str);
                                        }

                                        spinnerItemList.add(str);
                                    }
                                }

                                activityAudiodetailBinding.niceSpinner.attachDataSource(spinnerItemList);
                                activityAudiodetailBinding.niceSpinner.setOnSpinnerItemSelectedListener(AudioDetailActivity.this);
                            }
                        }else if(reslut.getCode() == 200 && reslut.getData().getList().size() == 0){
                            Toast.makeText(AudioDetailActivity.this, Constans.ALBUM_CONTENT_NULL, Toast.LENGTH_SHORT).show();
                            activityAudiodetailBinding.niceSpinner.setVisibility(View.GONE);
                            activityAudiodetailBinding.listLen.setText(Constans.ALBUM_CONTENT_NULL);
                        }else {
                            Toast.makeText(AudioDetailActivity.this, reslut.getMessage(), Toast.LENGTH_SHORT).show();
                            activityAudiodetailBinding.niceSpinner.setVisibility(View.GONE);
                            activityAudiodetailBinding.listLen.setText("共"+ 0 +"集");
                        }

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                activityAudiodetailBinding.recyclerView.setVisibility(View.VISIBLE);
                                activityAudiodetailBinding.spinKit.setVisibility(View.GONE);
                            }
                        },500);
                    }
                });
    }

    @AClick({R.id.backIcon,R.id.collection})
    public void click(View view) {
        switch (view.getId()) {
            case R.id.backIcon:
                finish();
                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                break;
            case R.id.collection:
                //收藏
//                Log.i("TAG","collection");
                if(mList.size() == 0){
                    Toast.makeText(this, Constans.DO_NOT_COLLECT, Toast.LENGTH_SHORT).show();
                }else {
                    dataBaseHelper = DataBaseHelper.getInstance(this);
                    int count = dataBaseHelper.queryCollectionCount(albumDetail.getId());
                    if( count > 0 ){
                        //取消
                        dataBaseHelper.cancleCollection(albumDetail.getId());
                        Glide.with(activityAudiodetailBinding.getRoot()).load(R.mipmap.unlike).into(activityAudiodetailBinding.collection);
                    }else{
                        //关注
                        dataBaseHelper.addCollection(albumDetail);
                        Glide.with(activityAudiodetailBinding.getRoot()).load(R.mipmap.like).into(activityAudiodetailBinding.collection);
                    }
                    dataBaseHelper.close();
                }
             
                break;
        }
    }

    @Override
    public void onAudioPlay(Bundle bundle){

        IntentAction.setValueActivity(this,PlayAudioActivity.class,bundle);
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);

//        Intent intent = new Intent(this, AudioService.class);
//        long audioDuration = dataBaseHelper.queryPlayHistory(bundle.getInt("albumId"),bundle.getInt("audioId"));
//        dataBaseHelper.close();
//        Bundle oldAudioInfo =  SharedPreferences.getOldAudioInfo(this);
//        if(oldAudioInfo.getString("src" ) != null){
//
//            if(oldAudioInfo.getString("src" ).equals(bundle.getString("src" ))){
////                Log.i("TAG","暂停");
//
//                bundle.putBoolean("continuePlay",true);
//
//            }else{
//                //stop并重新播放
////                Log.i("TAG","stop并重新播放");
//
//                bundle.putBoolean("continuePlay",false);
//            }
//        }else {
//
//            bundle.putBoolean("continuePlay",false);
//        }
//        bundle.putLong("audioDuration", audioDuration);
//        intent.putExtras(bundle);
//        startService(intent);
//        //播放时增加这条记录
//        dataBaseHelper = DataBaseHelper.getInstance(this);
//        dataBaseHelper.addPlayHistory(map);
//        dataBaseHelper.close();


    }

    @Override
    public void onDeleteItem(int albumId) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        activityAudiodetailBinding = null ;
    }

    @Override
    public void onItemSelected(NiceSpinner parent, View view, int position, long id) {
        if(albumDetail.getId() != 0){
            nextPage = position + 1;
//            Log.i("TAG","onItemSelected"+nextPage);
            this.queryList(albumDetail.getId(),false);
        }
    }
}
