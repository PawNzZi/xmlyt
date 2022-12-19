package cn.lingyikz.soundbook.soundbook.main;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;

import com.kongzue.dialogx.DialogX;
import com.kongzue.dialogx.style.KongzueStyle;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import cn.lingyikz.soundbook.soundbook.utils.Constans;
import cn.lingyikz.soundbook.soundbook.utils.SharedPreferences;

public abstract class BaseFragmentActivity extends FragmentActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        BaseActivity.ActivityCollector.addActivity(this);
        DialogX.init(this);
        //设置为Kongzue主题
        DialogX.globalStyle = new KongzueStyle();
        setContentView(setLayout());
        Constans.user = SharedPreferences.getUser(getApplicationContext());
//        Log.i("TAG",Constans.user+"");
        setView();
        setData();

    }
    protected abstract void setData();

    protected abstract void setView();

    protected abstract View setLayout();
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
