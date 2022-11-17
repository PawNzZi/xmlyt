package cn.lingyikz.soundbook.soundbook.user;


import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;


import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.liys.onclickme.LOnClickMe;
import com.liys.onclickme_annotations.AClick;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;
import cn.hutool.core.util.ObjectUtil;
import cn.lingyikz.soundbook.soundbook.R;
import cn.lingyikz.soundbook.soundbook.databinding.FragmentUserBinding;
import cn.lingyikz.soundbook.soundbook.home.activity.PlayAudioActivity;
import cn.lingyikz.soundbook.soundbook.main.BaseFragment;
import cn.lingyikz.soundbook.soundbook.service.AudioService;
import cn.lingyikz.soundbook.soundbook.user.activity.LoginActivity;
import cn.lingyikz.soundbook.soundbook.user.activity.SettingActivity;
import cn.lingyikz.soundbook.soundbook.user.fragment.CollectionFragment;
import cn.lingyikz.soundbook.soundbook.user.fragment.PlayHistoryFragment;
import cn.lingyikz.soundbook.soundbook.utils.Constans;
import cn.lingyikz.soundbook.soundbook.utils.DataBaseHelper;
import cn.lingyikz.soundbook.soundbook.utils.IntentAction;
import cn.lingyikz.soundbook.soundbook.utils.MediaPlayer;
import cn.lingyikz.soundbook.soundbook.utils.SharedPreferences;
import cn.lingyikz.soundbook.soundbook.utils.SuperMediaPlayer;

/**
 * Created by 1085054 on 2022-8-17.
 */

public class UserFragment extends BaseFragment {

    private static FragmentUserBinding binding ;

    private List<Fragment> fragmentList ;
    private List<String> titleList ;

    public static UserFragment newInstance() {
        return new UserFragment();
    }

    @Override
    protected View setLayout(LayoutInflater inflater, ViewGroup container) {
        binding = FragmentUserBinding.inflate(LayoutInflater.from(getActivity()),container,false);
        LOnClickMe.init(this,binding.getRoot());
        return binding.getRoot();
    }

    @Override
    protected void setView() {
        binding.titleBar.setting.setVisibility(View.VISIBLE);
        binding.titleBar.goSearch.setVisibility(View.GONE);
        binding.titleBar.goBacK.setVisibility(View.GONE);
        binding.titleBar.title.setText("我的");
        if (ObjectUtil.isNull(Constans.user)) {
            binding.userName.setText("点击登录");
        } else {
            binding.userName.setText(Constans.user.getNickname());
        }
    }

    @Override
    protected void setData() {
        initData();
    }

    private void initData() {
        fragmentList = new ArrayList<>();
        fragmentList.add(PlayHistoryFragment.newInstance());
        fragmentList.add(CollectionFragment.newInstance());

        titleList = new ArrayList<>();
        titleList.add("播放历史");
        titleList.add("收藏列表");

        binding.viewPager.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);
        binding.viewPager.setAdapter(new ViewPageAdatper(getActivity()));
        binding.viewPager.setOffscreenPageLimit(1);
        TabLayoutMediator mediator = new TabLayoutMediator(binding.tabLayout, binding.viewPager, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                tab.setText(titleList.get(position));
            }
        });
        mediator.attach();
        binding.viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
//                Log.i("TAG","position");
            }
        });

    }

    @AClick({R.id.go_play, R.id.userAvatar,R.id.titleSpinKit,R.id.setting,R.id.userName})
    public void click(View view) {
        switch (view.getId()) {
            case R.id.go_play:
            case R.id.titleSpinKit:
//                IntentAction.startService(getActivity(), AudioService.class, (ImageView) view, DataBaseHelper.getInstance(getActivity()));
//                Bundle bundle = DataBaseHelper.getInstance(getActivity()).queryPlayHistoryRecent();
                Bundle bundle = SharedPreferences.getOldAudioInfo(getActivity());
                if(bundle.getString("src") == null){
                    Toast.makeText(getActivity(), Constans.NO_OLD_AUDIOINFO, Toast.LENGTH_SHORT).show();
                }else {
                    IntentAction.setValueActivity(getActivity(),PlayAudioActivity.class,bundle);
                }
                break;
            case R.id.userAvatar:
                break;
            case R.id.setting:
                IntentAction.toNextActivity(getActivity(), SettingActivity.class);
                break;
            case R.id.userName:
                IntentAction.toNextActivity(getActivity(), LoginActivity.class);
                break;
        }
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
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onResume() {
        super.onResume();
        handler.sendEmptyMessage(Constans.UPDATE_AUDIO);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null ;


    }

    public class ViewPageAdatper extends FragmentStateAdapter{


        public ViewPageAdatper(@NonNull  FragmentActivity fragmentActivity) {
            super(fragmentActivity);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getItemCount() {
            return fragmentList.size();
        }
    }
}
