package cn.lingyikz.soundbook.soundbook.main;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.kongzue.dialogx.dialogs.MessageDialog;
import com.kongzue.dialogx.dialogs.PopTip;
import com.tencent.bugly.crashreport.CrashReport;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import cn.lingyikz.soundbook.soundbook.R;
import cn.lingyikz.soundbook.soundbook.api.RequestService;
import cn.lingyikz.soundbook.soundbook.base.BaseObsever;
import cn.lingyikz.soundbook.soundbook.category.CategoryFragment;
import cn.lingyikz.soundbook.soundbook.databinding.ActivityMainBinding;
import cn.lingyikz.soundbook.soundbook.home.HomeFragment;
import cn.lingyikz.soundbook.soundbook.modle.v2.Version;
import cn.lingyikz.soundbook.soundbook.service.AudioService;
import cn.lingyikz.soundbook.soundbook.user.UserFragment;
import cn.lingyikz.soundbook.soundbook.utils.Constans;
import cn.lingyikz.soundbook.soundbook.utils.SharedPreferences;
import cn.lingyikz.soundbook.soundbook.utils.UUIDUtils;
import cn.lingyikz.soundbook.soundbook.utils.VersionUtil;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainActivity extends BaseFragmentActivity implements BottomNavigationView.OnNavigationItemSelectedListener{


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
    protected void setData() {
        this.setDefaultFragment();
        viewBinding.navigation.setOnNavigationItemSelectedListener(this);
        Intent intent = new Intent(this,AudioService.class);
        intent.setAction(Constans.START_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(intent);
        }
        SharedPreferences.saveCategoryIndex(getApplicationContext(),0);
        this.checkVersion();
        CrashReport.initCrashReport(getApplicationContext(), Constans.BUGLY_APPID, false);
//        CrashReport.testJavaCrash();
//        SharedPreferences.cleanCurrentPlayHistoryInfo(getParent());

    }

    @Override
    protected void setView() {

    }

    @Override
    protected View setLayout() {
        viewBinding = ActivityMainBinding.inflate(getLayoutInflater());
//        AppCenter.start(getApplication(), Constans.APP_CENTER_SECRET,
//                Analytics.class, Crashes.class);
        return viewBinding.getRoot();
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
    public void checkVersion(){
//        Log.i("TAG：",  "checkVersion");
        int versionCode = VersionUtil.getVersonCode(this);
        Observable<Version> observable  = RequestService.getInstance().getApi().getVersion(versionCode);
        observable.subscribeOn(Schedulers.io()) // 在子线程中进行Http访问
                .observeOn(AndroidSchedulers.mainThread()) // UI线程处理返回接口
                .subscribe(new BaseObsever<Version>() { // 订阅

                    @Override
                    public void onNext(Version bean) {
//                        Log.i("TAG：", bean.toString() + "");
                        if(bean.getCode() == 200 && bean.getData() != null){
                            if(versionCode < bean.getData().getCode()){
                                MessageDialog.show("温馨提示", "最新版本为"+bean.getData().getNumber()+"，请及时加群更新", "确定")
                                        .setCancelable(false);
                            }
                        }else if(bean.getCode() != 200){
//                            Toast.makeText(getApplicationContext(), Constans.VERSION_ERROR, Toast.LENGTH_SHORT).show();
                            PopTip.show(R.mipmap.fail_tip,Constans.VERSION_ERROR).showShort().setAutoTintIconInLightOrDarkMode(false);

                        }

                    }
                });
        observable.unsubscribeOn(Schedulers.io());
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopService(new Intent(this, AudioService.class));
        viewBinding = null ;
        Bundle spBundle = new Bundle();
        spBundle.putString("lable","");
        spBundle.putInt("index",-1);
        SharedPreferences.saveBolckClose(this,spBundle);
    }


}
