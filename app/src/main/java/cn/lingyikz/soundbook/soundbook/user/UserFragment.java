package cn.lingyikz.soundbook.soundbook.user;

import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.liys.onclickme.LOnClickMe;
import com.liys.onclickme_annotations.AClick;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;
import cn.lingyikz.soundbook.soundbook.R;
import cn.lingyikz.soundbook.soundbook.databinding.FragmentUserBinding;
import cn.lingyikz.soundbook.soundbook.home.adapter.HomeAdapter;
import cn.lingyikz.soundbook.soundbook.pojo.ItemHome;
import cn.lingyikz.soundbook.soundbook.service.AudioService;
import cn.lingyikz.soundbook.soundbook.user.fragment.CollectionFragment;
import cn.lingyikz.soundbook.soundbook.user.fragment.PlayHistoryFragment;
import cn.lingyikz.soundbook.soundbook.utils.IntentAction;
import cn.lingyikz.soundbook.soundbook.utils.MediaPlayer;

/**
 * Created by 1085054 on 2022-8-17.
 */

public class UserFragment extends Fragment {

    private FragmentUserBinding binding ;

    private List<Fragment> fragmentList ;
    private List<String> titleList ;

    private HomeAdapter adapter ;
    private List<ItemHome> mList;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentUserBinding.inflate(LayoutInflater.from(getActivity()),container,false);
        View view = binding.getRoot();
        LOnClickMe.init(this,binding.getRoot());
        initData();
        return view;
    }

    private void initData() {
        binding.titleBar.goSearch.setVisibility(View.GONE);
        binding.titleBar.goBacK.setVisibility(View.GONE);
        binding.titleBar.title.setText("我的");


        fragmentList = new ArrayList<>();
        fragmentList.add(new PlayHistoryFragment());
        fragmentList.add(new CollectionFragment());

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

    @AClick({R.id.go_play, R.id.userAvatar})
    public void click(View view) {
        switch (view.getId()) {
            case R.id.go_play:
                IntentAction.startService(getActivity(), AudioService.class, (ImageView) view);
                break;
            case R.id.userAvatar:
                break;
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if(MediaPlayer.getInstance().isPlay()){
            binding.titleBar.goPlay.setImageDrawable(getResources().getDrawable(R.mipmap.title_play, getResources().newTheme()));
        }else {
            binding.titleBar.goPlay.setImageDrawable(getResources().getDrawable(R.mipmap.title_pause, getResources().newTheme()));
        }
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