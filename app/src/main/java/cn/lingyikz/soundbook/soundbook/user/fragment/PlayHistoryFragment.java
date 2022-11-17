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
import cn.lingyikz.soundbook.soundbook.databinding.FragmentPalyhistoryBinding;
import cn.lingyikz.soundbook.soundbook.home.activity.PlayAudioActivity;
import cn.lingyikz.soundbook.soundbook.home.adapter.AudioListAdapter;
import cn.lingyikz.soundbook.soundbook.main.BaseFragment;
import cn.lingyikz.soundbook.soundbook.modle.AlbumDetail;
import cn.lingyikz.soundbook.soundbook.modle.v2.AlbumSound;
import cn.lingyikz.soundbook.soundbook.service.AudioService;
import cn.lingyikz.soundbook.soundbook.utils.DataBaseHelper;
import cn.lingyikz.soundbook.soundbook.utils.IntentAction;
import cn.lingyikz.soundbook.soundbook.utils.SharedPreferences;

public class PlayHistoryFragment extends BaseFragment implements AudioListAdapter.AudioListen {
    private FragmentPalyhistoryBinding binding ;
    private AudioListAdapter adapter = null;
    private List<AlbumSound.DataDTO.RowsDTO> mList = new ArrayList<>();
    private DataBaseHelper dataBaseHelper ;


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

    }

    @Override
    protected void setData() {

    }

    private void initData() {
//        Log.i("TAG","initData");
//        dataBaseHelper = new DataBaseHelper(getActivity());
        dataBaseHelper = DataBaseHelper.getInstance(getActivity());
        if(mList == null){
            mList = new ArrayList<>();
        }
        mList.clear();
        List<AlbumSound.DataDTO.RowsDTO> newList = dataBaseHelper.queryPlayHistoryAll();
//        Log.i("TAG","PlayHistoryFragment:initData = "+newList.size());
        mList.addAll(newList);
        dataBaseHelper.close();
        if(adapter == null){
            binding.swipeRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            adapter = new AudioListAdapter(mList,getContext(), this,1);
            binding.swipeRecyclerView.setAdapter(adapter);
            DividerItemDecoration divider = new DividerItemDecoration(getActivity(),DividerItemDecoration.VERTICAL);
            binding.swipeRecyclerView.addItemDecoration(divider);
        }
        adapter.notifyDataSetChanged();
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
        initData();
//        Log.i("TAG","PlayHistoryFragment:onResume");

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
//        Log.i("TAG","onDestroy");
        binding = null ;
    }

    @Override
    public void onAudioPlay(Bundle bundle) {
        IntentAction.setValueActivity(getActivity(), PlayAudioActivity.class,bundle);
//        Intent intent = new Intent(getContext(), AudioService.class);
//        long audioDuration = dataBaseHelper.queryPlayHistory(bundle.getInt("albumId"),bundle.getInt("audioId"));
//        dataBaseHelper.close();
//        Bundle oldAudioInfo =  SharedPreferences.getOldAudioInfo(getContext());
//        if(oldAudioInfo.getString("src" ) != null){
//            if(oldAudioInfo.getString("src" ).equals(bundle.getString("src" ))){
////                Log.i("TAG","暂停");
//                bundle.putBoolean("continuePlay",true);
//
//            }else{
//                //stop并重新播放
////                Log.i("TAG","stop并重新播放");
//                bundle.putBoolean("continuePlay",false);
//            }
//        }else {
//            bundle.putBoolean("continuePlay",false);
//        }
//        bundle.putLong("audioDuration", audioDuration);
//        intent.putExtras(bundle);
//        getContext().startService(intent);
//        this.sendBroadCast();

    }

    @Override
    public void onDeleteItem(Long albumId) {
        dataBaseHelper = DataBaseHelper.getInstance(getActivity());
        dataBaseHelper.deletePlayHistory(albumId);
        dataBaseHelper.close();
        initData();
    }

//    public void sendBroadCast(){
//        Intent broadIntent = new Intent(Constans.CHANGE_PLAY_IMG);
//        IntentFilter filter = new IntentFilter();
//        filter.addAction(Constans.CHANGE_PLAY_IMG);
//        //设置接收优先级[-1000,1000]，默认为0
//        filter.setPriority(1000);
//        //实例化广播接收者
//        changePlayReceiver = new UserFragment.ChangePlayReceiver();
//        getContext().registerReceiver(changePlayReceiver,filter);
//        getContext().sendBroadcast(broadIntent);
//    }

}
