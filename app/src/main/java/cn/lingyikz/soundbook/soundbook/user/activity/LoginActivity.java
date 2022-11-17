package cn.lingyikz.soundbook.soundbook.user.activity;

import android.view.View;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.liys.onclickme.LOnClickMe;
import com.liys.onclickme_annotations.AClick;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;
import cn.lingyikz.soundbook.soundbook.R;
import cn.lingyikz.soundbook.soundbook.databinding.ActivityLoginBinding;
import cn.lingyikz.soundbook.soundbook.main.BaseFragmentActivity;
import cn.lingyikz.soundbook.soundbook.user.fragment.LoginFragment;
import cn.lingyikz.soundbook.soundbook.user.fragment.RegisterFragment;

public class LoginActivity extends BaseFragmentActivity {

    private ActivityLoginBinding binding ;
    private List<Fragment> fragmentList ;
    private List<String> titleList ;

    @Override
    protected void setData() {
       this.initData();
    }

    @Override
    protected void setView() {
        binding.titleBar.setting.setVisibility(View.GONE);
        binding.titleBar.goSearch.setVisibility(View.GONE);
        binding.titleBar.goBacK.setVisibility(View.VISIBLE);
        binding.titleBar.title.setVisibility(View.GONE);
        binding.titleBar.goPlay.setVisibility(View.GONE);
    }

    @Override
    protected View setLayout() {
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        LOnClickMe.init(this,binding.getRoot());
        return binding.getRoot();
    }

    private void initData() {
        fragmentList = new ArrayList<>();
        fragmentList.add(LoginFragment.newInstance());
        fragmentList.add(RegisterFragment.newInstance());

        titleList = new ArrayList<>();
        titleList.add("登录");
        titleList.add("注册");

        binding.viewPager.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);
        binding.viewPager.setAdapter(new LoginActivity.ViewPageAdatper(LoginActivity.this));
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

    @AClick({R.id.go_bacK})
    public void click(View view) {
        switch (view.getId()) {
            case R.id.go_bacK:
                finish();
                break;

        }
    }

    public class ViewPageAdatper extends FragmentStateAdapter {


        public ViewPageAdatper(@NonNull FragmentActivity fragmentActivity) {
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

    @Override
    public void onDestroy() {
        binding = null ;
        super.onDestroy();

    }
}
