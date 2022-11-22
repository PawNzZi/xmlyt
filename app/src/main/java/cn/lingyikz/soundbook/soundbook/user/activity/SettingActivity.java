package cn.lingyikz.soundbook.soundbook.user.activity;

import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.kongzue.dialogx.dialogs.MessageDialog;
import com.kongzue.dialogx.dialogs.PopTip;
import com.kongzue.dialogx.dialogs.WaitDialog;
import com.kongzue.dialogx.interfaces.OnDialogButtonClickListener;
import com.liys.onclickme.LOnClickMe;
import com.liys.onclickme_annotations.AClick;

import cn.hutool.core.util.ObjectUtil;
import cn.lingyikz.soundbook.soundbook.R;
import cn.lingyikz.soundbook.soundbook.api.RequestService;
import cn.lingyikz.soundbook.soundbook.base.BaseObsever;
import cn.lingyikz.soundbook.soundbook.databinding.ActivitySettingBinding;
import cn.lingyikz.soundbook.soundbook.main.BaseActivity;
import cn.lingyikz.soundbook.soundbook.modle.v2.Version;
import cn.lingyikz.soundbook.soundbook.utils.Constans;
import cn.lingyikz.soundbook.soundbook.utils.SharedPreferences;
import cn.lingyikz.soundbook.soundbook.utils.VersionUtil;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class SettingActivity extends BaseActivity {


    private ActivitySettingBinding binding;
    @Override
    protected void setData() {

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
                    PopTip.show("无登录账号，无需退出").showShort();
                }


                break;
        }
    }
    public void checkVersion(){
//        Log.i("TAG：",  "checkVersion");
        WaitDialog.show(Constans.CHECKING_UPDATE);
        int versionCode = VersionUtil.getVersonCode(this);
        Observable<Version> observable  = RequestService.getInstance().getApi().getVersion(versionCode);
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
//                        Log.i("TAG：", bean.toString() + "");
                        if(bean.getCode() == 200 && bean.getData() != null){
                            if(versionCode < bean.getData().getCode()){
                                MessageDialog.show("温馨提示", "最新版本为"+bean.getData().getNumber()+"，请及时加群更新", "确定")
                                        .setCancelable(false);
                            }else if(versionCode == bean.getData().getCode()) {
                                Toast.makeText(getApplicationContext(), Constans.LATEST_VERSION, Toast.LENGTH_SHORT).show();
                            }
                        }else if(bean.getCode() != 200){
                            Toast.makeText(getApplicationContext(), Constans.VERSION_ERROR, Toast.LENGTH_SHORT).show();
                        }

                    }
                });
        observable.unsubscribeOn(Schedulers.io());
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null ;
    }
}