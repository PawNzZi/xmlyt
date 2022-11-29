package cn.lingyikz.soundbook.soundbook.user.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.util.Log;
import android.view.View;

import com.arialyy.annotations.Download;
import com.arialyy.aria.core.Aria;
import com.arialyy.aria.core.task.DownloadTask;
import com.hjq.permissions.OnPermissionCallback;
import com.hjq.permissions.Permission;
import com.hjq.permissions.XXPermissions;
import com.kongzue.dialogx.dialogs.MessageDialog;
import com.kongzue.dialogx.dialogs.PopTip;
import com.kongzue.dialogx.dialogs.WaitDialog;
import com.kongzue.dialogx.interfaces.OnDialogButtonClickListener;
import com.liys.onclickme.LOnClickMe;
import com.liys.onclickme_annotations.AClick;

import java.io.File;
import java.util.List;

import androidx.core.content.FileProvider;
import cn.hutool.core.util.ObjectUtil;
import cn.lingyikz.soundbook.soundbook.R;
import cn.lingyikz.soundbook.soundbook.api.RequestService;
import cn.lingyikz.soundbook.soundbook.base.BaseObsever;
import cn.lingyikz.soundbook.soundbook.databinding.ActivitySettingBinding;
import cn.lingyikz.soundbook.soundbook.main.BaseActivity;
import cn.lingyikz.soundbook.soundbook.modle.v2.Version;
import cn.lingyikz.soundbook.soundbook.utils.Constans;
import cn.lingyikz.soundbook.soundbook.utils.IntentAction;
import cn.lingyikz.soundbook.soundbook.utils.SharedPreferences;
import cn.lingyikz.soundbook.soundbook.utils.VersionUtil;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class SettingActivity extends BaseActivity {


    private ActivitySettingBinding binding;
    @Override
    protected void setData() {
        Aria.download(this).register();
        Log.i("TAG",this.getPackageName());
    }

    @Override
    protected void setView() {
        binding.versionNubmer.setText(VersionUtil.getVersonName(this));
        binding.titleBar.goSearch.setVisibility(View.GONE);
        binding.titleBar.goPlay.setVisibility(View.GONE);
        binding.titleBar.goBacK.setVisibility(View.VISIBLE);
        binding.titleBar.title.setText("设置");
    }

    @Override
    protected View setLayout() {
        binding = ActivitySettingBinding.inflate(getLayoutInflater());
        LOnClickMe.init(this,binding.getRoot());
        return binding.getRoot();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(ObjectUtil.isNotNull(Constans.user)){
            binding.loginTextBtn.setText(R.string.login_out);
        }else {
            binding.loginTextBtn.setText(R.string.login_btn_text);
        }
    }

    @AClick({R.id.check_version,R.id.go_bacK,R.id.login_out_btn})
    public void click(View view) {
        switch (view.getId()) {
            case R.id.go_bacK:
                finish();
                break;
            case R.id.check_version:
                checkVersion();
                break;
            case R.id.login_out_btn:
                if(ObjectUtil.isNotNull(Constans.user)){
                    MessageDialog.show(Constans.WARN_TIP, "您确定要退出账号嘛？", Constans.DIALOG_SURE_BUTTON,Constans.DIALOG_CANCEL_BUTTON).setOkButton(new OnDialogButtonClickListener<MessageDialog>() {
                        @Override
                        public boolean onClick(MessageDialog baseDialog, View v) {
                            Constans.user = null ;
                            SharedPreferences.clearLoginUserInfo(SettingActivity.this);
                            finish();
                            return false;
                        }
                    });
                }else {
                    //PopTip.show(R.mipmap.warn_tip,"无登录账号，无需退出").showShort().setAutoTintIconInLightOrDarkMode(false);
                    IntentAction.toNextActivity(this,LoginActivity.class);
                }


                break;
        }
    }
    public void checkVersion(){
//        Log.i("TAG：",  "checkVersion");
        WaitDialog.show(Constans.CHECKING_UPDATE);
        int versionCode = VersionUtil.getVersonCode(this);
        Observable<Version> observable  = RequestService.getInstance().getApi().checkVersion();
        observable.subscribeOn(Schedulers.io()) // 在子线程中进行Http访问
                .observeOn(AndroidSchedulers.mainThread()) // UI线程处理返回接口
                .subscribe(new BaseObsever<Version>() { // 订阅

                    @Override
                    public void onCompleted() {
                        super.onCompleted();
                        WaitDialog.dismiss();
                    }

                    @Override
                    public void onNext(Version bean) {
                        if(bean.getCode() == 200 && ObjectUtil.isNotNull(bean.getData())){
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

                            }else if(versionCode >= bean.getData().getCode()) {
//                                Toast.makeText(getApplicationContext(), Constans.LATEST_VERSION, Toast.LENGTH_SHORT).show();
                                PopTip.show(R.mipmap.succes_tip,Constans.LATEST_VERSION).showShort().setAutoTintIconInLightOrDarkMode(false);
                            }
                        }else if(bean.getCode() == 200 && ObjectUtil.isNull(bean.getData())){
                            PopTip.show(R.mipmap.succes_tip,Constans.LATEST_VERSION).showShort().setAutoTintIconInLightOrDarkMode(false);
                        }
                        else if(bean.getCode() != 200){
//                            Toast.makeText(getApplicationContext(), Constans.VERSION_ERROR, Toast.LENGTH_SHORT).show();
                            PopTip.show(R.mipmap.fail_tip,bean.getMessage()).showShort().setAutoTintIconInLightOrDarkMode(false);

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

                        Aria.download(SettingActivity.this)
                                .load(url)     //读取下载地址
                                .setFilePath(Constans.DOWNLOAD_APP_PATH) //设置文件保存的完整路径
                                .create();   //创建并启动下载

                    }
                    @Override
                    public void onDenied(List<String> permissions, boolean never) {
                        if (never) {
                            // 如果是被永久拒绝就跳转到应用权限系统设置页面
                            XXPermissions.startPermissionActivity(SettingActivity.this, permissions);
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
    @Download.onTaskCancel void taskCancel(DownloadTask task) {
        WaitDialog.dismiss();
    }

    @Download.onTaskFail void taskFail(DownloadTask task) {
        WaitDialog.dismiss();
    }
    @Download.onTaskComplete void taskComplete(DownloadTask task) {
        //下载完成进行安装
        WaitDialog.dismiss();
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
        binding = null ;
        Aria.download(this).unRegister();
    }
}