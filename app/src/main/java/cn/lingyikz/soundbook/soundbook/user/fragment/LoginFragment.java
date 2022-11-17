package cn.lingyikz.soundbook.soundbook.user.fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kongzue.dialogx.dialogs.PopTip;
import com.kongzue.dialogx.dialogs.WaitDialog;
import com.liys.onclickme.LOnClickMe;
import com.liys.onclickme_annotations.AClick;

import cn.hutool.core.util.ReUtil;
import cn.hutool.crypto.SecureUtil;
import cn.lingyikz.soundbook.soundbook.R;
import cn.lingyikz.soundbook.soundbook.api.RequestService;
import cn.lingyikz.soundbook.soundbook.base.BaseObsever;
import cn.lingyikz.soundbook.soundbook.databinding.FragmentLoginBinding;
import cn.lingyikz.soundbook.soundbook.main.BaseFragment;
import cn.lingyikz.soundbook.soundbook.modle.v2.BaseModel;
import cn.lingyikz.soundbook.soundbook.modle.v2.User;
import cn.lingyikz.soundbook.soundbook.modle.v2.UserInfo;
import cn.lingyikz.soundbook.soundbook.utils.Constans;
import cn.lingyikz.soundbook.soundbook.utils.SharedPreferences;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class LoginFragment extends BaseFragment {

    private FragmentLoginBinding binding ;

    public static LoginFragment newInstance() {
        return new LoginFragment();
    }

    @Override
    protected View setLayout(LayoutInflater inflater, ViewGroup container) {
        binding = FragmentLoginBinding.inflate(LayoutInflater.from(getContext()),container,false);
        LOnClickMe.init(this,binding.getRoot());
        return binding.getRoot();
    }

    @Override
    protected void setView() {

    }

    @Override
    protected void setData() {

    }

    @AClick({R.id.login_btn})
    public void click(View view) {
        switch (view.getId()) {
            case R.id.login_btn:
                login();
                break;
        }
    }

    private void login() {
        String username = binding.userNameEditText.getText().toString();
        if(!ReUtil.isMatch(Constans.REGISTER_USERNAME_REGEX,username)){
            PopTip.show("账号格式不正确").showLong();
            return ;
        }
        String password = binding.passwordEditText.getText().toString();

        User user = new User();
        user.setNickname(username);
        user.setPassword(SecureUtil.hmacMd5("www.lingyikz.cn").digestHex(password));
        user.setStatus(100);
        user.setLevel(100);

        WaitDialog.show("");
        Observable<UserInfo> observable  = RequestService.getInstance().getApi().login(user);
        observable.subscribeOn(Schedulers.io()) // 在子线程中进行Http访问
                .observeOn(AndroidSchedulers.mainThread()) // UI线程处理返回接口
                .subscribe(new BaseObsever<UserInfo>() { // 订阅

                    @Override
                    public void onCompleted() {
                        super.onCompleted();
                        WaitDialog.dismiss();
                    }

                    @Override
                    public void onNext(UserInfo bean) {
//                        Log.i("TAG：", bean.toString() + "");
                        if(bean.getSuccess() && bean.getCode() == 200){
                            PopTip.show("登录成功").showLong();
                            Constans.user = bean.getData();
//                            Log.i("TAG：", Constans.user.toString());
                            binding.userNameEditText.setText("");
                            binding.passwordEditText.setText("");
                            SharedPreferences.saveUser(getContext(),bean.getData());
                            getActivity().finish();
                        }else if(!bean.getSuccess() && bean.getCode() == 201){
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
