package cn.lingyikz.soundbook.soundbook.main;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.arialyy.annotations.Download;
import com.arialyy.aria.core.Aria;
import com.arialyy.aria.core.task.DownloadTask;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.hjq.permissions.OnPermissionCallback;
import com.hjq.permissions.Permission;
import com.hjq.permissions.XXPermissions;
import com.kongzue.dialogx.dialogs.MessageDialog;
import com.kongzue.dialogx.dialogs.PopTip;
import com.kongzue.dialogx.dialogs.WaitDialog;
import com.kongzue.dialogx.interfaces.OnBackPressedListener;
import com.kongzue.dialogx.interfaces.OnBindView;
import com.kongzue.dialogx.interfaces.OnDialogButtonClickListener;
import com.tencent.bugly.crashreport.CrashReport;
import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.io.File;
import java.util.List;

import cn.lingyikz.soundbook.soundbook.R;
import cn.lingyikz.soundbook.soundbook.api.RequestService;
import cn.lingyikz.soundbook.soundbook.base.BaseObsever;
import cn.lingyikz.soundbook.soundbook.category.CategoryFragment;
import cn.lingyikz.soundbook.soundbook.databinding.ActivityMainBinding;
import cn.lingyikz.soundbook.soundbook.home.HomeFragment;
import cn.lingyikz.soundbook.soundbook.modle.v2.Version;
import cn.lingyikz.soundbook.soundbook.service.AudioService;
import cn.lingyikz.soundbook.soundbook.user.UserFragment;
import cn.lingyikz.soundbook.soundbook.user.activity.SettingActivity;
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
//    private ProgressBar progressBar = null ;

    // 设置默认进来是tab 显示的页面
    private void setDefaultFragment(){
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.content,HomeFragment.newInstance());
        fragmentTransaction.commit();
    }

    @Override
    protected void setData() {
        Aria.download(this).register();
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
                                MessageDialog.show("发现新版本 v"+bean.getData().getNumber(), bean.getData().getDescrition(), Constans.APP_INSTALL,Constans.DIALOG_CANCEL_BUTTON).setOkButtonClickListener((baseDialog, v) -> {
                                    File dir = new File(Constans.DOWNLOAD_FILE_PATH);
                                    if(!dir.exists()){
                                        dir.mkdir();
                                    }else{
                                        File appFile = new File(Constans.DOWNLOAD_APP_PATH);
                                        if(appFile.exists()){
                                            appFile.delete();
                                        }
                                    }

                                    dowmloadAndInstall(bean.getData().getDownloadUrl());
                                    return false;
                                });
                            }
                        }else if(bean.getCode() != 200){
//                            Toast.makeText(getApplicationContext(), Constans.VERSION_ERROR, Toast.LENGTH_SHORT).show();
                            PopTip.show(R.mipmap.fail_tip,Constans.VERSION_ERROR).showShort().setAutoTintIconInLightOrDarkMode(false);

                        }
                    }
                });
        observable.unsubscribeOn(Schedulers.io());
    }
    private void dowmloadAndInstall(String url){
        XXPermissions.with(this)
                // 申请权限
                .permission(Permission.REQUEST_INSTALL_PACKAGES)
                .permission(Permission.WRITE_EXTERNAL_STORAGE)
                .permission(Permission.READ_EXTERNAL_STORAGE)
                .request(new OnPermissionCallback() {
                    @Override
                    public void onGranted(List<String> permissions, boolean all) {
                        if (!all) {
                            return;
                        }

                        Aria.download(this)
                                .load(url)     //读取下载地址
                                .setFilePath(Constans.DOWNLOAD_APP_PATH) //设置文件保存的完整路径
                                .create();   //创建并启动下载

                    }
                    @Override
                    public void onDenied(List<String> permissions, boolean never) {
                        if (never) {
                            // 如果是被永久拒绝就跳转到应用权限系统设置页面
                            XXPermissions.startPermissionActivity(MainActivity.this, permissions);
                        } else {

                        }
                    }
                });
    }
    @Download.onPre void onPre(DownloadTask task) {
        WaitDialog.show(Constans.DOWNLOADING).setOnBackPressedListener(() -> {
            WaitDialog.dismiss();
            return false;
        });
    }
    //在这里处理任务执行中的状态，如进度进度条的刷新
    @Download.onTaskRunning protected void running(DownloadTask task) {



//        MessageDialog.show("正在下载", "", "")
//                .setCustomView(new OnBindView<MessageDialog>(progressBar) {
//                    @Override
//                    public void onBind(MessageDialog dialog, View v) {
//                        dialog.getDialogImpl().boxCustom.setPadding(40, 70, 40, 20);
//                        progressBar.setProgress(task.getPercent());
//                    }
//                });


    }
    @Download.onTaskCancel void taskCancel(DownloadTask task) {
        WaitDialog.dismiss();
    }

    @Download.onTaskFail void taskFail(DownloadTask task) {
        WaitDialog.dismiss();
    }
    @Download.onTaskComplete void taskComplete(DownloadTask task) {
        WaitDialog.dismiss();
        //下载完成进行安装
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(Intent.ACTION_VIEW);
        File apkFile = new File(Constans.DOWNLOAD_APP_PATH);
        Uri uri = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            uri = FileProvider.getUriForFile(this, this.getPackageName()+".fileProvider", apkFile);
        } else {
            uri = Uri.fromFile(apkFile);
        }
        intent.setDataAndType(uri, "application/vnd.android.package-archive");
        this.startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopService(new Intent(this, AudioService.class));
        viewBinding = null ;
        Aria.download(this).unRegister();
        Bundle spBundle = new Bundle();
        spBundle.putString("lable","");
        spBundle.putInt("index",-1);
        SharedPreferences.saveBolckClose(this,spBundle);
    }



}
