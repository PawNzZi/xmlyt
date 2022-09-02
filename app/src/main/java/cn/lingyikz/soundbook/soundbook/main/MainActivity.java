package cn.lingyikz.soundbook.soundbook.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import cn.lingyikz.soundbook.soundbook.R;
import cn.lingyikz.soundbook.soundbook.category.CategoryFragment;
import cn.lingyikz.soundbook.soundbook.databinding.ActivityMainBinding;
import cn.lingyikz.soundbook.soundbook.home.HomeFragment;
import cn.lingyikz.soundbook.soundbook.service.AudioService;
import cn.lingyikz.soundbook.soundbook.user.UserFragment;
import cn.lingyikz.soundbook.soundbook.utils.Constans;
import cn.lingyikz.soundbook.soundbook.utils.IntentAction;
import cn.lingyikz.soundbook.soundbook.utils.SharedPreferences;
import cn.lingyikz.soundbook.soundbook.utils.UUIDUtils;

public class MainActivity extends FragmentActivity implements BottomNavigationView.OnNavigationItemSelectedListener{


    private FragmentManager fragmentManager ;
    private FragmentTransaction fragmentTransaction ;

    private ActivityMainBinding viewBinding;

    // 设置默认进来是tab 显示的页面
    private void setDefaultFragment(){
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.content,HomeFragment.newInstance());
        fragmentTransaction.commit();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(viewBinding.getRoot());
//        this.checkUUID();
        this.setDefaultFragment();
//        this.deleteDatabase("audio");
        viewBinding.navigation.setOnNavigationItemSelectedListener(this);
        Intent intent = new Intent(this,AudioService.class);
        intent.setAction(Constans.START_SERVICE);
        startService(intent);

    }

    private void checkUUID() {
        String UUID = SharedPreferences.getUUID(this);
//        Log.i("TAG",UUID);
        if(UUID == null){
            //创建新UUID并保存
//            Log.i("TAG","uuid为空");
            SharedPreferences.saveUUid(this,UUIDUtils.createUUID());
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull  MenuItem item) {
        fragmentManager =  getSupportFragmentManager();  //使用fragmentmanager和transaction来实现切换效果
        fragmentTransaction = fragmentManager.beginTransaction();
        switch (item.getItemId()) {
            case R.id.navigation_home:
                fragmentTransaction.replace(R.id.content,HomeFragment.newInstance());  //对应的java class
                fragmentTransaction.commit();  //一定不要忘记commit，否则不会显示
                return true;
            case R.id.navigation_category:
                fragmentTransaction.replace(R.id.content,CategoryFragment.newInstance());  //对应的java class
                fragmentTransaction.commit();  //一定不要忘记commit，否则不会显示
                return true;
            case R.id.navigation_user:
//                fragmentTransaction.replace(R.id.content,new UserFragment());  //对应的java class
                fragmentTransaction.replace(R.id.content,UserFragment.newInstance());  //对应的java class
                fragmentTransaction.commit();  //一定不要忘记commit，否则不会显示
                return true;
        }
        return false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopService(new Intent(this, AudioService.class));
        viewBinding = null ;
    }

}
