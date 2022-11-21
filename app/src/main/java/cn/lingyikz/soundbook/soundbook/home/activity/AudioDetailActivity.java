package cn.lingyikz.soundbook.soundbook.home.activity;

import android.annotation.SuppressLint;


import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.arialyy.annotations.Download;
import com.arialyy.aria.core.Aria;
import com.arialyy.aria.core.task.DownloadTask;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.kongzue.dialogx.dialogs.PopTip;
import com.liys.onclickme.LOnClickMe;
import com.liys.onclickme_annotations.AClick;

import org.angmarch.views.NiceSpinner;
import org.angmarch.views.OnSpinnerItemSelectedListener;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import cn.hutool.core.util.ObjectUtil;
import cn.lingyikz.soundbook.soundbook.R;
import cn.lingyikz.soundbook.soundbook.api.RequestService;
import cn.lingyikz.soundbook.soundbook.base.BaseObsever;
import cn.lingyikz.soundbook.soundbook.databinding.ActivityAudiodetailBinding;
import cn.lingyikz.soundbook.soundbook.home.adapter.AudioListAdapter;
import cn.lingyikz.soundbook.soundbook.main.BaseActivity;
import cn.lingyikz.soundbook.soundbook.modle.v2.Album;
import cn.lingyikz.soundbook.soundbook.modle.AlbumDetail;
import cn.lingyikz.soundbook.soundbook.modle.v2.AlbumSound;
import cn.lingyikz.soundbook.soundbook.modle.v2.BaseModel;
import cn.lingyikz.soundbook.soundbook.utils.Constans;
import cn.lingyikz.soundbook.soundbook.utils.DataBaseHelper;
import cn.lingyikz.soundbook.soundbook.utils.IntentAction;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class AudioDetailActivity extends BaseActivity implements AudioListAdapter.AudioListen ,OnSpinnerItemSelectedListener{

    private ActivityAudiodetailBinding activityAudiodetailBinding;
    private AudioListAdapter adapter = null;
    private List<AlbumSound.DataDTO.RowsDTO> mList  = new ArrayList<>();

    private int nextPage = 1;
    private int basePostion = 0 ;
    private Album.DataDTO.RowsDTO albumDetail ;

    @Override
    protected void setData() {
        this.initData();
    }

    @Override
    protected void setView() {
        Bundle bundle = getIntent().getExtras();
        albumDetail = (Album.DataDTO.RowsDTO) bundle.getSerializable("bookObject");
        if(ObjectUtil.isNotNull(Constans.user)){
            this.isCollection(albumDetail.getId());
        }

        //设置图片圆角
        Glide.with(activityAudiodetailBinding.getRoot()).applyDefaultRequestOptions(RequestOptions.bitmapTransform(new CircleCrop())).load(albumDetail.getThumb()).into(activityAudiodetailBinding.bookThumb);
        activityAudiodetailBinding.bookName.setText(albumDetail.getName());
        activityAudiodetailBinding.bookAuthor.setText(albumDetail.getDescription());
        activityAudiodetailBinding.recyclerView.setVisibility(View.GONE);
    }

    @Override
    protected View setLayout() {
        activityAudiodetailBinding = ActivityAudiodetailBinding.inflate(getLayoutInflater());
        LOnClickMe.init(this,activityAudiodetailBinding.getRoot());
        return activityAudiodetailBinding.getRoot();
    }


    public void initData(){

//        activityAudiodetailBinding.spinKit.setVisibility(View.VISIBLE);
        //调接口查询列表
//        Log.i("TAG","ID:"+albumDetail.getId());
//        Aria.download(this).register();
        this.queryList(albumDetail.getId(),true);

    }
    @SuppressLint("SetTextI18n")
    public void queryList(Long id,boolean isSpinner){
        activityAudiodetailBinding.recyclerView.setVisibility(View.GONE);
        activityAudiodetailBinding.spinKit.setVisibility(View.VISIBLE);
        basePostion = nextPage ;
        Observable<AlbumSound> observable  = RequestService.getInstance().getApi().getAlbumDetail(nextPage, Constans.PAGE_SIZE,id);
        observable.subscribeOn(Schedulers.io()) // 在子线程中进行Http访问
                .observeOn(AndroidSchedulers.mainThread()) // UI线程处理返回接口
                .subscribe(new BaseObsever<AlbumSound>() { // 订阅
                    @Override
                    public void onNext(AlbumSound reslut) {
                        if(reslut.getCode() == 200 && reslut.getData().getRows().size() > 0 ) {
                            if(mList == null){
                                mList = new ArrayList<>();
                            }
                            mList.clear();
                            List<AlbumSound.DataDTO.RowsDTO> newList = reslut.getData().getRows();
                            mList.addAll(newList);
                            nextPage = reslut.getData().getNextPage();
                            if(adapter == null){
                                adapter = new AudioListAdapter(mList,AudioDetailActivity.this,AudioDetailActivity.this);
                                activityAudiodetailBinding.recyclerView.setLayoutManager(new LinearLayoutManager(AudioDetailActivity.this));
                                activityAudiodetailBinding.recyclerView.setAdapter(adapter);
                                DividerItemDecoration divider = new DividerItemDecoration(AudioDetailActivity.this,DividerItemDecoration.VERTICAL);
                                activityAudiodetailBinding.recyclerView.addItemDecoration(divider);
                                activityAudiodetailBinding.listLen.setText("共"+ reslut.getData().getTotalRows() +"集");
                            }else {
//                                Log.i("TAG","adapter != null" + nextPage);
                                adapter.notifyDataSetChange(basePostion - 1);      //数据没有刷新，是因为mList对象被指向新的地址，

                            }

                            if(isSpinner){
                                int pages = reslut.getData().getTotalPage();
//                                totalCount  = reslut.getData().getTotal();
                                List<String> spinnerItemList = new ArrayList<>();
                                if(pages == 1){
                                    for (int i = 0; i < pages + 1; i++) {
                                        String str = "" ;
                                        str = "第1—"+ reslut.getData().getTotalRows() +"集";
//                                       str = "第" + (i*SIZE+1) +"—"+(reslut.getData().getTotal() - (i*SIZE)) +  +"集";
//                                        Log.i("TAG","STR:"+str);
                                        spinnerItemList.add(str);
                                    }
                                }else if(pages > 1){
                                    for (int i = 0; i < pages; i++) {
                                        String str = "" ;
                                        if(i == pages - 1){
                                            str = "第" + (i*Constans.PAGE_SIZE+1) +"—"+ reslut.getData().getTotalRows() +"集";
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
                        }else if(reslut.getCode() == 200 && reslut.getData().getRows().size() == 0){
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
        observable.unsubscribeOn(Schedulers.io());
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
                if(ObjectUtil.isNotNull(Constans.user)){
                   this.changeCollection(albumDetail.getId());
                }else {
                    PopTip.show("请先登录,登录后才能收藏").showLong();
                }
             
                break;
        }
    }

    private void isCollection(Long albumId){
        Observable<BaseModel> observable  = RequestService.getInstance().getApi().isCollection(Constans.user.getId(),albumId);
        observable.subscribeOn(Schedulers.io()) // 在子线程中进行Http访问
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObsever<BaseModel>() {
                    @Override
                    public void onNext(BaseModel baseModel) {
                        if(baseModel.getCode() == 200 && Integer.parseInt(baseModel.getData()) == 0){
                            Glide.with(activityAudiodetailBinding.getRoot()).load(R.mipmap.like).into(activityAudiodetailBinding.collection);
                        }

                    }
                });
        observable.unsubscribeOn(Schedulers.io());
    }
    private void changeCollection(Long albumId){
        Observable<BaseModel> observable  = RequestService.getInstance().getApi().changeCollection(Constans.user.getId(),albumId);
        observable.subscribeOn(Schedulers.io()) // 在子线程中进行Http访问
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObsever<BaseModel>() {
                    @Override
                    public void onNext(BaseModel baseModel) {
                        if(baseModel.getCode() == 200 && Integer.parseInt(baseModel.getData()) == 0){
                            Glide.with(activityAudiodetailBinding.getRoot()).load(R.mipmap.like).into(activityAudiodetailBinding.collection);
                        }else if(baseModel.getCode() == 200 && Integer.parseInt(baseModel.getData()) == 1){
                            Glide.with(activityAudiodetailBinding.getRoot()).load(R.mipmap.unlike).into(activityAudiodetailBinding.collection);
                        }else {
                            PopTip.show("修改失败："+baseModel.getMessage()).showLong();
                        }

                    }
                });
        observable.unsubscribeOn(Schedulers.io());
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
    public void downLoad(String url, String targetPath, ProgressBar progressBar) {
        Aria.download(this)
                .load(url)     //读取下载地址
                .setFilePath(targetPath) //设置文件保存的完整路径
                .create();   //创建并启动下载

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
