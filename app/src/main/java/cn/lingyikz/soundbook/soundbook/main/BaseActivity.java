package cn.lingyikz.soundbook.soundbook.main;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;

import com.kongzue.dialogx.DialogX;
import com.kongzue.dialogx.style.KongzueStyle;

public class BaseActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        DialogX.init(this);
        //设置为Kongzue主题
        DialogX.globalStyle = new KongzueStyle();
    }
}