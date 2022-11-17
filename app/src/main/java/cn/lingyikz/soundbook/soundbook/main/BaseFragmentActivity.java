package cn.lingyikz.soundbook.soundbook.main;

import android.os.Bundle;
import android.view.View;
import android.view.Window;

import com.kongzue.dialogx.DialogX;
import com.kongzue.dialogx.style.KongzueStyle;
import com.liys.onclickme.LOnClickMe;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

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
