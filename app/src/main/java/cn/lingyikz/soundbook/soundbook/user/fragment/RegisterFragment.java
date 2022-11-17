package cn.lingyikz.soundbook.soundbook.user.fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.kongzue.dialogx.dialogs.MessageDialog;
import com.kongzue.dialogx.dialogs.PopTip;
import com.kongzue.dialogx.dialogs.WaitDialog;
import com.liys.onclickme.LOnClickMe;
import com.liys.onclickme_annotations.AClick;

import cn.hutool.core.util.ReUtil;
import cn.hutool.crypto.SecureUtil;
import cn.lingyikz.soundbook.soundbook.R;
import cn.lingyikz.soundbook.soundbook.api.RequestService;
import cn.lingyikz.soundbook.soundbook.base.BaseObsever;
import cn.lingyikz.soundbook.soundbook.databinding.FragmentRegsiterBinding;
import cn.lingyikz.soundbook.soundbook.main.BaseFragment;
import cn.lingyikz.soundbook.soundbook.modle.v2.BaseModel;
import cn.lingyikz.soundbook.soundbook.modle.v2.User;
import cn.lingyikz.soundbook.soundbook.modle.v2.Version;
import cn.lingyikz.soundbook.soundbook.utils.Constans;
import cn.lingyikz.soundbook.soundbook.utils.VersionUtil;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class RegisterFragment extends BaseFragment {

    private FragmentRegsiterBinding binding ;

    public static RegisterFragment newInstance() {
        return new RegisterFragment();
    }
    @Override
    protected View setLayout(LayoutInflater inflater, ViewGroup container) {
        binding = FragmentRegsiterBinding.inflate(LayoutInflater.from(getContext()),container,false);
        LOnClickMe.init(this,binding.getRoot());
        return binding.getRoot();
    }

    @Override
    protected void setView() {

    }

    @Override
    protected void setData() {

    }

    @AClick({R.id.register_btn})
    public void click(View view) {
        switch (view.getId()) {
            case R.id.register_btn:
                register();
                break;
        }
    }
    private void register(){
        String username = binding.userNameEditText.getText().toString();
        if(!ReUtil.isMatch(Constans.REGISTER_USERNAME_REGEX,username)){
              PopTip.show("账号必须由8-16位的小写字母与数字组合").showLong();
              return ;
        }
        String password = binding.passwordEditText.getText().toString();
        if(!ReUtil.isMatch(Constans.REGISTER_PASSWORD_REGEX,password)){
             PopTip.show("密码必须由6-12位的小写字母与数字组合").showLong();
             return ;
        }
        String rePassword = binding.passwordReEditText.getText().toString();
        if(!rePassword.equals(password)){
             PopTip.show("两次密码不匹配").showLong();
            return;
        }
        User user = new User();
        user.setNickname(username);
        user.setPassword(SecureUtil.hmacMd5("www.lingyikz.cn").digestHex(password));
        user.setStatus(0);
        user.setLevel(10);

        WaitDialog.show("");
        Observable<BaseModel> observable  = RequestService.getInstance().getApi().userRegister(user);
        observable.subscribeOn(Schedulers.io()) // 在子线程中进行Http访问
                .observeOn(AndroidSchedulers.mainThread()) // UI线程处理返回接口
                .subscribe(new BaseObsever<BaseModel>() { // 订阅

                    @Override
                    public void onCompleted() {
                        super.onCompleted();
                        WaitDialog.dismiss();
                    }

                    @Override
                    public void onNext(BaseModel bean) {
                        Log.i("TAG：", bean.toString() + "");
                        if(bean.getSuccess() && bean.getCode() == 200){
                            PopTip.show("注册成功").showLong();
                            binding.userNameEditText.setText("");
                            binding.passwordEditText.setText("");
                            binding.passwordReEditText.setText("");
                        }else if(!bean.getSuccess() && bean.getCode() == 1){
                            PopTip.show(bean.getMessage()).showLong();
                        }

                    }
                });
        observable.unsubscribeOn(Schedulers.io());
    }
    @Override
    public void onDestroy() {
        binding = null ;
        super.onDestroy();

    }
}
